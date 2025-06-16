package app.controller;

import app.model.Persona;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;

public class NuevaPersonaDialogController {
    @FXML private TextField txtNombre;
    @FXML private TextField txtDni;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtCorreo;

    /**
     * Inicializa el controlador. Se llama automáticamente después de cargar el FXML.
     * En este caso, no hay elementos ComboBox para inicializar, pero el método
     * se mantiene por consistencia y para futuras adiciones si fueran necesarias.
     */
    @FXML
    public void initialize() {
        // No hay inicializaciones específicas para ComboBoxes en este diálogo,
        // pero se puede añadir lógica aquí si fuera necesario en el futuro.
    }

    /**
     * Intentar construir y devolver un objeto Persona basado en los datos introducidos en el diálogo.
     * Realizar validaciones básicas y muestra alertas de error al usuario si faltan datos.
     *
     * @return Un objeto Persona si la entrada es válida, o null si hay errores.
     */
    public Persona getPersona() {
        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String telefono = txtTelefono.getText().trim();
        String correo = txtCorreo.getText().trim();

        // Validaciones de campos obligatorios
        if (nombre.isEmpty()) {
            showErrorAlert("Faltan datos", "El nombre de la persona no puede estar vacío.");
            return null;
        }
        if (dni.isEmpty()) {
            showErrorAlert("Faltan datos", "El DNI no puede estar vacío.");
            return null;
        }
        if (telefono.isEmpty()) {
            showErrorAlert("Faltan datos", "El teléfono no puede estar vacío.");
            return null;
        }
        if (correo.isEmpty()) {
            showErrorAlert("Faltan datos", "El correo electrónico no puede estar vacío.");
            return null;
        }

        // Añadir validaciones más específicas, por ejemplo, para el formato del DNI, teléfono o correo
        // if (!isValidEmail(correo)) {
        //     showErrorAlert("Formato inválido", "El correo electrónico no tiene un formato válido.");
        //     return null;
        // }

        return new Persona(nombre, dni, telefono, correo);
    }

    /**
     * Muestra una alerta de error al usuario.
     *
     * @param title   El título de la alerta.
     * @param message El mensaje de error.
     */
    private void showErrorAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null); // Sin encabezado adicional
        alert.setContentText(message);
        alert.showAndWait();
    }
}
