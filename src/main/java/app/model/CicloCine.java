package app.model;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.time.LocalDate;

public class CicloCine extends Evento {
    private Map<Integer, Pelicula> peliculasOrdenadas;
    private boolean conCharlasPosteriores;

    public CicloCine(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, boolean conCharlasPosteriores) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.peliculasOrdenadas = new LinkedHashMap<>();
        this.conCharlasPosteriores = conCharlasPosteriores;
    }

    @Override
    public boolean requiereInscripcion() {
        return true; // Supongamos que el ciclo requiere inscripción
    }

    public void agregarPelicula(int orden, Pelicula pelicula) {
        peliculasOrdenadas.put(orden, pelicula);
    }

    public Map<Integer, Pelicula> getPeliculasOrdenadas() {
        return Collections.unmodifiableMap(peliculasOrdenadas);
    }

    public boolean isConCharlasPosteriores() { return conCharlasPosteriores; }

    @Override
    public String toString() {
        return super.toString() + " - Ciclo de Cine: " + peliculasOrdenadas.size() + " películas" + (conCharlasPosteriores ? ", con charlas posteriores" : "");
    }
}
