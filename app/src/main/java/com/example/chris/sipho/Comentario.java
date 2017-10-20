package com.example.chris.sipho;

import java.io.Serializable;

/**
 * Created by salv8 on 19/10/2017.
 */

public class Comentario implements Serializable {
    private long usuario;
    private String coment, valoracion, fecha, nomusr, img;

    public Comentario(long usuario, String coment, String valoracion, String fecha, String nomusr, String img) {
        this.usuario = usuario;
        this.coment = coment;
        this.valoracion = valoracion;
        this.fecha = fecha;
        this.nomusr = nomusr;
        this.img = img;
    }

    public long getUsuario() {
        return usuario;
    }

    public void setUsuario(long usuario) {
        this.usuario = usuario;
    }

    public String getComent() {
        return coment;
    }

    public void setComent(String coment) {
        this.coment = coment;
    }

    public String getValoracion() {
        return valoracion;
    }

    public void setValoracion(String valoracion) {
        this.valoracion = valoracion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getNomusr() {
        return nomusr;
    }

    public void setNomusr(String nomusr) {
        this.nomusr = nomusr;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
