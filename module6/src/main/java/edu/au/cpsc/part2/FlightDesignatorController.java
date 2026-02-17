package edu.au.cpsc.part2;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;

public class FlightDesignatorController {

    // ===== Table =====
    @FXML private TableView<ScheduledFlight> flightsTable;

    @FXML private TableColumn<ScheduledFlight, String> flightCol;
    @FXML private TableColumn<ScheduledFlight, String> depCol;
    @FXML private TableColumn<ScheduledFlight, String> arrCol;
    @FXML private TableColumn<ScheduledFlight, String> depTimeCol;
    @FXML private TableColumn<ScheduledFlight, String> arrTimeCol;
    @FXML private TableColumn<ScheduledFlight, String> daysCol;

    // ===== Editor fields =====
    @FXML private TextField flightDesignatorField;
    @FXML private TextField departureAirportIdentField;
    @FXML private TextField arrivalAirportIdentField;
    @FXML private TextField departureTimeField; // H:mm
    @FXML private TextField arrivalTimeField;   // H:mm

    // ===== Day toggles =====
    @FXML private ToggleButton monToggle;
    @FXML private ToggleButton tueToggle;
    @FXML private ToggleButton wedToggle;
    @FXML private ToggleButton thuToggle; // R
    @FXML private ToggleButton friToggle;
    @FXML private ToggleButton satToggle;
    @FXML private ToggleButton sunToggle; // U

    // ===== Buttons =====
    @FXML private Button addUpdateButton;
    @FXML private Button newButton;
    @FXML private Button deleteButton;

    // ===== Database + IO =====
    private static final String DB_FILE = "airline.db";
    private AirlineDatabase db;

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("H:mm");

    // UI Model (must be initialized before bindings!)
    private FlightDesignatorUiModel model;

