package com.mx.ashe.ashecontrol;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mx.ashe.ashecontrol.app.EndPoints;
import com.mx.ashe.ashecontrol.app.MyApplication;
import com.mx.ashe.ashecontrol.model.UltimoCliente;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by RED on 16/08/16.
 */
public class Activity_ConcluirVisita extends AppCompatActivity {

    @InjectView(R.id.btnConcluirVisita)
    Button btnConcluir;
    @InjectView(R.id.tvClienteName)
    TextView tvCliente;
    @InjectView(R.id.tvDireccion)
    TextView tvDir;
    Toolbar toolbar;
    ActionBar actionBar;

    private View vewMap;
    private LatLng miUbicacion;
    private LocationManager locationManager;
    private boolean bDispositivoLocalizado = false;
    private static final int RADIO = 150;
    String serviceString = Context.LOCATION_SERVICE;
    private GoogleMap map;
    Button btnRegVisita;
    GPSTracker gps;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_concluir);

        ButterKnife.inject(this);
        toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
        context = this;
        TextView tv = new TextView(getApplicationContext());
        tv.setText("Concluir Visita");
        tv.setTextSize(25);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf"));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(tv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (map != null) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(mi_ubiacion);
        }
        vewMap = (View) findViewById(R.id.map);
        locationManager = (LocationManager) getSystemService(serviceString);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        obtenerUbicacion();


        btnConcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                double distance;
                LatLng point = new LatLng(MyApplication.getInstance().getPrefManager().getUltimoCliente().Lat
                        , MyApplication.getInstance().getPrefManager().getUltimoCliente().Long);
                Location instLoc = new Location("punto1");
                instLoc.setLatitude(point.latitude);
                instLoc.setLongitude(point.longitude);
                distance = (double) instLoc.distanceTo(gps.getLocation());
                Log.e("Concluir Distance", String.valueOf(distance));
                if (distance > 150) {
                    new AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
                            .setTitle("Ashe Monitor")
                            .setMessage("Para concluir una visita se requiere estar dentro del area marcada")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                } else {
                    mtd_registrar();
                }


            }
        });

        loadClienteMap();
        tvCliente.setText("Cliente: " + MyApplication.getInstance().getPrefManager().getUltimoCliente().getNombre());

        tvDir.setText("Dirección:  " + MyApplication.getInstance().getPrefManager().getUltimoCliente().getDireccion() + "\n" +
                "Hora: " + MyApplication.getInstance().getPrefManager().getUltimoCliente().getHora()

        );


    }

    public void loadClienteMap() {
        Marker marker = map.addMarker(new MarkerOptions()
                .title(MyApplication.getInstance().getPrefManager().getUltimoCliente().getNombre())
                .snippet(MyApplication.getInstance().getPrefManager().getUltimoCliente().getDireccion())
                .position(new LatLng(MyApplication.getInstance().getPrefManager().getUltimoCliente().Lat,
                        MyApplication.getInstance().getPrefManager().getUltimoCliente().Long))
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                .draggable(false));
        setUpCircle(marker);
    }


    private void setUpCircle(Marker marker) {
        LatLng latLng = marker.getPosition();
        // Configura y Agrega el Circle del Marker
        CircleOptions circleOptions = new CircleOptions();
        circleOptions.center(latLng);
        circleOptions.radius(RADIO);
        circleOptions.strokeColor(Color.rgb(130, 170, 215));
        circleOptions.fillColor(Color.argb(150, 130, 170, 215));
        map.addCircle(circleOptions);
    }

    public void obtenerUbicacion() {
        try {
            if (map != null)
                map.getMyLocation();
            gps = new GPSTracker(context);
            bDispositivoLocalizado = true;
            if (gps.canGetLocation()) {
                gps.getLatitude(); // returns latitude
                gps.getLongitude(); // returns longitude
                // check if GPS enabled
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    miUbicacion = new LatLng(latitude, longitude);
                    if (map != null)
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(miUbicacion, 18));
                } else {
                    gps.showSettingsAlert();
                }
            }
        } catch (Exception e) {
            Log.e("", e.toString());
        }
    }

    GoogleMap.OnMyLocationButtonClickListener mi_ubiacion = new GoogleMap.OnMyLocationButtonClickListener() {

        @Override
        public boolean onMyLocationButtonClick() {

            Location location = map.getMyLocation();
            gps = new GPSTracker(context);
            bDispositivoLocalizado = true;
            if (gps.canGetLocation()) {

                gps.getLatitude(); // returns latitude
                gps.getLongitude(); // returns longitude

                // check if GPS enabled
                if (gps.canGetLocation()) {
                    double latitude = gps.getLatitude();
                    double longitude = gps.getLongitude();
                    miUbicacion = new LatLng(latitude, longitude);
                } else {
                    gps.showSettingsAlert();
                }
            }
            return false;
        }
    };


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }


    private void mtd_registrar() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.FINVISITA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("", "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {

                        MyApplication.getInstance().getPrefManager().storeClienteU(new UltimoCliente("", 0, "", "", true, Float.parseFloat("0.23234"), Float.parseFloat("0.23234")));


                        new AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
                                .setTitle("Ashe Monitor")
                                .setMessage("Has concluido la visita")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        dialog.dismiss();
                                        finish();
                                    }
                                })
                                .setIcon(R.mipmap.ic_launcher)
                                .show();
                    } else {


                        Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    Log.e("", "json parsing error: " + e.getMessage());
                    Toast.makeText(getApplicationContext(), "Json parse error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e("", "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getApplicationContext(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();

                if (error.networkResponse==null){

                    new android.support.v7.app.AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
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

            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", MyApplication.getInstance().getPrefManager().getUser().getID());
                params.put("CLIENTE", String.valueOf(MyApplication.getInstance().getPrefManager().getUltimoCliente().idcliente));
                Log.e("Parametros save ", "params: " + params.toString());
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
    }
}
