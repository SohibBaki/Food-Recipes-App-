package ru.asmelnikov.android.foodrecipesapp.presentation.category_meal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.adapters.CategoriesMealsAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentCategoryMealsBinding


class CategoryMealsFragment : Fragment() {

    private var _binding: FragmentCategoryMealsBinding? = null
    private val binding get() = _binding
    private val viewModel by viewModels<CategoryMealsViewModel>()
    private val bundleArgs: CategoryMealsFragmentArgs by navArgs()
    private lateinit var categoriesMealsAdapter: CategoriesMealsAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryMealsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showLoading()
        initAdapter()
        val mealArgs = bundleArgs.categoryName
        viewModel.getCategoriesMeals(mealArgs)

        viewModel.observeCategoryMealsLivaData()
            .observe(viewLifecycleOwner) { mealsList ->
                binding?.tvCategoryCount?.text =
                    getString(R.string.categories_meals, mealArgs, mealsList.size.toString())
                categoriesMealsAdapter.differ.submitList(mealsList)
                stopLoading()
            }
        categoriesMealsAdapter.setOnItemClickListener {
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_categoryMealsFragment_to_detailsFragment, bundle)
        }

    }

    private fun showLoading() {
        binding?.tvCategoryCount?.visibility = View.INVISIBLE
        binding?.recyclerCategoryMeal?.visibility = View.INVISIBLE
        binding?.loadingGif?.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding?.tvCategoryCount?.visibility = View.VISIBLE
        binding?.recyclerCategoryMeal?.visibility = View.VISIBLE
        binding?.loadingGif?.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        categoriesMealsAdapter = CategoriesMealsAdapter()
        binding?.recyclerCategoryMeal?.apply {
            adapter = categoriesMealsAdapter
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        }
    }

}