package com.example.unspashaniskovtsev.ui.Onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment

fun <T: Fragment> T.withArgumnets(action: Bundle.() -> Unit): T {
    return apply {
        val args: Bundle = Bundle().apply(action)
        arguments = args
    }
}
