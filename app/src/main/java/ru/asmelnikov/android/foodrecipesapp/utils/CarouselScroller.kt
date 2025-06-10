package ru.asmelnikov.android.foodrecipesapp.utils

import androidx.recyclerview.widget.RecyclerView
import kotlin.math.abs
import kotlin.math.pow

class CarouselScroller : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
        val centerX = (recyclerView.left + recyclerView.right) / 2
        for (i in 0 until recyclerView.childCount) {
            val child = recyclerView.getChildAt(i)
            val childCenterX = (child.left + child.right) / 2
            val childOffset = abs(centerX - childCenterX) / centerX.toFloat()
            val factor = 0.8.pow(childOffset.toDouble()).toFloat()
            child.scaleX = factor
            child.scaleY = factor
        }
    }
}