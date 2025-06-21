package app.model;

import java.time.LocalDate;

import app.model.enums.Modalidad;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.ListChangeListener;

public class Taller extends Evento {
    private int cupoMaximo;
    private Persona instructor;
    private Modalidad modalidad;

    private final IntegerProperty cupoDisponible = new SimpleIntegerProperty();

    public Taller(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, int cupoMaximo, Persona instructor, Modalidad modalidad) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.cupoMaximo = cupoMaximo;
        this.instructor = instructor;
        this.modalidad = modalidad;

        // Inicializo el cupo disponible
        actualizarCupo();

        // Agrego listener para actualizar cupo cuando cambian los participantes
        getParticipantes().addListener((ListChangeListener<Participante>) change -> {
            actualizarCupo();
        });
    }

    private void actualizarCupo() {
        cupoDisponible.set(cupoMaximo - getParticipantes().size());
    }

    public IntegerProperty cupoDisponibleProperty() {
        return cupoDisponible;
    }

    @Override
    public boolean requiereInscripcion() {
        return true; // Taller requiere inscripción previa
    }

    @Override
    public boolean validarCupo() {
        return getParticipantes().size() < cupoMaximo;
    }

    // No sobrescribimos inscribirParticipante, usa la lógica de Evento

    public int getCupoMaximo() { return cupoMaximo; }
    public Persona getInstructor() { return instructor; }
    public Modalidad getModalidad() { return modalidad; }

    @Override
    public String toString() {
        return super.toString() + " - Taller: Cupo " + cupoMaximo + ", Instructor: " + instructor.getNombreCompleto();
    }
}
