package ru.asmelnikov.android.foodrecipesapp.presentation.category_meal

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.asmelnikov.android.foodrecipesapp.models.MealsByCategory
import ru.asmelnikov.android.foodrecipesapp.models.MealsByCategoryList
import ru.asmelnikov.android.foodrecipesapp.retrofit.RetrofitInstance

class CategoryMealsViewModel : ViewModel() {
    private var categoriesMealsLiveData = MutableLiveData<List<MealsByCategory>>()

    fun getCategoriesMeals(name: String) {
        RetrofitInstance.api.getPopularItems(name)
            .enqueue(object : Callback<MealsByCategoryList> {
                override fun onResponse(
                    call: Call<MealsByCategoryList>,
                    response: Response<MealsByCategoryList>
                ) {
                    if (response.body() != null) {
                        categoriesMealsLiveData.value = response.body()!!.meals
                    }
                }

                override fun onFailure(call: Call<MealsByCategoryList>, t: Throwable) {
                    Log.d(
                        "CategoryMeals", t.message.toString()
                    )
                }
            })
    }

    fun observeCategoryMealsLivaData(): LiveData<List<MealsByCategory>> {
        return categoriesMealsLiveData
    }

}