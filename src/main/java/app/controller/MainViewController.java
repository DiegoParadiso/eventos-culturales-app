package app.controller;

import java.time.LocalDate;

import app.model.CicloCine; // Asegúrate de importar CicloCine
import app.model.Concierto;
import app.model.Evento;
import app.model.Exposicion;
import app.model.Feria;
import app.model.Participante;
import app.model.Persona;
import app.model.Taller;
import app.model.enums.EstadoEvento;
import app.model.enums.Modalidad;
import app.model.enums.TipoEntrada;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

public class MainViewController {

    // --- Eventos ---
    @FXML private TableView<Evento> tablaEventos;
    @FXML private TableColumn<Evento, String> nombreCol;
    @FXML private TableColumn<Evento, LocalDate> fechaCol;
    @FXML private TableColumn<Evento, Integer> duracionCol;
    @FXML private TableColumn<Evento, String> tipoCol;
    @FXML private TableColumn<Evento, String> estadoCol;

    // --- Personas ---
    @FXML private TableView<Persona> tablaPersonas;
    @FXML private TableColumn<Persona, String> nombrePersonaCol;
    @FXML private TableColumn<Persona, String> dniCol;
    @FXML private TableColumn<Persona, String> telefonoCol;
    @FXML private TableColumn<Persona, String> correoCol;

    // --- Participantes ---
    @FXML private TableView<Participante> tablaParticipantes;
    @FXML private TableColumn<Participante, String> nombreParticipanteCol;
    @FXML private TableColumn<Participante, String> contactoParticipanteCol;
    @FXML private TableColumn<Participante, String> eventoAsociadoCol;

    // --- Calendario ---
    @FXML private DatePicker selectorFecha;
    @FXML private TableView<Evento> tablaEventosDia;
    @FXML private TableColumn<Evento, String> eventoDiaCol;
    @FXML private TableColumn<Evento, String> horarioDiaCol;
    @FXML private TableColumn<Evento, String> lugarDiaCol;

    // --- Botones ---
    @FXML private Button btnNuevoEvento;
    @FXML private Button btnVerDetallesEvento;
    @FXML private Button btnEliminarEvento;
    @FXML private Button btnNuevaPersona;
    @FXML private Button btnEliminarPersona;
    @FXML private Button btnRegistrarParticipante;
    @FXML private Button btnEliminarParticipante;