    // JavaFX calls this automatically after FXML loads
    public void initialize() {
        loadDatabase();

        // IMPORTANT: initialize model BEFORE any bindings that reference it
        model = new FlightDesignatorUiModel();

        // Bind table to DB list
        flightsTable.setItems(db.getScheduledFlights());

        // Column value factories
        flightCol.setCellValueFactory(cell ->
                new SimpleStringProperty(safe(cell.getValue().getFlightDesignator())));

        depCol.setCellValueFactory(cell ->
                new SimpleStringProperty(safe(cell.getValue().getDepartureAirportIdent())));

        arrCol.setCellValueFactory(cell ->
                new SimpleStringProperty(safe(cell.getValue().getArrivalAirportIdent())));

        depTimeCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getDepartureTime() == null
                        ? "" : cell.getValue().getDepartureTime().format(TIME_FMT)));

        arrTimeCol.setCellValueFactory(cell ->
                new SimpleStringProperty(cell.getValue().getArrivalTime() == null
                        ? "" : cell.getValue().getArrivalTime().format(TIME_FMT)));

        daysCol.setCellValueFactory(cell ->
                new SimpleStringProperty(formatDays(cell.getValue())));

        // ----- Bind editor textfields to UI model -----
        flightDesignatorField.textProperty().bindBidirectional(model.flightDesignatorProperty());
        departureAirportIdentField.textProperty().bindBidirectional(model.departureAirportProperty());
        arrivalAirportIdentField.textProperty().bindBidirectional(model.arrivalAirportProperty());
        departureTimeField.textProperty().bindBidirectional(model.departureTimeProperty());
        arrivalTimeField.textProperty().bindBidirectional(model.arrivalTimeProperty());

        // ----- Bind day toggles -> model days -----
        Runnable syncDaysToModel = () -> {
            EnumSet<DayOfWeek> d = EnumSet.noneOf(DayOfWeek.class);
            if (monToggle.isSelected()) d.add(DayOfWeek.MONDAY);
            if (tueToggle.isSelected()) d.add(DayOfWeek.TUESDAY);
            if (wedToggle.isSelected()) d.add(DayOfWeek.WEDNESDAY);
            if (thuToggle.isSelected()) d.add(DayOfWeek.THURSDAY);
            if (friToggle.isSelected()) d.add(DayOfWeek.FRIDAY);
            if (satToggle.isSelected()) d.add(DayOfWeek.SATURDAY);
            if (sunToggle.isSelected()) d.add(DayOfWeek.SUNDAY);
            model.daysProperty().set(d);
        };

        monToggle.selectedProperty().addListener((o, a, b) -> syncDaysToModel.run());
        tueToggle.selectedProperty().addListener((o, a, b) -> syncDaysToModel.run());
        wedToggle.selectedProperty().addListener((o, a, b) -> syncDaysToModel.run());
        thuToggle.selectedProperty().addListener((o, a, b) -> syncDaysToModel.run());
        friToggle.selectedProperty().addListener((o, a, b) -> syncDaysToModel.run());
        satToggle.selectedProperty().addListener((o, a, b) -> syncDaysToModel.run());
        sunToggle.selectedProperty().addListener((o, a, b) -> syncDaysToModel.run());
        // NOTE: do NOT call syncDaysToModel.run() here; it would mark modified immediately.

        // ----- Button text controlled ONLY by binding (do not call setText on this button) -----
        addUpdateButton.textProperty().bind(
                Bindings.when(model.hasSelectionProperty()).then("Update").otherwise("Add")
        );

        // ----- Enable/Disable rules -----
        deleteButton.disableProperty().bind(model.hasSelectionProperty().not());

        // Add enabled when: no selection, not all blank, all valid
        BooleanBinding addEnabled = model.hasSelectionProperty().not()
                .and(model.allBlankProperty().not())
                .and(model.allValidProperty());

        // Update enabled when: selection exists, modified, all valid
        BooleanBinding updateEnabled = model.hasSelectionProperty()
                .and(model.modifiedProperty())
                .and(model.allValidProperty());

        addUpdateButton.disableProperty().bind(addEnabled.or(updateEnabled).not());

        // Optional: disable New when editing existing
        newButton.disableProperty().bind(model.hasSelectionProperty());

        // ----- Highlight invalid inputs -----
        flightDesignatorField.styleProperty().bind(
                Bindings.when(model.flightDesignatorValidProperty())
                        .then("")
                        .otherwise("-fx-border-color: red; -fx-border-width: 2;")
        );

        departureAirportIdentField.styleProperty().bind(
                Bindings.when(model.departureAirportValidProperty())
                        .then("")
                        .otherwise("-fx-border-color: red; -fx-border-width: 2;")
        );

        arrivalAirportIdentField.styleProperty().bind(
                Bindings.when(model.arrivalAirportValidProperty())
                        .then("")
                        .otherwise("-fx-border-color: red; -fx-border-width: 2;")
        );

        departureTimeField.styleProperty().bind(
                Bindings.when(model.departureTimeValidProperty())
                        .then("")
                        .otherwise("-fx-border-color: red; -fx-border-width: 2;")
        );

        arrivalTimeField.styleProperty().bind(
                Bindings.when(model.arrivalTimeValidProperty())
                        .then("")
                        .otherwise("-fx-border-color: red; -fx-border-width: 2;")
        );

        // Selection behavior (model becomes source of truth)
        flightsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) {
                model.clearForNew();
                setDayToggles(Set.of()); // also clears toggles without forcing modified via sync
            } else {
                model.loadSelected(
                        newV.getFlightDesignator(),
                        newV.getDepartureAirportIdent(),
                        newV.getArrivalAirportIdent(),
                        newV.getDepartureTime() == null ? "" : newV.getDepartureTime().format(TIME_FMT),
                        newV.getArrivalTime() == null ? "" : newV.getArrivalTime().format(TIME_FMT),
                        newV.getDaysOfWeek()
                );
                setDayToggles(newV.getDaysOfWeek()); // model -> toggles
            }
        });

        // Start in "Add" mode
        flightsTable.getSelectionModel().clearSelection();
        model.clearForNew();
        setDayToggles(Set.of());
    }

    // ===== Button Handlers =====

    @FXML
    private void onAddOrUpdate() {
        try {
            ScheduledFlight selected = flightsTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                // ADD (build from model)
                ScheduledFlight sf = buildFromModel();
                db.addScheduledFlight(sf);
                saveDatabase();
                flightsTable.getSelectionModel().select(sf);
            } else {
                // UPDATE (apply from model)
                applyModelToExisting(selected);
                saveDatabase();
                flightsTable.refresh();
                // keep selection; model.modified will be reset by re-loading selection
                flightsTable.getSelectionModel().select(selected);
            }

        } catch (IllegalArgumentException ex) {
            System.err.println("Input error: " + ex.getMessage());
        }
    }

    @FXML
    private void onNew() {
        flightsTable.getSelectionModel().clearSelection();
        model.clearForNew();
        setDayToggles(Set.of());
    }

    @FXML
    private void onDelete() {
        ScheduledFlight selected = flightsTable.getSelectionModel().getSelectedItem();
        if (selected == null) return;

        db.removeScheduledFlight(selected);
        saveDatabase();

        flightsTable.getSelectionModel().clearSelection();
        model.clearForNew();
        setDayToggles(Set.of());
        flightsTable.refresh();
    }

    // ===== Build / Apply from UI Model =====

    private ScheduledFlight buildFromModel() {
        ScheduledFlight sf = new ScheduledFlight();

        sf.setFlightDesignator(required(model.flightDesignatorProperty().get(), "Flight Designator"));
        sf.setDepartureAirportIdent(required(model.departureAirportProperty().get(), "Departure Airport"));
        sf.setArrivalAirportIdent(required(model.arrivalAirportProperty().get(), "Arrival Airport"));

        sf.setDepartureTime(parseTime(model.departureTimeProperty().get(), "Departure Time (use H:mm like 13:30)"));
        sf.setArrivalTime(parseTime(model.arrivalTimeProperty().get(), "Arrival Time (use H:mm like 15:00)"));

        Set<DayOfWeek> days = model.daysProperty().get();
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("Select at least one day of the week.");
        }
        sf.setDaysOfWeek(new HashSet<>(days));

        return sf;
    }

    private void applyModelToExisting(ScheduledFlight target) {
        target.setFlightDesignator(required(model.flightDesignatorProperty().get(), "Flight Designator"));
        target.setDepartureAirportIdent(required(model.departureAirportProperty().get(), "Departure Airport"));
        target.setArrivalAirportIdent(required(model.arrivalAirportProperty().get(), "Arrival Airport"));

        target.setDepartureTime(parseTime(model.departureTimeProperty().get(), "Departure Time (use H:mm like 13:30)"));
        target.setArrivalTime(parseTime(model.arrivalTimeProperty().get(), "Arrival Time (use H:mm like 15:00)"));

        Set<DayOfWeek> days = model.daysProperty().get();
        if (days == null || days.isEmpty()) {
            throw new IllegalArgumentException("Select at least one day of the week.");
        }
        target.setDaysOfWeek(new HashSet<>(days));
    }

    private String required(String value, String label) {
        String s = value == null ? "" : value.trim();
        if (s.isEmpty()) throw new IllegalArgumentException(label + " is required.");
        return s;
    }

    private LocalTime parseTime(String value, String message) {
        String s = value == null ? "" : value.trim();
        try {
            return LocalTime.parse(s, TIME_FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(message);
        }
    }

    // ===== Days helpers =====

    private void setDayToggles(Set<DayOfWeek> days) {
        if (days == null) days = Set.of();

        monToggle.setSelected(days.contains(DayOfWeek.MONDAY));
        tueToggle.setSelected(days.contains(DayOfWeek.TUESDAY));
        wedToggle.setSelected(days.contains(DayOfWeek.WEDNESDAY));
        thuToggle.setSelected(days.contains(DayOfWeek.THURSDAY));
        friToggle.setSelected(days.contains(DayOfWeek.FRIDAY));
        satToggle.setSelected(days.contains(DayOfWeek.SATURDAY));
        sunToggle.setSelected(days.contains(DayOfWeek.SUNDAY));
    }

    private String formatDays(ScheduledFlight sf) {
        if (sf == null || sf.getDaysOfWeek() == null) return "";

        Set<DayOfWeek> d = sf.getDaysOfWeek();
        StringBuilder sb = new StringBuilder();
        if (d.contains(DayOfWeek.MONDAY)) sb.append("M");
        if (d.contains(DayOfWeek.TUESDAY)) sb.append("T");
        if (d.contains(DayOfWeek.WEDNESDAY)) sb.append("W");
        if (d.contains(DayOfWeek.THURSDAY)) sb.append("R");
        if (d.contains(DayOfWeek.FRIDAY)) sb.append("F");
        if (d.contains(DayOfWeek.SATURDAY)) sb.append("S");
        if (d.contains(DayOfWeek.SUNDAY)) sb.append("U");
        return sb.toString();
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    // ===== Persistence =====

    private Path getDbPath() {
        return Paths.get(DB_FILE);
    }

    private void loadDatabase() {
        Path p = getDbPath();

        if (!Files.exists(p)) {
            db = new AirlineDatabase();
            return;
        }

        try (InputStream in = Files.newInputStream(p)) {
            db = AirlineDatabaseIO.load(in);
        } catch (IOException e) {
            db = new AirlineDatabase();
            System.err.println("Could not load database; starting new. " + e.getMessage());
        }
    }

    private void saveDatabase() {
        Path p = getDbPath();

        try (OutputStream out = Files.newOutputStream(p)) {
            AirlineDatabaseIO.save(db, out);
        } catch (IOException e) {
            System.err.println("Could not save database: " + e.getMessage());
        }
    }
}
