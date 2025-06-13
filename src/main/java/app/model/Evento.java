package app.model;

import java.time.LocalDate;
import app.model.enums.EstadoEvento;
import java.util.*;

public abstract class Evento {
    private String nombre;
    private LocalDate fechaInicio;
    private int duracionEstimadasDias;
    private Set<Persona> responsables;
    private EstadoEvento estado;
    private Set<Participante> participantes;

    public Evento(String nombre, LocalDate fechaInicio, int duracionEstimadasDias) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracionEstimadasDias = duracionEstimadasDias;
        this.responsables = new HashSet<>();
        this.participantes = new LinkedHashSet<>();
        this.estado = EstadoEvento.PLANIFICACION;
    }

    public void agregarResponsable(Persona persona) {
        responsables.add(persona);
    }

    public boolean inscribirParticipante(Participante p) {
        if (estado != EstadoEvento.CONFIRMADO && estado != EstadoEvento.EN_EJECUCION) {
            throw new IllegalStateException("El evento no acepta inscripciones en su estado actual.");
        }
        return participantes.add(p);
    }

    public void cambiarEstado(EstadoEvento nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public int getDuracionEstimadasDias() { return duracionEstimadasDias; }
    public EstadoEvento getEstado() { return estado; }
    public Set<Persona> getResponsables() { return Collections.unmodifiableSet(responsables); }
    public Set<Participante> getParticipantes() { return Collections.unmodifiableSet(participantes); }

    @Override
    public String toString() {
        return nombre + " (" + fechaInicio + ", " + estado + ")";
    }
}
