package com.essensys.JB089.Fragment;
import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;
import com.essensys.JB089.Activity.ActivityScanDet;
import com.essensys.JB089.Activity.ActivityScanViewDet;
import com.essensys.JB089.Adapter.AdapterRecyclerScanDet;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.CustomView.IntentIntegrators;
import com.essensys.JB089.CustomView.IntentResults;
import com.essensys.JB089.CustomView.RecyclerItemClickListener;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.DataClass.ScanDetails;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.AvailableNetwork;
import com.essensys.JB089.Utility.PaginationScrollListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
public class FragmentScanHistory extends Fragment implements View.OnClickListener{
   private View view;
   private static final String TAG = "MainActivity";

   AdapterRecyclerScanDet adapter;
   LinearLayoutManager linearLayoutManager;

   RecyclerView rv;
   ProgressBar progressBar;

   private static final int PAGE_START = 0;
   private boolean isLoading = false;
   private boolean isLastPage = false;
   private int TOTAL_PAGES = 3;
   private int currentPage = PAGE_START;
   private ArrayList<ScanDetails> scanDetails;
   private ArrayList<ArrayList<ScanDetails>> mArraylistFinalProd;
   private String mStrScan="";
   private AQuery mAquery;
   private TransparentProgressDialog mProgress;
   private String  formatName="",barcodeImagePath="";
   private CustomTextView mTxtMsg;
   private FloatingActionButton fabScan;
   private String strScan="",contents="",date;
   public static String scanFlag="0",offset="0";
   private  String  loadingFlg="0";
   @Nullable
   @Override
   public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
     view=inflater.inflate(R.layout.fragment_scan_history,container,false);
      rv = (RecyclerView) view.findViewById(R.id.main_recycler);
      progressBar = (ProgressBar) view.findViewById(R.id.main_progress);
      mAquery=new AQuery(getActivity());
      //for current date
      date = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.getDefault()).format(new Date());

      mProgress=new TransparentProgressDialog(getActivity(),R.drawable.ic_loader_image);
      mTxtMsg=(CustomTextView)view.findViewById(R.id.txt_msg);
      fabScan=(FloatingActionButton)view.findViewById(R.id.fabScan);
      fabScan.setOnClickListener(this);
      scanDetails =new ArrayList<>();
      mArraylistFinalProd=new ArrayList<ArrayList<ScanDetails>>();
      linearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
      rv.setLayoutManager(linearLayoutManager);
      rv.setItemAnimator(new DefaultItemAnimator());
      return view;
   }
   @Override
   public void onResume() {
      super.onResume();
      ((CustomTextView)getActivity().findViewById(R.id.txt_title)).setText("Scan History");
      ((ImageView)getActivity().findViewById(R.id.img_edit)).setVisibility(View.GONE);
      if(scanFlag=="0")
      {
         getScanDet(offset);
      }else
      {
         Intent intent=new Intent(getActivity(),ActivityScanViewDet.class);
         intent.putExtra("scanText",contents);
         intent.putExtra("date",date);
         intent.putExtra("flagFromScan","0");
         intent.putExtra("imgScan",barcodeImagePath);
         intent.putExtra("formatName",formatName);
         intent.putExtra("flagscanfrag","0");

         startActivity(intent);
      }


   }
   @Override
   public void onPause() {
      super.onPause();
      offset="0";
      loadingFlg="0";
   }
   @Override
   public void onDestroy() {
      super.onDestroy();
      offset="0";
      loadingFlg="0";
   }
   //webservice for Scan List
   private void getScanDet(String offset)
   {
      mStrScan=getString(R.string.ws_host)+getString(R.string.ws_scanprod_list);
      HashMap<String,Object> mHash=new HashMap<>();
      mHash.put("uid",SessionClass.getUserId(getActivity()));
      mHash.put("offset", offset);

      if(AvailableNetwork.isConnectingToInternet(getActivity()))
      {
         mAquery.progress(mProgress).ajax(mStrScan, mHash, JSONObject.class, ajaxcallback);
      }else
      {
         showAlert(getString(R.string.str_not_internet_connection), -1);

      }
   }
   //response
   AjaxCallback<JSONObject> ajaxcallback=new AjaxCallback<JSONObject>()
   {
      @Override
      public void callback(String url, JSONObject object, AjaxStatus status) {
         super.callback(url, object, status);
         if(object!=null)
         {
            if(url.equalsIgnoreCase(mStrScan))
            {
               if(loadingFlg.equalsIgnoreCase("0"))
               {
                  if(!scanDetails.isEmpty())
                  {
                     scanDetails.clear();
                  }
               }

               int pos = 0;
               try {
                  JSONObject jsonObject=object.getJSONObject("result");
                  offset=jsonObject.getString("offset");

                  if(jsonObject.getString("msg").equalsIgnoreCase("1")) {
                     final JSONArray jsonArray = jsonObject.getJSONArray("productList");

                     if (jsonArray.length() > 0 ) {
                        mTxtMsg.setVisibility(View.GONE);
                        rv.setVisibility(View.VISIBLE);
                        for (int i = 0; i < jsonArray.length(); i++) {
                           pos=i;
                           JSONObject jsonScanProd = jsonArray.getJSONObject(i);
                           ScanDetails scanDetails = new ScanDetails();
                           scanDetails.setProduct_id(jsonScanProd.getString("product_id"));
                           scanDetails.setAdded_on_dt(jsonScanProd.getString("added_on_dt"));
                           scanDetails.setProduct_scan_details(jsonScanProd.getString("product_scan_details"));
                           scanDetails.setScan_image(jsonScanProd.getString("scan_image"));
                           scanDetails.setFormat_name(jsonScanProd.getString("format_name"));

                           FragmentScanHistory.this.scanDetails.add(scanDetails);
//                                    mArraylistFinalProd.add(scanDetails);
                           mArraylistFinalProd.add(FragmentScanHistory.this.scanDetails);
                           rv.setTag(i);
                        }

                     }else
                     {
                        if(!mArraylistFinalProd.isEmpty())
                        {
                           mTxtMsg.setVisibility(View.GONE);
                           rv.setVisibility(View.VISIBLE);
//                                    adapter = new AdapterRecyclerScanDet(mArraylistFinalProd, getActivity());
//                                    rv.setAdapter(adapter);
                        }else
                        {
                           mTxtMsg.setVisibility(View.VISIBLE);
                           rv.setVisibility(View.GONE);
                        }

                     }
                     adapter = new AdapterRecyclerScanDet(mArraylistFinalProd.get(pos), getActivity());
                     rv.setAdapter(adapter);
                     final int finalPos = pos;
                     rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int position) {
                           offset="0";
//                           scanFlag="0";
                           Intent intent=new Intent(getActivity(),ActivityScanViewDet.class);
                           intent.putExtra("scanText",mArraylistFinalProd.get(finalPos).get(position).getProduct_scan_details());
                           intent.putExtra("date",mArraylistFinalProd.get(finalPos).get(position).getAdded_on_dt());
                           intent.putExtra("imgScan",mArraylistFinalProd.get(finalPos).get(position).getScan_image());
                           intent.putExtra("formatName",mArraylistFinalProd.get(finalPos).get(position).getFormat_name());

                           startActivity(intent);
                           getActivity().finish();

                        }
                     }));
                     rv.addOnScrollListener(new PaginationScrollListener(linearLayoutManager) {
                        @Override
                        protected void loadMoreItems() {
                           isLoading = true;
                           currentPage += 1;
                           // mocking network delay for API call
                           new Handler().postDelayed(new Runnable() {
                              @Override
                              public void run() {
//                                            loadNextPage();
                                 loadingFlg="1";
                                 getScanDet(offset);
                                 progressBar.setVisibility(View.VISIBLE);
                              }
                           }, 1000);
                        }
                        @Override
                        public int getTotalPageCount() {
                           return TOTAL_PAGES;
                        }
                        @Override
                        public boolean isLastPage() {
                           return isLastPage;
                        }
                        @Override
                        public boolean isLoading() {
                           return isLoading;
                        }
                     });
//                            // mocking network delay for API call
                     new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
//                                        loadFirstPage();
                           progressBar.setVisibility(View.GONE);
                        }
                     }, 1000);

                  }

               } catch (JSONException e) {
                  e.printStackTrace();
               }
            }
         }
      }
   };
   private void loadFirstPage() {
      Log.d(TAG, "loadFirstPage: ");
      progressBar.setVisibility(View.GONE);
      if (currentPage <= TOTAL_PAGES)
         adapter.addLoadingFooter();
      else isLastPage = true;

   }

   private void loadNextPage() {
      Log.d(TAG, "loadNextPage: " + currentPage);
      adapter.removeLoadingFooter();
      isLoading = false;
      if (currentPage != TOTAL_PAGES) adapter.addLoadingFooter();
      else isLastPage = true;
   }
   /**
    * /**
    * Show alert
    **/
   private void showAlert(String message, final int flg) {
      AlertDialog.Builder aBuilder = new AlertDialog.Builder(getActivity());
      aBuilder.setCancelable(false);
      aBuilder.setMessage(message);
      aBuilder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
         @Override
         public void onClick(DialogInterface dialog, int which) {
            dialog.dismiss();
            setNextFlg(flg);
         }
      });
      aBuilder.create();
      aBuilder.show();
   }
   private void setNextFlg(int flg) {
      Intent intent;
      switch (flg) {
         case 1:

            break;

      }
   }
   @Override
   public void onClick(View view)
   {
      switch (view.getId())
      {
         case R.id.fabScan:
            scanCode();
            break;
      }
   }
   //method for scan
   public void scanCode() {
      if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
         new IntentIntegrators(getActivity()).setBarcodeImageEnabled(true).setOrientationLocked(false).forSupportFragment(FragmentScanHistory.this).setBarcodeImageEnabled(true).setCaptureActivity(ActivityScanDet.class).initiateScan();

//            Intent intent = new Intent(getActivity(), ActivityScanDet.class);
//            startActivityForResult(intent, 101);
//            startActivity(intent);

      } else {
         String[] perms = {"android.permission.CAMERA"};
         ActivityCompat.requestPermissions(getActivity(),perms, 102);

//            Intent intent = new Intent(getActivity(), ActivityScan.class);
////            startActivityForResult(intent, 101);
//            startActivity(intent);
//            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 102);

//            Toast.makeText(getActivity(), "Camera permission denied.", Toast.LENGTH_SHORT).show();
      }
   }
   @Override
   public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == 101) {

         if(resultCode==101)
         {
            IntentResults result = IntentIntegrators.parseActivityResult(101,Activity.RESULT_OK, data);

            if(result.getContents() == null) {
               Log.d("MainActivity", "Cancelled scan");
               Toast.makeText(getActivity(), "Cancelled", Toast.LENGTH_LONG).show();
            } else {
               contents=result.getContents();
               formatName=result.getFormatName();
               barcodeImagePath=result.getBarcodeImagePath();
               scanFlag="1";

//                Log.d("MainActivity", "Scanned");
//                Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
            }
         }
         offset="0";

      }
   }

}
