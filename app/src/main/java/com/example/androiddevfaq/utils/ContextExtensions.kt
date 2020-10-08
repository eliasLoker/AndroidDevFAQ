package com.example.androiddevfaq.utils

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.AdapterView
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

fun EditText.onTextChanged(text: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            text.invoke(s.toString())
        }

        override fun afterTextChanged(s: Editable?) { }
    })
}

fun Spinner.onItemSelected(selectedPosition: (Int) -> Unit) {
    this.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
        override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
            selectedPosition.invoke(position)
        }

        override fun onNothingSelected(parent: AdapterView<*>?) {}
    }
}

fun Context.showToast(message: String, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(this, message, duration).show()
}

fun Fragment.navigate(@IdRes actionID: Int, bundle: Bundle? = null) {
    this.findNavController().navigate(actionID, bundle)
}

fun Fragment.popBackStack() {
    this.findNavController().popBackStack()
}