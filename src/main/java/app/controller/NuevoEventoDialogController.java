package app.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import app.model.CicloCine; // Importar Platform
import app.model.Concierto;
import app.model.Evento;
import app.model.Exposicion;
import app.model.Feria;
import app.model.Persona;
import app.model.Taller;
import app.model.enums.Modalidad;
import app.model.enums.TipoEntrada;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class NuevoEventoDialogController {

    @FXML private TextField txtNombre;
    @FXML private DatePicker dateFechaInicio;
    @FXML private TextField txtDuracion;
    @FXML private ComboBox<String> comboTipo;

    // Contenedor principal de los campos comunes para forzar un layout pass
    // Asegúrate de que el VBox más externo en tu FXML tenga fx:id="mainVBoxContent"
    @FXML private VBox mainVBoxContent;

    // Grupos de VBox para controlar la visibilidad
    @FXML private VBox grupoFeria;
    @FXML private VBox grupoConcierto;
    @FXML private VBox grupoTaller;
    @FXML private VBox grupoExposicion;
    @FXML private VBox grupoCicloCine;

    // Feria
    @FXML private TextField txtCantidadStands;
    @FXML private CheckBox chkAlAireLibre;

    // Concierto
    @FXML private ComboBox<TipoEntrada> comboTipoEntrada;

    // Taller
    @FXML private TextField txtCupo;
    @FXML private ComboBox<Persona> comboInstructor;
    @FXML private ComboBox<Modalidad> comboModalidad;

    // Exposición
    @FXML private TextField txtTipoArte;
    @FXML private ComboBox<Persona> comboCurador;

    // Ciclo de Cine
    @FXML private CheckBox chkCharlasPosteriores;

    // Lista de personas disponibles para instructores y curadores (ejemplo)
    private List<Persona> personasDisponibles;

    /**
     * Inicializa el controlador. Se llama automáticamente después de cargar el FXML.
     * Aquí se inicializan los ComboBox con sus opciones y se configuran los listeners.
     */
    @FXML
    public void initialize() {
        // Inicializar la lista de tipos de evento
        comboTipo.setItems(FXCollections.observableArrayList(
                "Feria", "Concierto", "Taller", "Exposición", "Ciclo de Cine"
        ));
        comboTipo.setPromptText("Seleccione un tipo");

        // Inicializar ComboBoxes de enums
        comboTipoEntrada.setItems(FXCollections.observableArrayList(TipoEntrada.values()));
        comboModalidad.setItems(FXCollections.observableArrayList(Modalidad.values()));

        // Inicializar con personas de ejemplo. En una aplicación real, esto vendría de una base de datos o servicio.
        personasDisponibles = Arrays.asList(
                new Persona("Juan Pérez", "12345678A", "1111-2222", "juan@example.com"),
                new Persona("María García", "87654321B", "3333-4444", "maria@example.com"),
                new Persona("Pedro López", "99887766C", "5555-6666", "pedro@example.com")
        );

        comboInstructor.setItems(FXCollections.observableArrayList(personasDisponibles));
        comboCurador.setItems(FXCollections.observableArrayList(personasDisponibles));

        // Añadir un listener al ComboBox de tipo de evento para controlar la visibilidad de los grupos
        comboTipo.valueProperty().addListener((observable, oldValue, newValue) ->
                updateEventSpecificFieldsVisibility(newValue));

        // Inicializar la visibilidad (todos ocultos al principio)
        updateEventSpecificFieldsVisibility(null); // Oculta todos los grupos al inicio
    }

    /**
     * Actualiza la visibilidad y gestiona los grupos de campos específicos del evento.
     * Solo el grupo correspondiente al tipo de evento seleccionado será visible y gestionado.
     *
     * @param eventType El tipo de evento seleccionado (String), o null para ocultar todos.
     */
    private void updateEventSpecificFieldsVisibility(String eventType) {
        // Ocultar y desgestionar todos los grupos primero
        grupoFeria.setVisible(false);
        grupoFeria.setManaged(false);
        grupoConcierto.setVisible(false);
        grupoConcierto.setManaged(false);
        grupoTaller.setVisible(false);
        grupoTaller.setManaged(false);
        grupoExposicion.setVisible(false);
        grupoExposicion.setManaged(false);
        grupoCicloCine.setVisible(false);
        grupoCicloCine.setManaged(false);

        // Mostrar y gestionar solo el grupo relevante
        if (eventType != null) {
            switch (eventType) {
                case "Feria" -> {
                    grupoFeria.setVisible(true);
                    grupoFeria.setManaged(true);
                }
                case "Concierto" -> {
                    grupoConcierto.setVisible(true);
                    grupoConcierto.setManaged(true);
                }
                case "Taller" -> {
                    grupoTaller.setVisible(true);
                    grupoTaller.setManaged(true);
                }
                case "Exposición" -> {
                    grupoExposicion.setVisible(true);
                    grupoExposicion.setManaged(true);
                }
                case "Ciclo de Cine" -> {
                    grupoCicloCine.setVisible(true);
                    grupoCicloCine.setManaged(true);
                }
            }
        }
        
        // Forzar la re-evaluación del tamaño de la ventana después de cambiar la visibilidad
        // Esto se ejecuta en el hilo de la aplicación JavaFX para asegurar que el UI ya haya actualizado
        Platform.runLater(() -> {
            if (mainVBoxContent != null && mainVBoxContent.getScene() != null && mainVBoxContent.getScene().getWindow() != null) {
                mainVBoxContent.getScene().getWindow().sizeToScene();
            }
        });
    }

    /**
     * Intenta construir y devolver un objeto Evento basado en los datos introducidos en el diálogo.
     * Realiza validaciones básicas y muestra alertas de error al usuario si faltan datos.
     *
     * @return Un objeto Evento si la entrada es válida, o null si hay errores.
     */
    public Evento getEvento() {
        String nombre = txtNombre.getText().trim();
        LocalDate fecha = dateFechaInicio.getValue();
        String tipo = comboTipo.getValue();
        int duracion;

        // Validaciones comunes
        if (nombre.isEmpty()) {
            showErrorAlert("Faltan datos", "El nombre del evento no puede estar vacío.");
            return null;
        }
        if (fecha == null) {
            showErrorAlert("Faltan datos", "Debe seleccionar una fecha de inicio.");
            return null;
        }
        if (tipo == null) {
            showErrorAlert("Faltan datos", "Debe seleccionar un tipo de evento.");
            return null;
        }
        if (txtDuracion.getText().isEmpty()) {
            showErrorAlert("Faltan datos", "La duración del evento no puede estar vacía.");
            return null;
        }

        try {
            duracion = Integer.parseInt(txtDuracion.getText());
            if (duracion <= 0) {
                showErrorAlert("Entrada inválida", "La duración debe ser un número positivo.");
                return null;
            }
        } catch (NumberFormatException e) {
            showErrorAlert("Entrada inválida", "La duración debe ser un número entero válido.");
            return null;
        }

        // Validaciones y creación de eventos específicos
        switch (tipo) {
            case "Feria" -> {
                if (txtCantidadStands.getText().isEmpty()) {
                    showErrorAlert("Faltan datos", "La cantidad de stands para la Feria no puede estar vacía.");
                    return null;
                }
                try {
                    int cantidadStands = Integer.parseInt(txtCantidadStands.getText());
                    if (cantidadStands <= 0) {
                        showErrorAlert("Entrada inválida", "La cantidad de stands debe ser un número positivo.");
                        return null;
                    }
                    boolean alAireLibre = chkAlAireLibre.isSelected();
                    return new Feria(nombre, fecha, duracion, cantidadStands, alAireLibre);
                } catch (NumberFormatException e) {
                    showErrorAlert("Entrada inválida", "La cantidad de stands debe ser un número entero válido.");
                    return null;
                }
            }
            case "Concierto" -> {
                TipoEntrada tipoEntrada = comboTipoEntrada.getValue();
                if (tipoEntrada == null) {
                    showErrorAlert("Faltan datos", "Debe seleccionar un tipo de entrada para el Concierto.");
                    return null;
                }
                return new Concierto(nombre, fecha, duracion, tipoEntrada);
            }
            case "Taller" -> {
                if (txtCupo.getText().isEmpty()) {
                    showErrorAlert("Faltan datos", "El cupo máximo para el Taller no puede estar vacío.");
                    return null;
                }
                try {
                    int cupo = Integer.parseInt(txtCupo.getText());
                    if (cupo <= 0) {
                        showErrorAlert("Entrada inválida", "El cupo máximo debe ser un número positivo.");
                        return null;
                    }
                    Persona instructor = comboInstructor.getValue();
                    Modalidad modalidad = comboModalidad.getValue();
                    if (instructor == null) {
                        showErrorAlert("Faltan datos", "Debe seleccionar un instructor para el Taller.");
                        return null;
                    }
                    if (modalidad == null) {
                        showErrorAlert("Faltan datos", "Debe seleccionar una modalidad para el Taller.");
                        return null;
                    }
                    return new Taller(nombre, fecha, duracion, cupo, instructor, modalidad);
                } catch (NumberFormatException e) {
                    showErrorAlert("Entrada inválida", "El cupo máximo debe ser un número entero válido.");
                    return null;
                }
            }
            case "Exposición" -> {
                String tipoArte = txtTipoArte.getText().trim();
                Persona curador = comboCurador.getValue();
                if (tipoArte.isEmpty()) {
                    showErrorAlert("Faltan datos", "El tipo de arte para la Exposición no puede estar vacío.");
                    return null;
                }
                if (curador == null) {
                    showErrorAlert("Faltan datos", "Debe seleccionar un curador para la Exposición.");
                    return null;
                }
                return new Exposicion(nombre, fecha, duracion, tipoArte, curador);
            }
            case "Ciclo de Cine" -> {
                boolean conCharlas = chkCharlasPosteriores.isSelected();
                return new CicloCine(nombre, fecha, duracion, conCharlas);
            }
            default -> {
                // Esto no debería ocurrir si el ComboBox está bien configurado
                showErrorAlert("Error interno", "Tipo de evento no reconocido.");
                return null;
            }
        }
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
        alert.setHeaderText(null); // No queremos un encabezado adicional
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Método para establecer las personas disponibles desde el exterior,
     * útil si se cargan dinámicamente.
     * @param personas La lista de objetos Persona.
     */
    public void setPersonasDisponibles(List<Persona> personas) {
        this.personasDisponibles = personas;
        comboInstructor.setItems(FXCollections.observableArrayList(personasDisponibles));
        comboCurador.setItems(FXCollections.observableArrayList(personasDisponibles));
    }
}
