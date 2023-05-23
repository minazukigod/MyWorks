package com.example.unspashaniskovtsev.ui.Auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.example.unspashaniskovtsev.R
import com.example.unspashaniskovtsev.launchAndCollectIn
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.auth_fragment.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.launch
import net.openid.appauth.AuthorizationException
import net.openid.appauth.AuthorizationResponse

@AndroidEntryPoint
class AuthFragment: Fragment(R.layout.auth_fragment) {

    private val viewModel: AuthViewmodel by viewModels()
    private val getAuthResponse = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        val dataIntent = it.data ?: return@registerForActivityResult
        handleAuthResponseIntent(dataIntent)
    }
    private val shared_pref by lazy {
        requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        findNavController().navigate(R.id.authFragment)
        if (shared_pref.getString("token", "")?.isNotEmpty() == true){
            findNavController().navigate(R.id.mainFragment)
        }
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViewModel()
        handleBackPressButton()
    }

    private fun bindViewModel(){
        button2.setOnClickListener { viewModel.openLoginPage() }

        viewModel.openAuthPageFlow.launchAndCollectIn(viewLifecycleOwner){
            openAuthPage(it)
        }

        viewModel.authSuccessFlow.launchAndCollectIn(viewLifecycleOwner){
            findNavController().navigate(R.id.mainFragment)
        }
    }

    private fun openAuthPage(intent: Intent){
        getAuthResponse.launch(intent)
    }

    private fun handleAuthResponseIntent(intent: Intent){
        val exception  = AuthorizationException.fromIntent(intent) // пытаемся получить ошибку - null ок
        val tokenExchangeRequest = AuthorizationResponse.fromIntent(intent)?.createTokenExchangeRequest() // запрос для обмена респонса на токен

        when {
            // ошибка
            exception != null -> {}
            tokenExchangeRequest != null ->
                viewModel.onAuthCodeReceived(tokenExchangeRequest)
        }
    }

    private fun handleBackPressButton(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }


}