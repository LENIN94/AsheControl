package com.mx.ashe.ashecontrol.Fragments;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import com.mx.ashe.ashecontrol.GPSTracker;
import com.mx.ashe.ashecontrol.R;
import com.mx.ashe.ashecontrol.UI.ColoredSnackbar;
import com.mx.ashe.ashecontrol.app.EndPoints;
import com.mx.ashe.ashecontrol.app.MyApplication;
import com.mx.ashe.ashecontrol.model.Cliente;
import com.mx.ashe.ashecontrol.model.LastLocation;
import com.mx.ashe.ashecontrol.model.LocationNotWorking;
import com.mx.ashe.ashecontrol.model.UltimoCliente;
import com.mx.ashe.ashecontrol.service.Service_AutoConcluir;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import co.mobiwise.materialintro.animation.MaterialIntroListener;
import co.mobiwise.materialintro.shape.Focus;
import co.mobiwise.materialintro.shape.FocusGravity;
import co.mobiwise.materialintro.view.MaterialIntroView;

/**
 * Created by HP on 27/05/2016.
 */
public class FragmentRegAsistencia extends AppCompatActivity implements MaterialIntroListener {

    private View vewMap;
    private LatLng miUbicacion;
    private LocationManager locationManager;
    private boolean bDispositivoLocalizado = false;
    private Context context;
    String serviceString = Context.LOCATION_SERVICE;
    private GoogleMap map;
    Button btnRegVisita;
    GPSTracker gps;
    Spinner spnClientes;
    private static final int RADIO = 150;
    ColoredSnackbar ColoredSnackBar;
    Toolbar toolbar;
    ActionBar actionBar;
    ArrayList<Cliente> ListaCliente = new ArrayList<Cliente>();
    CardView cardView;
    String Localizacion = null;

