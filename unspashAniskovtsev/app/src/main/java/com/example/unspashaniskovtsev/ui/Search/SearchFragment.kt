package com.example.unspashaniskovtsev.ui.Search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.unspashaniskovtsev.ItemOffsetDecoration
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.SearchFragmentBinding
import com.example.unspashaniskovtsev.launchAndCollectIn
import com.example.unspashaniskovtsev.ui.Home.PhotosAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment) {

    private val viewBinding by viewBinding(SearchFragmentBinding::bind)
    private val viewModel: SearchViewModel by viewModels()
    private var photosAdapter: PhotosAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
        setupSearchInput()
        observe()
    }



    private fun observe(){

        lifecycleScope.launch {
        viewModel.originPhotoFlow.collectLatest {

        }
        }
        viewModel.originPhotoFlow.launchAndCollectIn(viewLifecycleOwner){
            photosAdapter?.submitData(it)
            viewModel.loadingFlow.value = false
        }
        viewModel.loadingFlowGetter.launchAndCollectIn(viewLifecycleOwner){
            showLoad(it)
        }
    }

    private fun showLoad(status: Boolean){
        viewBinding.searchProgressBar.isVisible = status != false
        if (status){
            lifecycleScope.launch {
                delay(5000)
                if (view != null){
                    viewBinding.searchProgressBar.isVisible = false
                }
            }
        }
    }


    private fun initRecycler() {
        photosAdapter = PhotosAdapter(onItemClicked = {
            val action = SearchFragmentDirections.actionSearchFragmentToDetailPhotoFragment(it)
            findNavController().navigate(action)
        },
        onLikeClicked = { photo, _ ->
            pressLike(photo)
        })
        with(viewBinding){
            searchRecycler.adapter = photosAdapter
            searchRecycler.layoutManager = StaggeredGridLayoutManager(2, RecyclerView.VERTICAL)
            searchRecycler.addItemDecoration(ItemOffsetDecoration(requireContext(), false))
        }
    }

     private fun pressLike(photo: Photo){
        viewModel.pressLike(photo)
    }



    private fun setupSearchInput(){
        viewBinding.queryText.addTextChangedListener(textWatcher)
    }

    private val textWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            s?.toString()?.let { viewModel.setSearch(it) }
        }

        override fun afterTextChanged(s: Editable?) {}
    }


    override fun onDestroyView() {
        viewBinding.queryText.removeTextChangedListener(textWatcher)
        super.onDestroyView()
    }



}