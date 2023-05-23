package com.example.unspashaniskovtsev

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toolbar
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.unspashaniskovtsev.data.Auth.AuthRepository
import com.example.unspashaniskovtsev.databinding.MainFragmentBinding
import com.example.unspashaniskovtsev.ui.Home.Detail.DetailPhotoFragment
import com.example.unspashaniskovtsev.ui.Home.Detail.Id
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.main_fragment.view.*

import net.openid.appauth.AuthorizationService
import net.openid.appauth.EndSessionRequest

@AndroidEntryPoint
class MainFragment: Fragment(R.layout.main_fragment) {

    private val viewBinding by viewBinding(MainFragmentBinding::bind)

    private val shared_prefs by lazy {
        requireContext().getSharedPreferences("shared_prefs", Context.MODE_PRIVATE)
    }
    lateinit var repository: AuthRepository

    private val activityForResult = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){
        repository.logout()
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        repository = AuthRepository(activity?.application!!)
        setUpBottomNav()
        handleBackPressButton()
        requireActivity().setActionBar(viewBinding.toolbar)
    }

    private fun handleBackPressButton(){
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true){
            override fun handleOnBackPressed() {
                activity?.finish()
            }
        })
    }

    private fun setUpBottomNav(){
        val navController = (childFragmentManager.findFragmentById(R.id.fragmentContainerViewMain) as NavHostFragment).navController
        NavigationUI.setupWithNavController(viewBinding.bottomNavMenu, navController)
        navController.addOnDestinationChangedListener { controller, destination, _ ->
            when (destination.id) {
                R.id.photoFragment -> {
                    with(viewBinding) {
                        toolbar.textToolbar.text = getString(R.string.home)
                        toolbar.title = ""
                        imageSearch.isVisible = true
                        imageExit.isVisible = false
                        imageShare.isVisible = false
                        imageBack.isVisible = false
                    }
                }
                R.id.collectionsFragment -> {
                    with(viewBinding){
                        toolbar.textToolbar.text = getString(R.string.collections)
                        toolbar.title = ""
                        imageSearch.isVisible = false
                        imageExit.isVisible = false
                        imageShare.isVisible = false
                        imageBack.isVisible = false

                    }
                }
                R.id.profileFragment -> {
                    with(viewBinding){
                        toolbar.textToolbar.text = getString(R.string.profile)
                        toolbar.title = ""
                        imageSearch.isVisible = false
                        imageExit.isVisible = true
                        imageShare.isVisible = false
                        imageBack.isVisible = false

                    }

                }
                R.id.detailCollectionsFragment -> {
                    with(viewBinding){
                        imageSearch.isVisible = false
                        imageExit.isVisible = false
                        imageShare.isVisible = false
                        imageBack.isVisible = false

                    }
                }
                R.id.detailPhotoFragment -> {
                    with(viewBinding){
                        toolbar.textToolbar.text = getString(R.string.detail_photo)
                        toolbar.title = ""
                        imageSearch.isVisible = false
                        imageExit.isVisible = false
                        imageShare.isVisible = true
                        imageBack.isVisible = false

                    }
                }
                R.id.searchFragment -> {
                    with(viewBinding){
                        toolbar.textToolbar.text = getString(R.string.search)
                        imageSearch.isVisible = false
                        imageExit.isVisible = false
                        imageShare.isVisible = false
                        imageBack.isVisible = true
                    }
                }
            }
            with(viewBinding){
                imageExit.setOnClickListener {
                    setupExitButton()
                }

                imageSearch.setOnClickListener {
                    controller.navigate(R.id.searchFragment)
                }
                imageShare.setOnClickListener {
                    setupShare()
                }
                imageBack.setOnClickListener {
                    findNavController().navigate(R.id.mainFragment)
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        AuthorizationService(requireContext()).dispose()
    }

    private fun setupExitButton() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(getString(R.string.title_alert))
            setMessage(getString(R.string.message_alert))
            setNegativeButton(getString(R.string.no)) { dialog, _ ->
                dialog.dismiss()
            }
            setPositiveButton(getString(R.string.yes)) { _, _ ->
                shared_prefs.edit()
                    .remove("token")
                    .commit()
                val endSessionRequest = repository.getEndSessionRequest()
                val customTabsIntent = CustomTabsIntent.Builder().build()
                val endSessionIntent = AuthorizationService(requireContext()).getEndSessionRequestIntent(endSessionRequest, customTabsIntent)
                activityForResult.launch(endSessionIntent)
                findNavController().navigate(R.id.authFragment)
            }.show()
        }
    }

    private fun setupShare(){
        val photoId = Id.photoId
        val intent = Intent(Intent.ACTION_SEND).apply {
            putExtra(Intent.EXTRA_TEXT, "https://unsplash.com/photos/${photoId}")
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }

}