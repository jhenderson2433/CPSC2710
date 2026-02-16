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
    private FlightDesignatorUiModel model;

    // JavaFX calls this automatically after FXML loads
    public void initialize() {
        loadDatabase();

        // Bind table to DB list
        flightsTable.setItems(db.getScheduledFlights());
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
        monToggle.selectedProperty().addListener((o,a,b)->syncDaysToModel.run());
        tueToggle.selectedProperty().addListener((o,a,b)->syncDaysToModel.run());
        wedToggle.selectedProperty().addListener((o,a,b)->syncDaysToModel.run());
        thuToggle.selectedProperty().addListener((o,a,b)->syncDaysToModel.run());
        friToggle.selectedProperty().addListener((o,a,b)->syncDaysToModel.run());
        satToggle.selectedProperty().addListener((o,a,b)->syncDaysToModel.run());
        sunToggle.selectedProperty().addListener((o,a,b)->syncDaysToModel.run());
        syncDaysToModel.run(); // initial

// ----- Button text (optional feature) -----
        addUpdateButton.textProperty().bind(
                Bindings.when(model.hasSelectionProperty()).then("Update").otherwise("Add")
        );

// ----- Enable/Disable rules (REQUIRED rubric parts) -----
        deleteButton.disableProperty().bind(model.hasSelectionProperty().not());

// New state vs modified state for Add/Update (single button):
        BooleanBinding addEnabled = model.hasSelectionProperty().not()
                .and(model.allBlankProperty().not())
                .and(model.allValidProperty());

        javafx.beans.binding.BooleanBinding updateEnabled = model.hasSelectionProperty()
                .and(model.modifiedProperty())
                .and(model.allValidProperty());

        addUpdateButton.disableProperty().bind(addEnabled.or(updateEnabled).not());

// Optional: disable New when editing existing
        newButton.disableProperty().bind(model.hasSelectionProperty());

// ----- Highlight invalid inputs (REQUIRED) -----
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

        // Selection behavior
        flightsTable.getSelectionModel().selectedItemProperty().addListener((obs, oldV, newV) -> {
            if (newV == null) {
                clearEditor();
                addUpdateButton.setText("Add");
            } else {
                populateEditorFrom(newV);
                addUpdateButton.setText("Update");
            }
        });

        // Start in "Add" mode
        flightsTable.getSelectionModel().clearSelection();
        addUpdateButton.setText("Add");
    }

    // ===== Button Handlers =====

    @FXML
    private void onAddOrUpdate() {
        try {
            ScheduledFlight selected = flightsTable.getSelectionModel().getSelectedItem();

            if (selected == null) {
                // ADD
                ScheduledFlight sf = buildFromEditor();
                db.addScheduledFlight(sf);
                saveDatabase();
                flightsTable.getSelectionModel().select(sf);
            } else {
                // UPDATE (modify the selected object in-place)
                applyEditorToExisting(selected);
                saveDatabase();
                flightsTable.refresh();
            }

        } catch (IllegalArgumentException ex) {
            // Keep it simple: show error in console (you can swap to Alert later)
            System.err.println("Input error: " + ex.getMessage());
        }
    }

    @FXML
    private void onNew() {
        flightsTable.getSelectionModel().clearSelection();
        clearEditor();
        addUpdateButton.setText("Add");
    }

    @FXML
    private void onDelete() {
        ScheduledFlight selected = flightsTable.getSelectionModel().getSelectedItem();
        if (selected == null) {
            return;
        }

        db.removeScheduledFlight(selected);
        saveDatabase();

        flightsTable.getSelectionModel().clearSelection();
        clearEditor();
        addUpdateButton.setText("Add");
        flightsTable.refresh();
    }

    // ===== Build / Populate Helpers =====

    private ScheduledFlight buildFromEditor() {
        ScheduledFlight sf = new ScheduledFlight();

        sf.setFlightDesignator(required(flightDesignatorField, "Flight Designator"));
        sf.setDepartureAirportIdent(required(departureAirportIdentField, "Departure Airport"));
        sf.setArrivalAirportIdent(required(arrivalAirportIdentField, "Arrival Airport"));

        sf.setDepartureTime(parseTime(departureTimeField, "Departure Time (use H:mm like 13:30)"));
        sf.setArrivalTime(parseTime(arrivalTimeField, "Arrival Time (use H:mm like 15:00)"));

        Set<DayOfWeek> days = selectedDays();
        if (days.isEmpty()) {
            throw new IllegalArgumentException("Select at least one day of the week.");
        }
        sf.setDaysOfWeek(days);

        return sf;
    }

    private void applyEditorToExisting(ScheduledFlight target) {
        target.setFlightDesignator(required(flightDesignatorField, "Flight Designator"));
        target.setDepartureAirportIdent(required(departureAirportIdentField, "Departure Airport"));
        target.setArrivalAirportIdent(required(arrivalAirportIdentField, "Arrival Airport"));

        target.setDepartureTime(parseTime(departureTimeField, "Departure Time (use H:mm like 13:30)"));
        target.setArrivalTime(parseTime(arrivalTimeField, "Arrival Time (use H:mm like 15:00)"));

        Set<DayOfWeek> days = selectedDays();
        if (days.isEmpty()) {
            throw new IllegalArgumentException("Select at least one day of the week.");
        }
        target.setDaysOfWeek(new HashSet<>(days));
    }

    private void populateEditorFrom(ScheduledFlight sf) {
        flightDesignatorField.setText(safe(sf.getFlightDesignator()));
        departureAirportIdentField.setText(safe(sf.getDepartureAirportIdent()));
        arrivalAirportIdentField.setText(safe(sf.getArrivalAirportIdent()));

        departureTimeField.setText(sf.getDepartureTime() == null ? "" : sf.getDepartureTime().format(TIME_FMT));
        arrivalTimeField.setText(sf.getArrivalTime() == null ? "" : sf.getArrivalTime().format(TIME_FMT));

        setDayToggles(sf.getDaysOfWeek());
    }

    private void clearEditor() {
        flightDesignatorField.clear();
        departureAirportIdentField.clear();
        arrivalAirportIdentField.clear();
        departureTimeField.clear();
        arrivalTimeField.clear();

        monToggle.setSelected(false);
        tueToggle.setSelected(false);
        wedToggle.setSelected(false);
        thuToggle.setSelected(false);
        friToggle.setSelected(false);
        satToggle.setSelected(false);
        sunToggle.setSelected(false);
    }

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

    private Set<DayOfWeek> selectedDays() {
        EnumSet<DayOfWeek> days = EnumSet.noneOf(DayOfWeek.class);
        if (monToggle.isSelected()) days.add(DayOfWeek.MONDAY);
        if (tueToggle.isSelected()) days.add(DayOfWeek.TUESDAY);
        if (wedToggle.isSelected()) days.add(DayOfWeek.WEDNESDAY);
        if (thuToggle.isSelected()) days.add(DayOfWeek.THURSDAY);
        if (friToggle.isSelected()) days.add(DayOfWeek.FRIDAY);
        if (satToggle.isSelected()) days.add(DayOfWeek.SATURDAY);
        if (sunToggle.isSelected()) days.add(DayOfWeek.SUNDAY);
        return days;
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

    private String required(TextField tf, String label) {
        String s = tf.getText() == null ? "" : tf.getText().trim();
        if (s.isEmpty()) throw new IllegalArgumentException(label + " is required.");
        return s;
    }

    private LocalTime parseTime(TextField tf, String message) {
        String s = tf.getText() == null ? "" : tf.getText().trim();
        try {
            return LocalTime.parse(s, TIME_FMT);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(message);
        }
    }

    private String safe(String s) {
        return s == null ? "" : s;
    }

    // ===== Persistence =====

    private Path getDbPath() {
        // Relative file name (no absolute paths)
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
