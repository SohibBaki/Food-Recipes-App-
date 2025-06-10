package ru.asmelnikov.android.foodrecipesapp.presentation.details

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.asmelnikov.android.foodrecipesapp.db.MealDatabase

class DetailsViewModelFactory(
    private val mealDatabase: MealDatabase
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return DetailsViewModel(mealDatabase) as T
    }
}