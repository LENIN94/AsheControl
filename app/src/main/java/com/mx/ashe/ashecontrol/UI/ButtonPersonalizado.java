package com.mx.ashe.ashecontrol.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;

/**
 * Created by LENIN on 13/06/2016.
 */
public class ButtonPersonalizado extends Button {
    public ButtonPersonalizado(Context context, AttributeSet attrs) {
        super(context, attrs);
        setTypeface(Typeface.createFromAsset(context.getAssets(),"CaviarDreams.ttf"));
    }
}
