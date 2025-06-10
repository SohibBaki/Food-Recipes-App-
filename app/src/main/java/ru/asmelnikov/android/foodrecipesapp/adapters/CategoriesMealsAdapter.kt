package ru.asmelnikov.android.foodrecipesapp.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import ru.asmelnikov.android.foodrecipesapp.databinding.MealItemBinding
import ru.asmelnikov.android.foodrecipesapp.models.MealsByCategory
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class CategoriesMealsAdapter :
    RecyclerView.Adapter<CategoriesMealsAdapter.CategoriesViewHolder>() {

    inner class CategoriesViewHolder(val viewBinding: MealItemBinding) :
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoriesViewHolder {
        val binding = MealItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return CategoriesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoriesViewHolder, position: Int) {
        val meal = differ.currentList[position]
        holder.viewBinding.imgCategoryMeals.loadImage(meal.strMealThumb)
        holder.viewBinding.tvMealName.text = meal.strMeal
        holder.viewBinding.root.setOnClickListener {
            onItemClickListener?.let {
                it(meal.idMeal)
            }
        }
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener: ((String) -> Unit)? = null

    fun setOnItemClickListener(listener: (String) -> Unit) {
        onItemClickListener = listener
    }

}