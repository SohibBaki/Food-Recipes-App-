package ru.asmelnikov.android.foodrecipesapp.presentation.details

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.asmelnikov.android.foodrecipesapp.db.MealDatabase
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.models.MealList
import ru.asmelnikov.android.foodrecipesapp.retrofit.RetrofitInstance

class DetailsViewModel(
    private val mealDatabase: MealDatabase
) : ViewModel() {

    private val mealDetails = MutableLiveData<List<Meal>>()

    fun getMealById(id: String) {
        RetrofitInstance.api.getMealById(id).enqueue(object : Callback<MealList> {
            override fun onResponse(call: Call<MealList>, response: Response<MealList>) {
                mealDetails.value = response.body()!!.meals
            }

            override fun onFailure(call: Call<MealList>, t: Throwable) {
                Log.e("onFailure get meal by id", t.message.toString())
            }
        })
    }

    fun observeMealDetail(): LiveData<List<Meal>> {
        return mealDetails
    }

    fun insertMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().upsertMeal(meal)
        }
    }

    fun deleteMeal(meal: Meal) {
        viewModelScope.launch {
            mealDatabase.mealDao().deleteMeal(meal)
        }
    }
}