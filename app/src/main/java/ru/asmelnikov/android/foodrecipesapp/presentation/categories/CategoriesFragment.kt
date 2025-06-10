package ru.asmelnikov.android.foodrecipesapp.presentation.categories

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.adapters.CategoriesAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentCategoriesBinding
import ru.asmelnikov.android.foodrecipesapp.presentation.MainActivity
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModel

class CategoriesFragment : Fragment() {

    private var _binding: FragmentCategoriesBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
    private lateinit var categoriesAdapter: CategoriesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        showLoading()

        initAdapter()

        viewModel.getCategories()
        observeCategoriesLiveData()
        categoriesAdapter.setOnItemClickListener {
            val bundle = bundleOf("category_name" to it)
            findNavController().navigate(R.id.action_categoriesFragment_to_categoryMealsFragment, bundle)
        }

    }

    private fun observeCategoriesLiveData() {
        viewModel.observeCategoriesLiveData()
            .observe(viewLifecycleOwner) { categories ->
                categoriesAdapter.differ
                    .submitList(categories)
            }
        stopLoading()
    }

    private fun showLoading() {
        binding?.recyclerCategories?.visibility = View.INVISIBLE
        binding?.loadingGif?.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding?.recyclerCategories?.visibility = View.VISIBLE
        binding?.loadingGif?.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        categoriesAdapter = CategoriesAdapter()
        binding?.recyclerCategories?.apply {
            adapter = categoriesAdapter
            layoutManager = GridLayoutManager(activity, 2, GridLayoutManager.VERTICAL, false)
        }

    }
}