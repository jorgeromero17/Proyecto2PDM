package ues.proyecto2pdm;

public class Pomodoro {
    private Integer idPomodoro;
    private String fecha;

    public Pomodoro() {
    }

    public Pomodoro(Integer idPomodoro, String fecha) {
        this.idPomodoro = idPomodoro;
        this.fecha = fecha;
    }

    public Integer getIdPomodoro() {
        return idPomodoro;
    }

    public void setIdPomodoro(Integer idPomodoro) {
        this.idPomodoro = idPomodoro;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }
}
