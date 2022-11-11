package com.vegano.en21.dias.ui.view.main
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.isVisible
import com.google.android.gms.ads.*
import com.vegano.en21.dias.databinding.ActivityMainBinding
import com.vegano.en21.dias.ui.base.BaseActivity
import com.vegano.en21.dias.ui.common.extension.observe


class MainActivity : BaseActivity() {

    private val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private val adRequest = AdRequest.Builder().build()

    companion object {
        fun newIntent(context: Context) = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel.initFlow()
        viewModel.eventsFlow.observe(this, ::updateUi)

    }

    private fun updateUi(model: MainViewModel.Event) {
        when (model) {
            is MainViewModel.Event.SetUp -> {
                MobileAds.initialize(this) {}
                binding.adViewSup.loadAd(adRequest)
                binding.adViewInf.loadAd(adRequest)
            }
            is MainViewModel.Event.ShowBanner -> {
                binding.adViewInf.isVisible = model.isVisible
                binding.adViewSup.isVisible = model.isVisible
            }
        }
    }


}


