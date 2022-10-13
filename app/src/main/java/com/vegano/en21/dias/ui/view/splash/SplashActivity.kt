package com.vegano.en21.dias.ui.view.splash


import android.os.Bundle
import androidx.activity.viewModels
import androidx.core.view.WindowCompat
import com.vegano.en21.dias.databinding.ActivitySplashBinding
import com.vegano.en21.dias.ui.base.BaseActivity
import com.vegano.en21.dias.ui.common.extension.observe
import com.vegano.en21.dias.ui.view.main.MainActivity
import com.vegano.en21.dias.ui.view.splash.SplashViewModel.Event.*
import com.vegano.en21.dias.ui.view.tutorial.TutorialActivity

class SplashActivity : BaseActivity() {

    private lateinit var binding: ActivitySplashBinding
    private val viewModel: SplashViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        super.init(viewModel)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.eventsFlow.observe(this, ::updateUi)
    }

    private fun updateUi(model: SplashViewModel.Event) {
        when (model) {
            is GoToMain -> startActivity(MainActivity.newIntent(this))
            is GoToTutorial -> startActivity(TutorialActivity.newIntent(this))
        }
        finish()
    }
}