package com.nyererefy.utilities

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Build
import android.text.Editable
import android.text.Html
import android.text.TextWatcher
import android.view.View
import android.widget.EditText
import android.webkit.MimeTypeMap
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.nyererefy.App
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.nyererefy.R
import java.util.*

@Suppress("DEPRECATION")
fun TextView.htmlText(text: String) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        setText(Html.fromHtml(text, Html.FROM_HTML_MODE_LEGACY))
    } else {
        setText(Html.fromHtml(text))
    }
}

/**
 * Extension function to simplify setting an afterTextChanged action to EditText components.
 */
fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}


fun View.setViewColor(color: Int) {
    this.setBackgroundColor(ContextCompat.getColor(App.appContext, color))
}

fun Uri.getMimeType(context: Context): String? {
    return if (this.scheme == ContentResolver.SCHEME_CONTENT) {
        val cr = context.contentResolver
        cr.getType(this)
    } else {
        val fileExtension = MimeTypeMap.getFileExtensionFromUrl(this.toString())
        MimeTypeMap.getSingleton().getMimeTypeFromExtension(
                fileExtension.toLowerCase(Locale.ENGLISH))
    }
}

fun ImageView.setImageFromUrl(imageUrl: String?) {
    if (!imageUrl.isNullOrEmpty()) {
        Glide.with(this.context)
                .load(imageUrl)
                .placeholder(R.drawable.holder)
                .error(R.drawable.holder)
                .transition(DrawableTransitionOptions.withCrossFade())
                .into(this)
    }
}