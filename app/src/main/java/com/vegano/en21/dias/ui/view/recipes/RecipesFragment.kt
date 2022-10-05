package com.vegano.en21.dias.ui.view.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.sixtema.smartiago.common.extension.observe
import com.vegano.en21.dias.R
import com.vegano.en21.dias.databinding.FragmentRecipesBinding
import com.vegano.en21.dias.extensionfuntions.load
import com.vegano.en21.dias.ui.base.BaseFragment
import com.vegano.en21.dias.ui.view.recipes.RecipesViewModel.Event.*

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
                binding.textIngredients.text = model.Ingredients
                binding.textNameFood.text = model.title
                binding.imagenFood.load(model.image)
            }
            is ShowBackground -> {
                binding.textNameFood.setBackgroundColor(ContextCompat.getColor(requireContext(), model.color))
                binding.constraintColor.setBackgroundColor(ContextCompat.getColor(requireContext(), model.color))
            }
            is ShowLoad -> binding.progressBar.isVisible = model.isVisible
        }

    }
}