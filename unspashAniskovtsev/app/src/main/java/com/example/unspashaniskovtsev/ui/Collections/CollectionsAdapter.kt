package com.example.unspashaniskovtsev.ui.Collections

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions
import com.example.unspashaniskovtsev.Models.Collections
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.CollectionItemForListBinding
import com.example.unspashaniskovtsev.databinding.PhotoItemForListBinding
import com.example.unspashaniskovtsev.inflate
import com.example.unspashaniskovtsev.utils.BlurHashDecoder
import kotlinx.android.extensions.LayoutContainer

class CollectionsAdapter(
    private val onItemClicked: (id: String) -> Unit,
    private val onLikeClicked: (collections: Collections) -> Unit,
    private val context: Context
) : PagingDataAdapter<Collections, CollectionsAdapter.CollectionsViewHolder>(CollectionsDifUtilCallback) {


    override fun onBindViewHolder(holder: CollectionsViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CollectionsViewHolder {
        return CollectionsViewHolder(
            parent.inflate(R.layout.collection_item_for_list),
            onItemClicked,
            onLikeClicked,
            context
        )
    }


    class CollectionsViewHolder(
        override val containerView: View,
        val onItemClicked: (id: String) -> Unit,
        val onLikeClicked: (collection: Collections) -> Unit,
        private val context: Context

    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val viewBinding by viewBinding(CollectionItemForListBinding::bind)


        fun bind(collection: Collections) {


            val blurHash = BlurHashDecoder.blurHashBitmap(itemView.resources, collection)


            with(viewBinding){

                viewBinding.collectionImage.setOnClickListener {
                    onItemClicked(collection.id)
                }
                // bind cover
                Glide.with(collectionImage)
                    .asBitmap()
                    .load(collection.cover_photo.urls.full)
                    .placeholder(blurHash)
                    .into(collectionImage)

                //bindUser

                collectionsUsername.text = collection.cover_photo.user.username
                Glide.with(containerView)
                    .load(collection.cover_photo.user.profile_image.small)
                    .circleCrop()
                    .into(collectionUserImage)

                //bind likes
                var photoCurrentLike = collection.cover_photo.liked_by_user
                var photoCurrentLikesCount = collection.cover_photo.likes

                likeCollectionButtonRed.isVisible = photoCurrentLike
                likeCollectionButtonWhite.isVisible = !photoCurrentLike

                likeCollections.text = photoCurrentLikesCount.toString()


                viewBinding.likeCollectionButtonRed.setOnClickListener {
                    photoCurrentLikesCount -= 1
                    viewBinding.likeCollections.text = photoCurrentLikesCount.toString()
                    onLikeClicked(collection)
                    likeCollectionButtonRed.isVisible = false
                    likeCollectionButtonWhite.isVisible = true
                    photoCurrentLike = false
                }
                viewBinding.likeCollectionButtonWhite.setOnClickListener {
                    photoCurrentLikesCount += 1
                    viewBinding.likeCollections.text = photoCurrentLikesCount.toString()
                    onLikeClicked(collection)
                    likeCollectionButtonRed.isVisible = false
                    likeCollectionButtonWhite.isVisible = true
                    photoCurrentLike = false
                }
                collectionName.text = context.getString(R.string.collection_count_info,
                    collection.total_photos,
                    collection.title)


            }

        }


    }

}
private object CollectionsDifUtilCallback : DiffUtil.ItemCallback<Collections>() {
    override fun areItemsTheSame(oldItem: Collections, newItem: Collections): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Collections, newItem: Collections): Boolean {
        return oldItem == newItem
    }

}
