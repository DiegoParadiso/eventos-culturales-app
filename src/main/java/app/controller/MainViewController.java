package app.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class MainViewController {

    @FXML
    private Button btnManageEvents;

    @FXML
    private Button btnManagePersons;

    @FXML
    private Button btnViewCalendar;

    @FXML
    private Button btnRegisterParticipants;

    @FXML
    private void initialize() {
        // Agregar acciones a botones
        btnManageEvents.setOnAction(e -> {
            System.out.println("Abrir gestión de eventos");
            // Aquí cargarás la vista para eventos
        });

        btnManagePersons.setOnAction(e -> {
            System.out.println("Abrir gestión de personas");
            // Aquí cargarás la vista para personas
        });

        btnViewCalendar.setOnAction(e -> {
            System.out.println("Abrir calendario");
            // Aquí cargarás la vista de calendario
        });

        btnRegisterParticipants.setOnAction(e -> {
            System.out.println("Registrar participantes");
            // Aquí cargarás la vista para registrar participantes
        });
    }
}
