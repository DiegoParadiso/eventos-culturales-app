package app.model;

import java.time.LocalDate;

public class Exposicion extends Evento {
    private String tipoArte;
    private Persona curador;

    public Exposicion(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, String tipoArte, Persona curador) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.tipoArte = tipoArte;
        this.curador = curador;
    }

    public String getTipoArte() { return tipoArte; }
    public Persona getCurador() { return curador; }

    @Override
    public String toString() {
        return super.toString() + " - Exposición: " + tipoArte + ", Curador: " + curador.getNombreCompleto();
    }
}