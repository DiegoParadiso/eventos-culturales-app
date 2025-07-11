package app.model;

import jakarta.persistence.Embeddable;

@Embeddable
public class Pelicula {

    private String titulo;
    private int orden;
    private boolean tieneCharla;

    public Pelicula() {}

    public Pelicula(String titulo, int orden, boolean tieneCharla) {
        this.titulo = titulo;
        this.orden = orden;
        this.tieneCharla = tieneCharla;
    }

    public String getTitulo() {
        return titulo;
    }

    public int getOrden() {
        return orden;
    }

    public boolean isTieneCharla() {
        return tieneCharla;
    }

    @Override
    public String toString() {
        return orden + ". " + titulo + (tieneCharla ? " (con charla)" : "");
    }
}
