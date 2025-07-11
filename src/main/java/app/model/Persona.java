package app.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
public class Persona {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombreCompleto;
    private String dni;
    private String telefono;
    private String correo;

    @Transient
    private final Map<Evento, Set<String>> rolesPorEvento = new HashMap<>();

    public Persona() {}

    public Persona(String nombreCompleto, String dni, String telefono, String correo) {
        this.nombreCompleto = nombreCompleto;
        this.dni = dni;
        this.telefono = telefono;
        this.correo = correo;
    }

    public void asignarRol(Evento evento, String rol) {
        rolesPorEvento.computeIfAbsent(evento, e -> new HashSet<>()).add(rol);
    }

    public Set<String> obtenerRoles(Evento evento) {
        return rolesPorEvento.getOrDefault(evento, Collections.emptySet());
    }

    // Getters y setters

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public String getDni() {
        return dni;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;
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
