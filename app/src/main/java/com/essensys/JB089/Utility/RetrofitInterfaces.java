package com.essensys.JB089.Utility;

import java.util.HashMap;
import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
/**
 * Created by root on 23/3/18.
 */

public class RetrofitInterfaces {

    // Added by SM
    public interface IScanCode {
        @POST("IScanCode")
        @Multipart
        Call<ResponseBody> post(@PartMap HashMap<String, RequestBody> map,
                                @Part List<MultipartBody.Part> filesArray);
    }






}



