package com.eleven.baselibrary.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;

import java.util.List;

/**
 * Created by huangwz on 2017/6/3.
 *
 * @Link: vaitt_joy@163.com
 */

public abstract class VzBaseExpandableAdapter<T> extends BaseExpandableListAdapter {

    protected List<T> mDatas;
    private int mParentLayoutId, mChildLayoutId;
    protected Context mContext;

    public VzBaseExpandableAdapter(Context context, List<T> list, int parentLayoutId, int childLayoutId) {
        this.mContext = context;
        this.mDatas = list;
        this.mChildLayoutId = childLayoutId;
        this.mParentLayoutId = parentLayoutId;
    }


    @Override
    public int getGroupCount() {
        return mDatas.size();
    }

    @Override
    public abstract int getChildrenCount(int groupPosition);

    @Override
    public T getGroup(int groupPosition) {
        return mDatas.get(groupPosition);
    }

    @Override
    public abstract Object getChild(int groupPosition, int childPosition);

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return groupPosition * childPosition;
    }


    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final VzViewHolder viewHolder = getGroupViewHolder(groupPosition, convertView, parent);
        convertGroupView(viewHolder, groupPosition, isExpanded);
        return viewHolder.getConvertView();
    }

    protected VzViewHolder getGroupViewHolder(int groupPosition, View convertView, ViewGroup parent) {
        return VzViewHolder.get(mContext, convertView, parent, mParentLayoutId, groupPosition);
    }

    protected abstract void convertGroupView(VzViewHolder viewHolder, int groupPosition, boolean isExpanded);

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final VzViewHolder viewHolder = getChildViewHolder(groupPosition, childPosition, convertView, parent);
        convertChildView(viewHolder, groupPosition, childPosition, isLastChild);
        return viewHolder.getConvertView();
    }

    protected VzViewHolder getChildViewHolder(int groupPosition, int childPosition, View convertView, ViewGroup parent) {
        return VzViewHolder.get(mContext, convertView, parent, mChildLayoutId, childPosition);
    }

    protected abstract void convertChildView(VzViewHolder viewHolder, int groupPosition, int childPosition, boolean isLastChild);

    @Override
    public abstract boolean isChildSelectable(int groupPosition, int childPosition);

    @Override
    public abstract boolean hasStableIds();
}

