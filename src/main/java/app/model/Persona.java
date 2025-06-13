package app.model;

import java.util.Objects;

public class Persona {
    private final String nombreCompleto;
    private final String dni;
    private final String telefono;
    private final String correo;

    public Persona(String nombreCompleto, String dni, String telefono, String correo) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.correo = correo;
    }

    public String getNombreCompleto() { return nombreCompleto; }
    public String getDni() { return dni; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Persona persona = (Persona) o;
        return dni.equals(persona.dni);
    }

    @Override
    public int hashCode() {
        return Objects.hash(dni);
    }

    @Override
    public String toString() {
        return nombreCompleto + " (DNI: " + dni + ")";
    }
}
