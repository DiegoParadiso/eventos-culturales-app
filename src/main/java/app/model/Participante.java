package app.model;

public class Participante extends Persona {

    public Participante(String nombreCompleto, String dni, String telefono, String correo) {
        super(nombreCompleto, dni, telefono, correo);
    }

    public boolean confirmarInscripcion(Evento evento) {
        // Lógica para confirmar inscripción, a definir
        return true;
    }

    @Override
    public String toString() {
        return super.toString(); // Ya incluye nombre, dni, teléfono y correo
    }
}