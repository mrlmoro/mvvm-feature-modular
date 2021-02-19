package com.mrlmoro.core.ui.extensions

import android.content.Context.INPUT_METHOD_SERVICE
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.google.android.material.snackbar.Snackbar
import com.mrlmoro.core.R

fun View.hideKeyboard() {
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(windowToken, 0)
}

fun View.showKeyboard() {
    val inputMethodManager = context.getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
}

fun View.showSnackbar(
    resId: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    hasBottomNav: Boolean = false,
    actionTextId: Int = R.string.retry,
    listener: View.OnClickListener? = null
) = Snackbar.make(this, resId, duration).apply {
    listener?.let { setAction(actionTextId, it) }

    if (hasBottomNav) {
        view.translationY = -context.resources.getDimension(R.dimen.bottom_nav_height)
    }

    show()
}