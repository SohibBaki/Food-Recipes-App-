package ru.asmelnikov.android.foodrecipesapp.presentation.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.adapters.FavoritesAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentSearchBinding
import ru.asmelnikov.android.foodrecipesapp.presentation.MainActivity
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoriteAdapter: FavoritesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = (activity as MainActivity).viewModel
        initAdapter()
        observeSearchLiveData()
        binding?.arrowBack?.setOnClickListener {
            searchMeals()
        }
        var searchJob: Job? = null
        binding?.editTextSearch?.addTextChangedListener {
            searchJob?.cancel()
            searchJob = lifecycleScope.launch {
                delay(500)
                viewModel.searchMeals(it.toString())
            }
        }

        favoriteAdapter.setOnItemClickListener {
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_searchFragment_to_detailsFragment, bundle)
        }
    }

    private fun observeSearchLiveData() {
        viewModel.observeSearchLiveData().observe(viewLifecycleOwner) {
            favoriteAdapter.differ.submitList(it)
        }
    }

    private fun searchMeals() {
        val searchQuery = binding?.editTextSearch?.text.toString()
        if (searchQuery.isNotEmpty()) {
            viewModel.searchMeals(searchQuery)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        favoriteAdapter = FavoritesAdapter()
        binding?.recyclerSearch?.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}