    // --- Datos ---
    private final ObservableList<Evento> eventos = FXCollections.observableArrayList();
    private final ObservableList<Persona> personas = FXCollections.observableArrayList();
    private final ObservableList<Participante> participantes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // --- Configurar columnas ---
        nombreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        fechaCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFechaInicio()));
        duracionCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDuracionEstimadasDias()));
        tipoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        estadoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado().toString()));
        tablaEventos.setItems(eventos);

        nombrePersonaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        dniCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDni()));
        telefonoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));
        correoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));
        tablaPersonas.setItems(personas);

        nombreParticipanteCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        contactoParticipanteCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCorreo() + " / " + data.getValue().getTelefono()));
        // Asegúrate de que la clase Participante tenga el método toString() adecuado para mostrar el nombre
        // o ajusta la factoría para mostrar el nombre del evento asociado correctamente.
        eventoAsociadoCol.setCellValueFactory(data -> new SimpleStringProperty(buscarEventoDeParticipante(data.getValue())));
        tablaParticipantes.setItems(participantes);

        eventoDiaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        horarioDiaCol.setCellValueFactory(data -> new SimpleStringProperty("")); // Puedes completar si tienes horario
        lugarDiaCol.setCellValueFactory(data -> new SimpleStringProperty(""));   // Puedes completar si tienes lugar

        selectorFecha.valueProperty().addListener((obs, old, nuevaFecha) -> filtrarEventosPorFecha(nuevaFecha));

        // --- Cargar datos de ejemplo ---
        cargarDatosEjemplo();
    }
    
    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Campos incompletos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarDatosEjemplo() {
        // Personas
        Persona p1 = new Persona("Ana López", "12345678", "111-222-333", "ana@email.com");
        Persona p2 = new Persona("Juan Pérez", "87654321", "222-333-444", "juan@email.com");
        Persona p3 = new Persona("María Gómez", "11223344", "333-444-555", "maria@email.com");
        personas.addAll(p1, p2, p3);

        // Participantes (pueden ser personas, pero para la tabla usamos Participante)
        Participante part1 = new Participante("Carlos Ruiz", "99887766", "444-555-666", "carlos@email.com");
        Participante part2 = new Participante("Lucía Torres", "88776655", "555-666-777", "lucia@email.com");
        participantes.addAll(part1, part2);

        // Eventos
        Evento feria = new Feria("Feria del Libro", LocalDate.now().plusDays(2), 3, 20, true);
        Evento taller = new Taller("Taller de Pintura", LocalDate.now().plusDays(5), 2, 15, p1, Modalidad.PRESENCIAL);
        Evento concierto = new Concierto("Concierto Rock", LocalDate.now().plusDays(10), 1, TipoEntrada.PAGA);
        Evento expo = new Exposicion("Expo Arte", LocalDate.now().plusDays(7), 4, "Pintura", p2);
        // Añadimos un CicloCine de ejemplo
        Evento cicloCine = new CicloCine("Ciclo de Cine Clásico", LocalDate.now().plusDays(15), 5, true);

        // Asignar responsables y roles
        feria.agregarResponsable(p1, "Organizador");
        taller.agregarResponsable(p2, "Instructor");
        concierto.agregarResponsable(p3, "Productor");
        expo.agregarResponsable(p2, "Curador");
        cicloCine.agregarResponsable(p1, "Coordinador"); // Añadido responsable para cicloCine

        // Cambiar estado para permitir inscripciones
        feria.cambiarEstado(EstadoEvento.CONFIRMADO);
        taller.cambiarEstado(EstadoEvento.CONFIRMADO);
        cicloCine.cambiarEstado(EstadoEvento.CONFIRMADO); // Añadido estado para cicloCine

        // Inscribir participantes en eventos que lo permiten
        if (feria.requiereInscripcion()) {
            try { feria.inscribirParticipante(part1); } catch (Exception ignored) {}
        }
        if (taller.requiereInscripcion()) {
            try { taller.inscribirParticipante(part2); } catch (Exception ignored) {}
        }
        if (cicloCine.requiereInscripcion()) { // Inscribir en cicloCine si requiere
            try { cicloCine.inscribirParticipante(new Participante("Nuevo Cinefilo", "10101010", "123-456-789", "cinefilo@example.com")); } catch (Exception ignored) {}
        }

        eventos.addAll(feria, taller, concierto, expo, cicloCine); // Añadir cicloCine a la lista
    }

    private void filtrarEventosPorFecha(LocalDate fecha) {
        if (fecha == null) {
            tablaEventosDia.setItems(FXCollections.observableArrayList());
            return;
        }
        ObservableList<Evento> filtrados = FXCollections.observableArrayList();
        for (Evento e : eventos) {
            if (e.getFechaInicio().equals(fecha)) {
                filtrados.add(e);
            }
        }
        tablaEventosDia.setItems(filtrados);
    }

    private String buscarEventoDeParticipante(Participante p) {
        for (Evento e : eventos) {
            if (e.getParticipantes().contains(p)) {
                return e.getNombre();
            }
        }
        return "";
    }

    // --- Acciones de menú y botones ---

    @FXML
    public void salir() {
        System.exit(0);
    }

    @FXML
    public void nuevoEvento() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NuevoEventoDialog.fxml"));
            DialogPane dialogPane = loader.load();
            NuevoEventoDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nuevo Evento");
            dialog.setDialogPane(dialogPane);
            dialog.setResizable(true); // ¡ESTA ES LA LÍNEA QUE NECESITAS AÑADIR/VERIFICAR!

            // Opcional: fijar el owner del diálogo para que el diálogo se centre en la ventana principal
            dialog.initOwner(btnNuevoEvento.getScene().getWindow());

            // Mostrar el diálogo y esperar respuesta
            dialog.showAndWait().ifPresent(response -> {
                if (response.getButtonData() == ButtonBar.ButtonData.OK_DONE) {
                    Evento nuevoEvento = controller.getEvento();
                    if (nuevoEvento != null) {
                        eventos.add(nuevoEvento);
                    } else {
                        mostrarAlerta("Por favor complete todos los campos correctamente.");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el diálogo de nuevo evento: " + e.getMessage());
        }
    }

    @FXML
    public void verDetallesEvento() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Detalles del Evento");
            alert.setHeaderText(seleccionado.getNombre());
            alert.setContentText(seleccionado.toString());
            alert.showAndWait();
        }
    }

    @FXML
    public void eliminarEvento() {
        Evento seleccionado = tablaEventos.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            eventos.remove(seleccionado);
        }
    }

    @FXML
    public void nuevaPersona() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/NuevaPersonaDialog.fxml"));
            DialogPane dialogPane = loader.load();
            NuevaPersonaDialogController controller = loader.getController();

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setTitle("Nueva Persona");
            dialog.setDialogPane(dialogPane);
            dialog.setResizable(true); // También aquí, es una buena práctica

            dialog.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    Persona nueva = controller.getPersona();
                    if (nueva != null) {
                        personas.add(nueva);
                    } else {
                        mostrarAlerta("Todos los campos son obligatorios.");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el diálogo de nueva persona: " + e.getMessage());
        }
    }

    @FXML
    public void eliminarPersona() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            personas.remove(seleccionada);
        }
    }

    @FXML
    public void registrarParticipante() {
        // Ejemplo: agrega un participante rápido, puedes cambiar por un formulario
        Participante participante = new Participante("Nuevo Participante", "11112222", "999-888-777", "participante@email.com");
        participantes.add(participante);
    }

    @FXML
    public void eliminarParticipante() {
        Participante seleccionado = tablaParticipantes.getSelectionModel().getSelectedItem();
        if (seleccionado != null) {
            participantes.remove(seleccionado);
        }
    }
    
}
