package app.model;

import app.model.enums.TipoEntrada;
import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class Concierto extends Evento {
    private Set<Artista> artistas;
    private TipoEntrada tipoEntrada;

    public Concierto(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, TipoEntrada tipoEntrada) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.artistas = new HashSet<>();
        this.tipoEntrada = tipoEntrada;
    }

    @Override
    public boolean requiereInscripcion() {
        return tipoEntrada == TipoEntrada.PAGA; // Ejemplo: solo paga requiere inscripción
    }

    public void agregarArtista(Artista artista) {
        artistas.add(artista);
    }

    public Set<Artista> getArtistas() {
        return Collections.unmodifiableSet(artistas);
    }

    public TipoEntrada getTipoEntrada() { return tipoEntrada; }

    @Override
    public String toString() {
        return super.toString() + " - Concierto: " + tipoEntrada + ", Artistas: " + artistas;
    }
}
