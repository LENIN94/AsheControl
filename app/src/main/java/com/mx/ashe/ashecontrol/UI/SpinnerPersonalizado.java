package com.mx.ashe.ashecontrol.UI;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.List;

/**
 * Created by LENIN on 12/06/2016.
 */
public class SpinnerPersonalizado extends ArrayAdapter<String> {
    // Initialise custom font, for example:
    Typeface font = Typeface.createFromAsset(getContext().getAssets(),
            "CaviarDreams.ttf");
    // (In reality I used a manager which caches the Typeface objects)
    // Typeface font = FontManager.getInstance().getFont(getContext(), BLAMBOT);

    public SpinnerPersonalizado(Context context, int resource, List<String> items) {
        super(context, resource, items);
    }

    // Affects default (closed) state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }

    // Affects opened state of the spinner
    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        TextView view = (TextView) super.getDropDownView(position, convertView, parent);
        view.setTypeface(font);
        return view;
    }

}
