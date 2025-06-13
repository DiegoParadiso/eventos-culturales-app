package app.model;

import app.model.enums.TipoEntrada;
import java.time.LocalDate;
import java.util.Collections;
import java.util.Set;
import java.util.TreeSet;

public class Concierto extends Evento {
    private Set<String> artistas;
    private TipoEntrada tipoEntrada;

    public Concierto(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, TipoEntrada tipoEntrada) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.artistas = new TreeSet<>();
        this.tipoEntrada = tipoEntrada;
    }

    public void agregarArtista(String artista) {
        artistas.add(artista);
    }

    public Set<String> getArtistas() { return Collections.unmodifiableSet(artistas); }
    public TipoEntrada getTipoEntrada() { return tipoEntrada; }

    @Override
    public String toString() {
        return super.toString() + " - Concierto: " + tipoEntrada + ", Artistas: " + artistas;
    }
}