    String ClienteSeleccionado;
    int IDclienteSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_reg_asistencia);
        toolbar = (Toolbar) findViewById(R.id.toolbarr);
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();


        TextView tv = new TextView(getApplicationContext());
        tv.setText("Registrar Nueva Visita");
        tv.setTextSize(25);
        tv.setTypeface(Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf"));
        actionBar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);

        actionBar.setCustomView(tv);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        context = this;


        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        if (map != null) {
            map.setMyLocationEnabled(true);
            map.setOnMyLocationButtonClickListener(mi_ubiacion);
        }
        vewMap = (View) findViewById(R.id.map);
        locationManager = (LocationManager) getSystemService(serviceString);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setMyLocationButtonEnabled(true);
        spnClientes = (Spinner) findViewById(R.id.spnClientes);
        btnRegVisita = (Button) findViewById(R.id.btn_asistencia);
        fillSpinner();
        LoadSpiner();
        cardView = (CardView) findViewById(R.id.card_map);

        btnRegVisita.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                if (spnClientes.getSelectedItemPosition() != 0) {


                    double distance;
                    LatLng point = new LatLng(Double.parseDouble(ListaCliente.get(spnClientes.getSelectedItemPosition()).getLatitud())
                            , Double.parseDouble(ListaCliente.get(spnClientes.getSelectedItemPosition()).getLongitud()));
                    Location instLoc = new Location("punto1");
                    instLoc.setLatitude(point.latitude);
                    instLoc.setLongitude(point.longitude);
                    distance = (double) instLoc.distanceTo(gps.getLocation());

                    Log.e("Distance to ", String.valueOf(distance));
                    if (distance > 150) {

                        new AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
                                .setTitle("Ashe Monitor")
                                .setMessage("Necesitas estar dentro del perimetro permitido para poder registrar esta visita")
                                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        // continue with delete
                                        dialog.dismiss();
                                    }
                                })
                                .setIcon(R.mipmap.ic_launcher)
                                .show();
                    } else {


                        ClienteSeleccionado = ListaCliente.get(spnClientes.getSelectedItemPosition()).getCliente();
                        IDclienteSeleccionado = ListaCliente.get(spnClientes.getSelectedItemPosition()).getIntIdCliente();


                        LayoutInflater layoutInflater = LayoutInflater.from(context);
                        final View promptView = layoutInflater.inflate(R.layout.form_visita, null);
                        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                        alertDialogBuilder.setView(promptView);

                        final EditText editText = (EditText) promptView.findViewById(R.id.edtDetalles);
                        final Button btnSave = (Button) promptView.findViewById(R.id.btncheck);
                        final Button btndimiss = (Button) promptView.findViewById(R.id.btndismis);
                        // setup a dialog window
                        alertDialogBuilder.setCancelable(false);
                        final AlertDialog alert = alertDialogBuilder.create();
                        btnSave.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Log.e(editText.getText().toString(), editText.getText().toString());
                                if (editText.getText().toString().trim().equals("")) {

                                    Snackbar.make(view, "Antes de enviar los datos asegurate de ingresa una descripción", Snackbar.LENGTH_LONG).show();
                                } else {

                                    mtd_registrar(v, editText.getText().toString());

                                    alert.cancel();
                                }

                            }
                        });

                        btndimiss.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                alert.cancel();
                            }
                        });


                        // create an alert dialog

                        alert.show();


                        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault());
                        List<android.location.Address> list = null;
                        try {
                            list = geocoder.getFromLocation(
                                    gps.getLatitude(), gps.getLongitude(), 1);

                            if (!list.isEmpty()) {
                                android.location.Address address = list.get(0);
                                Localizacion = address.getAddressLine(0) + ", " + address.getAddressLine(1) + ", " + address.getLocality() + ", " + address.getCountryName();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                } else {

                    new AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
                            .setTitle("Ashe Monitor")
                            .setMessage("Necesitas Seleccionar un cliente para continuar")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    // continue with delete
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(R.mipmap.ic_launcher)
                            .show();
                }


                //   Snackbar.make(v,"Posicion "+ String.valueOf(spnClientes.getSelectedItemPosition()),Snackbar.LENGTH_LONG).show();
            }
        });
        obtenerUbicacion();
        spnClientes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (ListaCliente.get(position).getCliente() != "Selecciona") {


                    Marker marker = map.addMarker(new MarkerOptions()
                            .title(ListaCliente.get(position).getCliente())
                            .snippet(String.valueOf(ListaCliente.get(position).getIntIdCliente()))
                            .position(new LatLng(Double.parseDouble(ListaCliente.get(position).getLatitud()),
                                    Double.parseDouble(ListaCliente.get(position).getLongitud())))
                            .icon(BitmapDescriptorFactory.fromResource(R.drawable.marker))
                            .draggable(false));
                    setUpCircle(marker);

                    btnRegVisita.setEnabled(true);

                } else {
                    //btnRegVisita.setEnabled(false);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        showIntro(spnClientes, "1", "Selecciona un cliente.", Focus.MINIMUM, FocusGravity.CENTER);

    }

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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
        }
        return super.onKeyDown(keyCode, event);
    }

    private void LoadSpiner() {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.GETCLIENT, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                Log.e("", "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {
                        JSONArray json_arr = obj.getJSONArray("DATA");
                        try {
                            if (json_arr.length() == 0) {
                                ListaCliente.add(new Cliente(0, "Ninguno", "0", "0"));
                            } else {
                                ListaCliente.add(new Cliente(0, "Selecciona", "0", "0"));
                                for (int i = 1; i < json_arr.length() + 1; i++) {
                                    String Lat, Long;
                                    if (json_arr.getJSONObject(i - 1).getString("vchLatitud") == "null") {
                                        Lat = "0";
                                        Long = "0";
                                    } else {
                                        Lat = (json_arr.getJSONObject(i - 1).getString("vchLatitud"));
                                        Long = (json_arr.getJSONObject(i - 1).getString("vchLongitud"));
                                    }
                                    ListaCliente.add(new Cliente(json_arr.getJSONObject(i - 1).getInt("intIdCliente"),
                                            json_arr.getJSONObject(i - 1).getString("vchEmpresa"),
                                            Lat,
                                            Long
                                    ));
                                }
                            }
                            fillSpinner();
                        } catch (Exception ex) {
                            Log.e("Exepcion", ex.toString());
                        }
                        Log.e("Mensaje", json_arr.toString());

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
                return params;
            }
        };


        MyApplication.getInstance().addToRequestQueue(strReq);

    }

    private void fillSpinner() {
        CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(FragmentRegAsistencia.this, ListaCliente);
        spnClientes.setAdapter(adapter);
    }

    private void mtd_registrar(final View v, final String editText) {
        StringRequest strReq = new StringRequest(Request.Method.POST,
                EndPoints.REGVISITA, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e("", "response: " + response);
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getBoolean("error") == false) {

                        Snackbar snackbar = Snackbar.make(v, "Datos registrados correctamente", Snackbar.LENGTH_SHORT);
                        ColoredSnackBar.info(snackbar).show();
                        LastLocation loc = new LastLocation("1", String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()));
                        MyApplication.getInstance().getPrefManager().storeLocation(loc);

                        LocationNotWorking locnot = new LocationNotWorking(String.valueOf(gps.getLatitude()), String.valueOf(gps.getLongitude()), false);
                        MyApplication.getInstance().getPrefManager().storeLocationNotWorking(locnot);

                        Calendar calendar = Calendar.getInstance();
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
                        String hora = simpleDateFormat.format(calendar.getTimeInMillis());


                        UltimoCliente ultimoCliente = new UltimoCliente(ClienteSeleccionado, IDclienteSeleccionado, Localizacion, hora, false,
                                Float.parseFloat(ListaCliente.get(spnClientes.getSelectedItemPosition()).getLatitud()), Float.parseFloat(ListaCliente.get(spnClientes.getSelectedItemPosition()).getLongitud()));
                        MyApplication.getInstance().getPrefManager().storeClienteU(ultimoCliente);

                         startService(new Intent(FragmentRegAsistencia.this, Service_AutoConcluir.class));


                        new AlertDialog.Builder(context, R.style.AppTheme_Dark_Dialog)
                                .setTitle("Ashe Monitor")
                                .setMessage("Has registrado tu visita")
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
                        Snackbar snackbar = Snackbar.make(v, "Ocurrio un error", Snackbar.LENGTH_SHORT);
                        ColoredSnackBar.alert(snackbar).show();
                        // Toast.makeText(getApplicationContext(), "Ocurrio un error", Toast.LENGTH_LONG).show();
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
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("ID", MyApplication.getInstance().getPrefManager().getUser().getID());
                params.put("LONG", String.valueOf(gps.getLongitude()));
                params.put("LAT", String.valueOf(gps.getLatitude()));

                params.put("DESC", editText);
                Log.e("lat", String.valueOf(gps.getLatitude()));

                params.put("UBICACION", Localizacion);
                params.put("CLIENTE", String.valueOf(ListaCliente.get(spnClientes.getSelectedItemPosition()).getIntIdCliente()));
                Log.e("Parametros save ", "params: " + params.toString());


                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq);
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
    public void onUserClicked(String materialIntroViewId) {
        if (materialIntroViewId == "1")
            showIntro(cardView, "2", "Puedes ver el rango permitido para poder registrar\n" +
                    "la ubicacion y actualizar la ubicación\n" +
                    "el icono de gps en la esquina del mapa", Focus.MINIMUM, FocusGravity.RIGHT);
        else if (materialIntroViewId == "2")
            showIntro(btnRegVisita, "3", "Una vez cerca del cliente da clic para continuar", Focus.MINIMUM, FocusGravity.CENTER);
    }

    public class CustomSpinnerAdapter extends BaseAdapter implements SpinnerAdapter {
        private final Context activity;
        private ArrayList<Cliente> asr;

        public CustomSpinnerAdapter(Context context, ArrayList<Cliente> asr) {
            this.asr = asr;
            activity = context;
        }

        public int getCount() {
            return asr.size();
        }

        public Object getItem(int i) {
            return asr.get(i);
        }

        public long getItemId(int i) {
            return (long) i;
        }

        @Override
        public View getDropDownView(int position, View convertView, ViewGroup parent) {
            TextView txt = new TextView(activity);
            txt.setPadding(12, 12, 12, 12);
            txt.setTextSize(20);
            txt.setGravity(Gravity.CENTER_VERTICAL);
            txt.setText(asr.get(position).getCliente());
            txt.setTypeface(Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf"));
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }

        public View getView(int i, View view, ViewGroup viewgroup) {
            TextView txt = new TextView(activity);
            txt.setGravity(Gravity.CENTER);
            txt.setPadding(8, 8, 8, 8);
            txt.setTextSize(16);
            txt.setTypeface(Typeface.createFromAsset(getAssets(), "CaviarDreams.ttf"));
            txt.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_down, 0);
            txt.setText(asr.get(i).getCliente());
            txt.setTextColor(Color.parseColor("#000000"));
            return txt;
        }
    }

    public void showIntro(View view, String id, String text, Focus focusType, FocusGravity focusGravity) {
        new MaterialIntroView.Builder(FragmentRegAsistencia.this)
                .enableDotAnimation(false)
                .setFocusGravity(focusGravity)
                .setFocusType(focusType)
                .setDelayMillis(200)
                .enableFadeAnimation(true)
                .setListener(this)
                .performClick(true)
                .setInfoText(text)
                .setTarget(view)
                .setUsageId(id) //THIS SHOULD BE UNIQUE ID
                .show();
    }
}
