package com.birdeveloper.springyview.sample.util

import android.widget.ImageView
import androidx.annotation.DrawableRes
import coil.load
import coil.transform.CircleCropTransformation

object AppExtention {
    fun ImageView.setImageDrawableWithAnimation(imageUrl: String, duration: Long = 300) {
        if (drawable != null) {
            animate()
                .alpha(0f)
                .setDuration(duration)
                .withEndAction {
                    load(imageUrl) {
                        crossfade(true)
                        placeholder(android.R.drawable.ic_menu_gallery)
                        transformations(CircleCropTransformation())
                     }
                    animate()
                        .alpha(1f)
                        .setDuration(duration)
                }

        } else if (drawable == null) {
            setAlpha(0f)
            load(imageUrl) {
                crossfade(true)
                placeholder(android.R.drawable.ic_menu_gallery)
                transformations(CircleCropTransformation())
            }
            animate()
                .alpha(1f)
                .setDuration(duration)
        }
    }
}