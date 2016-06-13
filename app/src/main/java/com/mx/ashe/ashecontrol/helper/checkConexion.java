package com.mx.ashe.ashecontrol.helper;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by LENIN on 13/06/2016.
 */
public class checkConexion {
    Context c;

    public checkConexion(Context c) {
        this.c = c;
    }

    public boolean WIFIActivo() {
        ConnectivityManager conn = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo info = conn.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }
    public boolean DATOSActivo() {
        ConnectivityManager conn = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            NetworkInfo info = conn.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean estaConectado(){
        if(WIFIActivo()){
            return true;
        }else {
            if (DATOSActivo()){
                return true;
            }else {
                return false;
            }
        }
    }

}
