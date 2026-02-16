package edu.au.cpsc.part2;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * In-memory database for scheduled flights.
 */
public class AirlineDatabase {

    private final ObservableList<ScheduledFlight> scheduledFlights;

    /** Creates an empty database. */
    public AirlineDatabase() {
        scheduledFlights = FXCollections.observableArrayList();
    }

    /** Returns the observable list backing this database. */
    public ObservableList<ScheduledFlight> getScheduledFlights() {
        return scheduledFlights;
    }

    /** Adds a scheduled flight to the database. */
    public void addScheduledFlight(ScheduledFlight sf) {
        if (sf == null) {
            throw new IllegalArgumentException("ScheduledFlight cannot be null");
        }
        scheduledFlights.add(sf);
    }

    /** Removes a scheduled flight from the database. */
    public void removeScheduledFlight(ScheduledFlight sf) {
        if (sf == null) {
            throw new IllegalArgumentException("ScheduledFlight cannot be null");
        }
        scheduledFlights.remove(sf);
    }

    /**
     * Updates an existing scheduled flight in the database.
     * This replaces the item at the same index as the given object (if found).
     *
     * If it's not found, this method throws IllegalArgumentException.
     */
    public void updateScheduledFlight(ScheduledFlight sf) {
        if (sf == null) {
            throw new IllegalArgumentException("ScheduledFlight cannot be null");
        }

        int idx = scheduledFlights.indexOf(sf);
        if (idx < 0) {
            throw new IllegalArgumentException("ScheduledFlight not found in database");
        }

        scheduledFlights.set(idx, sf);
    }
}
