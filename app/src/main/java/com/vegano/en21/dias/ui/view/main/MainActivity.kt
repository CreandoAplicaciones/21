package com.vegano.en21.dias.ui.view.main

import android.os.Bundle
import com.google.android.gms.ads.*
import com.vegano.en21.dias.R
import com.vegano.en21.dias.databinding.ActivityMainBinding
import com.vegano.en21.dias.ui.base.BaseActivity


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()
        binding.adViewSup.loadAd(adRequest)
        binding.adViewInf.loadAd(adRequest)
    }




}
