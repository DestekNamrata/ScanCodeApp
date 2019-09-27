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
import android.widget.RadioButton;

import com.essensys.JB089.R;
import com.essensys.JB089.Utility.TypeFaceProvider;
/**
 * @author Windows
 *
 */
@SuppressLint("AppCompatCustomView")
public class CustomRadioButton extends RadioButton
{
    private final String TAG="Custom Radio Button";
    /**
     * @param context
     */
    public CustomRadioButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public CustomRadioButton(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        setCustomFont(context, attrs);
    }
    public CustomRadioButton(Context context, AttributeSet attrs, int defStyle)
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
        try
        {
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
}
