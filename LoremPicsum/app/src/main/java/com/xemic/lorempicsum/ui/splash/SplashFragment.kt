package com.xemic.lorempicsum.ui.splash

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import com.xemic.lorempicsum.common.base.BaseFragment
import com.xemic.lorempicsum.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashFragment :
    BaseFragment<FragmentSplashBinding>(FragmentSplashBinding::inflate) {

    private val SPLASH_DELAY_MILLIS = 3000L // 몇 초 동안 화면을 유지할 지에 대한 값 

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed({
            moveToMain()
        }, SPLASH_DELAY_MILLIS)
    }

    private fun moveToMain() {
        navController.navigate(SplashFragmentDirections.actionSplashFragmentToMainFragment())
    }
}