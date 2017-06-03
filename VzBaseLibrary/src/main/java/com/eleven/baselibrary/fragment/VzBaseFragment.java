package com.eleven.baselibrary.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by huangwz on 2017/6/3.
 *
 * @Link: vaitt_joy@163.com
 *
 * fragment基类
 */

public abstract class VzBaseFragment extends Fragment {
    protected View mRoomView;
    protected Context mContext;
    protected final String TAG = this.getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (mRoomView == null) {
            mRoomView = initView();
            initData();
            initEvent();
        }
        ViewGroup parent = (ViewGroup) mRoomView.getParent();
        if (parent != null) {
            parent.removeView(mRoomView);
        }
        return mRoomView;
    }

    protected abstract View initView();

    private void initData() {

    }

    private void initEvent() {

    }
}
