package app.model;

import app.model.enums.Modalidad;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
public class Taller extends Evento {

    private int cupoMaximo;

    @ManyToOne
    private Persona instructor;

    @Enumerated(EnumType.STRING)
    private Modalidad modalidad;

    public Taller() {}

    public Taller(String nombre, LocalDate fechaInicio, int duracionEstimadasDias, int cupoMaximo, Persona instructor, Modalidad modalidad) {
        super(nombre, fechaInicio, duracionEstimadasDias);
        this.cupoMaximo = cupoMaximo;
        this.instructor = instructor;
        this.modalidad = modalidad;
    }

    @Override
    public boolean requiereInscripcion() {
        return true;
    }

    @Override
    public boolean validarCupo() {
        return getParticipantes().size() < cupoMaximo;
    }

    public int getCupoMaximo() {
        return cupoMaximo;
    }

    public Persona getInstructor() {
        return instructor;
    }

    public Modalidad getModalidad() {
        return modalidad;
    }

    @Override
    public String toString() {
        return super.toString() + " - Taller: Cupo " + cupoMaximo + ", Instructor: " + instructor.getNombreCompleto();
    }
}
