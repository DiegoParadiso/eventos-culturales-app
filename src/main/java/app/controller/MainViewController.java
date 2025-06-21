package app.controller;

import java.io.IOException;
import java.time.LocalDate;

import app.model.CicloCine;
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
import javafx.beans.property.SimpleIntegerProperty;
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
import javafx.scene.control.TableCell;
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
    @FXML private TableColumn<Evento, Number> cupoCol;

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
        // Configurar columnas eventos
        nombreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        fechaCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFechaInicio()));
        duracionCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDuracionEstimadasDias()));
        tipoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        estadoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado().toString()));
        tablaEventos.setItems(eventos);

        // Columna cupo con lógica específica para Taller, Feria o sin límite
        cupoCol.setCellValueFactory(data -> {
            Evento evento = data.getValue();
            if (evento instanceof Taller taller) {
                return taller.cupoDisponibleProperty();
            } else if (evento instanceof Feria feria) {
                int cupo = feria.getCantidadStands() - feria.getParticipantes().size();
                return new SimpleIntegerProperty(cupo);
            } else {
                return new SimpleIntegerProperty(-1); // Sin límite
            }
        });
        cupoCol.setCellFactory(col -> new TableCell<>() {
            @Override
            protected void updateItem(Number item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setText(null);
                } else if (item.intValue() == -1) {
                    setText("Sin límite");
                } else {
                    setText(item.intValue() + " lugares");
                }
            }
        });

        // Configurar columnas personas
        nombrePersonaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        dniCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDni()));
        telefonoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));
        correoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));
        tablaPersonas.setItems(personas);

        // Configurar columnas participantes
        nombreParticipanteCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        contactoParticipanteCol.setCellValueFactory(data -> new SimpleStringProperty(
                data.getValue().getCorreo() + " / " + data.getValue().getTelefono()));
                eventoAsociadoCol.setCellValueFactory(data -> new SimpleStringProperty(buscarEventosDeParticipante(data.getValue())));
        tablaParticipantes.setItems(participantes);

        // Configurar columnas tabla eventos por día
        eventoDiaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        horarioDiaCol.setCellValueFactory(data -> new SimpleStringProperty(""));  // Puedes agregar lógica si tienes horarios
        lugarDiaCol.setCellValueFactory(data -> new SimpleStringProperty(""));    // Puedes agregar lógica si tienes lugar

        // Listener para filtrar eventos por fecha seleccionada
        selectorFecha.valueProperty().addListener((obs, old, nuevaFecha) -> filtrarEventosPorFecha(nuevaFecha));

        // Cargar datos de prueba
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
        // Crear personas ejemplo
        Persona p1 = new Persona("Claudio Biale", "01234567", "3758-123-456", "cb@email.com");
        Persona p2 = new Persona("Melissa Kolb", "76543210", "3758-123-456", "mk@email.com");
        Persona p3 = new Persona("John Doe", "11223344", "3758-123-456", "jd@email.com");
        personas.addAll(p1, p2, p3);

        // Participantes ejemplo
        Participante part1 = new Participante("Carlos Ruiz", "01010101", "3758-123-456", "cr@email.com");
        Participante part2 = new Participante("Lucía Torres", "10101010", "3758-123-456", "lt@email.com");
        participantes.addAll(part1, part2);

        // Crear eventos ejemplo
        Evento feria = new Feria("Feria del Libro", LocalDate.now().plusDays(2), 3, 20, true);
        Evento taller = new Taller("Taller de Pintura", LocalDate.now().plusDays(5), 2, 15, p1, Modalidad.PRESENCIAL);
        Evento concierto = new Concierto("Concierto Rock", LocalDate.now().plusDays(10), 1, TipoEntrada.PAGA);
        Evento expo = new Exposicion("Expo Arte", LocalDate.now().plusDays(7), 4, "Pintura", p2);
        Evento cicloCine = new CicloCine("Ciclo de Cine Clásico", LocalDate.now().plusDays(15), 5, true);

        // Asignar responsables y roles
        feria.agregarResponsable(p1, "Organizador");
        taller.agregarResponsable(p2, "Instructor");
        concierto.agregarResponsable(p3, "Productor");
        expo.agregarResponsable(p2, "Curador");
        cicloCine.agregarResponsable(p1, "Coordinador");

        // Cambiar estados para permitir inscripciones
        feria.cambiarEstado(EstadoEvento.CONFIRMADO);
        taller.cambiarEstado(EstadoEvento.CONFIRMADO);
        cicloCine.cambiarEstado(EstadoEvento.CONFIRMADO);

        // Inscribir participantes si se requiere
        if (feria.requiereInscripcion()) {
            try { feria.inscribirParticipante(part1); } catch (Exception ignored) {}
        }
        if (taller.requiereInscripcion()) {
            try { taller.inscribirParticipante(part2); } catch (Exception ignored) {}
        }
        if (cicloCine.requiereInscripcion()) {
        }

        eventos.addAll(feria, taller, concierto, expo, cicloCine);
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

    private String buscarEventosDeParticipante(Participante p) {
        StringBuilder eventosAsociados = new StringBuilder();
        for (Evento e : eventos) {
            for (Participante registrado : e.getParticipantes()) {
                if (registrado.getDni().equals(p.getDni())) {
                    if (!eventosAsociados.isEmpty()) {
                        eventosAsociados.append(", ");
                    }
                    eventosAsociados.append(e.getNombre());
                }
            }
        }
        return eventosAsociados.toString();
    }

    // --- Acciones botones y menús ---

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
            dialog.setResizable(true);
            dialog.initOwner(btnNuevoEvento.getScene().getWindow());

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
            dialog.setResizable(true);
            dialog.initOwner(btnNuevaPersona.getScene().getWindow());

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

    private void actualizarListaParticipantesDesdeEventos() {
        participantes.clear();
        for (Evento e : eventos) {
            for (Participante p : e.getParticipantes()) {
                if (!participantes.contains(p)) {
                    participantes.add(p);
                }
            }
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
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/ParticipacionView.fxml"));
            DialogPane dialogPane = loader.load();

            ParticipacionController controller = loader.getController();
            controller.cargarDatosIniciales(personas, eventos);
            controller.setParticipantesGlobales(participantes);

            controller.setOnParticipacionAgregada(() -> {
                actualizarListaParticipantesDesdeEventos();
                tablaParticipantes.refresh();
                tablaEventos.refresh();
            });
controller.setPersonas(personas);

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Registrar Participante");
            dialog.initOwner(btnRegistrarParticipante.getScene().getWindow());

            dialog.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
                    // Aquí también actualizar la lista y refrescar
                    actualizarListaParticipantesDesdeEventos();
                    tablaParticipantes.setItems(participantes);
                    tablaParticipantes.refresh();
                    tablaEventos.refresh();
                }
            });

        } catch (IOException e) {
            e.printStackTrace();
            mostrarAlerta("Error al abrir el diálogo de registro de participación: " + e.getMessage());
        }
    }

@FXML
public void eliminarParticipante() {
    Participante seleccionado = tablaParticipantes.getSelectionModel().getSelectedItem();
    if (seleccionado != null) {
        // Remover participante de los eventos a los que está inscripto
        for (Evento e : eventos) {
            e.getParticipantes().remove(seleccionado);
        }
        actualizarListaParticipantesDesdeEventos();
        tablaParticipantes.refresh();
        tablaEventos.refresh();
    }
}
}
