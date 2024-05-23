package com.serhiikulyk.payseratestapp.extensions

import android.content.Context
import com.google.android.material.dialog.MaterialAlertDialogBuilder

fun Context.showMaterialAlertDialog(
    title: String,
    message: String,
    positiveButtonText: String,
    onPositiveButtonClick: () -> Unit
) {
    MaterialAlertDialogBuilder(this)
        .setTitle(title)
        .setMessage(message)
        .setPositiveButton(positiveButtonText) { dialog, _ ->
            onPositiveButtonClick()
            dialog.dismiss()
        }
        .show()
}