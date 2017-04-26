package com.vz.phpkotlin.widget.fadingactionbar

import android.annotation.SuppressLint
import android.app.ActionBar
import android.app.Activity
import android.graphics.drawable.Drawable

/**
 * Created by huangwz on 2017/4/26.
 */

class FadingActionBarHelper : FadingActionBarHelperBase() {

    override fun actionBarHeight(): Int {
        return mActionBar!!.height
    }

    private var mActionBar: ActionBar? = null

    @SuppressLint("NewApi")
    override fun initActionBar(activity: Activity) {
        mActionBar = activity.actionBar
        super.initActionBar(activity)
    }

    override fun isActionBarNull(): Boolean {
        return mActionBar == null
    }

    @SuppressLint("NewApi")
    override fun setActionBarBackgroundDrawable(drawable: Drawable) {
        mActionBar!!.setBackgroundDrawable(drawable)
    }
}
