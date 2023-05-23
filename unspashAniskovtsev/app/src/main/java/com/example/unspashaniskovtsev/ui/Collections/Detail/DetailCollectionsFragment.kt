package com.example.unspashaniskovtsev.ui.Collections.Detail

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.unspashaniskovtsev.ItemOffsetDecoration
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.DetailCollectionsInfoBinding
import com.example.unspashaniskovtsev.ui.Home.PhotosAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.search_fragment.*
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailCollectionsFragment: Fragment(R.layout.detail_collections_info) {

    val viewBinding by viewBinding(DetailCollectionsInfoBinding::bind)
    private var photosAdapter: PhotosAdapter? = null
    private val viewModel: DetailCollectionsViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setId(navArgs<DetailCollectionsFragmentArgs>().value.collectionId)
        observe()

    }

    private fun initRecycler(){
        photosAdapter = PhotosAdapter(onItemClicked = {
            val action = DetailCollectionsFragmentDirections.actionDetailCollectionsFragmentToDetailPhotoFragment(it)
            findNavController().navigate(action)
        },
            onLikeClicked = { photo, _ ->
                pressLike(photo)
            })
        with(viewBinding){
            detailCollectionsList.smoothScrollToPosition(0)
            detailCollectionsList.adapter = photosAdapter
            detailCollectionsList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            detailCollectionsList.addItemDecoration(ItemOffsetDecoration(requireContext(), false))
        }
    }

    private fun observe(){
        lifecycleScope.launch{
            viewModel.photosFLow.collectLatest {
                photosAdapter?.submitData(it)
            }
        }

    }

    private fun pressLike(photo: Photo){
        viewModel.pressLike(photo)
    }

    private fun setId(id: String){
        viewModel.setId(id)
    }

}