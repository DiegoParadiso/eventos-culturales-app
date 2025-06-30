package app.controller;

import app.model.Artista;
import app.model.Concierto;
import app.model.Evento;
import app.model.Participacion;
import app.model.Participante;
import app.model.Persona;
import app.model.enums.RolEnEvento;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class ParticipacionController {

    @FXML private ComboBox<Persona> comboPersona;
    @FXML private ComboBox<RolEnEvento> comboRol;
    @FXML private TextField inputArtista; // Usamos TextField en lugar de ComboBox
    @FXML private ComboBox<Evento> comboEvento;

    @FXML private TableView<Participacion> tablaParticipaciones;
    @FXML private TableColumn<Participacion, String> colPersona;
    @FXML private TableColumn<Participacion, String> colRol;
    @FXML private TableColumn<Participacion, String> colEvento;

    @FXML private HBox hboxPersonaRol;
    @FXML private HBox hboxArtista;

    private ObservableList<Participacion> participaciones = FXCollections.observableArrayList();
    private ObservableList<Participante> participantesGlobales;

    private Runnable onParticipacionAgregada;

    public void setParticipantesGlobales(ObservableList<Participante> lista) {
        this.participantesGlobales = lista;
    }

    @FXML
    public void initialize() {
        comboRol.setItems(FXCollections.observableArrayList(RolEnEvento.values()));

        comboEvento.valueProperty().addListener((obs, oldEvento, newEvento) -> {
            if (newEvento == null) {
                mostrarControlesPersona(false);
                mostrarControlesArtista(false);
            } else if (newEvento instanceof Concierto) {
                mostrarControlesPersona(false);
                mostrarControlesArtista(true);
            } else {
                mostrarControlesPersona(true);
                mostrarControlesArtista(false);
            }
        });

        colPersona.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getPersona().getNombreCompleto()));

        colRol.setCellValueFactory(data -> {
            RolEnEvento rol = data.getValue().getRol();
            String texto = (rol == RolEnEvento.SIN_ROL) ? "Sin rol asignado" : rol.name();
            return new javafx.beans.property.SimpleStringProperty(texto);
        });

        colEvento.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
                data.getValue().getEvento().getNombre()));

        tablaParticipaciones.setItems(participaciones);
    }

    private void mostrarControlesPersona(boolean mostrar) {
        hboxPersonaRol.setVisible(mostrar);
        hboxPersonaRol.setManaged(mostrar);
    }

    private void mostrarControlesArtista(boolean mostrar) {
        hboxArtista.setVisible(mostrar);
        hboxArtista.setManaged(mostrar);
    }

    @FXML
    private void agregarParticipacion() {
        Evento evento = comboEvento.getValue();
        if (evento == null) {
            mostrarAlerta("Error", "Seleccione un evento.");
            return;
        }

        if (evento instanceof Concierto) {
            String nombreArtista = inputArtista.getText();
            if (nombreArtista == null || nombreArtista.trim().isEmpty()) {
                mostrarAlerta("Error", "Debe ingresar un nombre de artista o banda.");
                return;
            }

            // Crear artista y agregarlo al concierto si no existe
            Artista artista = new Artista(nombreArtista.trim());
            Concierto concierto = (Concierto) evento;

            if (concierto.getArtistas().contains(artista)) {
                mostrarAlerta("El artista ya está agregado al concierto.");
                return;
            }

            concierto.agregarArtista(artista);

            // Crear un Participante artificial con DNI "ARTISTA" y agregarlo a la lista global
            Participante artistaParticipante = new Participante(nombreArtista.trim(), "ARTISTA", "", "");
            if (participantesGlobales != null && !participantesGlobales.contains(artistaParticipante)) {
                participantesGlobales.add(artistaParticipante);
            }

            // Crear la Participacion y agregarla a la lista observable
            Participacion nueva = new Participacion(artistaParticipante, evento, RolEnEvento.SIN_ROL);
            participaciones.add(nueva);

            // Forzar refresco de la tabla para que actualice la vista
            tablaParticipaciones.refresh();

        } else {
            // Para eventos normales
            Persona persona = comboPersona.getValue();
            RolEnEvento rol = comboRol.getValue();

            if (persona == null || rol == null) {
                mostrarAlerta("Error", "Debe seleccionar una persona y un rol.");
                return;
            }

            Participante participante = obtenerOReutilizarParticipante(persona);

            try {
                evento.inscribirParticipante(participante);
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo inscribir al participante: " + e.getMessage());
                return;
            }

            Participacion nueva = new Participacion(participante, evento, rol);
            participaciones.add(nueva);
        }

        limpiarFormulario();
        if (onParticipacionAgregada != null) onParticipacionAgregada.run();
    }

    @FXML
    private void eliminarParticipacion() {
        Participacion seleccionada = tablaParticipaciones.getSelectionModel().getSelectedItem();
        if (seleccionada == null) return;

        Evento evento = seleccionada.getEvento();
        Persona participante = seleccionada.getPersona();

        if (evento instanceof Concierto && participante.getDni().equals("ARTISTA")) {
            Concierto concierto = (Concierto) evento;

            Artista artistaAEliminar = null;
            for (Artista a : concierto.getArtistas()) {
                if (a.getNombre().equals(participante.getNombreCompleto())) {
                    artistaAEliminar = a;
                    break;
                }
            }

            if (artistaAEliminar != null) {
                concierto.getArtistas().remove(artistaAEliminar);
            }
        } else {
            if (participante instanceof Participante) {
                evento.getParticipantes().remove((Participante) participante);
            }
        }

        participaciones.remove(seleccionada);
        tablaParticipaciones.refresh();
    }

    public void setPersonas(ObservableList<Persona> personas) {
        comboPersona.setItems(personas);
    }

    public void cargarDatosIniciales(ObservableList<Persona> personas, ObservableList<Evento> eventos) {
        comboPersona.setItems(personas);
        comboEvento.setItems(eventos);
        comboRol.setItems(FXCollections.observableArrayList(RolEnEvento.values()));
    }

    private void limpiarFormulario() {
        comboPersona.setValue(null);
        comboRol.setValue(null);
        comboEvento.setValue(null);
        inputArtista.clear();
    }

    private Participante obtenerOReutilizarParticipante(Persona persona) {
        if (participantesGlobales != null) {
            for (Participante p : participantesGlobales) {
                if (p.getDni().equals(persona.getDni())) {
                    return p;
                }
            }
        }

        Participante nuevo = new Participante(
                persona.getNombreCompleto(),
                persona.getDni(),
                persona.getTelefono(),
                persona.getCorreo()
        );

        if (participantesGlobales != null) {
            participantesGlobales.add(nuevo);
        }
        return nuevo;
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    private void mostrarAlerta(String mensaje) {
        mostrarAlerta("Atención", mensaje);
    }

    public ObservableList<Participacion> getParticipaciones() {
        return participaciones;
    }

    public void setOnParticipacionAgregada(Runnable callback) {
        this.onParticipacionAgregada = callback;
    }
}
