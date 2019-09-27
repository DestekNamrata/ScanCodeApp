/**
 *
 */
package com.essensys.JB089.CustomView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Button;

import com.essensys.JB089.R;
/**
 * @author Windows
 */
@SuppressLint("AppCompatCustomView")
public class CustomButton extends Button {
    private final String TAG = "Custom Button";
    /**
     * @param context
     */
    public CustomButton(Context context) {
        super(context);
        // TODO Auto-generated constructor stub
    }
    public CustomButton(Context context, AttributeSet attrs) {
        super(context, attrs);
      //  this.setOnTouchListener(this);
      //  setAlpha(context, attrs);
       // setCustomFont(context, attrs);
    }
    public CustomButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
      //  setAlpha(context, attrs);
     //   setCustomFont(context, attrs);
    }
    private void setCustomFont(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomView);
        String customFont = a.getString(R.styleable.CustomView_customFont);
      //  setCustomFont(ctx, customFont);
        a.recycle();
    }
    private void setAlpha(Context ctx, AttributeSet attrs) {
        TypedArray a = ctx.obtainStyledAttributes(attrs, R.styleable.CustomView);
        boolean alpha = a.getBoolean(R.styleable.CustomView_setAlpha, false);
        if (alpha) {
          //  setOnTouchListener(this);
        }
    }

   /* public boolean setCustomFont(Context ctx, String asset) {
        Typeface tf = null;
        try {
            tf = TypeFaceProvider.getTypeFace(ctx,asset);
        } catch (Exception e) {
            Log.e(TAG, "Could not get typeface: " + e.getMessage());
            return false;
        }
        setTypeface(tf);
        return true;
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                v.setAlpha(0.5f);
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_BUTTON_RELEASE:
                v.setAlpha(1);
                break;
        }
        return false;
    }*/
}
