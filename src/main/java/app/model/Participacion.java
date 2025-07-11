package app.model;

import app.model.enums.RolEnEvento;
import jakarta.persistence.*;

@Entity
public class Participacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Persona persona;

    @ManyToOne
    private Evento evento;

    @Enumerated(EnumType.STRING)
    private RolEnEvento rol;

    public Participacion() {}

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
