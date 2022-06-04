package ues.proyecto2pdm;

public class Usuario {
    private int idUsuario;
    private String correo;
    private String nombre;

    public Usuario(){

    }

    public Usuario(int idUsuario, String correo, String nombre) {
        this.idUsuario = idUsuario;
        this.correo = correo;
        this.nombre = nombre;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
}
