package com.example.unspashaniskovtsev.ui.Account

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.unspashaniskovtsev.ItemOffsetDecoration
import com.example.unspashaniskovtsev.Models.CurrentUser
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.ProfileFragmentBinding
import com.example.unspashaniskovtsev.launchAndCollectIn
import com.example.unspashaniskovtsev.ui.Home.PhotosAdapter
import com.example.unspashaniskovtsev.ui.Search.SearchFragmentDirections
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class ProfileFragment: Fragment(R.layout.profile_fragment) {


    private val viewBinding by viewBinding(ProfileFragmentBinding::bind)
    private val viewModel: ProfileViewModel by viewModels()
    private var photosAdapter: PhotosAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observe()
        getUser()
        initRecycler()



    }

    private fun initRecycler(){
        photosAdapter = PhotosAdapter(onItemClicked = {
            val action = ProfileFragmentDirections.actionProfileFragmentToDetailPhotoFragment(it)
            findNavController().navigate(action)
        },
            onLikeClicked = { photo, _ ->
                pressLike(photo)

            })
        with(viewBinding){
            profileRecycler.adapter = photosAdapter
            profileRecycler.layoutManager = LinearLayoutManager(requireContext())
            profileRecycler.addItemDecoration(ItemOffsetDecoration(requireContext(), true))
        }
    }

    private fun pressLike(photo: Photo){
        viewModel.pressLike(photo)
    }

    private fun setUsernameForLikesList(username: String){
        viewModel.setUsername(username)
    }


    private fun observe(){
        viewModel.loadingFlowGetter.launchAndCollectIn(viewLifecycleOwner){
            showLoad(it)
        }

        viewModel.userFlowGetter.launchAndCollectIn(viewLifecycleOwner){
            bindUser(it)
            try {
                setUsernameForLikesList(it.username)
            }catch (e: Exception){

            }
        }
        viewModel.imageFlowGetter.launchAndCollectIn(viewLifecycleOwner){
            bindImage(it)
        }
        viewModel.toastFlowGetter.launchAndCollectIn(viewLifecycleOwner){
            showToast(it)
        }
        viewModel.photoFlow.launchAndCollectIn(viewLifecycleOwner){
            Log.d("data", it.toString())
            photosAdapter?.submitData(it)
        }
    }




    private fun getUser(){
        viewModel.getInfoAboutCurrentUser()
    }

    private fun showLoad(status: Boolean){
        viewBinding.progressBar.isVisible = status != false
    }

    private fun showToast(message: String){
        if (message.isEmpty()) return
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun bindUser(user: CurrentUser){
        with(viewBinding){
            email.text = user.email
            downloads.text = user.downloads
            profileTag.text = user.username
            profileName.text = user.first_name + " " + user.last_name
            city.text = user.location ?: ""
            if (city.text.isNotEmpty()){
                city.setOnClickListener {
                    val intent = Intent(Intent.ACTION_VIEW)
                    intent.data = Uri.parse("geo:0,0?q=${user.location}");
                    val chooser = Intent.createChooser(intent, "Maps")
                    startActivity(chooser)
                }
            }
        }

    }

    private fun bindImage(link: String) {
        with(viewBinding){
            Glide.with(profilePicture)
                .load(link)
                .circleCrop()
                .into(profilePicture)

        }
    }


}