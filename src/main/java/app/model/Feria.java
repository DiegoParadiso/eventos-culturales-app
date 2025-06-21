package app.model;

import java.time.LocalDate;

public class Feria extends Evento {
    private int cantidadStands;
    private boolean alAireLibre;

    public Feria(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, int cantidadStands, boolean alAireLibre) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.cantidadStands = cantidadStands;
        this.alAireLibre = alAireLibre;
    }

    public int getCantidadStands() { return cantidadStands; }
    public boolean isAlAireLibre() { return alAireLibre; }

    @Override
    public String toString() {
        return super.toString() + " - Feria con " + cantidadStands + " stands, " + (alAireLibre ? "al aire libre" : "techada");
    }
    @Override
public boolean requiereInscripcion() {
    return true; // o una variable si querés que sea configurable
}

@Override
public boolean validarCupo() {
    return getParticipantes().size() < cantidadStands;
}

}
