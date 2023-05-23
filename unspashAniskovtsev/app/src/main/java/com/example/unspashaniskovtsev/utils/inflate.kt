package com.example.unspashaniskovtsev

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes

fun ViewGroup.inflate(@LayoutRes layRes: Int): View {
    val inflater = LayoutInflater.from(context)
    return inflater.inflate(layRes, this, false)
}
