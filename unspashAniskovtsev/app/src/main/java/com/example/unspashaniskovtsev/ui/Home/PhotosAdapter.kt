package com.example.unspashaniskovtsev.ui.Home


import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.bumptech.glide.Glide
import com.example.unspashaniskovtsev.Models.Photo
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.PhotoItemForListBinding
import com.example.unspashaniskovtsev.inflate
import com.example.unspashaniskovtsev.utils.BlurHashDecoder
import kotlinx.android.extensions.LayoutContainer


class PhotosAdapter(
    private val onItemClicked: (id: String) -> Unit,
    private val onLikeClicked: (photo: Photo, position: Int) -> Unit,
) : PagingDataAdapter<Photo, PhotosAdapter.PhotoViewHolder>(PhotoDifUtilCallback) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PhotoViewHolder {
        return PhotoViewHolder(
            parent.inflate(R.layout.photo_item_for_list),
            onItemClicked,
            onLikeClicked
        )
    }

    override fun onBindViewHolder(holder: PhotoViewHolder, position: Int) {
        getItem(position)?.let { holder.bind(it) }
    }

    class PhotoViewHolder(
        override val containerView: View,
        val onItemClicked: (id: String) -> Unit,
        val onLikeClicked: (photo: Photo, position: Int) -> Unit,

    ) : RecyclerView.ViewHolder(containerView), LayoutContainer {

        private val viewBinding by viewBinding(PhotoItemForListBinding::bind)



        fun bind(photo: Photo) {


            val blurHash = BlurHashDecoder.blurHashBitmap(containerView.resources, photo)
            viewBinding.photoItemImage.setOnClickListener {
                onItemClicked(photo.id)
            }

            with(viewBinding) {


                Glide.with(photoItemImage)
                    .asBitmap()
                    .load(photo.urls.full)
                    .placeholder(blurHash)
                    .error(blurHash)
                    .into(photoItemImage)

                Glide.with(containerView)
                    .load(photo.user.profile_image.small)
                    .circleCrop()
                    .into(photoOwnerImage)

                var photoCurrentLike = photo.liked_by_user
                var photoCurrentLikesCount = photo.likes

                likesCounter.text = photoCurrentLikesCount.toString()
                likeRed.isVisible = photoCurrentLike
                likeWhite.isVisible = !photoCurrentLike

                likeRed.setOnClickListener {
                    photoCurrentLikesCount -= 1
                    likesCounter.text = photoCurrentLikesCount.toString()
                    onLikeClicked(photo, bindingAdapterPosition)
                    likeRed.isVisible = false
                    likeWhite.isVisible = true
                    photoCurrentLike = false
                }
                likeWhite.setOnClickListener {
                    photoCurrentLikesCount += 1
                    likesCounter.text = photoCurrentLikesCount.toString()
                    onLikeClicked(photo, bindingAdapterPosition)
                    likeWhite.isVisible = false
                    likeRed.isVisible = true
                    photoCurrentLike = true
                }

            }

        }


    }

}



object PhotoDifUtilCallback : DiffUtil.ItemCallback<Photo>() {
    override fun areItemsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Photo, newItem: Photo): Boolean {
        return oldItem == newItem
    }
}