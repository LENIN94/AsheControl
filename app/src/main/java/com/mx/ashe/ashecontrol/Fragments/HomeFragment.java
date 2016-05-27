package com.mx.ashe.ashecontrol.Fragments;


import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mx.ashe.ashecontrol.ActivityMenu;
import com.mx.ashe.ashecontrol.R;
import com.mx.ashe.ashecontrol.app.MyApplication;


public class HomeFragment extends Fragment {


    public static HomeFragment instancia;

    private Bitmap bitmap;
    private ProgressDialog pDialog;

    private String Mensaje = "Error de Conexión";

    private ListView lista;
    private ArrayAdapter<String> adaptador ;



    TextView tvUsuario;
    private ViewPager pager;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Filtra el layout en el fragment
        final View view = inflater.inflate(R.layout.fragment_home, container, false);
        //obtenemos en titulo y el actionbar
        ((ActivityMenu) getActivity()).getSupportActionBar().setTitle("ASHE Control");
        pager = (ViewPager) view.findViewById(R.id.pager);
        //adaptamos las imagenes
        tvUsuario=(TextView) view.findViewById(R.id.tvU);

     //   tvUsuario.setText("Bienvenido "+ MyApplication.getInstance().getPrefManager().getUser().getId());
        return view;
    }


    public void onBackPressed() {
        if (pager.getCurrentItem() == 0) {

            super.getActivity().onBackPressed();
        } else {
            pager.setCurrentItem(pager.getCurrentItem() - 1);
        }
    }






}
