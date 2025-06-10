package ru.asmelnikov.android.foodrecipesapp.presentation.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.adapters.FavoritesAdapter
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentFavoritesBinding
import ru.asmelnikov.android.foodrecipesapp.presentation.MainActivity
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModel

class FavoritesFragment : Fragment() {

    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding
    private lateinit var viewModel: HomeViewModel
    private lateinit var favoriteAdapter: FavoritesAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel

        showLoading()

        observeFavorites()

        initAdapter()

        favoriteAdapter.setOnItemClickListener {
            val bundle = bundleOf("meal_id" to it)
            findNavController().navigate(R.id.action_favoritesFragment_to_detailsFragment, bundle)
        }

        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.RIGHT or ItemTouchHelper.LEFT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ) = true

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val meal = favoriteAdapter.differ.currentList[position]
                viewModel.deleteMeal(meal)
                Snackbar.make(view, R.string.meal_deleted, Snackbar.LENGTH_LONG).apply {
                    setAction(
                        R.string.undo
                    ) {
                        viewModel.insertMeal(meal)
                    }.show()
                }
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(binding?.recyclerFavorites)
    }

    private fun observeFavorites() {
        viewModel.observeFavoritesLiveData().observe(viewLifecycleOwner) { meals ->
            favoriteAdapter.differ.submitList(meals)
            stopLoading()
        }
    }

    private fun showLoading() {
        binding?.recyclerFavorites?.visibility = View.INVISIBLE
        binding?.loadingGif?.visibility = View.VISIBLE
    }

    private fun stopLoading() {
        binding?.recyclerFavorites?.visibility = View.VISIBLE
        binding?.loadingGif?.visibility = View.INVISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initAdapter() {
        favoriteAdapter = FavoritesAdapter()
        binding?.recyclerFavorites?.apply {
            adapter = favoriteAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }
}