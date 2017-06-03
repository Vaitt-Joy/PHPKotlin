package com.eleven.baselibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.List;

/**
 * Created by huangwz on 2017/6/3.
 *
 * @Link: vaitt_joy@163.com
 */

public abstract class VzBaseAdapter<T> extends BaseAdapter {

    protected List<T> mDatas;
    protected Context mContext;
    private int mItemLayoutId;
    protected int type;

    public VzBaseAdapter(Context context, List<T> list, int itemLayoutId) {
        this.mContext = context;
        this.mDatas = list;
        this.mItemLayoutId = itemLayoutId;
    }


    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public T getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final VzViewHolder viewHolder = getViewHolder(position, convertView, parent);
        convert(viewHolder, position);
        return viewHolder.getConvertView();
    }

    public abstract void convert(VzViewHolder holder, int position);

    private VzViewHolder getViewHolder(int position, View convertView, ViewGroup parent) {
        return VzViewHolder.get(mContext, convertView, parent, mItemLayoutId, position);
    }
}
