package ues.proyecto2pdm;

public class Pomodoro {
    private Integer idPomodoro;
    private Integer idUsuario;
    private String fecha;

    public Pomodoro() {
    }

    public Pomodoro(Integer idPomodoro, Integer idUsuario, String fecha) {
        this.idPomodoro = idPomodoro;
        this.idUsuario = idUsuario;
        this.fecha = fecha;
    }

    public Integer getIdPomodoro() {
        return idPomodoro;
    }

    public void setIdPomodoro(Integer idPomodoro) {
        this.idPomodoro = idPomodoro;
    }

    public Integer getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
