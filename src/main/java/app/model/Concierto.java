package app.model;

import app.model.enums.TipoEntrada;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Concierto extends Evento {

    @ManyToMany(cascade = CascadeType.ALL)
    private Set<Artista> artistas = new HashSet<>();

    @Enumerated(EnumType.STRING)
    private TipoEntrada tipoEntrada;

    public Concierto() {}

    public Concierto(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, TipoEntrada tipoEntrada) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.tipoEntrada = tipoEntrada;
    }

    @Override
    public boolean requiereInscripcion() {
        return tipoEntrada == TipoEntrada.PAGA;
    }

    public void agregarArtista(Artista artista) {
        artistas.add(artista);
    }

    public Set<Artista> getArtistas() {
        return Collections.unmodifiableSet(artistas);
    }

    public TipoEntrada getTipoEntrada() {
        return tipoEntrada;
    }

    @Override
    public String toString() {
        return super.toString() + " - Concierto: " + tipoEntrada + ", Artistas: " + artistas;
    }
}
