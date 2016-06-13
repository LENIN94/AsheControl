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


public class HomeFragment extends Fragment {
    checkConexion checkConexion;
    ImageView imgRed;

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
        return view;
    }
}
