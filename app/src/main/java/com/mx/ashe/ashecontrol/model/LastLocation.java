package com.mx.ashe.ashecontrol.model;

import java.io.Serializable;

/**
 * Created by HP on 30/05/2016.
 */
public class LastLocation  implements Serializable {

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

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

    String ID, Lat, Long;

    public LastLocation() {
    }

    public LastLocation(String ID, String lat, String lgn) {
        this.ID = ID;
        this.Lat = lat;
        this.Long = lgn;


    }
}
