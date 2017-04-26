package com.vz.phpkotlin.widget

import android.content.Context
import android.support.v4.widget.SwipeRefreshLayout
import android.util.AttributeSet

class AutoSwipeRefreshLayout : SwipeRefreshLayout {

    private var mMeasured = false
    private var mPreMeasureRefreshing = false

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (!mMeasured) {
            mMeasured = true
            isRefreshing = mPreMeasureRefreshing
        }
    }

    override fun setRefreshing(refreshing: Boolean) {
        if (mMeasured) {
            super.setRefreshing(refreshing)
        } else {
            mPreMeasureRefreshing = refreshing
        }
    }

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}

}
