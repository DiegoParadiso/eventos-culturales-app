package app.model;

public final class Pelicula {
    private final String titulo;
    private final int orden;
    private final boolean tieneCharla;

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
