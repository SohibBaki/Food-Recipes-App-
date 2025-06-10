package ru.asmelnikov.android.foodrecipesapp.utils

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import ru.asmelnikov.android.foodrecipesapp.R

fun ImageView.loadImage(url: String?) {
    Glide.with(this)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .placeholder(R.color.white)
        .error(R.drawable.ic_error_image)
        .into(this)
}
