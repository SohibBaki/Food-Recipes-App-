package ru.asmelnikov.android.foodrecipesapp.retrofit

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import ru.asmelnikov.android.foodrecipesapp.models.CategoryList
import ru.asmelnikov.android.foodrecipesapp.models.MealList
import ru.asmelnikov.android.foodrecipesapp.models.MealsByCategoryList

interface MealApi {

    @GET("random.php")
    fun getRandomMeal(): Call<MealList>

    @GET("lookup.php")
    fun getMealById(@Query("i") id: String): Call<MealList>

    @GET("filter.php")
    fun getPopularItems(@Query("c") categoryName: String): Call<MealsByCategoryList>

    @GET("categories.php")
    fun getCategories(): Call<CategoryList>

    @GET("search.php")
    fun searchMeals(@Query("s") searchQuery: String): Call<MealList>
}