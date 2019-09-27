package com.essensys.JB089.Adapter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.androidquery.AQuery;
import com.bumptech.glide.Glide;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.R;

import java.util.ArrayList;
import java.util.HashMap;
public class AdapterScanProd extends BaseAdapter {
    private ArrayList<HashMap<String,String>> mArraylistScan;
    private LayoutInflater inflater;
    private Context mContext;
    private AQuery mAquery;

    public AdapterScanProd(ArrayList<HashMap<String, String>> mArraylistScan, Context mContext) {
        this.mArraylistScan = mArraylistScan;
        this.mContext = mContext;
        mAquery=new AQuery(mContext);
        inflater=LayoutInflater.from(this.mContext);
    }
    @Override
    public int getCount() {
        return mArraylistScan.size();
    }
    @Override
    public Object getItem(int i) {
        return mArraylistScan.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ViewHolder viewHolder;
        if(view==null)
        {
            viewHolder=new ViewHolder();
            view=inflater.inflate(R.layout.layout_scan_list,null);
            viewHolder.mTxtScanData=(CustomTextView)view.findViewById(R.id.txt_scanData);
            viewHolder.mTxtScanData1=(CustomTextView)view.findViewById(R.id.txt_scanData1);
            viewHolder.mImgScan=(ImageView)view.findViewById(R.id.img_scan);
            view.setTag(viewHolder);

        }else
        {
            viewHolder=(ViewHolder)view.getTag();
        }
        viewHolder.mTxtScanData.setText(mArraylistScan.get(i).get("product_scan_details"));
        viewHolder.mTxtScanData1.setText(mArraylistScan.get(i).get("added_on_dt"));
//        mAquery.id(viewHolder.mImgScan).image(mArraylistScan.get(i).get("scan_image"),true,true,150,R.drawable.ic_vt_logo);
        Glide.with(mContext)
                .load(mArraylistScan.get(i).get("scan_image"))
                .placeholder(R.drawable.ic_vt_logo).centerCrop()
                .error(R.drawable.ic_vt_logo).centerCrop()
                .into(viewHolder.mImgScan);
        return view;
    }
    private class ViewHolder{
        private CustomTextView mTxtScanData,mTxtScanData1;
        private ImageView mImgScan;
    }
}
