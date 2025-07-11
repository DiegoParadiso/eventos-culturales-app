package app.model;

import app.model.enums.EstadoEvento;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.*;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class Evento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private LocalDate fechaInicio;
    private int duracionEstimadasDias;

    @Enumerated(EnumType.STRING)
    private EstadoEvento estado;

    @ManyToMany
    private Set<Persona> responsables = new HashSet<>();

    @ManyToMany
    private Set<Participante> participantes = new HashSet<>();

    public Evento() {
        this.estado = EstadoEvento.PLANIFICACION;
    }

    public Evento(String nombre, LocalDate fechaInicio, int duracionEstimadasDias) {
        this();
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracionEstimadasDias = duracionEstimadasDias;
    }

    public void agregarResponsable(Persona persona) {
        responsables.add(persona);
    }

    public void agregarResponsable(Persona persona, String rol) {
        responsables.add(persona);
        persona.asignarRol(this, rol);
    }

    public Set<Persona> getResponsablesConRol(String rol) {
        Set<Persona> conRol = new HashSet<>();
        for (Persona p : responsables) {
            if (p.obtenerRoles(this).contains(rol)) {
                conRol.add(p);
            }
        }
        return conRol;
    }

    public boolean tieneRol(Persona persona, String rol) {
        return responsables.contains(persona) && persona.obtenerRoles(this).contains(rol);
    }

    public boolean requiereInscripcion() {
        return false;
    }

    public boolean validarCupo() {
        return true;
    }

    public boolean inscribirParticipante(Participante p) {
        if (estado != EstadoEvento.CONFIRMADO && estado != EstadoEvento.EN_EJECUCION) {
            throw new IllegalStateException("El evento no acepta inscripciones en su estado actual.");
        }
        if (!requiereInscripcion()) {
            throw new IllegalStateException("El evento no requiere inscripción previa.");
        }
        if (!validarCupo()) {
            throw new IllegalStateException("Cupo máximo alcanzado.");
        }
        if (!participantes.contains(p)) {
            return participantes.add(p);
        }
        return false;
    }

    public boolean eliminarParticipante(Participante p) {
        return participantes.remove(p);
    }

    public void cambiarEstado(EstadoEvento nuevoEstado) {
        this.estado = nuevoEstado;
    }

    // Getters y Setters
    public String getNombre() { return nombre; }
    public LocalDate getFechaInicio() { return fechaInicio; }
    public int getDuracionEstimadasDias() { return duracionEstimadasDias; }
    public EstadoEvento getEstado() { return estado; }

    public Set<Persona> getResponsables() {
        return Collections.unmodifiableSet(responsables);
    }

    public Set<Participante> getParticipantes() {
        return Collections.unmodifiableSet(participantes);
    }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
    public void setDuracionEstimadasDias(int dias) { this.duracionEstimadasDias = dias; }

    @Override
    public String toString() {
        return nombre + " (" + fechaInicio + ", " + estado + ")";
    }
}
