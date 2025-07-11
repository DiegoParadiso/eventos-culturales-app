package app.model;

import jakarta.persistence.Entity;

@Entity
public class Feria extends Evento {

    private int cantidadStands;
    private boolean alAireLibre;

    public Feria() {}

    public Feria(String nombre, java.time.LocalDate fechaInicio, int duracionEstimadasDias, int cantidadStands, boolean alAireLibre) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.cantidadStands = cantidadStands;
        this.alAireLibre = alAireLibre;
    }

    public int getCantidadStands() {
        return cantidadStands;
    }

    public boolean isAlAireLibre() {
        return alAireLibre;
    }

    @Override
    public boolean requiereInscripcion() {
        return true;
    }

    @Override
    public boolean validarCupo() {
        return getParticipantes().size() < cantidadStands;
    }

    @Override
    public String toString() {
        return super.toString() + " - Feria con " + cantidadStands + " stands, " + (alAireLibre ? "al aire libre" : "techada");
    }
}
