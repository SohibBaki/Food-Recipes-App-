package ru.asmelnikov.android.foodrecipesapp.presentation.meal_bottom

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentMealBottomBinding
import ru.asmelnikov.android.foodrecipesapp.presentation.MainActivity
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModel
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

private const val MEAL_ID = "param1"

class MealBottomFragment : BottomSheetDialogFragment() {

    private var _binding: FragmentMealBottomBinding? = null
    private val binding get() = _binding
    private var mealId: String? = null
    private lateinit var viewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mealId = it.getString(MEAL_ID)
        }
        viewModel = (activity as MainActivity).viewModel
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMealBottomBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mealId?.let { viewModel.getMealById(it) }
        binding?.bottomSheet?.setOnClickListener {
            val bundle = bundleOf("meal_id" to mealId)
            findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        }

        observeBottomMeal()


    }

    private fun observeBottomMeal() {
        viewModel.observeBottomMeal().observe(viewLifecycleOwner) {
            binding?.imgBottom?.loadImage(it.strMealThumb)
            binding?.tvBottomArea?.text = it.strArea
            binding?.tvBottomCategory?.text = it.strCategory
            binding?.tvMealName?.text = it.strMeal
        }

    }

    companion object {

        @JvmStatic
        fun newInstance(param1: String) =
            MealBottomFragment().apply {
                arguments = Bundle().apply {
                    putString(MEAL_ID, param1)
                }
            }
    }
}