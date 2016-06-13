package com.mx.ashe.ashecontrol.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * Created by LENIN on 13/06/2016.
 */
public class EditTextPersonalizado extends EditText {

    public EditTextPersonalizado(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(),"CaviarDreams.ttf"));
    }
}
