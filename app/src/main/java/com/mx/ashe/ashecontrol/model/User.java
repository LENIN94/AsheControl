package com.mx.ashe.ashecontrol.model;

import java.io.Serializable;

/**
 * Created by OmarLenin on 03/03/2016.
 */
public class User implements Serializable {
    String id, name, UltimoFolio;

    public User() {
    }

    public User(String id, String name, String Folio) {
        this.id = id;
        this.name = name;

        UltimoFolio=Folio;

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUltimoFolio() {
        return UltimoFolio;
    }

    public void setUltimoFolio(String Folio) {
        this.UltimoFolio = Folio;
    }


}