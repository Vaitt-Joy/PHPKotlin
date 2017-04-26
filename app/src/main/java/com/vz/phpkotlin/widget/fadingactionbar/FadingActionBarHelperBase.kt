package com.vz.phpkotlin.widget.fadingactionbar

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.View.MeasureSpec
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams
import android.widget.AbsListView
import android.widget.AbsListView.OnScrollListener
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ListView
import com.vz.phpkotlin.R
import com.vz.phpkotlin.utils.ToolUI
import com.vz.phpkotlin.widget.fadingactionbar.view.ObservableScrollView
import com.vz.phpkotlin.widget.fadingactionbar.view.ObservableWebViewWithHeader
import com.vz.phpkotlin.widget.fadingactionbar.view.OnScrollChangedCallback
import java.lang.reflect.InvocationTargetException

/**
 * Created by huangwz on 2017/4/26.
 */

abstract class FadingActionBarHelperBase {

    private var mActionBarBackgroundDrawable: Drawable? = null
    private var mHeaderContainer: FrameLayout? = null
    private var mActionBarBackgroundResId: Int = 0
    private var mHeaderLayoutResId: Int = 0
    private var mHeaderView: View? = null
    private var mHeaderOverlayLayoutResId: Int = 0
    private var mHeaderOverlayView: View? = null
    private var mContentLayoutResId: Int = 0
    private var mContentView: View? = null
    private var mInflater: LayoutInflater? = null
    private var mLightActionBar: Boolean = false
    private var mUseParallax = true
    private var mLastDampedScroll: Int = 0
    private var mLastHeaderHeight = -1
    private var mFirstGlobalLayoutPerformed: Boolean = false
    private var mMarginView: FrameLayout? = null
    private var mListViewBackgroundView: View? = null

    fun <T : FadingActionBarHelperBase> actionBarBackground(drawableResId: Int): T {
        mActionBarBackgroundResId = drawableResId
        return this as T
    }

    fun <T : FadingActionBarHelperBase> actionBarBackground(drawable: Drawable): T {
        mActionBarBackgroundDrawable = drawable
        return this as T
    }

    fun <T : FadingActionBarHelperBase> headerLayout(layoutResId: Int): T {
        mHeaderLayoutResId = layoutResId
        return this as T
    }

    fun <T : FadingActionBarHelperBase> headerView(view: View): T {
        mHeaderView = view
        return this as T
    }

    fun <T : FadingActionBarHelperBase> headerOverlayLayout(layoutResId: Int): T {
        mHeaderOverlayLayoutResId = layoutResId
        return this as T
    }

    fun <T : FadingActionBarHelperBase> headerOverlayView(view: View): T {
        mHeaderOverlayView = view
        return this as T
    }

    fun <T : FadingActionBarHelperBase> contentLayout(layoutResId: Int): T {
        mContentLayoutResId = layoutResId
        return this as T
    }

    fun <T : FadingActionBarHelperBase> contentView(view: View): T {
        mContentView = view
        return this as T
    }

    fun <T : FadingActionBarHelperBase> lightActionBar(value: Boolean): T {
        mLightActionBar = value
        return this as T
    }

    fun <T : FadingActionBarHelperBase> parallax(value: Boolean): T {
        mUseParallax = value
        return this as T
    }

    fun createView(context: Context): View {
        return createView(LayoutInflater.from(context))
    }

    fun createView(inflater: LayoutInflater): View {
        //
        // Prepare everything

        mInflater = inflater
        if (mContentView == null) {
            mContentView = inflater.inflate(mContentLayoutResId, null)
        }
        if (mHeaderView == null) {
            mHeaderView = inflater.inflate(mHeaderLayoutResId, null, false)
        }

        //
        // See if we are in a ListView, WebView or ScrollView scenario

        val listView = mContentView!!.findViewById(android.R.id.list) as ListView
        val root: View
        if (listView != null) {
            root = createListView(listView)
        } else if (mContentView is ObservableWebViewWithHeader) {
            root = createWebView()
        } else {
            root = createScrollView()
        }

        if (mHeaderOverlayView == null && mHeaderOverlayLayoutResId != 0) {
            mHeaderOverlayView = inflater.inflate(mHeaderOverlayLayoutResId, mMarginView, false)
        }
        if (mHeaderOverlayView != null) {
            mMarginView!!.addView(mHeaderOverlayView)
        }

        // Use measured height here as an estimate of the header height, later on after the layout is complete
        // we'll use the actual height
        val widthMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.MATCH_PARENT, MeasureSpec.EXACTLY)
        val heightMeasureSpec = MeasureSpec.makeMeasureSpec(LayoutParams.WRAP_CONTENT, MeasureSpec.EXACTLY)
        mHeaderView!!.measure(widthMeasureSpec, heightMeasureSpec)
        updateHeaderHeight(mHeaderView!!.measuredHeight)

