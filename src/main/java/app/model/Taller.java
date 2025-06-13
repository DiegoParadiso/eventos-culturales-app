package app.model;

import app.model.enums.Modalidad;
import java.time.LocalDate;

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
    public boolean inscribirParticipante(Participante p) {
        if (getParticipantes().size() >= cupoMaximo) {
            throw new IllegalStateException("Cupo máximo alcanzado para este taller.");
        }
        return super.inscribirParticipante(p);
    }

    public int getCupoMaximo() { return cupoMaximo; }
    public Persona getInstructor() { return instructor; }
    public Modalidad getModalidad() { return modalidad; }

    @Override
    public String toString() {
        return super.toString() + " - Taller: Cupo " + cupoMaximo + ", Instructor: " + instructor.getNombreCompleto();
    }
}
