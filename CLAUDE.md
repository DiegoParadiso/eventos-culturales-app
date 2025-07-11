# CodeViz Research Context

> **Note**: This file contains research context from CodeViz. Most recent contexts are at the bottom.

---

## Research Query

Investigate the "Controllers" component in detail. Focus your analysis on these key files:
1. ParticipacionController.java (c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/ParticipacionController.java)
2. NuevoEventoDialogController.java (c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevoEventoDialogController.java)
3. NuevaPersonaDialogController.java (c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevaPersonaDialogController.java)
4. MainViewController.java (c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java)

Provide insights about the component's main responsibilities and functionality and how it integrates with other system components

*Session: 0ed388a6acbfa04dbbdd3df1092fabf1 | Generated: 7/10/2025, 9:57:10 PM*

### Analysis Summary

# Controllers Component Analysis

The **Controllers** component in this application is responsible for managing the user interface (UI) and handling user interactions, acting as the intermediary between the **View** (FXML files) and the **Model** (data and business logic classes). These controllers are primarily implemented using JavaFX, leveraging FXML for UI definition and Java classes for logic.

## High-Level Architecture

The application follows a Model-View-Controller (MVC) pattern. The **Controllers** component orchestrates the flow of data and user actions. The primary controller, **MainViewController**, serves as the central hub, managing the main application window and delegating specific tasks to other dialog controllers like **NuevoEventoDialogController**, **NuevaPersonaDialogController**, and **ParticipacionController**. These dialog controllers are responsible for specific data entry and modification workflows.

## Detailed Component Breakdown

### **MainViewController**
The **MainViewController** [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java) is the core controller for the main application window.
*   **Purpose:** It manages the display and interaction with lists of events, persons, and participants. It provides functionalities for adding, viewing details, and deleting events and persons, as well as registering and removing participants. It also includes a calendar view to filter events by date.
*   **Internal Parts:**
    *   **UI Elements:** It contains numerous FXML-injected UI components such as `TableView` instances for events, persons, and participants (`tablaEventos`, `tablaPersonas`, `tablaParticipantes`), associated `TableColumn` definitions (e.g., `nombreCol`, `dniCol`), a `DatePicker` for date filtering (`selectorFecha`), and various `Button` controls (e.g., `btnNuevoEvento`, `btnNuevaPersona`).
    *   **Data Management:** It maintains `ObservableList` instances (`eventos`, `personas`, `participantes`) to hold and display application data, ensuring UI updates automatically reflect changes in these lists.
    *   **Core Methods:**
        *   `initialize()`: Configures table columns, sets up cell factories for editable fields (e.g., [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:100-103) for event name editing), and loads initial example data via `cargarDatosEjemplo()` [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:180).
        *   `nuevoEvento()`: Handles the creation of new events by loading and interacting with the **NuevoEventoDialogController** [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:229).
        *   `nuevaPersona()`: Manages the creation of new persons using the **NuevaPersonaDialogController** [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:265).
        *   `registrarParticipante()`: Orchestrates the registration of participants by loading and passing data to the **ParticipacionController** [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:290).
        *   `eliminarEvento()`, `eliminarPersona()`, `eliminarParticipante()`: Implement deletion functionalities for the respective entities.
        *   `filtrarEventosPorFecha()` [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:200): Filters events displayed in the calendar view.
        *   `actualizarListaParticipantesDesdeEventos()` [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:280): Synchronizes the global participants list based on participants registered in events.
*   **External Relationships:**
    *   **View:** Directly controls the `MainView.fxml` [MainView.fxml](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/resources/view/MainView.fxml).
    *   **Model:** Interacts extensively with **Model** classes such as `Evento` (and its subclasses like `Feria`, `Taller`, `Concierto`, `Exposicion`, `CicloCine`), `Persona`, `Participante`, and enums like `EstadoEvento`, `Modalidad`, `TipoEntrada`. It manipulates these objects based on user input.
    *   **Other Controllers:** Instantiates and interacts with `NuevoEventoDialogController`, `NuevaPersonaDialogController`, and `ParticipacionController` to manage dialog-based operations.

### **ParticipacionController**
The **ParticipacionController** [ParticipacionController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/ParticipacionController.java) manages the process of associating persons or artists with events.
*   **Purpose:** To allow users to add participants (either general `Persona` objects with a `RolEnEvento` or `Artista` objects specifically for `Concierto` events) to selected events, and to view/remove these participations.
*   **Internal Parts:**
    *   **UI Elements:** Includes `ComboBox` for selecting persons, roles, and events (`comboPersona`, `comboRol`, `comboEvento`), a `TextField` for artist names (`inputArtista`), and a `TableView` to display participations (`tablaParticipaciones`). It uses `HBox` containers (`hboxPersonaRol`, `hboxArtista`) to dynamically show/hide relevant input fields.
    *   **Data Management:** Manages an `ObservableList` of `Participacion` objects (`participaciones`) and receives a global list of `Participante` objects (`participantesGlobales`) from the `MainViewController`.
    *   **Core Methods:**
        *   `initialize()`: Sets up `ComboBox` items and adds a listener to `comboEvento` to dynamically adjust visible input fields based on the selected event type (e.g., showing artist input for concerts) [ParticipacionController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/ParticipacionController.java:30-40).
        *   `agregarParticipacion()` [ParticipacionController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/ParticipacionController.java:76): Handles the logic for adding a new participation, distinguishing between `Concierto` (artist-based) and other event types (person-based). It creates `Artista` or `Participante` objects as needed and adds them to the event.
        *   `eliminarParticipacion()` [ParticipacionController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/ParticipacionController.java:149): Removes a selected participation from the table and the associated event.
        *   `cargarDatosIniciales()` [ParticipacionController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/ParticipacionController.java:199): Populates the initial data for the combo boxes.
        *   `obtenerOReutilizarParticipante()` [ParticipacionController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/ParticipacionController.java:216): Reuses existing `Participante` objects or creates new ones.
