package com.airei.app.phc.attendance.ui

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.OnBackPressedCallback
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.airei.app.phc.attendance.R
import com.airei.app.phc.attendance.common.AppPreferences
import com.airei.app.phc.attendance.databinding.FragmentSplashBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SplashFragment : Fragment() {
    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!

    val handler = Handler(Looper.getMainLooper())
    private lateinit var runnable: Runnable

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentSplashBinding.inflate(layoutInflater, container, false)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back press to exit app
        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {

                }
            })

       /* // 🕒 Wait 3 seconds, then navigate
        viewLifecycleOwner.lifecycleScope.launch {
            delay(3000) // 3 seconds
            redirectToHome()
        }*/
    }

    override fun onPause() {
        super.onPause()
        if (::runnable.isInitialized){
            handler.removeCallbacks(runnable)
        }
    }

    override fun onResume() {
        super.onResume()
        animateText(binding.txtAppName)
    }

    private fun animateText(textView: TextView) {
        val fullText = "MY PALM ATTENDANCE APP"
        textView.text = ""

        var index = 0
        val handler = Handler(Looper.getMainLooper())
        val delay: Long = 80 // typing speed (ms per letter)

        runnable = object : Runnable {
            override fun run() {
                if (index <= fullText.length) {
                    textView.text = fullText.substring(0, index)
                    index++
                    handler.postDelayed(this, delay)
                } else {
                    handler.postDelayed({
                        if (isAdded && view != null) {
                            Log.d(TAG, "✅ Animation complete, navigating...")
                            redirectToHome()
                        } else {
                            Log.w(TAG, "⏸️ Fragment not attached, skipping redirect")
                        }
                    }, 1000)

                }
            }
        }

        handler.post(runnable)
    }


    private fun redirectToHome() {
        Log.d(TAG, "redirectToHome: ")
        val navController = findNavController()
        val currentDestination = navController.currentDestination?.id
        val targetDestination = when {
            AppPreferences.loginId.isEmpty() -> R.id.loginFragment
            !AppPreferences.isDataDownloaded -> R.id.onlineDataSyncFragment
            else -> R.id.attendanceHomeFragment
        }

        // ✅ Avoid navigating if already on the same screen
        if (currentDestination != targetDestination) {
            try {
                navController.navigate(targetDestination)
            } catch (e: IllegalArgumentException) {
                // In case navigation graph state changes (rare)
                e.printStackTrace()
            }
        }
    }




    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TAG: String = "SplashFragment"
    }
}

