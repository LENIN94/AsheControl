package com.mx.ashe.ashecontrol.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.mx.ashe.ashecontrol.model.LastLocation;
import com.mx.ashe.ashecontrol.model.LocationNotWorking;
import com.mx.ashe.ashecontrol.model.UltimoCliente;
import com.mx.ashe.ashecontrol.model.User;


/**
 * Created by OmarLenin on 03/03/2016.
 */
public class MyPreferenceManager {

    private String TAG = MyPreferenceManager.class.getSimpleName();

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "ASHEControl";

    // All Shared Preferences Keys
    private static final String KEY_USER_ID = "ID";
    private static final String KEY_USER_NAME = "Usuario";
    private static final String KEY_NAME = "Nombre";
    private static final String KEY_PASS = "Pass";
    private static final String KEY_IMG = "Imagen";

    // Constructor
    public MyPreferenceManager(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }
    public void storeUser(User user) {
        editor.putString(KEY_USER_ID, user.getID());
        editor.putString(KEY_NAME, user.getNombre());
        editor.putString(KEY_PASS, user.getPass());
        editor.putString(KEY_USER_NAME, user.getUsuario());
        editor.putString(KEY_IMG, user.getImagen());

        editor.commit();

        Log.e(TAG, "Usuario Almacenado: " + user.getNombre() + ", " );
    }

    public void storeLocation(LastLocation location){
        editor.putString("IDloc", location.getID());
        editor.putString("Lat", location.getLat());
        editor.putString("Long", location.getLong());
        editor.commit();
        Log.e(TAG, "Localizacion Almacenada: " + location.getLat() + ", " );
    }
    public void storeLocationNotWorking(LocationNotWorking location){
        editor.putBoolean("CONCLUIDO", location.isConcluido());
        editor.putString("Lat2", location.getLat());
        editor.putString("Long2", location.getLong());
        editor.commit();
        Log.e(TAG, "Localizacion fuera de visita: " + location.isConcluido() + ", " );
    }

    public void storeClienteU(UltimoCliente location){
        editor.putBoolean("fin", location.isFinalizado());
        editor.putString("cliente", location.getNombre());
        editor.putString("dir", location.getDireccion());
        editor.putString("hora", location.getHora());
        editor.putInt("idcliente",location.idcliente);
        editor.putFloat("ulat", location.Lat);
        editor.putFloat("ulong",location.Long);
        editor.commit();
        Log.e(TAG, "Localizacion fuera de visita: " + location.isFinalizado() + ", " );
    }

    public UltimoCliente getUltimoCliente(){
        if (pref.getString("ID", null) != null) {
            String nombre, dir,hora;
            boolean conc;int idc;
            float Long, Lat;

            nombre = pref.getString("cliente", null);
            dir = pref.getString("dir", null);
            hora = pref.getString("hora", null);
            conc=pref.getBoolean("fin",false);
            idc=pref.getInt("idcliente",0);
            Long=pref.getFloat("ulong",0);
            Lat=pref.getFloat("ulat",0);

            UltimoCliente ultimoCliente = new UltimoCliente(nombre,idc,dir,hora,conc,Lat,Long);
            return ultimoCliente;
        }
        return null;
    }


    public LastLocation getLocation(){
        if (pref.getString("ID", null) != null) {
            String ID, Lat,Long;
            ID = pref.getString("ID", null);
            Lat = pref.getString("Lat", null);
            Long = pref.getString("Long", null);
            LastLocation lastLocation = new LastLocation(ID,Lat,Long);
            return lastLocation;
        }
        return null;
    }
    public LocationNotWorking getLocationNotWorking(){
        if (pref.getString("ID", null) != null) {
            boolean Concluido;
            String Lat;
            String Long;
            Concluido = pref.getBoolean("CONCLUIDO", false);
            Lat = pref.getString("Lat2", null);
            Long = pref.getString("Long2", null);
            LocationNotWorking notWorking = new LocationNotWorking(Lat,Long,Concluido);
            return notWorking;
        }
        return null;
    }


    public User getUser() {
        if (pref.getString(KEY_USER_ID, null) != null) {
            String ID, Nombre,Usuario, Pass,Imagen;
            ID = pref.getString(KEY_USER_ID, null);
            Nombre = pref.getString(KEY_NAME, null);
            Usuario = pref.getString(KEY_USER_NAME, null);
            Pass = pref.getString(KEY_PASS, null);
            Imagen = pref.getString(KEY_IMG, null);


            User user = new User(ID,Nombre,Usuario,Pass,Imagen);
            return user;
        }
        return null;
    }

    public void clear() {
        editor.clear();
        editor.commit();
    }
}