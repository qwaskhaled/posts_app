package com.example.test.ui

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    private val inputMethodManager: InputMethodManager by lazy { inputMethodManger()}

    private fun inputMethodManger(): InputMethodManager {
        return requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    }

    fun showSoftInputFromWindow() {
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_IMPLICIT, 0)
    }

    fun hideSoftInputFromWindow(): Boolean {
        return inputMethodManager.hideSoftInputFromWindow(requireView().windowToken, 0)
    }

    fun delayExecution(delayMillis: Long, runnable: () -> Unit) {
        Handler(Looper.getMainLooper()).postDelayed({ runnable.invoke() }, delayMillis)
    }}