package com.vegano.en21.dias.extensionfuntions

import android.widget.ImageView
import com.bumptech.glide.Glide


    fun ImageView.load(url: String) {
        if (url.isNotEmpty()) {

            Glide.with(this.context).load(url).into(this)
            Glide.with(context)
                .load(url)
                .centerCrop()
                .into(this);
        }
    }







