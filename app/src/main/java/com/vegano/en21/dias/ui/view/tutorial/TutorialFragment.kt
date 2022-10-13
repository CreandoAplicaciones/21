package com.vegano.en21.dias.ui.view.tutorial

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.vegano.en21.dias.databinding.FragmentTutorialBinding
import com.vegano.en21.dias.save.SaveSheared
import com.vegano.en21.dias.ui.base.BaseFragment
import com.vegano.en21.dias.ui.view.main.MainActivity


class TutorialFragment : BaseFragment() {

    private lateinit var binding: FragmentTutorialBinding
    private lateinit var viewModel: TutorialViewModel

    companion object {
        private const val ARG_SECTION_NUMBER = "section_number"

        fun newInstance(sectionNumber: Int): TutorialFragment {
            return TutorialFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[TutorialViewModel::class.java].apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 3)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = FragmentTutorialBinding.inflate(inflater, container, false)

        val textView: TextView = binding.txtTutorialDescription
        viewModel.text.observe(viewLifecycleOwner) {
            textView.text = getString(it)
        }

        val imageView: ImageView = binding.iVTutorial
        viewModel.image.observe(viewLifecycleOwner) {
            imageView.setImageResource(it)
        }

        val button: Button = binding.btBegin
        viewModel.btn.observe(viewLifecycleOwner) {
            button.visibility = it
            binding.btBegin.setOnClickListener {
                viewModel.didClickOnClickBegin()
                SaveSheared.prefs.saveFirstDay(true)
                startActivity(MainActivity.newIntent(requireContext()))}

        }

        return binding.root
    }
}