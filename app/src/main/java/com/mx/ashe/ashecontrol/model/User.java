package com.mx.ashe.ashecontrol.model;

import java.io.Serializable;

/**
 * Created by OmarLenin on 03/03/2016.
 */
public class User implements Serializable {
    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public String getPass() {
        return Pass;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getImagen() {
        return Imagen;
    }

    public void setImagen(String imagen) {
        Imagen = imagen;
    }

    String ID, Nombre, Usuario, Pass,Imagen;

    public User() {
    }

    public User(String ID, String Nombre, String Usuario, String Pass, String Imagen) {
        this.ID = ID;
        this.Nombre = Nombre;
        this.Usuario = Usuario;
        this.Pass = Pass;
        this.Imagen = Imagen;

    }



}