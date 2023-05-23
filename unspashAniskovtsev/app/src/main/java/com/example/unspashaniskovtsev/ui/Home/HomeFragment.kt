package com.example.unspashaniskovtsev.ui.Home

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.unspashaniskovtsev.ItemOffsetDecoration
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.Models.ProfileImage
import com.example.unspashaniskovtsev.Models.Urls
import com.example.unspashaniskovtsev.Models.User
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.data.Network.Network
import com.example.unspashaniskovtsev.data.db.Models.PhotoEntity
import com.example.unspashaniskovtsev.databinding.HomeFragmentBinding
import com.example.unspashaniskovtsev.launchAndCollectIn
import com.example.unspashaniskovtsev.utils.BlurHashDecoder
import com.example.unspashaniskovtsev.utils.hasConnection
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment: Fragment(R.layout.home_fragment) {

    private val viewBinding by viewBinding(HomeFragmentBinding::bind)
    private val viewModel: HomeViewModel by viewModels()
    private var photosAdapter: PhotosAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Network.token = requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
            .getString("token", "")
            .toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRecycler()
        checkInternetConnection()
        observe()
    }

    private fun checkInternetConnection(){
        if (!hasConnection(requireContext())) {
            with(viewBinding) {
                todayTopText.isVisible = false
                topImage.isVisible = false
                photosRecycler.isVisible = false
                userImageTop.isVisible = false
                usernameTop.isVisible = false
                likeButtonTop.isVisible = false
                likesCountTop.isVisible = false
                errorText.isVisible = true
            }
        }
    }
    private fun observe() {
        viewModel.photosFlow.launchAndCollectIn(viewLifecycleOwner) {
            photosAdapter?.submitData(it)
        }
        viewModel.topPhotoFlow.launchAndCollectIn(viewLifecycleOwner) {
            bindTopPhoto(it)
        }
    }

    private fun setUpRecycler() {
        photosAdapter = PhotosAdapter(onItemClicked = {
            val action = HomeFragmentDirections.actionHomeFragmentToDetailPhotoFragment(it)
            findNavController().navigate(action)
        },
            onLikeClicked = { photo, _ ->
                pressLike(photo)
            }
        )

        with(viewBinding) {
            photosRecycler.adapter = photosAdapter
            photosRecycler.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            photosRecycler.addItemDecoration(ItemOffsetDecoration(requireContext(), false))
        }
    }

    private fun pressLike(photo: Photo) {
        viewModel.pressLike(photo)
    }

    private fun bindTopPhoto(photos: List<PhotoEntity>) {
        if (photos.isEmpty()){
            with(viewBinding){
                todayTopText.isVisible = false
                topImage.isVisible = false
                userImageTop.isVisible = false
                usernameTop.isVisible = false
                likeButtonTop.isVisible = false
                likesCountTop.isVisible = false
            }
            return
        }
        val photo = photos.first()
        val blurHash = BlurHashDecoder.blurHashBitmap(resources, photo)
        with(viewBinding) {
            Glide.with(topImage)
                .load(photo.url)
                .placeholder(blurHash)
                .into(topImage)
            topImage.setOnClickListener {
                findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToDetailPhotoFragment(photo.id))
            }

            usernameTop.text = photo.username
            Glide.with(userImageTop)
                .load(photo.userImageLink)
                .placeholder(blurHash)
                .circleCrop()
                .into(userImageTop)


            var photoCurrentLike = photo.liked_by_user

            var likes = photo.likes

            likesCountTop.text = likes.toString()

            likeButtonTop.setOnClickListener {
                if (photoCurrentLike) {
                    Glide.with(likeButtonTop)
                        .load(R.drawable.white_like)
                        .into(likeButtonTop)
                    if (likes != 0){
                        likes-=1
                    }
                    likesCountTop.text = likes.toString()
                    photoCurrentLike = false
                } else {
                    Glide.with(likeButtonTop)
                        .load(R.drawable.red_like)
                        .into(likeButtonTop)
                    likes+= 1
                    likesCountTop.text = likes.toString()
                    photoCurrentLike = true
                }
                pressLike(
                    Photo(
                        photo.id,
                        photo.likes,
                        photo.liked_by_user,
                        User(photo.userID, photo.username, ProfileImage(photo.userImageLink), null),
                        Urls(photo.url),
                        photo.blurHash
                    )
                )
            }


        }

    }
}


