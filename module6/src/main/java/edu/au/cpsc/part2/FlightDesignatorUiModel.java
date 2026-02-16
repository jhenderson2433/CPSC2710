package edu.au.cpsc.part2;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.BooleanBinding;
import javafx.beans.property.*;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.EnumSet;
import java.util.Set;

public class FlightDesignatorUiModel {

    private static final DateTimeFormatter TIME_FMT = DateTimeFormatter.ofPattern("H:mm");


    // Editor values
    private final StringProperty flightDesignator = new SimpleStringProperty("");
    private final StringProperty departureAirport = new SimpleStringProperty("");
    private final StringProperty arrivalAirport = new SimpleStringProperty("");
    private final StringProperty departureTime = new SimpleStringProperty("");
    private final StringProperty arrivalTime = new SimpleStringProperty("");

    // Days
    private final ObjectProperty<Set<DayOfWeek>> days = new SimpleObjectProperty<>(EnumSet.noneOf(DayOfWeek.class));

    // State
    private final BooleanProperty hasSelection = new SimpleBooleanProperty(false);
    private final BooleanProperty modified = new SimpleBooleanProperty(false);

    // Validation flags
    private final BooleanProperty flightDesignatorValid = new SimpleBooleanProperty(true);
    private final BooleanProperty departureAirportValid = new SimpleBooleanProperty(true);
    private final BooleanProperty arrivalAirportValid = new SimpleBooleanProperty(true);
    private final BooleanProperty departureTimeValid = new SimpleBooleanProperty(true);
    private final BooleanProperty arrivalTimeValid = new SimpleBooleanProperty(true);
    private final BooleanProperty daysValid = new SimpleBooleanProperty(true);

    // Derived
    private final BooleanBinding allBlank = Bindings.createBooleanBinding(
            () -> blank(flightDesignator.get())
                    && blank(departureAirport.get())
                    && blank(arrivalAirport.get())
                    && blank(departureTime.get())
                    && blank(arrivalTime.get()),
            flightDesignator, departureAirport, arrivalAirport, departureTime, arrivalTime
    );

    private final BooleanBinding allValid = flightDesignatorValid
            .and(departureAirportValid)
            .and(arrivalAirportValid)
            .and(departureTimeValid)
            .and(arrivalTimeValid)
            .and(daysValid);

    public FlightDesignatorUiModel() {
        // mark modified + validate on changes
        flightDesignator.addListener((o, a, b) -> { modified.set(true); validate(); });
        departureAirport.addListener((o, a, b) -> { modified.set(true); validate(); });
        arrivalAirport.addListener((o, a, b) -> { modified.set(true); validate(); });
        departureTime.addListener((o, a, b) -> { modified.set(true); validate(); });
        arrivalTime.addListener((o, a, b) -> { modified.set(true); validate(); });
        days.addListener((o, a, b) -> { modified.set(true); validate(); });

        validate();
    }

    public void clearForNew() {
        hasSelection.set(false);
        modified.set(false);

        flightDesignator.set("");
        departureAirport.set("");
        arrivalAirport.set("");
        departureTime.set("");
        arrivalTime.set("");
        days.set(EnumSet.noneOf(DayOfWeek.class));

        validate();
    }

    public void loadSelected(String fd, String dep, String arr, String depT, String arrT, Set<DayOfWeek> d) {
        hasSelection.set(true);
        modified.set(false);

        flightDesignator.set(fd == null ? "" : fd);
        departureAirport.set(dep == null ? "" : dep);
        arrivalAirport.set(arr == null ? "" : arr);
        departureTime.set(depT == null ? "" : depT);
        arrivalTime.set(arrT == null ? "" : arrT);
        days.set(d == null ? EnumSet.noneOf(DayOfWeek.class) : EnumSet.copyOf(d));

        validate();
    }

    public void validate() {
        // These match your Module 4 "required" checks + time format check.
        // If your course has stricter length/pattern rules, tighten these here.

        flightDesignatorValid.set(validRequired(flightDesignator.get(), 1, 10)); // adjust max if needed
        departureAirportValid.set(validRequired(departureAirport.get(), 1, 10));
        arrivalAirportValid.set(validRequired(arrivalAirport.get(), 1, 10));

        departureTimeValid.set(validTime(departureTime.get()));
        arrivalTimeValid.set(validTime(arrivalTime.get()));

        Set<DayOfWeek> d = days.get();
        daysValid.set(d != null && !d.isEmpty());
    }

    private boolean validRequired(String s, int min, int max) {
        if (blank(s)) return false;
        int len = s.trim().length();
        return len >= min && len <= max;
    }

    private boolean validTime(String s) {
        if (blank(s)) return false;
        try {
            LocalTime.parse(s.trim(), TIME_FMT);
            return true;
        } catch (DateTimeParseException e) {
            return false;
        }
    }

    private boolean blank(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Properties
    public StringProperty flightDesignatorProperty() { return flightDesignator; }
    public StringProperty departureAirportProperty() { return departureAirport; }
    public StringProperty arrivalAirportProperty() { return arrivalAirport; }
    public StringProperty departureTimeProperty() { return departureTime; }
    public StringProperty arrivalTimeProperty() { return arrivalTime; }

    public ObjectProperty<Set<DayOfWeek>> daysProperty() { return days; }

    public BooleanProperty hasSelectionProperty() { return hasSelection; }
    public BooleanProperty modifiedProperty() { return modified; }

    public BooleanProperty flightDesignatorValidProperty() { return flightDesignatorValid; }
    public BooleanProperty departureAirportValidProperty() { return departureAirportValid; }
    public BooleanProperty arrivalAirportValidProperty() { return arrivalAirportValid; }
    public BooleanProperty departureTimeValidProperty() { return departureTimeValid; }
    public BooleanProperty arrivalTimeValidProperty() { return arrivalTimeValid; }
    public BooleanProperty daysValidProperty() { return daysValid; }

    public BooleanBinding allBlankProperty() { return allBlank; }
    public BooleanBinding allValidProperty() { return allValid; }
}
