package app.example.usuario.proyecto.models;


/**
 * Clase que permite almacenar la información de cada una de las noticias que obtenemos del json
 */
public class Noticia {

    private int id;
    private String fecha;
    private String titulo;
    private String imagen;
    private String texto;

    public Noticia(int id, String fecha, String titulo, String imagen, String texto) {
        this.id= id;
        this.fecha = fecha;
        this.titulo = titulo;
        this.imagen = imagen;
        this.texto = texto;
    }

    public int getId(){
        return id;
    }

    public String getFecha() {
        return fecha;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getImagen() {
        return imagen;
    }

    public String getTexto() {
        return texto;
    }

    public void setId(int id){
        this.id=id;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String toString(){
        return this.titulo.toString();
    }
}