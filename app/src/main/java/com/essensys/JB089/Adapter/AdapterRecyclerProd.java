package com.essensys.JB089.Adapter;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.DataClass.ProductList;
import com.essensys.JB089.DataClass.ScanDetails;
import com.essensys.JB089.R;

import java.util.ArrayList;
import java.util.List;
public class AdapterRecyclerProd extends RecyclerView.Adapter<AdapterRecyclerProd.MyViewHolder> {

    private ArrayList<ProductList> productLists;
    private Context context;
    private boolean isLoadingAdded = false;


    public AdapterRecyclerProd(ArrayList<ProductList> productLists, Context context) {
        this.productLists = productLists;
        this.context = context;
    }
    @Override
    public AdapterRecyclerProd.MyViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_prod_list,parent,false);

        return new AdapterRecyclerProd.MyViewHolder(view);
    }
    @Override
    public void onBindViewHolder(AdapterRecyclerProd.MyViewHolder viewHolder, int i)
    {
        ProductList productList = this.productLists.get(i);
        viewHolder.mTxtProdName.setText(productList.getName());
        viewHolder.mTxtPrice.setText(context.getString(R.string.Rs)+" "+productList.getPrice());
        viewHolder.mTxtdesc.setText(productList.getDescription());

        Glide.with(context)
                .load(productList.getImage())
                .placeholder(R.drawable.ic_vt_logo)
                .into(viewHolder.mImgProd);
    }
    @Override
    public int getItemCount() {
        return productLists.size();
    }

    public static class  MyViewHolder extends RecyclerView.ViewHolder{

        private CustomTextView mTxtProdName,mTxtPrice,mTxtdesc;
        private ImageView mImgProd;

        public MyViewHolder(View itemView) {
            super(itemView);
            mTxtProdName=(CustomTextView)itemView.findViewById(R.id.txt_prodName);
            mTxtPrice=(CustomTextView)itemView.findViewById(R.id.txt_price);
            mTxtdesc=(CustomTextView)itemView.findViewById(R.id.txt_desc);
            mImgProd=(ImageView)itemView.findViewById(R.id.img_prod);
        }
    }
    public void add(ProductList pd) {
        productLists.add(pd);
        notifyItemInserted(productLists.size() - 1);
    }

    public void addAll(ArrayList<ProductList> pdList) {
        for (ProductList pd: pdList) {
            add(pd);
        }
    }

    public void remove(ScanDetails pd) {
        int position = productLists.indexOf(pd);
        if (position > -1) {
            productLists.remove(position);
            notifyItemRemoved(position);
        }
    }
    public void addLoadingFooter() {
        isLoadingAdded = true;
        add(new ProductList());
    }

    public void removeLoadingFooter() {
        isLoadingAdded = false;
        int position = productLists.size() - 1;
        ProductList item = getItem(position);
        if (item != null) {
            productLists.remove(position);
            notifyItemRemoved(position);
        }
    }

    public ProductList getItem(int position)
    {
        return productLists.get(position);
    }


}
