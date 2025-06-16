package app.model;

import java.time.LocalDate;

import app.model.enums.Modalidad;

public class Taller extends Evento {
    private int cupoMaximo;
    private Persona instructor;
    private Modalidad modalidad;

    public Taller(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, int cupoMaximo, Persona instructor, Modalidad modalidad) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.cupoMaximo = cupoMaximo;
        this.instructor = instructor;
        this.modalidad = modalidad;
    }

    @Override
    public boolean requiereInscripcion() {
        return true; // Taller requiere inscripción previa
    }

    @Override
    public boolean validarCupo() {
        return getParticipantes().size() < cupoMaximo;
    }

    // No sobreescribimos inscribirParticipante, usa la lógica de Evento

    public int getCupoMaximo() { return cupoMaximo; }
    public Persona getInstructor() { return instructor; }
    public Modalidad getModalidad() { return modalidad; }

    @Override
    public String toString() {
        return super.toString() + " - Taller: Cupo " + cupoMaximo + ", Instructor: " + instructor.getNombreCompleto();
    }
}
