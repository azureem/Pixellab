package com.example.piceditor.splash

import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.piceditor.R
import com.example.piceditor.databinding.ScreenSplashBinding
import kotlinx.coroutines.delay

class Splash : Fragment() {
    var binding: ScreenSplashBinding? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = ScreenSplashBinding.inflate(layoutInflater, container, false)
        return binding!!.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requireActivity().window.statusBarColor = Color.parseColor("#011627")
        requireActivity().window.navigationBarColor = Color.parseColor("#011627")
        object : CountDownTimer(1500, 900) {
            override fun onTick(millisUntilFinished: Long) {
            }

            override fun onFinish() {
              findNavController().navigate(R.id.action_splash_to_mainScreen)
            }

        }.start()
    }

}