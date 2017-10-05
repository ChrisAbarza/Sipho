package com.example.chris.sipho;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by salv8 on 02/10/2017.
 */

public class Oferta implements Serializable {
    private int id, precioOferta;
    private String nomOferta, descOferta, cateOferta, usuario, imagen, fecha;

    public Oferta(int id, int precioOferta, String nomOferta, String descOferta, String cateOferta, String usuario, String imagen, String fecha) {
        this.id = id;
        this.precioOferta = precioOferta;
        this.nomOferta = nomOferta;
        this.descOferta = descOferta;
        this.cateOferta = cateOferta;
        this.usuario = usuario;
        this.imagen = imagen;
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPrecioOferta() {
        return precioOferta;
    }

    public void setPrecioOferta(int precioOferta) {
        this.precioOferta = precioOferta;
    }

    public String getNomOferta() {
        return nomOferta;
    }

    public void setNomOferta(String nomOferta) {
        this.nomOferta = nomOferta;
    }

    public String getDescOferta() {
        return descOferta;
    }

    public void setDescOferta(String descOferta) {
        this.descOferta = descOferta;
    }

    public String getCateOferta() {
        return cateOferta;
    }

    public void setCateOferta(String cateOferta) {
        this.cateOferta = cateOferta;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
