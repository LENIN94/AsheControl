package com.mx.ashe.ashecontrol.model;

import java.io.Serializable;

/**
 * Created by HP on 02/06/2016.
 */
public class LocationNotWorking implements Serializable {

    private String Lat;

    public String getLat() {
        return Lat;
    }

    public void setLat(String lat) {
        Lat = lat;
    }

    public String getLong() {
        return Long;
    }

    public void setLong(String aLong) {
        Long = aLong;
    }

    public boolean isConcluido() {
        return Concluido;
    }

    public void setConcluido(boolean concluido) {
        Concluido = concluido;
    }

    private String Long;
    private boolean Concluido;

    public LocationNotWorking(String lat,String longt, boolean con){
        this.Concluido=con;
        this.Lat=lat;
        this.Long=longt;
    }



}
