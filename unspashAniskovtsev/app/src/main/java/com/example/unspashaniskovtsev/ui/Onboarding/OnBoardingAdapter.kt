package com.example.unspashaniskovtsev.ui.Onboarding

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class OnBoardingAdapter(
    private val screens: List<OnboardingScreen>,
    fragment: Fragment
): FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return screens.size
    }

    override fun createFragment(position: Int): Fragment {
        val screen: OnboardingScreen = screens[position]
        if (position == screens.size-1){
            return OnboardingScreenFragment.newInstance(screen.text, true)
        }
        return OnboardingScreenFragment.newInstance(screen.text, false)
    }
}