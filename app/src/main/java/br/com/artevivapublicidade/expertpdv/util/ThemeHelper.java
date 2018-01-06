package br.com.artevivapublicidade.expertpdv.util;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.util.TypedValue;

import br.com.artevivapublicidade.expertpdv.R;

/**
 * Created by vinicius on 27/07/2017.
 */

public class ThemeHelper {
    public int getPrimaryColor(Context mContext) {
        TypedValue typedValue = new TypedValue();

        TypedArray a = mContext.obtainStyledAttributes(typedValue.data, new int[] { R.attr.colorPrimary });
        int color = a.getColor(0, 0);

        a.recycle();

        return color;
    }
}
