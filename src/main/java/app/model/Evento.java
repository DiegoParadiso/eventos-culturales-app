package app.model;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import app.model.enums.EstadoEvento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public abstract class Evento {
    private String nombre;
    private LocalDate fechaInicio;
    private int duracionEstimadasDias;
    private Set<Persona> responsables;
    private EstadoEvento estado;
    private ObservableList<Participante> participantes;
    

    public Evento(String nombre, LocalDate fechaInicio, int duracionEstimadasDias) {
        this.nombre = nombre;
        this.fechaInicio = fechaInicio;
        this.duracionEstimadasDias = duracionEstimadasDias;
        this.responsables = new HashSet<>();
        this.participantes = FXCollections.observableArrayList();
        this.estado = EstadoEvento.PLANIFICACION;
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

    /**
     * Indica si el evento requiere inscripción previa.
     * Por defecto NO requiere inscripción.
     * Subclases que sí requieran inscripción deben sobrescribirlo.
     */
    public boolean requiereInscripcion() {
        return false; // Por defecto, no requiere inscripción
    }

    /**
     * Valida si el evento tiene cupo disponible para inscribir a otro participante.
     * Por defecto no hay cupo limitado.
     * Subclases con cupo máximo deben sobrescribirlo.
     */
    public boolean validarCupo() {
        return true; // Por defecto no hay límite de cupo
    }

    /**
     * Inscribe un participante si se cumplen las condiciones de estado,
     * inscripción previa y cupo.
     * @throws IllegalStateException si no se puede inscribir.
     */
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

    /**
     * Elimina un participante del evento.
     * @param p Participante a eliminar.
     * @return true si fue eliminado, false si no estaba en la lista.
     */
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
    public Set<Persona> getResponsables() { return Collections.unmodifiableSet(responsables); }
    public ObservableList<Participante> getParticipantes() {
        return participantes;
    }
public void setNombre(String nombre) { this.nombre = nombre; }
public void setFechaInicio(LocalDate fechaInicio) { this.fechaInicio = fechaInicio; }
public void setDuracionEstimadasDias(int dias) { this.duracionEstimadasDias = dias; }

    @Override
    public String toString() {
        return nombre + " (" + fechaInicio + ", " + estado + ")";
    }
}
