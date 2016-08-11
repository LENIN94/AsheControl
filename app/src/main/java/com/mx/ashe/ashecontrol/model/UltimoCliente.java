package com.mx.ashe.ashecontrol.model;

/**
 * Created by RED on 18/08/16.
 */
public class UltimoCliente {

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        Direccion = direccion;
    }

    public String getHora() {
        return Hora;
    }

    public void setHora(String hora) {
        Hora = hora;
    }

    public boolean isFinalizado() {
        return Finalizado;
    }

    public void setFinalizado(boolean finalizado) {
        Finalizado = finalizado;
    }

    public String Nombre;
    public String Direccion;
    public String Hora;
    boolean Finalizado;
    public  int idcliente;
    public Float Lat;
    public Float Long;


    public UltimoCliente(String Nombre, int id, String dir, String hora, boolean fin, Float lat, Float Long) {
        this.Nombre = Nombre;
        this.Direccion = dir;
        this.Hora = hora;
        this.idcliente=id;
        Finalizado=fin;
        this.Lat=lat;
        this.Long=Long;


    }
}
