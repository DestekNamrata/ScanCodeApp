package com.essensys.JB089.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.DataClass.DataNavigation;
import com.essensys.JB089.R;

import java.util.ArrayList;


/**
 * Created by WinDOWS on 08-12-2015.
 */
public class NavigationDrawerAdapter extends BaseAdapter {

    private Context mContext;
    private ArrayList<DataNavigation> mArrListNavItem;
    private LayoutInflater mInflater;

    public NavigationDrawerAdapter(Context mContext, ArrayList<DataNavigation> mArrListNavItem) {
        this.mContext = mContext;
        this.mArrListNavItem = mArrListNavItem;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mArrListNavItem.size();
    }

    @Override
    public Object getItem(int position) {
        return mArrListNavItem.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder mViewHolder;
        if (convertView == null) {
            mViewHolder = new ViewHolder();
            convertView = mInflater.inflate(R.layout.activity_drawer_item, null);
            mViewHolder.mImgMenu = (ImageView) convertView.findViewById(R.id.img_nav_item_menu);
            mViewHolder.mTxtMenuName = (CustomTextView) convertView.findViewById(R.id.tv_nav_item_title);
            convertView.setTag(mViewHolder);
        } else {
            mViewHolder = (ViewHolder) convertView.getTag();
        }
//        mAquery.id(mViewHolder.mImgMenu).image(mArrListNavItem.get(position).getmStrNavIcon());
        mViewHolder.mImgMenu.setImageResource(mArrListNavItem.get(position).getmStrNavIcon());
        mViewHolder.mTxtMenuName.setText(mArrListNavItem.get(position).getMstrNavTitle().trim());

        return convertView;
    }

    private class ViewHolder {
        ImageView mImgMenu;
        CustomTextView mTxtMenuName;
    }
}