        root.viewTreeObserver.addOnGlobalLayoutListener {
            val headerHeight = mHeaderContainer!!.height
            if (!mFirstGlobalLayoutPerformed && headerHeight != 0) {
                updateHeaderHeight(headerHeight)
                mFirstGlobalLayoutPerformed = true
            }
        }
        return root
    }

    open fun initActionBar(activity: Activity) {
        if (mActionBarBackgroundDrawable == null) {
            mActionBarBackgroundDrawable = activity.resources.getDrawable(mActionBarBackgroundResId)
        }
        setActionBarBackgroundDrawable(mActionBarBackgroundDrawable!!)
        if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN) {
            mActionBarBackgroundDrawable!!.callback = mDrawableCallback
        }
        mActionBarBackgroundDrawable!!.alpha = 0
    }

    protected abstract fun actionBarHeight(): Int
    protected abstract fun isActionBarNull(): Boolean
    protected abstract fun setActionBarBackgroundDrawable(drawable: Drawable)

    protected fun <T> getActionBarWithReflection(activity: Activity, methodName: String): T? {
        try {
            val method = activity.javaClass.getMethod(methodName)
            return method.invoke(activity) as T
        } catch (e: NoSuchMethodException) {
            e.printStackTrace()
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: InvocationTargetException) {
            e.printStackTrace()
        } catch (e: ClassCastException) {
            e.printStackTrace()
        }

        return null
    }

    private val mDrawableCallback = object : Drawable.Callback {
        override fun invalidateDrawable(who: Drawable) {
            setActionBarBackgroundDrawable(who)
        }

        override fun scheduleDrawable(who: Drawable, what: Runnable, `when`: Long) {}

        override fun unscheduleDrawable(who: Drawable, what: Runnable) {}
    }

    private fun createWebView(): View {
        val webViewContainer = mInflater!!.inflate(R.layout.fab__webview_container, null) as ViewGroup

        val webView = mContentView as ObservableWebViewWithHeader?
        webView!!.setOnScrollChangedCallback(mOnScrollChangedListener)

        webViewContainer.addView(webView)

        mHeaderContainer = webViewContainer.findViewById(R.id.fab__header_container) as FrameLayout
        initializeGradient(mHeaderContainer!!)
        mHeaderContainer!!.addView(mHeaderView, 0)

        mMarginView = FrameLayout(webView.context)
        mMarginView!!.setBackgroundColor(Color.TRANSPARENT)
        mMarginView!!.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        webView.addView(mMarginView)

        return webViewContainer
    }

    private fun createScrollView(): View {
        val scrollViewContainer = mInflater!!.inflate(R.layout.fab__scrollview_container, null) as ViewGroup

        val scrollView = scrollViewContainer.findViewById(R.id.fab__scroll_view) as ObservableScrollView
        scrollView.setOnScrollChangedCallback(mOnScrollChangedListener)

        val contentContainer = scrollViewContainer.findViewById(R.id.fab__container) as ViewGroup
        val layoutParams = LinearLayout.LayoutParams(
                LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        mContentView!!.layoutParams = layoutParams
        contentContainer.addView(mContentView)
        mHeaderContainer = scrollViewContainer.findViewById(R.id.fab__header_container) as FrameLayout
        initializeGradient(mHeaderContainer!!)
        mHeaderContainer!!.addView(mHeaderView, 0)
        mMarginView = contentContainer.findViewById(R.id.fab__content_top_margin) as FrameLayout

        return scrollViewContainer
    }

    private val mOnScrollChangedListener = object : OnScrollChangedCallback {
        override fun onScroll(l: Int, t: Int) {
            onNewScroll(t)
        }
    }

    private fun createListView(listView: ListView): View {
        val contentContainer = mInflater!!.inflate(R.layout.fab__listview_container, null) as ViewGroup
        contentContainer.addView(mContentView)

        mHeaderContainer = contentContainer.findViewById(R.id.fab__header_container) as FrameLayout
        initializeGradient(mHeaderContainer!!)
        mHeaderContainer!!.addView(mHeaderView, 0)

        mMarginView = FrameLayout(listView.context)
        mMarginView!!.layoutParams = AbsListView.LayoutParams(LayoutParams.MATCH_PARENT, 0)
        listView.addHeaderView(mMarginView, null, false)

        // Make the background as high as the screen so that it fills regardless of the amount of scroll.
        mListViewBackgroundView = contentContainer.findViewById(R.id.fab__listview_background)
        val params = mListViewBackgroundView!!.layoutParams as FrameLayout.LayoutParams
        params.height = ToolUI.getDisplayHeight(listView!!.context)
        listView.context
        mListViewBackgroundView!!.layoutParams = params

        listView.setOnScrollListener(mOnScrollListener)
        return contentContainer
    }

    private val mOnScrollListener = object : OnScrollListener {
        override fun onScroll(view: AbsListView, firstVisibleItem: Int, visibleItemCount: Int, totalItemCount: Int) {
            val topChild = view.getChildAt(0)
            if (topChild == null) {
                onNewScroll(0)
            } else if (topChild !== mMarginView) {
                onNewScroll(mHeaderContainer!!.height)
            } else {
                onNewScroll(-topChild.top)
            }
        }

        override fun onScrollStateChanged(view: AbsListView, scrollState: Int) {}
    }
    private var mLastScrollPosition: Int = 0

    private fun onNewScroll(scrollPosition: Int) {
        if (isActionBarNull()) {
            return
        }

        val currentHeaderHeight = mHeaderContainer!!.height
        if (currentHeaderHeight != mLastHeaderHeight) {
            updateHeaderHeight(currentHeaderHeight)
        }

        val headerHeight = currentHeaderHeight - actionBarHeight()
        val ratio = Math.min(Math.max(scrollPosition, 0), headerHeight).toFloat() / headerHeight
        val newAlpha = (ratio * 255).toInt()
        mActionBarBackgroundDrawable!!.alpha = newAlpha

        addParallaxEffect(scrollPosition)
    }

    private fun addParallaxEffect(scrollPosition: Int) {
        val damping = if (mUseParallax) 0.5f else 1.0f
        val dampedScroll = (scrollPosition * damping).toInt()
        var offset = mLastDampedScroll - dampedScroll
        mHeaderContainer!!.offsetTopAndBottom(offset)

        if (mListViewBackgroundView != null) {
            offset = mLastScrollPosition - scrollPosition
            mListViewBackgroundView!!.offsetTopAndBottom(offset)
        }

        if (mFirstGlobalLayoutPerformed) {
            mLastScrollPosition = scrollPosition
            mLastDampedScroll = dampedScroll
        }
    }

    private fun updateHeaderHeight(headerHeight: Int) {
        val params = mMarginView!!.layoutParams
        params.height = headerHeight
        mMarginView!!.layoutParams = params
        if (mListViewBackgroundView != null) {
            val params2 = mListViewBackgroundView!!.layoutParams as FrameLayout.LayoutParams
            params2.topMargin = headerHeight
            mListViewBackgroundView!!.layoutParams = params2
        }
        mLastHeaderHeight = headerHeight
    }

    private fun initializeGradient(headerContainer: ViewGroup) {
        val gradientView = headerContainer.findViewById(R.id.fab__gradient)
        var gradient = R.drawable.fab__gradient
        if (mLightActionBar) {
            gradient = R.drawable.fab__gradient_light
        }
        gradientView.setBackgroundResource(gradient)
    }
}
