package ru.asmelnikov.android.foodrecipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.asmelnikov.android.foodrecipesapp.databinding.PopularItemsBinding
import ru.asmelnikov.android.foodrecipesapp.models.MealsByCategory
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage


class PopularMealsAdapter :
    RecyclerView.Adapter<PopularMealsAdapter.PopularMealsViewHolder>() {

    inner class PopularMealsViewHolder(val viewBinding: PopularItemsBinding) :
        RecyclerView.ViewHolder(viewBinding.root)

    private val callBack = object : DiffUtil.ItemCallback<MealsByCategory>() {
        override fun areItemsTheSame(oldItem: MealsByCategory, newItem: MealsByCategory): Boolean {
            return oldItem.idMeal == newItem.idMeal
        }

        override fun areContentsTheSame(
            oldItem: MealsByCategory,
            newItem: MealsByCategory
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, callBack)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMealsViewHolder {
        val binding = PopularItemsBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return PopularMealsViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PopularMealsViewHolder, position: Int) {
        val meal = differ.currentList[position]
        holder.viewBinding.imgPopularMealItem.loadImage(meal.strMealThumb)
        holder.viewBinding.root.setOnClickListener {
            onItemClickListener?.let {
                it(meal.idMeal)
            }
        }
        holder.viewBinding.root.setOnLongClickListener {
            onLongItemClick?.invoke(meal)
            true
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    private var onLongItemClick: ((MealsByCategory) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

    fun setOnLongItemClickListener(listener: (MealsByCategory) -> Unit) {
        onLongItemClick = listener
    }

}