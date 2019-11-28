package com.accelo.util

import android.view.View
import android.view.View.*
import androidx.databinding.BindingAdapter

/**
 * Created by Yuri Popiv on 11/28/2019.
 */


@BindingAdapter("visibleUnless")
fun visibleUnless(view: View, visible: Boolean){
    view.visibility = if (visible) VISIBLE else GONE
}

@BindingAdapter("goneUnless")
fun goneUnless(view: View, gone: Boolean){
    view.visibility = if (gone) GONE else VISIBLE
}