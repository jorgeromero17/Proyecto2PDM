package ues.proyecto2pdm;

import android.net.Uri;

public class Publicacion {
    private String id,nombreusuario,descripcion;
    private String urlimagen;
    private String urlImagenUsuario;
    private int likes;
    private String fecha;

    public  Publicacion(){

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombreusuario() {
        return nombreusuario;
    }

    public void setNombreusuario(String nombreusuario) {
        this.nombreusuario = nombreusuario;
    }

    public String getUrlimagen() {
        return urlimagen;
    }

    public void setUrlimagen(String urlimagen) {
        this.urlimagen = urlimagen;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getUrlImagenUsuario() {
        return urlImagenUsuario;
    }

    public void setUrlImagenUsuario(String urlImagenUsuario) {
        this.urlImagenUsuario = urlImagenUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
