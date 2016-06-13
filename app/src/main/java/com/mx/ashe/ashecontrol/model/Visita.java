package com.mx.ashe.ashecontrol.model;

/**
 * Created by LENIN on 10/06/2016.
 */
public class Visita {


    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getData2() {
        return data2;
    }

    public void setData2(String data2) {
        this.data2 = data2;
    }

    public int getPhotoId() {
        return photoId;
    }

    public void setPhotoId(int photoId) {
        this.photoId = photoId;
    }

    private String data;
    private String data2;
    private int photoId;

    public Visita(String data, String data2, int photoId) {
        this.data = data;
        this.data2 = data2;
        this.photoId = photoId;
    }

}
