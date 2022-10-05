package com.vegano.en21.dias.ui.view.main

import android.os.Bundle
import com.vegano.en21.dias.databinding.ActivityMainBinding
import com.vegano.en21.dias.ui.base.BaseActivity


class MainActivity : BaseActivity() {
    private lateinit var binding: ActivityMainBinding



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}
