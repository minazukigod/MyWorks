package com.example.unspashaniskovtsev.ui.Onboarding

import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.OnboardingScreenFragmentBinding

class OnboardingScreenFragment: Fragment(R.layout.onboarding_screen_fragment) {

    private val viewBinding by viewBinding(OnboardingScreenFragmentBinding::bind)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(viewBinding){
            onboardingText.setText(requireArguments().getInt(KEY_TEXT))
            button.isVisible =
                requireArguments().getBoolean(KEY_BOOL) == true
            button.setOnClickListener {
                findNavController().navigate(R.id.authFragment)
            }
        }


    }

    companion object {
        const val KEY_TEXT = "text"
        const val KEY_BOOL = "last"
        fun newInstance(@StringRes text: Int, last: Boolean) : Fragment{
            return OnboardingScreenFragment().withArgumnets{
               putInt(KEY_TEXT, text)
               putBoolean(KEY_BOOL, last)
            }
            }
        }

    }

