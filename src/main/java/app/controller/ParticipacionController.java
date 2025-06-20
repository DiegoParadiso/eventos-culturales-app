package app.controller;

import app.model.Evento;
import app.model.Participacion;
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

    @FXML
    public void initialize() {
        // Cargar combos con datos (deberías conectar con tus servicios o listas globales)
        comboRol.setItems(FXCollections.observableArrayList(RolEnEvento.values()));

        // Configurar columnas
        colPersona.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getPersona().getNombreCompleto()));
        colRol.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getRol().name()));
        colEvento.setCellValueFactory(data -> new javafx.beans.property.SimpleStringProperty(
            data.getValue().getEvento().getNombre()));

        tablaParticipaciones.setItems(participaciones);
    }

    @FXML
    private void agregarParticipacion() {
        Persona persona = comboPersona.getValue();
        RolEnEvento rol = comboRol.getValue();
        Evento evento = comboEvento.getValue();

        if (persona == null || rol == null || evento == null) {
            mostrarAlerta("Campos incompletos", "Debe seleccionar una persona, un evento y un rol.");
            return;
        }

        Participacion nueva = new Participacion(persona, evento, rol);
        participaciones.add(nueva);
        limpiarFormulario();
    }

    @FXML
    private void eliminarParticipacion() {
        Participacion seleccionada = tablaParticipaciones.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            participaciones.remove(seleccionada);
        }
    }

    private void limpiarFormulario() {
        comboPersona.setValue(null);
        comboRol.setValue(null);
        comboEvento.setValue(null);
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

    public ObservableList<Participacion> getParticipaciones() {
        return participaciones;
    }
}
