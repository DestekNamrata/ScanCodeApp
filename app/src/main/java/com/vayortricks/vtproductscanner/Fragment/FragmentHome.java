package com.vayortricks.vtproductscanner.Fragment;
import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScanner;
import com.edwardvanraak.materialbarcodescanner.MaterialBarcodeScannerBuilder;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.vayortricks.vtproductscanner.Activity.ActivityBarcodeReader;
import com.vayortricks.vtproductscanner.Activity.ActivityDashBoard;
import com.vayortricks.vtproductscanner.Activity.ActivityProdDet;
import com.vayortricks.vtproductscanner.Activity.ActivityScanDet;
import com.vayortricks.vtproductscanner.Activity.ActivityScanHistory;
import com.vayortricks.vtproductscanner.Activity.ActivityScanViewDet;
import com.vayortricks.vtproductscanner.Activity.MaterialScanActivity;
import com.vayortricks.vtproductscanner.Adapter.AdapterRecyclerProd;
import com.vayortricks.vtproductscanner.CustomView.CustomButton;
import com.vayortricks.vtproductscanner.CustomView.CustomTextView;
import com.vayortricks.vtproductscanner.CustomView.IntentIntegrators;
import com.vayortricks.vtproductscanner.CustomView.IntentResults;
import com.vayortricks.vtproductscanner.CustomView.TransparentProgressDialog;
import com.vayortricks.vtproductscanner.DataClass.ProductList;
import com.vayortricks.vtproductscanner.Interface.InterfaceClickListener;
import com.vayortricks.vtproductscanner.R;
import com.vayortricks.vtproductscanner.Session.SessionClass;
import com.vayortricks.vtproductscanner.Utility.AvailableNetwork;
import com.vayortricks.vtproductscanner.Utility.PaginationScrollGrid;
import com.vayortricks.vtproductscanner.Utility.RetrofitClient;
import com.vayortricks.vtproductscanner.Utility.RetrofitInterfaces;

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
    private ArrayList<ProductList> productLists,mArraylistFinalProd;
    private AQuery mAquery;
    private TransparentProgressDialog mProgress;
    private static final int PAGE_START = 0;
    private boolean isLoading = false;
    private boolean isLastPage = false;
    private int TOTAL_PAGES = 1;
    private int currentPage = PAGE_START;
    private CustomTextView mTxtMsg;
    private String mStrProd="";

    private FloatingActionButton fabScan;
    private CustomButton mBtnScan;
    public static String offset="0",loadingFlg="0";
    private String date;
    private ProgressDialog progressDialog;
    private String formatType="";
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
        progressDialog = AvailableNetwork.getDefaultLoader(getActivity());
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
            showLoader();
            mAquery.ajax(mStrProd, mHash, JSONObject.class, ajaxcallbackProd);
        }else
        {
            showAlert(getString(R.string.str_not_internet_connection), -1);

        }
    }
    //to show loader
    private void showLoader() {
        if (progressDialog == null || progressDialog.isShowing()){
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.show();
            }
        });
    }

    private void hideLoader() {
        if (progressDialog == null || !progressDialog.isShowing()){
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @Override
            public void run() {
                progressDialog.dismiss();
            }
        });
    }
    //response
    AjaxCallback<JSONObject> ajaxcallbackProd=new AjaxCallback<JSONObject>()
    {
        @Override
        public void callback(String url, JSONObject object, AjaxStatus status) {
            super.callback(url, object, status);
            if(object!=null)
            {
//                if(loadingFlg.equalsIgnoreCase("0"))
//                {
                    if(!productLists.isEmpty())
                    {
                        productLists.clear();
                    }
//                }

                int pos = 0;
                if(url.equalsIgnoreCase(mStrProd))
                {
                    hideLoader();

                    try {
                        JSONObject jsonObject=object.getJSONObject("result");
                        offset=jsonObject.getString("offset");

                        JSONArray jsonArray=jsonObject.getJSONArray("ecomProductList");
                        if (jsonArray.length() > 0 ) {
                            mTxtMsg.setVisibility(View.GONE);
                            rv.setVisibility(View.VISIBLE);
//                            for (int i = 0; i < jsonArray.length(); i++) {
//
//                                JSONObject jsonProd = jsonArray.getJSONObject(i);
//                                ProductList productList = new ProductList();
//                                productList.setProduct_id(jsonProd.getString("product_id"));
//                                productList.setName(jsonProd.getString("name"));
//                                productList.setDescription(jsonProd.getString("description"));
//                                productList.setPrice(jsonProd.getString("price"));
//                                productList.setImage(jsonProd.getString("image"));
//                                productList.setAmazone_url(jsonProd.getString("amazone_url"));
//                                productList.setFlipkart_url(jsonProd.getString("flipkart_url"));
//                                productLists.add(productList);
//                                rv.setTag(i);
//                            }
                             productLists =
                                    new Gson().fromJson(jsonArray.toString(),
                                            new TypeToken<ArrayList<ProductList>>() {
                                            }.getType());

                            if(!productLists.isEmpty())
                            {
                                mArraylistFinalProd.addAll(productLists);
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
                        //on scroll the position remains same
                        if(adapter!=null)
                        {
                            adapter.setData(mArraylistFinalProd);
                            rv.setAdapter(adapter);
                        }else
                        {
                            adapter = new AdapterRecyclerProd(mArraylistFinalProd, getActivity(),interfaceClickListener);
                            rv.setAdapter(adapter);
                        }

//                        final int finalPos = pos;
//                        rv.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
//                            @Override
//                            public void onItemClick(View view, int position) {
//                                offset="0";
//                                Intent intent=new Intent(getActivity(),ActivityProdDet.class);
//                                intent.putExtra("name",mArraylistFinalProd.get(position).getName());
//                                intent.putExtra("price",mArraylistFinalProd.get(position).getPrice());
//                                intent.putExtra("desc",mArraylistFinalProd.get(position).getDescription());
//                                intent.putExtra("image",mArraylistFinalProd.get(position).getImage());
//                                intent.putExtra("amazoneUrl",mArraylistFinalProd.get(position).getAmazone_url());
//                                intent.putExtra("flipcartUrl",mArraylistFinalProd.get(position).getFlipkart_url());
//                                startActivity(intent);
////                                getActivity().finish();
//
//                            }
//                        }));

                        rv.addOnScrollListener(new PaginationScrollGrid(gridLayoutManager) {
                            @Override
                            protected void loadMoreItems() {
                                isLoading = true;
                                currentPage += 1;
//                                showLoader();
                                TOTAL_PAGES++;
                                getProducts(offset);
//                                progressBar.setVisibility(View.VISIBLE);
//                                isLastPage=true;
//                                // mocking network delay for API call
//                                new Handler().postDelayed(new Runnable() {
//                                    @Override
//                                    public void run() {
////                                            loadNextPage();
//                                        loadingFlg="1";
//                                        getProducts(offset);
//                                        isLastPage=true;
//                                        progressBar.setVisibility(View.VISIBLE);
//                                    }
//                                }, 1000);
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
//                        new Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
////                                        loadFirstPage();
//                                loadingFlg="1";
//                                getProducts(offset);
//                                isLastPage=true;
//                                progressBar.setVisibility(View.GONE);
//                            }
//                        }, 1000);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    InterfaceClickListener interfaceClickListener=new InterfaceClickListener() {
        @Override
        public void onClickItem(int position) {
            offset="0";
            Intent intent=new Intent(getActivity(),ActivityProdDet.class);
            intent.putExtra("name",mArraylistFinalProd.get(position).getName());
            intent.putExtra("price",mArraylistFinalProd.get(position).getPrice());
            intent.putExtra("desc",mArraylistFinalProd.get(position).getDescription());
            intent.putExtra("image",mArraylistFinalProd.get(position).getImage());
            intent.putExtra("amazoneUrl",mArraylistFinalProd.get(position).getAmazone_url());
            intent.putExtra("flipcartUrl",mArraylistFinalProd.get(position).getFlipkart_url());
            startActivity(intent);
//          getActivity().finish();

        }
    };
    @Override
    public void onPause() {
        super.onPause();
        offset="0";
        loadingFlg="0";
        mArraylistFinalProd.clear();

    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        offset="0";
        loadingFlg="0";
        mArraylistFinalProd.clear();

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
//            new IntentIntegrators(getActivity()).setBarcodeImageEnabled(true).forSupportFragment(FragmentHome.this).setOrientationLocked(false).setBarcodeImageEnabled(true).setCaptureActivity(ActivityScanDet.class).initiateScan();

//            Intent intent = new Intent(getActivity(), MaterialScanActivity.class);
//            startActivity(intent);
//            getActivity().finish();
            startScan();

        } else {
            String[] perms = {"android.permission.CAMERA"};
            ActivityCompat.requestPermissions(getActivity(),perms, 102);

        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode != MaterialBarcodeScanner.RC_HANDLE_CAMERA_PERM) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            return;
        }
        if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            startScan();
            return;
        }
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.cancel();
            }
        };
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getActivity());
        builder.setTitle("Error")
                .setMessage(R.string.no_camera_permission)
                .setPositiveButton(android.R.string.ok, listener)
                .show();
    }
    //to material scan
    private void startScan() {
        /**
         * Build a new MaterialBarcodeScanner
         */
        final MaterialBarcodeScanner materialBarcodeScanner = new MaterialBarcodeScannerBuilder()
                .withActivity(getActivity())
                .withEnableAutoFocus(true)
                .withBleepEnabled(true)
                .withBackfacingCamera()
                .withCenterTracker()
                .withText("Scanning...")

                .withResultListener(new MaterialBarcodeScanner.OnResultListener() {
                    @Override
                    public void onResult(Barcode barcode) {
                        ActivityScanHistory.scanFlag="1";
                        decodeFormat(barcode.format);
                        Intent intent=new Intent(getActivity(),ActivityScanViewDet.class);
                        intent.putExtra("scanText",barcode.rawValue);
                        intent.putExtra("date",date);
                        intent.putExtra("imgScan","");
                        intent.putExtra("formatName",formatType);
                        startActivity(intent);
                        getActivity().finish();
                    }
                })
                .build();
        materialBarcodeScanner.startScan();
    }


    private String decodeFormat(int format) {
        switch (format) {
            case Barcode.CODE_128:
                formatType = "CODE_128";
                return formatType;
            case Barcode.CODE_39:
                formatType = "CODE_39";
                return formatType;
            case Barcode.CODE_93:
                formatType = "CODE_93";
                return formatType;
            case Barcode.CODABAR:
                formatType = "CODABAR";
                return formatType;
            case Barcode.DATA_MATRIX:
                return "DATA_MATRIX";
            case Barcode.EAN_13:
                return "EAN_13";
            case Barcode.EAN_8:
                return "EAN_8";
            case Barcode.ITF:
                return "ITF";
            case Barcode.QR_CODE:
                formatType = "QR_CODE";
                return formatType;
            case Barcode.UPC_A:
                formatType = "UPC_A";
                return formatType;
            case Barcode.UPC_E:
                return "UPC_E";
            case Barcode.PDF417:
                return "PDF417";
            case Barcode.AZTEC:
                return "AZTEC";
            default:
                return "";
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
                    ActivityScanHistory.scanFlag="1";
                    String contents = result.getContents();
                    String formatName=result.getFormatName();
                   String barcodeImagePath=result.getBarcodeImagePath();
                   Intent intent =new Intent(getActivity(),ActivityScanViewDet.class);
                   intent.putExtra("scanText",contents);
                   intent.putExtra("date",date);
                   intent.putExtra("imgScan",barcodeImagePath);

                   intent.putExtra("formatName",formatName);
                   intent.putExtra("flagFromScan","1");//coming from home screen
                   startActivity(intent);

//
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
