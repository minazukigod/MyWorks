package com.example.unspashaniskovtsev.ui.Onboarding

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.databinding.OnboardFragmentBinding
import kotlin.math.abs

class OnboardFragment: Fragment(R.layout.onboard_fragment) {

    private val screens = listOf(
        OnboardingScreen(R.string.onboardTextFirstPage),
        OnboardingScreen(R.string.onboardTextSecondPage),
        OnboardingScreen(R.string.onboardTextThirdPage)
    )

    private val shared_pref by lazy {
        requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
    }

    private val viewBinding by viewBinding(OnboardFragmentBinding::bind)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!shared_pref.getBoolean("isNeedOnboarding", true)){
            findNavController().navigate(R.id.authFragment)
        } else {
            shared_pref.edit()
                .putBoolean("isNeedOnboarding", false)
                .apply()
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = OnBoardingAdapter(screens, this)

        with(viewBinding){
            viewPager.adapter = adapter
            wormDotsIndicator.attachTo(viewPager)
            setPageTransformer(viewBinding)
        }

    }

    private fun setPageTransformer(viewBinding: OnboardFragmentBinding){
        with(viewBinding){
            viewPager.setPageTransformer { page, position ->
                if (position < -1){    // [-Infinity,-1)
                    // This page is way off-screen to the left.
                    page.alpha = 0F;

                }
                else if (position <= 0){    // [-1,0]
                    page.alpha = 1F;
                    page.translationX = 0F;
                    page.scaleX = 1F;
                    page.scaleY = 1F;

                }
                else if (position <= 1){    // (0,1]
                    page.translationX = -position*page.width;
                    page.alpha = 1- abs(position);
                    page.scaleX = 1- abs(position);
                    page.scaleY = 1- abs(position);

                }
                else {    // (1,+Infinity]
                    // This page is way off-screen to the right.
                    page.alpha = 0F;

                }

            }
        }

    }
}