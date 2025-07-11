package app.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class CicloCine extends Evento {

    @ElementCollection
    @OrderColumn(name = "orden")
    private List<Pelicula> peliculasOrdenadas = new ArrayList<>();

    private boolean conCharlasPosteriores;

    @Transient
    private Set<Persona> participantes = new HashSet<>();

    public CicloCine() {}

    public CicloCine(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, boolean conCharlasPosteriores) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.conCharlasPosteriores = conCharlasPosteriores;
    }

    @Override
    public boolean requiereInscripcion() {
        return true;
    }

    public void agregarPelicula(Pelicula pelicula) {
        peliculasOrdenadas.add(pelicula);
    }

    // Devuelve la lista inmutable para proteger la colección interna
    public List<Pelicula> getPeliculasOrdenadas() {
        return Collections.unmodifiableList(peliculasOrdenadas);
    }

    public boolean isConCharlasPosteriores() {
        return conCharlasPosteriores;
    }

    @Override
    public String toString() {
        return super.toString() + " - Ciclo de Cine: " + peliculasOrdenadas.size() + " películas" +
                (conCharlasPosteriores ? ", con charlas posteriores" : "");
    }
}
