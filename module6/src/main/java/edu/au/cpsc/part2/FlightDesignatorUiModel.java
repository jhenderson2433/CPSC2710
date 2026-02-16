package edu.au.cpsc.part1;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;

public class FlightDesignatorUiModel {

    // Editor fields
    private final StringProperty airline = new SimpleStringProperty("");
    private final StringProperty flightNumber = new SimpleStringProperty("");
    private final StringProperty time = new SimpleStringProperty("");

    // State flags
    private final BooleanProperty hasSelection = new SimpleBooleanProperty(false);
    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    // Validation flags
    private final BooleanProperty airlineValid = new SimpleBooleanProperty(true);
    private final BooleanProperty flightNumberValid = new SimpleBooleanProperty(true);
    private final BooleanProperty timeValid = new SimpleBooleanProperty(true);

    // Derived
    private final BooleanBinding allBlank = Bindings.createBooleanBinding(
            () -> isBlank(airline.get()) && isBlank(flightNumber.get()) && isBlank(time.get()),
            airline, flightNumber, time
    );

    private final BooleanBinding allValid = airlineValid.and(flightNumberValid).and(timeValid);

    public FlightDesignatorUiModel() {

        airline.addListener((obs, o, n) -> { modified.set(true); validate(); });
        flightNumber.addListener((obs, o, n) -> { modified.set(true); validate(); });
        time.addListener((obs, o, n) -> { modified.set(true); validate(); });

        validate();
    }

    public void loadSelected(String a, String f, String t) {
        hasSelection.set(true);
        modified.set(false);

        airline.set(a == null ? "" : a);
        flightNumber.set(f == null ? "" : f);
        time.set(t == null ? "" : t);

        validate();
    }

    public void clearForNew() {
        hasSelection.set(false);
        modified.set(false);

        airline.set("");
        flightNumber.set("");
        time.set("");

        validate();
    }

    public void validate() {
        airlineValid.set(isValidAirline(airline.get()));
        flightNumberValid.set(isValidFlightNumber(flightNumber.get()));
        timeValid.set(isValidTime(time.get()));
    }

    // ---- Validity rules (adjust if your Module 2/3 rules differ) ----
    private boolean isValidAirline(String s) {
        if (isBlank(s)) return false;
        return s.matches("[A-Z]{2}");     // 2 uppercase letters
    }

    private boolean isValidFlightNumber(String s) {
        if (isBlank(s)) return false;
        if (s.length() > 4) return false; // length check
        return s.matches("\\d+");         // digits only
    }

    // Time format: HH:MM (24 hour)
    private boolean isValidTime(String s) {
        if (isBlank(s)) return false;
        if (!s.matches("\\d{2}:\\d{2}")) return false;

        int hh = Integer.parseInt(s.substring(0, 2));
        int mm = Integer.parseInt(s.substring(3, 5));
        return hh >= 0 && hh <= 23 && mm >= 0 && mm <= 59;
    }

    private boolean isBlank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // ---- Expose properties ----
    public StringProperty airlineProperty() { return airline; }
    public StringProperty flightNumberProperty() { return flightNumber; }
    public StringProperty timeProperty() { return time; }

    public BooleanProperty hasSelectionProperty() { return hasSelection; }
    public BooleanProperty modifiedProperty() { return modified; }

    public BooleanProperty airlineValidProperty() { return airlineValid; }
    public BooleanProperty flightNumberValidProperty() { return flightNumberValid; }
    public BooleanProperty timeValidProperty() { return timeValid; }

    public BooleanBinding allBlankProperty() { return allBlank; }
    public BooleanBinding allValidProperty() { return allValid; }
}
