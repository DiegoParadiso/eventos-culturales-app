package app.model;

import app.model.enums.RolEnEvento;

public class Participacion {

    private Persona persona;
    private Evento evento;
    private RolEnEvento rol;

    public Participacion(Persona persona, Evento evento, RolEnEvento rol) {
        this.persona = persona;
        this.evento = evento;
        this.rol = rol;
    }

    public Persona getPersona() {
        return persona;
    }

    public Evento getEvento() {
        return evento;
    }

    public RolEnEvento getRol() {
        return rol;
    }

    public void setRol(RolEnEvento rol) {
        this.rol = rol;
    }

    @Override
    public String toString() {
        return persona.getNombreCompleto() + " como " + rol;
    }
}
