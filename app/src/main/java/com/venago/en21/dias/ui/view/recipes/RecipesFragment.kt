package com.venago.en21.dias.ui.view.recipes

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sixtema.smartiago.common.extension.observe
import com.venago.en21.dias.R
import com.venago.en21.dias.databinding.FragmentRecipesBinding
import com.venago.en21.dias.extensionfuntions.load
import com.venago.en21.dias.ui.base.BaseFragment
import com.venago.en21.dias.ui.view.main.MainActivity
import com.venago.en21.dias.ui.view.recipes.RecipesViewModel.Event.*

class RecipesFragment : BaseFragment() {

    private val viewModel: RecipesViewModel by viewModels()

    private lateinit var binding: FragmentRecipesBinding
    private val args: RecipesFragmentArgs by navArgs()

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
                binding.textIngredients.text = model.steps
                binding.textNameFood.text = model.title
                binding.imagenFood.load(model.image)

            }
        }

    }
}