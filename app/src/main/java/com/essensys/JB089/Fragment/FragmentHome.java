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
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
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
import com.essensys.JB089.Activity.ActivityDashBoard;
import com.essensys.JB089.Activity.ActivityProdDet;
import com.essensys.JB089.Activity.ActivityScanDet;
import com.essensys.JB089.Activity.ActivityScanHistory;
import com.essensys.JB089.Activity.ActivityScanViewDet;
import com.essensys.JB089.Adapter.AdapterRecyclerProd;
import com.essensys.JB089.Adapter.AdapterRecyclerScanDet;
import com.essensys.JB089.CustomView.CustomButton;
import com.essensys.JB089.CustomView.CustomTextView;
import com.essensys.JB089.CustomView.IntentIntegrators;
import com.essensys.JB089.CustomView.IntentResults;
import com.essensys.JB089.CustomView.RecyclerItemClickListener;
import com.essensys.JB089.CustomView.TransparentProgressDialog;
import com.essensys.JB089.DataClass.ProductList;
import com.essensys.JB089.DataClass.ScanDetails;
import com.essensys.JB089.R;
import com.essensys.JB089.Session.SessionClass;
import com.essensys.JB089.Utility.AvailableNetwork;
import com.essensys.JB089.Utility.PaginationScrollGrid;
import com.essensys.JB089.Utility.PaginationScrollListener;
import com.essensys.JB089.Utility.RetrofitClient;
import com.essensys.JB089.Utility.RetrofitInterfaces;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
public class FragmentHome extends Fragment implements View.OnClickListener {
    private View view;
    private static final String TAG = "MainActivity";
    private AdapterRecyclerProd adapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView rv;
    private ProgressBar progressBar;
    private ArrayList<ProductList> productLists;
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 3;
    private int currentPage = PAGE_START;
    private CustomTextView mTxtMsg;
    private String mStrProd="";
    private ArrayList<ArrayList<ProductList>> mArraylistFinalProd;
    private FloatingActionButton fabScan;
    private CustomButton mBtnScan;
    public static String offset="0",loadingFlg="0";
    private String date;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view=inflater.inflate(R.layout.fragment_prod,container,false);
        rv = (RecyclerView) view.findViewById(R.id.recyclerProd);
        progressBar = (ProgressBar) view.findViewById(R.id.pg_prod);
        mTxtMsg=(CustomTextView)view.findViewById(R.id.txt_msg_prod);
        //for current date
        date = new SimpleDateFormat("dd/MM/yyy HH:mm:ss", Locale.getDefault()).format(new Date());
        fabScan=(FloatingActionButton)view.findViewById(R.id.fabScanProd);
        fabScan.setOnClickListener(this);
        mAquery=new AQuery(getActivity());
        productLists =new ArrayList<>();
        mArraylistFinalProd=new ArrayList<>();
        mBtnScan=(CustomButton)view.findViewById(R.id.btn_scan);
        mBtnScan.setOnClickListener(this);
        gridLayoutManager = new GridLayoutManager(getActivity(),2,LinearLayoutManager.VERTICAL,false);
//        gridLayoutManager.setOrientation(LinearLayoutManager.VERTICAL); // set Horizontal Orientation
        rv.setLayoutManager(gridLayoutManager);
//        rv.setItemAnimator(new DefaultItemAnimator());
        return view;
    }
    @Override
    public void onResume() {
        super.onResume();
        ((CustomTextView)getActivity().findViewById(R.id.txt_title)).setText("Vayortricks");
        ((ImageView)getActivity().findViewById(R.id.img_edit)).setVisibility(View.GONE);

             if(((DrawerLayout)getActivity().findViewById(R.id.drawer_layout)).isDrawerOpen(GravityCompat.START))
             {
                 ((DrawerLayout)getActivity().findViewById(R.id.drawer_layout)).closeDrawers();

             }


        getProducts(offset);
    }
    //webservice for product
    private void getProducts(String offset)
    {
        mStrProd=getString(R.string.ws_host)+getString(R.string.ws_prod_list);
        HashMap<String,Object> mHash=new HashMap<>();
        mHash.put("uid",SessionClass.getUserId(getActivity()));
        mHash.put("offset", offset);
        if(AvailableNetwork.isConnectingToInternet(getActivity()))
        {
            mAquery.progress(mProgress).ajax(mStrProd, mHash, JSONObject.class, ajaxcallbackProd);
        }else
        {
            showAlert(getString(R.string.str_not_internet_connection), -1);

        }
    }

    //response
    AjaxCallback<JSONObject> ajaxcallbackProd=new AjaxCallback<JSONObject>()
    {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if(object!=null)
            {
                if(loadingFlg.equalsIgnoreCase("0"))
                {
                    if(!productLists.isEmpty())
                    {
                        productLists.clear();
                    }
                }

                int pos = 0;
                if(url.equalsIgnoreCase(mStrProd))
                {
                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        offset=jsonObject.getString("offset");

                        JSONArray jsonArray=jsonObject.getJSONArray("ecomProductList");
                        if (jsonArray.length() > 0 ) {
                            mTxtMsg.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonProd = jsonArray.getJSONObject(i);
                                ProductList productList = new ProductList();
                                productList.setProduct_id(jsonProd.getString("product_id"));
                                productList.setName(jsonProd.getString("name"));
                                productList.setDescription(jsonProd.getString("description"));
                                productList.setPrice(jsonProd.getString("price"));
                                productList.setImage(jsonProd.getString("image"));
                                productList.setAmazone_url(jsonProd.getString("amazone_url"));
                                productList.setFlipkart_url(jsonProd.getString("flipkart_url"));
                                productLists.add(productList);
                                mArraylistFinalProd.add(productLists);
                                rv.setTag(i);
                            }
                        }else
                        {
                            if(!mArraylistFinalProd.isEmpty())
                            {
                                mTxtMsg.setVisibility(View.GONE);
                                rv.setVisibility(View.VISIBLE);
                            }else
                            {
                                mTxtMsg.setVisibility(View.VISIBLE);
                                rv.setVisibility(View.GONE);
                            }
                        }
                        adapter = new AdapterRecyclerProd(mArraylistFinalProd.get(pos), getActivity());
                        rv.setAdapter(adapter);
                        final int finalPos = pos;
                        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                            @Override
                            public void onItemClick(View view, int position) {
                                offset="0";
                                Intent intent=new Intent(getActivity(),ActivityProdDet.class);
                                intent.putExtra("name",mArraylistFinalProd.get(finalPos).get(position).getName());
                                intent.putExtra("price",mArraylistFinalProd.get(finalPos).get(position).getPrice());
                                intent.putExtra("desc",mArraylistFinalProd.get(finalPos).get(position).getDescription());
                                intent.putExtra("image",mArraylistFinalProd.get(finalPos).get(position).getImage());
                                intent.putExtra("amazoneUrl",mArraylistFinalProd.get(finalPos).get(position).getAmazone_url());
                                intent.putExtra("flipcartUrl",mArraylistFinalProd.get(finalPos).get(position).getFlipkart_url());
                                startActivity(intent);
//                                getActivity().finish();

                            }
                        }));

                        rv.addOnScrollListener(new PaginationScrollGrid(gridLayoutManager) {
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
                                        getProducts(offset);
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


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };
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
            case R.id.btn_scan:
                scanCode();
                break;
            case R.id.fabScanProd:
                scanCode();
                break;
        }
    }
    //method for scan
    public void scanCode() {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            new IntentIntegrators(getActivity()).setBarcodeImageEnabled(true).forSupportFragment(FragmentHome.this).setOrientationLocked(false).setBarcodeImageEnabled(true).setCaptureActivity(ActivityScanDet.class).initiateScan();

//            Intent intent = new Intent(getActivity(), ActivityScanDet.class);
//            startActivityForResult(intent, 101);
//            startActivity(intent);

        } else {
            String[] perms = {"android.permission.CAMERA"};
            ActivityCompat.requestPermissions(getActivity(),perms, 102);

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
                    String contents = result.getContents();
                    String formatName=result.getFormatName();
                   String barcodeImagePath=result.getBarcodeImagePath();
                    FragmentScanHistory.scanFlag="1";
                   Intent intent =new Intent(getActivity(),ActivityScanViewDet.class);
                   intent.putExtra("scanText",contents);
                   intent.putExtra("date",date);
                   intent.putExtra("imgScan",barcodeImagePath);
                   intent.putExtra("formatName",formatName);
                   intent.putExtra("flagFromScan","1");//coming from home screen
                   startActivity(intent);
//                   getActivity().finish();
                   //webservice to add contents
//                    ScanRetrofit(contents,barcodeImagePath,formatName);
//                    scanFlag="1";

//                Log.d("MainActivity", "Scanned");
//                Toast.makeText(getActivity(), "Scanned: " + result.getContents(), Toast.LENGTH_LONG).show();
                }
            }
            offset="0";

        }
    }

    //webservice in retrofit
    private void ScanRetrofit(final String contents, final String barcodeImage,final String format)
    {
//        progressBar.show();
        final HashMap<String, RequestBody> scanCode=new HashMap<>();
        scanCode.put("uid", RetrofitClient.getRequestBodyFromString(SessionClass.getUserId(getActivity())));
        scanCode.put("scan_details", RetrofitClient.getRequestBodyFromString(contents));
        scanCode.put("format_name",RetrofitClient.getRequestBodyFromString(format));
        List<MultipartBody.Part> partList = new ArrayList<>();

        if (barcodeImage!=null) {

            File file = new File(barcodeImage);
            // RequestBody fileBody = RequestBody.create(MediaType.parse("*/*"), file);
            MultipartBody.Part regDocumentPart = MultipartBody.Part.createFormData("scan_image",
                    file.getName(), RetrofitClient.getRequestBodyFromStringFile(barcodeImage));
            partList.add(regDocumentPart);
            scanCode.put("isFileUpload", RetrofitClient.getRequestBodyFromString("true"));
//            showAlert(String.valueOf(scanCode),-1);

        } else {
            scanCode.put("isFileUpload", RetrofitClient.getRequestBodyFromString("false"));
        }

        RetrofitClient
                .getClient(getString(R.string.ws_host)+getString(R.string.ws_add_productScan))
                .create(RetrofitInterfaces.IScanCode.class)
                .post(scanCode, partList)
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String result=response.body().string();
                            JSONObject jobj = new JSONObject(result);
                            JSONObject jsonObject=jobj.getJSONObject("result");
                            if (jsonObject.getString("msg").equalsIgnoreCase("1")) {
//                                progressBar.dismiss();
//                                onBackPressed();
                                Intent intent=new Intent(getActivity(),ActivityDashBoard.class);
                                intent.putExtra("flagScan","0"); //for redirecting to scan history
                                startActivity(intent);
                            } else {
                                showAlert(jsonObject.getString("msg_string"), -1);
                            }


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                    }
                });

    }
}
