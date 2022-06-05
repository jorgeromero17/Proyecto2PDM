package ues.proyecto2pdm;

public class Objetivo {
    private int idObjetivo;
    private int idUsuario;
    private String objetivo;
    private String estado;
    private int cantPomodoros;

    public Objetivo(){

    }

    public Objetivo(int idObjetivo, int idUsuario, String objetivo, String estado, int cantPomodoros) {
        this.idObjetivo = idObjetivo;
        this.idUsuario = idUsuario;
        this.objetivo = objetivo;
        this.estado = estado;
        this.cantPomodoros = cantPomodoros;
    }

    public int getIdObjetivo() {
        return idObjetivo;
    }

    public void setIdObjetivo(int idObjetivo) {
        this.idObjetivo = idObjetivo;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getObjetivo() {
        return objetivo;
    }

    public void setObjetivo(String objetivo) {
        this.objetivo = objetivo;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public int getCantPomodoros() {
        return cantPomodoros;
    }

    public void setCantPomodoros(int cantPomodoros) {
        this.cantPomodoros = cantPomodoros;
    }
}
