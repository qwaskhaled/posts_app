package com.example.test.util

import android.content.Context
import android.graphics.drawable.Drawable
import com.bumptech.glide.GlideBuilder
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.module.AppGlideModule
import com.bumptech.glide.request.RequestOptions
import com.example.test.R


@GlideModule
class GlideModule : AppGlideModule() {

    override fun applyOptions(context: Context, builder: GlideBuilder) {
        val requestOptions = RequestOptions()
            .error(R.color.colorPlaceholder)
            .placeholder(R.color.colorPlaceholder)

        builder.setDefaultRequestOptions(requestOptions)
            .setDefaultTransitionOptions(
                Drawable::class.java,
                DrawableTransitionOptions.withCrossFade()
            )
    }
}


