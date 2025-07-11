package app.model;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;

@Entity
public class CicloCine extends Evento {

    @ElementCollection
    @MapKeyColumn(name = "orden")
    @Column(name = "pelicula")
    private Map<Integer, Pelicula> peliculasOrdenadas = new LinkedHashMap<>();

    private boolean conCharlasPosteriores;

    public CicloCine() {}

    public CicloCine(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, boolean conCharlasPosteriores) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.conCharlasPosteriores = conCharlasPosteriores;
    }

    @Override
    public boolean requiereInscripcion() {
        return true;
    }

    public void agregarPelicula(int orden, Pelicula pelicula) {
        peliculasOrdenadas.put(orden, pelicula);
    }

    public Map<Integer, Pelicula> getPeliculasOrdenadas() {
        return Collections.unmodifiableMap(peliculasOrdenadas);
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
