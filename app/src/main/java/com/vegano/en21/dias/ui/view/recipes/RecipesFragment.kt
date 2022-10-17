package com.vegano.en21.dias.ui.view.recipes

import android.content.ContentValues
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.google.android.gms.ads.*
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.vegano.en21.dias.ui.common.extension.observe
import com.vegano.en21.dias.databinding.FragmentRecipesBinding
import com.vegano.en21.dias.extensionfuntions.load
import com.vegano.en21.dias.ui.base.BaseFragment
import com.vegano.en21.dias.ui.view.fullday.FullDayViewModel
import com.vegano.en21.dias.ui.view.recipes.RecipesViewModel.Event.*

class RecipesFragment : BaseFragment() {

    private val viewModel: RecipesViewModel by viewModels()

    private lateinit var binding: FragmentRecipesBinding
    private val args: RecipesFragmentArgs by navArgs()

    private val adRequest = AdRequest.Builder().build()
    private var mInterstitialAd: InterstitialAd? = null
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.init(viewModel)
        viewModel.initFlow(args.day, args.typeFood)
        viewModel.eventsFlow.observe(viewLifecycleOwner, ::updateUi)
    }

    private fun updateUi(model: RecipesViewModel.Event) {
        when (model) {
            is ShowRecipes -> {
                binding.textRecipes.text = model.steps
                binding.textIngredients.text = model.Ingredients
                binding.textNameFood.text = model.title
                binding.imagenFood.load(model.image)
            }
            is ShowBackground -> {
                binding.textNameFood.setBackgroundColor(ContextCompat.getColor(requireContext(), model.color))
                binding.constraintColor.setBackgroundColor(ContextCompat.getColor(requireContext(), model.color))
            }
            is ShowLoad -> binding.progressBar.isVisible = model.isVisible

            is InitialInterstitial -> {

                MobileAds.initialize(requireContext()) {}
                InterstitialAd.load(requireContext(),"ca-app-pub-4849545913451935/2895475350", adRequest, object : InterstitialAdLoadCallback() {
                    override fun onAdFailedToLoad(adError: LoadAdError) {
                        mInterstitialAd = null
                    }

                    override fun onAdLoaded(interstitialAd: InterstitialAd) {
                        mInterstitialAd = interstitialAd
                    }
                })
                mInterstitialAd?.fullScreenContentCallback = object: FullScreenContentCallback() {
                    override fun onAdClicked() {
                        // Called when a click is recorded for an ad.
                        Log.d(ContentValues.TAG, "Ad was clicked.")
                    }

                    override fun onAdDismissedFullScreenContent() {
                        // Called when ad is dismissed.
                        Log.d(ContentValues.TAG, "Ad dismissed fullscreen content.")
                    }

                    override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                        // Called when ad fails to show.
                        Log.e(ContentValues.TAG, "Ad failed to show fullscreen content.")
                        mInterstitialAd = null
                    }

                    override fun onAdImpression() {
                        // Called when an impression is recorded for an ad.
                        Log.d(ContentValues.TAG, "Ad recorded an impression.")
                    }

                    override fun onAdShowedFullScreenContent() {
                        // Called when ad is shown.
                        Log.d(ContentValues.TAG, "Ad showed fullscreen content.")
                    }
                }
            }
            is ShowInterstitial -> {
                if (mInterstitialAd != null && model.isVisible) {
                    mInterstitialAd?.show(requireActivity())
                    viewModel.showedInterstitial()
                }
            }

        }

    }
}