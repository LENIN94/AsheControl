package com.mx.ashe.ashecontrol.Fragments;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mx.ashe.ashecontrol.ActivityMenu;
import com.mx.ashe.ashecontrol.R;
import com.mx.ashe.ashecontrol.UI.ColoredSnackbar;
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
    ProgressDialog progressDialog;
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
        progressDialog = new ProgressDialog(getActivity(),
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Descargando datos...");
        progressDialog.show();

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
                        Toast.makeText(getActivity().getApplicationContext(), "No se encontraron registros", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("", "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity().getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
                progressDialog.dismiss();//ocultamos progess dialog.
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("", "Volley error: " + error.getMessage() + ", code: " + networkResponse   );

                if (error.networkResponse==null){

                    new android.support.v7.app.AlertDialog.Builder(getContext(), R.style.AppTheme_Dark_Dialog)
                            .setTitle("Ashe Monitor")
                            .setMessage("Porfavor verifica tu conexión a internet")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {

                                    dialog.dismiss();

                                }
                            })
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                }


                Toast.makeText(getActivity().getApplicationContext(), "Volley error: " + error.getMessage()   , Toast.LENGTH_SHORT).show();



                progressDialog.dismiss();//ocultamos progess dialog.
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", MyApplication.getInstance().getPrefManager().getUser().getID());

                Log.d("Params", String.valueOf(params));
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