*   **External Relationships:**
    *   **View:** Controls the `ParticipacionView.fxml` [ParticipacionView.fxml](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/resources/view/ParticipacionView.fxml).
    *   **Model:** Interacts with `Evento`, `Concierto`, `Persona`, `Artista`, `Participante`, `Participacion`, and `RolEnEvento`. It modifies the `participantes` list within `Evento` objects.
    *   **Other Controllers:** Its instance is created and managed by `MainViewController` [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:290), which passes necessary data (`personas`, `eventos`, `participantesGlobales`) and sets a callback for when a participation is added.

### **NuevoEventoDialogController**
The **NuevoEventoDialogController** [NuevoEventoDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevoEventoDialogController.java) is responsible for the creation of new event instances.
*   **Purpose:** To provide a form for users to input details for a new event, dynamically adjusting the required fields based on the selected event type (e.g., `Feria`, `Concierto`, `Taller`). It performs input validation before creating the event object.
*   **Internal Parts:**
    *   **UI Elements:** Contains common input fields like `TextField` for name and duration (`txtNombre`, `txtDuracion`), `DatePicker` for date (`dateFechaInicio`), and a `ComboBox` for event type (`comboTipo`). It also includes specific input groups (`grupoFeria`, `grupoConcierto`, etc.) that are shown or hidden based on the selected event type.
    *   **Data Management:** Holds a list of `Persona` objects (`personasDisponibles`) for selection as instructors or curators.
    *   **Core Methods:**
        *   `initialize()`: Populates the `comboTipo` with event types and sets up a listener to call `updateEventSpecificFieldsVisibility()` [NuevoEventoDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevoEventoDialogController.java:69) when the event type changes.
        *   `updateEventSpecificFieldsVisibility()` [NuevoEventoDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevoEventoDialogController.java:84): Dynamically shows or hides the relevant input field groups based on the `eventType` parameter.
        *   `getEvento()` [NuevoEventoDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevoEventoDialogController.java:126): Validates all input fields and constructs the appropriate `Evento` subclass object (e.g., `Feria`, `Concierto`) based on the selected type. It returns `null` if validation fails.
        *   `showErrorAlert()` [NuevoEventoDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevoEventoDialogController.java:249): Displays error messages to the user.
*   **External Relationships:**
    *   **View:** Controls the `NuevoEventoDialog.fxml` [NuevoEventoDialog.fxml](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/resources/view/NuevoEventoDialog.fxml).
    *   **Model:** Creates instances of `Evento` and its subclasses (`Feria`, `Concierto`, `Taller`, `Exposicion`, `CicloCine`), and uses `Persona`, `Modalidad`, `TipoEntrada` enums.
    *   **Other Controllers:** Its instance is created and its `getEvento()` method is called by `MainViewController` [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:237) to retrieve the newly created event.

### **NuevaPersonaDialogController**
The **NuevaPersonaDialogController** [NuevaPersonaDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevaPersonaDialogController.java) handles the creation of new person records.
*   **Purpose:** To provide a simple form for users to input basic personal details (name, DNI, phone, email) and create a new `Persona` object. It includes basic input validation.
*   **Internal Parts:**
    *   **UI Elements:** Contains `TextField` components for `txtNombre`, `txtDni`, `txtTelefono`, and `txtCorreo`.
    *   **Core Methods:**
        *   `initialize()`: A placeholder method, as no specific initialization is required for its simple text fields.
        *   `getPersona()` [NuevaPersonaDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevaPersonaDialogController.java:29): Validates that all required text fields are not empty and constructs a new `Persona` object. Returns `null` if validation fails.
        *   `showErrorAlert()` [NuevaPersonaDialogController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/NuevaPersonaDialogController.java:59): Displays error messages for invalid or missing input.
*   **External Relationships:**
    *   **View:** Controls the `NuevaPersonaDialog.fxml` [NuevaPersonaDialog.fxml](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/resources/view/NuevaPersonaDialog.fxml).
    *   **Model:** Creates instances of the `Persona` class.
    *   **Other Controllers:** Its instance is created and its `getPersona()` method is called by `MainViewController` [MainViewController.java](c:/Users/Usuario/Documents/GitHub/eventos-culturales-app/src/main/java/app/controller/MainViewController.java:273) to obtain the new `Persona` object.

