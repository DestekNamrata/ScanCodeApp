package com.essensys.JB089.Utility;
import android.content.Context;
import android.graphics.Typeface;

import java.util.Hashtable;

/**
 * Created by ubuntu on 15/3/16.
 */
public class TypeFaceProvider {
    public static final String TYPEFACE_FOLDER = "fonts";
    public static final String TYPEFACE_EXTENSION = ".otf";

    private static Hashtable<String, Typeface> sTypeFaces = new Hashtable<String, Typeface>(
            4);

    public static Typeface getTypeFace(Context context, String fileName) {
        Typeface tempTypeface = sTypeFaces.get(fileName);

        if (tempTypeface == null) {
            String fontPath = new StringBuilder(fileName).toString();
            tempTypeface = Typeface.createFromAsset(context.getAssets(), fontPath);
            sTypeFaces.put(fileName, tempTypeface);
        }
        return tempTypeface;
    }
}
