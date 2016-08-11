package com.mx.ashe.ashecontrol.service;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.maps.model.LatLng;
import com.mx.ashe.ashecontrol.app.EndPoints;
import com.mx.ashe.ashecontrol.app.MyApplication;
import com.mx.ashe.ashecontrol.helper.Cronometro;
import com.mx.ashe.ashecontrol.model.UltimoCliente;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Service_AutoConcluir extends Service {
    private static final String TAG = "Servicio ";
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private double Lat, Long;
    public static boolean Tiempo300 = false;
    public Location GLOC = null;
    private Cronometro cronometro = null;

    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;


        public boolean valida(String strHoraPospuesta, String strHoraActual) {


            boolean resultado = false;
            try {
                /**Obtenemos las fechas enviadas en el formato a comparar*/
                SimpleDateFormat formateador = new SimpleDateFormat("HH:mm:ss");
                Date DateHoraP = formateador.parse(strHoraPospuesta);
                Date DateHoraA = formateador.parse(strHoraActual);

                if (DateHoraP.before(DateHoraA)) {
                    resultado = true;
                } else {
              /*  if ( DateHoraA.before(DateHoraP) ){*/
                    resultado = false;
              /* }else{
                    resultado= true;
                }*/
                }
            } catch (ParseException e) {
                System.out.println("Se Produjo un Error!!!  " + e.getMessage());
            }
            return resultado;

        }


        private boolean validaLocalizacion() {
            Calendar cal = Calendar.getInstance();
            SimpleDateFormat formatoFechaHora = new SimpleDateFormat(
                    "dd-MM-yyyy HH:mm:ss", Locale.getDefault());
            String strfechaHora = formatoFechaHora.format(cal
                    .getTimeInMillis());
            int HORA = cal.get(Calendar.HOUR_OF_DAY);
            int DIA = cal.get(Calendar.DAY_OF_WEEK);
            if ((HORA >= 8 && HORA < 19)  && DIA != 1 && DIA != 7 ) {
                Log.i("Service", "En horario de trabajo");
                return true;
            } else {
                Log.i("Service", "Fuera de horario de trabajo");
                return false;
            }


        }

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        public Date sumarRestarHorasFecha(Date fecha, int minutos) {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(fecha); // Configuramos la fecha que se recibe
            calendar.add(Calendar.SECOND, minutos);  // numero de minutos a añadir, o restar en caso de minutos<0
            return calendar.getTime(); // Devuelve el objeto Date con las nuevas minutos añadidas
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged en servicio: " + location);
            Log.e(TAG, "onLocationChanged isfinalizado: " + MyApplication.getInstance().getPrefManager().getUltimoCliente().isFinalizado());
            if ( validaLocalizacion() &&
                    MyApplication.getInstance().getPrefManager().getUltimoCliente().isFinalizado()== false) {
                mLastLocation.set(location);
                double currentLatitude = location.getLatitude();
                double currentLongitude = location.getLongitude();
                Lat = currentLatitude;
                Long = currentLongitude;
                GLOC = location;


                Log.i("Latitud guardada", String.valueOf(MyApplication.getInstance().getPrefManager().getUltimoCliente().Lat));
                Log.i("Longitud guardada", String.valueOf(MyApplication.getInstance().getPrefManager().getUltimoCliente().Long));
                Log.i("Concluido", String.valueOf(MyApplication.getInstance().getPrefManager().getUltimoCliente().isFinalizado()));

                if (MyApplication.getInstance().getPrefManager().getUltimoCliente().isFinalizado()==false) {




                        double distance;
                        LatLng point = new LatLng(MyApplication.getInstance().getPrefManager().getUltimoCliente().Lat
                                , MyApplication.getInstance().getPrefManager().getUltimoCliente().Long);
                        Location instLoc = new Location("punto1");
                        instLoc.setLatitude(point.latitude);
                        instLoc.setLongitude(point.longitude);
                        distance = (double) instLoc.distanceTo(GLOC);

                        if (distance < 150) {

                           Log.e("Serivice_AutoConcluir", "Esta dentro del rango ");



                        } else {

                            Log.e("Serivice_AutoConcluir", "ha salido del perimetro");
                          //  LocationNotWorking locnot = new LocationNotWorking(String.valueOf(currentLatitude), String.valueOf(currentLongitude), MyApplication.getInstance().getPrefManager().getLocationNotWorking().isConcluido());
                           // MyApplication.getInstance().getPrefManager().storeLocationNotWorking(locnot);

                            mtd_registrar();

                        }

                } else {

                }
            }

        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
          //  Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.GPS_PROVIDER),
            new LocationListener(LocationManager.NETWORK_PROVIDER)
    };

    @Override
    public IBinder onBind(Intent arg0) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e(TAG, "onStartCommand");
        super.onStartCommand(intent, flags, startId);
        return START_STICKY;
    }

    @Override
    public void onCreate() {
        Log.e(TAG, "onCreate");
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[1]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE,
                    mLocationListeners[0]);
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "gps provider does not exist " + ex.getMessage());
        }
    }

    @Override
    public void onDestroy() {
        Log.e(TAG, "onDestroy");
        super.onDestroy();
        if (mLocationManager != null) {
            for (int i = 0; i < mLocationListeners.length; i++) {
                try {

                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mLocationManager.removeUpdates(mLocationListeners[i]);
                } catch (Exception ex) {
                    Log.i(TAG, "fail to remove location listners, ignore", ex);
                }
            }
        }
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager");
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
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

                        Log.e("Serivio auto", "Visita finalizada enviado al servidor ");
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

                 Log.e("Servicio auto conluir ", "No hay conexión de red");
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
