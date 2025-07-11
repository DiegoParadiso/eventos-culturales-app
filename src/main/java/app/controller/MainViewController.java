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
import javafx.scene.control.*;
import javafx.scene.control.cell.TextFieldTableCell;

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

    // --- Botones ---
    @FXML private Button btnNuevoEvento;
    @FXML private Button btnVerDetallesEvento;
    @FXML private Button btnEliminarEvento;
    @FXML private Button btnNuevaPersona;
    @FXML private Button btnEliminarPersona;
    @FXML private Button btnRegistrarParticipante;
    @FXML private Button btnEliminarParticipante;

    // --- Datos ---
    // Para mantener la simplicidad: usamos ObservableList solo en el controlador, para vincular con JavaFX,
    // pero internamente los modelos usan colecciones normales (Set, List).
    private final ObservableList<Evento> eventos = FXCollections.observableArrayList();
    private final ObservableList<Persona> personas = FXCollections.observableArrayList();
    private final ObservableList<Participante> participantes = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        // --- Configurar columnas de tablaEventos ---
        nombreCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));
        fechaCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getFechaInicio()));
        duracionCol.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().getDuracionEstimadasDias()));
        tipoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getClass().getSimpleName()));
        estadoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getEstado().toString()));

        // Cupo: si es Taller o Feria, calculamos cupo disponible con métodos normales
        cupoCol.setCellValueFactory(data -> {
            Evento evento = data.getValue();
            if (evento instanceof Taller taller) {
                return new SimpleIntegerProperty(taller.getCupoMaximo() - taller.getParticipantes().size());
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

        tablaEventos.setItems(eventos);

        // --- Personas ---
        tablaPersonas.setItems(personas);
        tablaPersonas.setEditable(true);

        nombrePersonaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        dniCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getDni()));
        telefonoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getTelefono()));
        correoCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getCorreo()));

        nombrePersonaCol.setCellFactory(TextFieldTableCell.forTableColumn());
        dniCol.setCellFactory(TextFieldTableCell.forTableColumn());
        telefonoCol.setCellFactory(TextFieldTableCell.forTableColumn());
        correoCol.setCellFactory(TextFieldTableCell.forTableColumn());

        nombrePersonaCol.setOnEditCommit(event -> {
            Persona p = event.getRowValue();
            p.setNombreCompleto(event.getNewValue());
        });
        dniCol.setOnEditCommit(event -> {
            Persona p = event.getRowValue();
            p.setDni(event.getNewValue());
        });
        telefonoCol.setOnEditCommit(event -> {
            Persona p = event.getRowValue();
            p.setTelefono(event.getNewValue());
        });
        correoCol.setOnEditCommit(event -> {
            Persona p = event.getRowValue();
            p.setCorreo(event.getNewValue());
        });

        // --- Participantes ---
        tablaParticipantes.setItems(participantes);
        nombreParticipanteCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombreCompleto()));
        contactoParticipanteCol.setCellValueFactory(data ->
                new SimpleStringProperty(data.getValue().getCorreo() + " / " + data.getValue().getTelefono()));
        eventoAsociadoCol.setCellValueFactory(data ->
                new SimpleStringProperty(buscarEventosDeParticipante(data.getValue())));

        // --- Calendario ---
        eventoDiaCol.setCellValueFactory(data -> new SimpleStringProperty(data.getValue().getNombre()));

        selectorFecha.valueProperty().addListener((obs, old, nuevaFecha) -> filtrarEventosPorFecha(nuevaFecha));

        // --- Cargar datos de ejemplo ---
        cargarDatosEjemplo();

        // --- Editable eventos ---
        tablaEventos.setEditable(true);

        nombreCol.setCellFactory(TextFieldTableCell.forTableColumn());
        nombreCol.setOnEditCommit(event -> {
            Evento ev = event.getRowValue();
            ev.setNombre(event.getNewValue());
        });

        fechaCol.setCellFactory(column -> new TableCell<>() {
            private final DatePicker datePicker = new DatePicker();

            {
                datePicker.setOnAction(e -> {
                    Evento evento = getTableView().getItems().get(getIndex());
                    if (evento != null) {
                        evento.setFechaInicio(datePicker.getValue());
                        commitEdit(datePicker.getValue());
                    }
                });
            }

            @Override
            protected void updateItem(LocalDate item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    datePicker.setValue(item);
                    setGraphic(datePicker);
                }
            }
        });

        duracionCol.setCellFactory(TextFieldTableCell.forTableColumn(new javafx.util.converter.IntegerStringConverter()));
        duracionCol.setOnEditCommit(event -> {
            Evento ev = event.getRowValue();
            ev.setDuracionEstimadasDias(event.getNewValue());
            tablaEventos.refresh();
        });

        estadoCol.setCellFactory(column -> new TableCell<>() {
            private final ComboBox<EstadoEvento> combo = new ComboBox<>();

            {
                combo.getItems().setAll(EstadoEvento.values());
                combo.setOnAction(e -> {
                    Evento evento = getTableView().getItems().get(getIndex());
                    if (evento != null) {
                        evento.cambiarEstado(combo.getValue());
                        commitEdit(combo.getValue().toString());
                        tablaEventos.refresh();
                    }
                });
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || item == null) {
                    setGraphic(null);
                } else {
                    combo.setValue(EstadoEvento.valueOf(item));
                    setGraphic(combo);
                }
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Campos incompletos");
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private void cargarDatosEjemplo() {
        Persona p1 = new Persona("Claudio Biale", "01234567", "3758-123-456", "cb@email.com");
        Persona p2 = new Persona("Melissa Kolb", "76543210", "3758-123-456", "mk@email.com");
        Persona p3 = new Persona("John Doe", "11223344", "3758-123-456", "jd@email.com");
        personas.addAll(p1, p2, p3);

        Participante part1 = new Participante("Carlos Ruiz", "01010101", "3758-123-456", "cr@email.com");
        Participante part2 = new Participante("Lucía Torres", "10101010", "3758-123-456", "lt@email.com");
        participantes.addAll(part1, part2);

        Evento feria = new Feria("Feria del Libro", LocalDate.now().plusDays(2), 3, 20, true);
        Evento taller = new Taller("Taller de Pintura", LocalDate.now().plusDays(5), 2, 15, p1, Modalidad.PRESENCIAL);
        Evento concierto = new Concierto("Concierto Rock", LocalDate.now().plusDays(10), 1, TipoEntrada.PAGA);
        Evento expo = new Exposicion("Expo Arte", LocalDate.now().plusDays(7), 4, "Pintura", p2);
        Evento cicloCine = new CicloCine("Ciclo de Cine Clásico", LocalDate.now().plusDays(15), 5, true);

        feria.agregarResponsable(p1, "Organizador");
        taller.agregarResponsable(p2, "Instructor");
        concierto.agregarResponsable(p3, "Productor");
        expo.agregarResponsable(p2, "Curador");
        cicloCine.agregarResponsable(p1, "Coordinador");

        feria.cambiarEstado(EstadoEvento.CONFIRMADO);
        taller.cambiarEstado(EstadoEvento.CONFIRMADO);
        cicloCine.cambiarEstado(EstadoEvento.CONFIRMADO);

        try { feria.inscribirParticipante(part1); } catch (Exception ignored) {}
        try { taller.inscribirParticipante(part2); } catch (Exception ignored) {}

        eventos.addAll(feria, taller, concierto, expo, cicloCine);

        actualizarListaParticipantesDesdeEventos();
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
                    if (eventosAsociados.length() > 0) eventosAsociados.append(", ");
                    eventosAsociados.append(e.getNombre());
                }
            }
        }
        return eventosAsociados.toString();
    }

    @FXML public void salir() { System.exit(0); }

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
                if (response.getButtonData().isDefaultButton()) {
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
            actualizarListaParticipantesDesdeEventos();
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

    @FXML
    public void eliminarPersona() {
        Persona seleccionada = tablaPersonas.getSelectionModel().getSelectedItem();
        if (seleccionada != null) {
            personas.remove(seleccionada);
            // Opcional: también remover personas de responsables en eventos si querés
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
            controller.setPersonas(personas);

            controller.setOnParticipacionAgregada(() -> {
                actualizarListaParticipantesDesdeEventos();
                tablaParticipantes.refresh();
                tablaEventos.refresh();
            });

            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(dialogPane);
            dialog.setTitle("Registrar Participante");
            dialog.initOwner(btnRegistrarParticipante.getScene().getWindow());

            dialog.showAndWait().ifPresent(response -> {
                if (response.getButtonData().isDefaultButton()) {
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
            for (Evento e : eventos) {
                e.getParticipantes().remove(seleccionado);
            }
            actualizarListaParticipantesDesdeEventos();
            tablaParticipantes.refresh();
            tablaEventos.refresh();
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
}
