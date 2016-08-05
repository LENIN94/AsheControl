package com.mx.ashe.ashecontrol.model;

/**
 * Created by LENIN on 04/07/2016.
 */
public class Cliente {

    public Cliente(int intIdCliente, String cliente, String latitud, String longitud) {
        this.intIdCliente = intIdCliente;
        Cliente = cliente;
        Latitud = latitud;
        this.longitud = longitud;
    }

    public int getIntIdCliente() {
        return intIdCliente;
    }

    public void setIntIdCliente(int intIdCliente) {
        this.intIdCliente = intIdCliente;
    }

    public String getCliente() {
        return Cliente;
    }

    public void setCliente(String cliente) {
        Cliente = cliente;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    private int intIdCliente;
    private String Cliente,Latitud,longitud;
}
