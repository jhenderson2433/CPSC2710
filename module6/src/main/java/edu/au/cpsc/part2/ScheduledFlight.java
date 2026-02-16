package edu.au.cpsc.part2;

import java.io.Serializable;
import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

public class ScheduledFlight implements Serializable {

    private static final long serialVersionUID = 1L;

    private String flightDesignator;
    private String departureAirportIdent;
    private LocalTime departureTime;
    private String arrivalAirportIdent;
    private LocalTime arrivalTime;
    private Set<DayOfWeek> daysOfWeek;

    public ScheduledFlight() {
        daysOfWeek = new HashSet<>();
    }

    public String getFlightDesignator() { return flightDesignator; }
    public void setFlightDesignator(String v) {
        if (v == null) throw new IllegalArgumentException();
        flightDesignator = v;
    }

    public String getDepartureAirportIdent() { return departureAirportIdent; }
    public void setDepartureAirportIdent(String v) {
        if (v == null) throw new IllegalArgumentException();
        departureAirportIdent = v;
    }

    public LocalTime getDepartureTime() { return departureTime; }
    public void setDepartureTime(LocalTime v) {
        if (v == null) throw new IllegalArgumentException();
        departureTime = v;
    }

    public String getArrivalAirportIdent() { return arrivalAirportIdent; }
    public void setArrivalAirportIdent(String v) {
        if (v == null) throw new IllegalArgumentException();
        arrivalAirportIdent = v;
    }

    public LocalTime getArrivalTime() { return arrivalTime; }
    public void setArrivalTime(LocalTime v) {
        if (v == null) throw new IllegalArgumentException();
        arrivalTime = v;
    }

    public Set<DayOfWeek> getDaysOfWeek() { return daysOfWeek; }
    public void setDaysOfWeek(Set<DayOfWeek> v) {
        if (v == null) throw new IllegalArgumentException();
        daysOfWeek = v;
    }
}
