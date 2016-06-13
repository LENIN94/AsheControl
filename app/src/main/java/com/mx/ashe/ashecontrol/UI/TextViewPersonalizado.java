package com.mx.ashe.ashecontrol.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

/**
 * Created by LENIN on 11/06/2016.
 */
public class TextViewPersonalizado extends TextView{
    public TextViewPersonalizado(Context con, AttributeSet att){
        super(con,att);
        setTypeface(Typeface.createFromAsset(con.getAssets(),"CaviarDreams.ttf"));
    }
}
