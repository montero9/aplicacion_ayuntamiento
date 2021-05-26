package app.example.usuario.proyecto.models;

/**
 * Clase modelo para prediccion
 */
public class Prediccion {

    String temp;
    String temp_min;
    String temp_max;
    String icono;

    public Prediccion(String temp,  String temp_max, String temp_min, String icono) {
        this.temp = temp;
        this.temp_min = temp_min;
        this.temp_max = temp_max;
        this.icono=icono;

    }

    public String getTemp() {
        return temp;
    }

    public String getTemp_min() {
        return temp_min;
    }

    public String getTemp_max() {
        return temp_max;
    }

    public String getIcono() {
        return icono;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public void setTemp_min(String temp_min) {
        this.temp_min = temp_min;
    }

    public void setTemp_max(String temp_max) {
        this.temp_max = temp_max;
    }

    public void setIcono(String icono) {
        this.icono = icono;
    }
}
