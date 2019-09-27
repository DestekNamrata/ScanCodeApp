package com.essensys.JB089.DataClass;
import com.google.gson.annotations.SerializedName;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
public class ScanDetails {
    @SerializedName("product_id")
    private String product_id;

    @SerializedName("product_scan_details")
    private String product_scan_details;

    @SerializedName("scan_image")
    private String scan_image;

    @SerializedName("added_on_dt")
    private String added_on_dt;

    @SerializedName("format_name")
    private String format_name;
    public String getFormat_name() {
        return format_name;
    }
    public void setFormat_name(String format_name) {
        this.format_name = format_name;
    }
    public String getProduct_id() {
        return product_id;
    }

    public String getProduct_scan_details() {
        return product_scan_details;
    }

    public String getScan_image() {
        return scan_image;
    }

    public String getAdded_on_dt() {
        return added_on_dt;
    }
    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }
    public void setProduct_scan_details(String product_scan_details) {
        this.product_scan_details = product_scan_details;
    }
    public void setScan_image(String scan_image) {
        this.scan_image = scan_image;
    }
    public void setAdded_on_dt(String added_on_dt) {
        this.added_on_dt = added_on_dt;
    }

    public static ArrayList<ScanDetails> AddScanDet(int itemCount, JSONArray jsonArray) {
        ArrayList<ScanDetails> product = new ArrayList<>();
        try {
        for (int i = 0; i < jsonArray.length(); i++) {
//            ScanDetails product = new Pro("Movie " + (itemCount == 0 ?
//                    (itemCount + 1 + i) : (itemCount + i)));
//            movies.add(movie);
            JSONObject jsonScanProd = null;

                jsonScanProd = jsonArray.getJSONObject(i);

            ScanDetails scanDetails =new ScanDetails();
            scanDetails.setProduct_id(jsonScanProd.getString("product_id"));
            scanDetails.setAdded_on_dt(jsonScanProd.getString("added_on_dt"));
            scanDetails.setProduct_scan_details(jsonScanProd.getString("product_scan_details"));
            scanDetails.setScan_image(jsonScanProd.getString("scan_image"));
            product.add(scanDetails);
        }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return product;
    }
}
