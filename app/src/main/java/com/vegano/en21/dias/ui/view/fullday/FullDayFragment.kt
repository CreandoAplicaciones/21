package com.vegano.en21.dias.ui.view.fullday

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.sixtema.smartiago.common.extension.observe
import com.vegano.en21.dias.R
import com.vegano.en21.dias.databinding.FragmentFullDayBinding
import com.vegano.en21.dias.extensionfuntions.load
import com.vegano.en21.dias.ui.base.BaseFragment
import com.vegano.en21.dias.ui.view.fullday.FullDayViewModel.Event.*
import com.vegano.en21.dias.ui.view.recipes.RecipesViewModel


class FullDayFragment : BaseFragment() {

    private val viewModel: FullDayViewModel by viewModels()

    private lateinit var binding: FragmentFullDayBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentFullDayBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        super.init(viewModel)
        viewModel.initFlow()
        viewModel.eventsFlow.observe(viewLifecycleOwner, ::updateUi)
    }


    private fun updateUi(model: FullDayViewModel.Event) {
        when (model) {
            is SetUp -> {
                binding.buttonNext.setOnClickListener { viewModel.nextDay() }
                binding.buttonPrevious.setOnClickListener { viewModel.previousDay() }
                binding.viewBreakfast.setOnClickListener { viewModel.didClickBreakfast() }
                binding.viewLunch.setOnClickListener { viewModel.didClickLunch() }
                binding.viewDinner.setOnClickListener { viewModel.didClickDinner() }
            }
            is ShowBreakfast -> {
                binding.textNameBreakfast.text = model.titleBreakfast
                binding.imageBreakfast.load(model.imageBreakfast)
            }
            is ShowDinner -> {
                binding.textNameDinner.text = model.titleDinner
                binding.imageDinner.load(model.imageDinner)
            }
            is ShowLunch -> {
                binding.textNameLunch.text = model.titleLunch
                binding.imageLunch.load(model.imageLunch)
            }
            is ShowNumberDay -> binding.textDay.text = getString(R.string.full_day_day, model.numberDay)
            is GoToRecipes -> findNavController().navigate(FullDayFragmentDirections.actionFullDayFragmentToRecipesFragment(model.day, model.typeFood))
            is ShowLoad -> binding.progressBar.isVisible = model.isVisible
            is ShowBackButton -> binding.buttonPrevious.text = getString(R.string.full_day_back_to_day, model.day.toString())
            is ShowNextButton -> binding.buttonNext.text = getString(R.string.full_day_go_to_day, model.day.toString())
        }

    }
}