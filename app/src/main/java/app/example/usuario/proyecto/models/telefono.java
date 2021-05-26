package app.example.usuario.proyecto.models;

public class telefono {

    /**
     * Clase modelo para telefonos
     */
    String num_telf;
    String nombre_organismo;

    public telefono(String num_telf, String nombre_organismo) {
        this.num_telf = num_telf;
        this.nombre_organismo = nombre_organismo;
    }

    public String getNum_telf() {
        return num_telf;
    }

    public String getNombre_organismo() {
        return nombre_organismo;
    }

    public void setNum_telf(String num_telf) {
        this.num_telf = num_telf;
    }

    public void setNombre_organismo(String nombre_organismo) {
        this.nombre_organismo = nombre_organismo;
    }
}
