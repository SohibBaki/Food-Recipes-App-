package ru.asmelnikov.android.foodrecipesapp.presentation.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import ru.asmelnikov.android.foodrecipesapp.R
import ru.asmelnikov.android.foodrecipesapp.databinding.FragmentDetailsBinding
import ru.asmelnikov.android.foodrecipesapp.models.Meal
import ru.asmelnikov.android.foodrecipesapp.presentation.MainActivity
import ru.asmelnikov.android.foodrecipesapp.presentation.home.HomeViewModel
import ru.asmelnikov.android.foodrecipesapp.utils.NotificationHelper
import ru.asmelnikov.android.foodrecipesapp.utils.loadImage

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding
    private val bundleArgs: DetailsFragmentArgs by navArgs()
    private lateinit var dtMeal: Meal
    private lateinit var viewModel: HomeViewModel
    private lateinit var notificationHelper: NotificationHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding?.root
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = (activity as MainActivity).viewModel
        notificationHelper = NotificationHelper(requireContext())

        // Setup WebView
        binding?.webview?.apply {
            settings.javaScriptEnabled = true
            settings.mediaPlaybackRequiresUserGesture = false
            settings.domStorageEnabled = true
            settings.cacheMode = WebSettings.LOAD_NO_CACHE
            webChromeClient = WebChromeClient()
            webViewClient = WebViewClient()
        }

        showLoading()
        val mealArgs = bundleArgs.mealId
        viewModel.getMealById(mealArgs)
        viewModel.observeBottomMeal().observe(
            viewLifecycleOwner
        ) { meal ->
            meal?.let {
                setTextsInViews(it)
                setupVideo(it.strYoutube)
                stopLoading()
            }
        }
    }

    private fun setupVideo(youtubeUrl: String?) {
        youtubeUrl?.let { url ->
            val videoId = extractYoutubeVideoId(url)
            val embedHtml = """
                <html>
                    <body style='margin:0;padding:0;'>
                        <iframe 
                            width="100%" 
                            height="100%" 
                            src="https://www.youtube.com/embed/$videoId" 
                            frameborder="0" 
                            allowfullscreen>
                        </iframe>
                    </body>
                </html>
            """.trimIndent()
            
            binding?.webview?.loadData(embedHtml, "text/html", "utf-8")
        }
    }

    private fun extractYoutubeVideoId(url: String): String {
        return url.split("v=")[1].split("&")[0]
    }

    private fun setTextsInViews(meal: Meal) {
        this.dtMeal = meal
        binding?.collapsingToolBar?.title = meal.strMeal
        binding?.content?.text = meal.strInstructions
        binding?.tvCategories?.text = getString(R.string.categories2, meal.strCategory)
        binding?.tvArea?.text = getString(R.string.area, meal.strArea)
        meal.strMealThumb.let {
            binding?.imgMealDetails?.loadImage(meal.strMealThumb)
        }
        binding?.btnFavoritesAdd?.setOnClickListener {
            viewModel.insertMeal(meal)
            Toast.makeText(requireContext(), R.string.meal_saved, Toast.LENGTH_LONG).show()
            
            notificationHelper.showRecipeReminder(
                meal.strMeal ?: "New Recipe Added",
                "Don't forget to try this delicious ${meal.strCategory} recipe from ${meal.strArea}!"
            )
        }
    }

    private fun showLoading() {
        binding?.collapsingToolBar?.visibility = View.INVISIBLE
        binding?.tvCategories?.visibility = View.INVISIBLE
        binding?.tvArea?.visibility = View.INVISIBLE
        binding?.progressBar?.visibility = View.VISIBLE
        binding?.btnFavoritesAdd?.visibility = View.GONE
        binding?.webview?.visibility = View.INVISIBLE
    }

    private fun stopLoading() {
        binding?.collapsingToolBar?.visibility = View.VISIBLE
        binding?.tvCategories?.visibility = View.VISIBLE
        binding?.tvArea?.visibility = View.VISIBLE
        binding?.progressBar?.visibility = View.GONE
        binding?.btnFavoritesAdd?.visibility = View.VISIBLE
        binding?.webview?.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding?.webview?.loadUrl("about:blank")
        _binding = null
    }
}