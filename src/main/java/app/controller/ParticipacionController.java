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

public class ParticipacionController {

    @FXML
    private ComboBox<Persona> comboPersona;

    @FXML
    private ComboBox<RolEnEvento> comboRol;

    @FXML
    private ComboBox<Artista> comboArtista;
    
    @FXML
    private ComboBox<Evento> comboEvento;

    @FXML
    private TableView<Participacion> tablaParticipaciones;

    @FXML
    private TableColumn<Participacion, String> colPersona;

    @FXML
    private TableColumn<Participacion, String> colRol;

    @FXML
    private TableColumn<Participacion, String> colEvento;

    private ObservableList<Participacion> participaciones = FXCollections.observableArrayList();
    private ObservableList<Participante> participantesGlobales;

    public void setParticipantesGlobales(ObservableList<Participante> lista) {
        this.participantesGlobales = lista;
    }
    
    // Callback para notificar cuando se agrega una participación (para refrescar UI)
    private Runnable onParticipacionAgregada;

    @FXML
    public void initialize() {
        // Cargar combos con datos
        comboRol.setItems(FXCollections.observableArrayList(RolEnEvento.values()));
        
        comboEvento.valueProperty().addListener((obs, oldEvento, newEvento) -> {
            if (newEvento == null) {
                mostrarControlesPersona(false);
                mostrarControlesArtista(false);
            } else if (newEvento instanceof Concierto) {
                mostrarControlesPersona(false);
                mostrarControlesArtista(true);
                cargarArtistasEnCombo(); // cargar artistas en comboArtista
            } else {
                mostrarControlesPersona(true);
                mostrarControlesArtista(false);
            }
        });
        
        // Configurar columnas
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
        comboPersona.setVisible(mostrar);
        comboRol.setVisible(mostrar);
        // Si tienes labels para estos combos, ocultalos/mostralos aquí también
    }

    private void mostrarControlesArtista(boolean mostrar) {
        comboArtista.setVisible(mostrar);
        // Si tienes labels para comboArtista, ocultalos/mostralos aquí también
    }

    private void cargarArtistasEnCombo() {
        // TODO: Carga aquí la lista global de artistas si la tienes
        // Ejemplo: comboArtista.setItems(listaGlobalArtistas);
    }

    @FXML
    private void agregarParticipacion() {
        Evento evento = comboEvento.getValue();
        if (evento == null) {
            mostrarAlerta("Error", "Seleccione un evento.");
            return;
        }

        if (evento instanceof Concierto) {
            Artista artista = comboArtista.getValue();
            if (artista == null) {
                mostrarAlerta("Error", "Seleccione un artista.");
                return;
            }
            ((Concierto) evento).agregarArtista(artista);
            // Aquí podrías agregar lógica para actualizar la UI o persistir cambios
        } else {
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
        if (seleccionada != null) {
            participaciones.remove(seleccionada);
        }
    }

    public void setPersonas(ObservableList<Persona> personas) {
        comboPersona.setItems(personas);
    }

    private void limpiarFormulario() {
        comboPersona.setValue(null);
        comboRol.setValue(null);
        comboEvento.setValue(null);
        comboArtista.setValue(null);
    }

    private Participante obtenerOReutilizarParticipante(Persona persona) {
        for (Participante p : participantesGlobales) {
            if (p.getDni().equals(persona.getDni())) {
                return p; // Reutilizar participante existente
            }
        }

        Participante nuevo = new Participante(
            persona.getNombreCompleto(),
            persona.getDni(),
            persona.getTelefono(),
            persona.getCorreo()
        );

        participantesGlobales.add(nuevo); // Importante: agregar a la lista global
        return nuevo;
    }

    public void cargarDatosIniciales(ObservableList<Persona> personas, ObservableList<Evento> eventos) {
        comboPersona.setItems(personas);
        comboEvento.setItems(eventos);
        comboRol.setItems(FXCollections.observableArrayList(RolEnEvento.values()));
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alerta = new Alert(Alert.AlertType.WARNING);
        alerta.setTitle(titulo);
        alerta.setHeaderText(null);
        alerta.setContentText(mensaje);
        alerta.showAndWait();
    }

    // Sobrecarga para mostrar alerta con solo mensaje y título por defecto
    private void mostrarAlerta(String mensaje) {
        mostrarAlerta("Atención", mensaje);
    }

    public ObservableList<Participacion> getParticipaciones() {
        return participaciones;
    }

    // Setter para el callback
    public void setOnParticipacionAgregada(Runnable callback) {
        this.onParticipacionAgregada = callback;
    }
}
