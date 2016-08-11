package com.mx.ashe.ashecontrol.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mx.ashe.ashecontrol.ActivityMenu;
import com.mx.ashe.ashecontrol.R;
import com.mx.ashe.ashecontrol.helper.checkConexion;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;


public class HomeFragment extends Fragment implements MaterialIntroListener {
    checkConexion checkConexion;
    ImageView imgRed;
    private static final String MENU_SEARCH_ID_TAG = "menuSearchIdTag";
    private static final String MENU_ABOUT_ID_TAG = "menuAboutIdTag";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Filtra el layout en el fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        //obtenemos en titulo y el actionbar
        ((ActivityMenu) getActivity()).getSupportActionBar().setTitle("ASHE Control");
        checkConexion = new checkConexion(getContext());


        imgRed = (ImageView) view.findViewById(R.id.notnet);
        if (!checkConexion.estaConectado()) {
            imgRed.setImageResource(R.drawable.notnetwork);
        }

      //  showIntro(imgRed, "lab", "menu", FocusGravity.CENTER);

        return view;
    }
    public void showIntro(View view, String id, String text, FocusGravity focusGravity) {
        new MaterialIntroView.Builder(getActivity())
                .enableDotAnimation(true)
                .setFocusGravity(focusGravity)
                .setFocusType(Focus.MINIMUM)
                .setDelayMillis(100)
                .enableFadeAnimation(true)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setListener((MaterialIntroListener) this)
                .setUsageId(id)
                .show();
    }

    @Override
    public void onUserClicked(String s) {
        switch (s) {
            case MENU_SEARCH_ID_TAG:
                showIntro(imgRed, MENU_ABOUT_ID_TAG, "Hola", FocusGravity.LEFT);
                break;
        }
    }
}
