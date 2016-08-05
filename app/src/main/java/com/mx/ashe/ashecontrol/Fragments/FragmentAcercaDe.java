package com.mx.ashe.ashecontrol.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mikepenz.aboutlibraries.Libs;
import com.mikepenz.aboutlibraries.LibsBuilder;
import com.mikepenz.aboutlibraries.ui.LibsSupportFragment;
import com.mx.ashe.ashecontrol.ActivityMenu;
import com.mx.ashe.ashecontrol.R;
import com.mx.ashe.ashecontrol.helper.checkConexion;

/**
 * Created by LENIN on 13/06/2016.
 */
public class FragmentAcercaDe extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Filtra el layout en el fragment
        final View view = inflater.inflate(R.layout.fragment_acercade, container, false);
        //obtenemos en titulo y el actionbar
        LibsSupportFragment fragment = new LibsBuilder()
                .withLibraries()
                .withVersionShown(false)
                .withLicenseShown(false)
                .supportFragment();

        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frame_container, fragment).commit();
        return view;
    }
}
