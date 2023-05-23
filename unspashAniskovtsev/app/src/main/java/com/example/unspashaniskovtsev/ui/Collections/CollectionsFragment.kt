package com.example.unspashaniskovtsev.ui.Collections

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.unspashaniskovtsev.ItemOffsetDecoration
import com.example.unspashaniskovtsev.Models.Collections
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.CollectionsFragmentBinding
import com.example.unspashaniskovtsev.databinding.MainFragmentBinding
import com.example.unspashaniskovtsev.launchAndCollectIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.*

@AndroidEntryPoint
class CollectionsFragment: Fragment(R.layout.collections_fragment) {

    private val viewBinding by viewBinding(CollectionsFragmentBinding::bind)


    private val viewModel: CollectionsViewModel by viewModels()
    var collectionsAdapter: CollectionsAdapter? = null



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecycler()
        observe()
    }

    private fun observe(){
        viewModel.collectionsFlow.launchAndCollectIn(viewLifecycleOwner){
            collectionsAdapter!!.submitData(it)
        }
    }

    private fun setupRecycler(){
        collectionsAdapter = CollectionsAdapter(onItemClicked = {
            val action = CollectionsFragmentDirections.actionCollectionsFragmentToDetailCollectionsFragment(it)
            findNavController().navigate(action)
        },
            onLikeClicked = {collection ->
                pressLike(collection)
            },
        requireContext()
        )
        with(viewBinding){
            collectionsList.adapter = collectionsAdapter
            collectionsList.layoutManager = LinearLayoutManager(requireContext())
            collectionsList.addItemDecoration(ItemOffsetDecoration(requireContext(), true))
        }
    }

    private fun pressLike(collections: Collections) {
        viewModel.pressLike(collections)
    }


}