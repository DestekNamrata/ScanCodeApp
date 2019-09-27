/**
 *
 */
package com.essensys.JB089.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.EditText;

import com.essensys.JB089.R;
import com.essensys.JB089.Utility.TypeFaceProvider;
/**
 * @author Windows
 *
 */
@SuppressLint("AppCompatCustomView")
public class CustomEditText extends EditText
{
    private final String TAG="Custom Edit Text";
    /**
     * @param context
     */
    public CustomEditText(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public CustomEditText(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setCustomFont(context, attrs);
    }
    public CustomEditText(Context context, AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        setCustomFont(context, attrs);
    }
    private void setCustomFont(Context ctx, AttributeSet attrs)
    {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomView);
        String customFont = a.getString(R.styleable.CustomView_customFont);
        setCustomFont(ctx, customFont);
        a.recycle();
    }
    public boolean setCustomFont(Context ctx, String asset)
    {
        Typeface tf = null;
        try{
            //tf = Typeface.createFromAsset(ctx.getAssets(), asset);
            tf = TypeFaceProvider.getTypeFace(ctx, asset);
        }
        catch (Exception e)
        {
            Log.e(TAG, "Could not get typeface: "+e.getMessage());
            return false;
        }
        setTypeface(tf);
        return true;
    }
    public int getLengthWithTrim(){
        return getText().toString().trim().length();
    }
    public int getLength(){
        return getText().toString().length();
    }
    @Override
    public void setError(CharSequence error) {
        super.setError(error);
        requestFocus();
    }
    public String getString()
    {
        return getText().toString().trim();
    }
    public boolean isEmpty()
    {
        if(getLengthWithTrim()>0){
            return false;
        }else
        {
            return true;
        }
    }
}
