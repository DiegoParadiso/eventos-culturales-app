package app.model;

import java.util.*;

public class Persona {
    private final String nombreCompleto;
    private final String dni;
    private final String telefono;
    private final String correo;

    private final Map<Evento, Set<String>> rolesPorEvento = new HashMap<>();

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
