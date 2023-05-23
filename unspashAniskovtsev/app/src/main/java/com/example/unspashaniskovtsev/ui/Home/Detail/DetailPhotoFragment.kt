package com.example.unspashaniskovtsev.ui.Home.Detail

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.os.bundleOf
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.work.WorkInfo
import androidx.work.WorkManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.unspashaniskovtsev.Models.DetailPhoto
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.DetailPhotoInfoBinding
import com.example.unspashaniskovtsev.launchAndCollectIn
import com.example.unspashaniskovtsev.ui.Home.HomeViewModel
import com.example.unspashaniskovtsev.utils.BlurHashDecoder
import com.example.unspashaniskovtsev.utils.haveQ
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.snackbar.Snackbar.LENGTH_SHORT
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailPhotoFragment: Fragment(R.layout.detail_photo_info) {

    private val viewBinding by viewBinding(DetailPhotoInfoBinding::bind)
    private val viewModel: DetailPhotoViewModel by viewModels()
    private val photoViewModel: HomeViewModel by viewModels()
    private val args by navArgs<DetailPhotoFragmentArgs>()

    private lateinit var requestPermissonLauncher: ActivityResultLauncher<Array<String>>
    private var photoId: String? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        photoId = args.photoId
        Id.photoId = photoId.toString()
        photoId?.let { getPhotoById(it) }
        observe()
        initPermissionsResultListener()
    }


    private fun getPhotoById(photoID: String) {
        viewModel.getDetailPhoto(photoID)
    }

    private fun observe() {
        viewModel.detailPhotoFlow.launchAndCollectIn(viewLifecycleOwner) {
            bindInfo(it)
        }
        viewModel.loadingFlow.launchAndCollectIn(viewLifecycleOwner){
            showLoad(it)
        }
        var boolFlagForCorrectToast = true
        WorkManager.getInstance(requireContext())
            .getWorkInfosForUniqueWorkLiveData(DownloadWorker.DOWNLOAD_WORK_ID)
            .observe(viewLifecycleOwner){
                try {
                    if (!boolFlagForCorrectToast){
                        handleWork(it.first())
                    }
                    boolFlagForCorrectToast = false
                }catch (e: Exception){

                }
            }
    }

    private fun showLoad(loading: Boolean) {
            with(viewBinding){
                downloadImage.isVisible = !loading == true
                username.isVisible =  !loading == true
                userImage.isVisible =  !loading == true
                likesCount.isVisible =  !loading == true
                likeButtonRed.isVisible =  !loading == true
                likeButtonWhite.isVisible =  !loading == true
                exifText.isVisible = !loading == true
                about.isVisible = !loading == true
                download.isVisible = !loading == true
                progressBar2.isVisible = loading == true
            }
    }

    private fun bindInfo(photo: DetailPhoto) {


        val blurHashPhoto = BlurHashDecoder.blurHashBitmap(resources, photo)
        bindDownload(photo)
        bindDetails(photo)
        bindPhotoAndLikes(photo, blurHashPhoto)


    }

    private fun bindDownload(photo: DetailPhoto) {
        viewBinding.downloadImage.setOnClickListener {
            if (hasPermissons())
            downloadPicture(photo.urls!!.full)
            else {
                requestPermisson()
            }
        }
    }

    private fun requestPermisson(){
        requestPermissonLauncher.launch(PERMISSIONS.toTypedArray())
    }

    private fun initPermissionsResultListener(){
        requestPermissonLauncher = registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { map ->
            if (map.values.all { it }){

            } else {
                Snackbar.make(requireView(), "You denied permissions", LENGTH_SHORT).show()
            }
        }
    }

    private fun hasPermissons(): Boolean {
        return PERMISSIONS.all {
            ActivityCompat.checkSelfPermission(requireContext(), it) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun bindDetails(photo: DetailPhoto) {
        with(viewBinding) {
            exifText.text = getString(
                R.string.exif,
                photo.exif?.make ?: "",
                photo.exif?.model ?: "",
                photo.exif?.exposure_time ?: "",
                photo.exif?.aperture ?: "",
                photo.exif?.focal_length ?: "",
                photo.exif?.iso ?: 0
            )
                about.text = getString(R.string.about, photo.user?.username , photo.user?.bio ?:"")

        }

    }

    private fun bindPhotoAndLikes(photo: DetailPhoto, blurHashPhoto: Drawable){

        with(viewBinding) {
            Glide.with(requireContext())
                .asBitmap()
                .load(photo.urls?.full)
                .placeholder(blurHashPhoto)
                .transition(BitmapTransitionOptions.withCrossFade(80))
                .into(mainImage)

            //bind текста

            //bind user
            Glide.with(requireContext())
                .load(photo.user?.profile_image?.small)
                .placeholder(R.drawable.ic_baseline_circle_24)
                .circleCrop()
                .into(userImage)
            username.text = "@${photo.user?.username}"
            //bind likes
            if (photo.location?.city == null){
                city.isVisible = false
            } else {
                city.text = buildString {
                    append(photo.location.country)
                    append(", ")
                    append(photo.location.city)
                }
                city.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("geo: ${photo.location.position?.latitude} ${photo.location.position?.longitude}")
                    val chooser = Intent.createChooser(intent, "Maps")
                    startActivity(chooser)
                }
            }

            likesCount.text = photo.likes.toString()

            var photoCurrentLikeState = photo.liked_by_user!!
            var photoCurrentLikesCount = photo.likes!!
            likeButtonRed.isVisible = photoCurrentLikeState
            likeButtonWhite.isVisible = !photoCurrentLikeState

            viewBinding.likeButtonRed.setOnClickListener {
                setlikes(photoCurrentLikesCount, true)
                photoCurrentLikesCount -= 1
                likeButtonRed.isVisible = false
                likeButtonWhite.isVisible = true
                photoCurrentLikeState = false
                photoViewModel.pressLike(
                    Photo(
                        navArgs<DetailPhotoFragmentArgs>().value.photoId,
                        photo.likes,
                        photo.liked_by_user,
                        photo.user!!,
                        photo.urls!!,
                        photo.blur_hash!!
                    )
                )
            }
            viewBinding.likeButtonWhite.setOnClickListener {
                setlikes(photoCurrentLikesCount, false)
                photoCurrentLikesCount += 1
                likeButtonWhite.isVisible = false
                likeButtonRed.isVisible = true
                photoCurrentLikeState = true
                photoViewModel.pressLike(
                    Photo(
                        navArgs<DetailPhotoFragmentArgs>().value.photoId,
                        photo.likes,
                        photo.liked_by_user,
                        photo.user!!,
                        photo.urls!!,
                        photo.blur_hash!!
                    )
                )
            }



        }
    }

    private fun setlikes(likes: Int, isLiked: Boolean){
        if (isLiked){
            viewBinding.likesCount.text = (likes - 1).toString()
        } else {
            viewBinding.likesCount.text = (likes + 1).toString()

        }
    }
    private fun downloadPicture(url: String) {
        viewModel.downloadImage(url)
    }

    private fun handleWork(workInfo : WorkInfo){
        if (workInfo.state == WorkInfo.State.SUCCEEDED){
            Snackbar.make(requireView(), getString(R.string.snackbar_success), LENGTH_SHORT).show()
        }
        if (workInfo.state == WorkInfo.State.CANCELLED){
            Snackbar.make(requireView(), getString(R.string.snackbar_error), LENGTH_SHORT).show()
        }

    }

    companion object {
        private val PERMISSIONS = listOfNotNull(
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.WRITE_EXTERNAL_STORAGE.takeIf {
                haveQ().not()
            }

            )
    }

}