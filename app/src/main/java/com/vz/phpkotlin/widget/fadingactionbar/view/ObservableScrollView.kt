package com.vz.phpkotlin.widget.fadingactionbar.view

import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.ScrollView

/**
 * Created by huangwz on 2017/4/26.
 */

class ObservableScrollView : ScrollView, ObservableScrollable {
    // Edge-effects don't mix well with the translucent action bar in Android 2.X
    private val mDisableEdgeEffects = true

    private var mOnScrollChangedListener: OnScrollChangedCallback? = null

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {}

    override fun onScrollChanged(l: Int, t: Int, oldl: Int, oldt: Int) {
        super.onScrollChanged(l, t, oldl, oldt)
        if (mOnScrollChangedListener != null) {
            mOnScrollChangedListener!!.onScroll(l, t)
        }
    }

    override fun getTopFadingEdgeStrength(): Float {
        // http://stackoverflow.com/a/6894270/244576
        if (mDisableEdgeEffects && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return 0.0f
        }
        return super.getTopFadingEdgeStrength()
    }

    override fun getBottomFadingEdgeStrength(): Float {
        // http://stackoverflow.com/a/6894270/244576
        if (mDisableEdgeEffects && Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
            return 0.0f
        }
        return super.getBottomFadingEdgeStrength()
    }

    override fun setOnScrollChangedCallback(callback: OnScrollChangedCallback) {
        mOnScrollChangedListener = callback
    }
}
