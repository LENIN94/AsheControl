package com.mx.ashe.ashecontrol.Fragments;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mx.ashe.ashecontrol.ActivityMenu;
import com.mx.ashe.ashecontrol.R;
import com.mx.ashe.ashecontrol.UI.ColoredSnackbar;
import com.mx.ashe.ashecontrol.UI.TextViewPersonalizado;
import com.mx.ashe.ashecontrol.adapter.RecyclerItemClickListener;
import com.mx.ashe.ashecontrol.adapter.adapterVisita;
import com.mx.ashe.ashecontrol.app.EndPoints;
import com.mx.ashe.ashecontrol.app.MyApplication;
import com.mx.ashe.ashecontrol.model.Visita;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentHistorialVisitas extends Fragment {
    private RecyclerView recycler;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager lManager;

    List<Visita> itemVisita;
    ColoredSnackbar ColoredSnackBar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Filtra el layout en el fragment
        final View view = inflater.inflate(R.layout.fragment_historial_visita, container, false);
        //obtenemos en titulo y el actionbar
        ((ActivityMenu) getActivity()).getSupportActionBar().setTitle("Historial de Visitas");
        // Obtener el Recycler
        recycler = (RecyclerView) view.findViewById(R.id.reciclador);
        recycler.setHasFixedSize(true);
        // Usar un administrador para LinearLayout
        lManager = new LinearLayoutManager(getActivity());
        recycler.setLayoutManager(lManager);
        LoadData();


        recycler.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override public void onItemClick(View view, int position) {
                        // do whatever
                        Snackbar snackbar = Snackbar.make(view, "Posicion " + position +" - " +itemVisita.get(position).getData(), Snackbar.LENGTH_SHORT);
                        ColoredSnackBar.info(snackbar).show();
                    }
                })
        );
        return view;


    }

    private void LoadData() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.GETVISITAS, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("", "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {
                        JSONArray json_arr = obj.getJSONArray("DATA");
                        try {
                            itemVisita = new ArrayList<>();
                            for (int i = 0; i < json_arr.length(); i++) {
                                itemVisita.add(new Visita(json_arr.getJSONObject(i).getString("intIdVisita") + " " + json_arr.getJSONObject(i).getString("vchCliente") + " ", json_arr.getJSONObject(i).getString("dtFecha") + " " + json_arr.getJSONObject(i).getString("tmHoraE"), R.drawable.itemvisita));
                            }
                            // Crear un nuevo adaptador
                            adapter = new adapterVisita(itemVisita);
                            recycler.setAdapter(adapter);
                        } catch (Exception ex) {
                            Log.e("Exepcion", ex.toString());
                        }
                        Log.e("Mensaje", json_arr.toString());
                    } else {
                        Toast.makeText(getActivity().getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("", "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity().getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", MyApplication.getInstance().getPrefManager().getUser().getID());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
