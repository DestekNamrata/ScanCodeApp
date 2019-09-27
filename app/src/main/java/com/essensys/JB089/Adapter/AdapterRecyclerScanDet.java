package com.essensys.JB089.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.DataClass.ScanDetails;
import com.essensys.JB089.R;

import java.util.ArrayList;
import java.util.List;
public class AdapterRecyclerScanDet extends RecyclerView.Adapter<AdapterRecyclerScanDet.MyViewHolder> {

    private ArrayList<ScanDetails> scanDetails;
    private Context context;
    private boolean isLoadingAdded = false;


    public AdapterRecyclerScanDet(ArrayList<ScanDetails> scanDetails, Context context) {
        this.scanDetails = scanDetails;
        this.context = context;
    }
    @Override
    public MyViewHolder onCreateViewHolder( ViewGroup parent, int i) {
       View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_scan_list,parent,false);

       return new MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(MyViewHolder viewHolder, int i)
    {
        ScanDetails scanDetails = this.scanDetails.get(i);
        viewHolder.mTxtScanData.setText(scanDetails.getProduct_scan_details());
        viewHolder.mTxtScanData1.setText(scanDetails.getAdded_on_dt());
        Glide.with(context)
                .load(scanDetails.getScan_image())
                .placeholder(R.drawable.ic_vt_logo)
                .into(viewHolder.mImgScan);
    }
    @Override
    public int getItemCount() {
        return scanDetails.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        private CustomTextView mTxtScanData,mTxtScanData1;
        private ImageView mImgScan;

        public MyViewHolder(View itemView) {
            super(itemView);
           mTxtScanData=(CustomTextView)itemView.findViewById(R.id.txt_scanData);
            mTxtScanData1=(CustomTextView)itemView.findViewById(R.id.txt_scanData1);
            mImgScan=(ImageView)itemView.findViewById(R.id.img_scan);
        }
    }
    public void add(ScanDetails pd) {
        scanDetails.add(pd);
        notifyItemInserted(scanDetails.size() - 1);
    }

    public void addAll(ArrayList<ScanDetails> pdList) {
        for (ScanDetails pd: pdList) {
            add(pd);
        }
    }

    public void remove(ScanDetails pd) {
        int position = scanDetails.indexOf(pd);
        if (position > -1) {
            scanDetails.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ScanDetails());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = scanDetails.size() - 1;
        ScanDetails item = getItem(position);
        if (item != null) {
            scanDetails.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ScanDetails getItem(int position)
    {
        return scanDetails.get(position);
    }

    public void addScanDet(List<ScanDetails> products)
    {
  for(ScanDetails pd:products)
  {
     scanDetails.add(pd);
  }
    }
}
