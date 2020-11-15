package com.vayortricks.vtproductscanner.DataClass;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

/**
 * Created by Jaison
 */

public class WebAppInterface
{
    Context mContext;
    Activity act;

    /** Instantiate the receiver and set the context */
    public WebAppInterface(Context c) {
        mContext = c;
        act= (Activity) mContext;
    }

    @JavascriptInterface
    public void showShopping()
    {
//        Intent intent = new Intent(mContext, ShoppingActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//        act.finish();
        Toast.makeText(mContext,"clicked",Toast.LENGTH_LONG).show();
    }

    @JavascriptInterface
    public void showOrders(int orderid)
    {
//        Intent intent = new Intent(mContext, MyOrdersActivity.class);
//        intent.putExtra("order",orderid);
//        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
//        mContext.startActivity(intent);
//        act.finish();
    }
}
