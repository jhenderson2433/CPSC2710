package com.example.module2;


public class SeatReservation {

   private String flightDesignator;
   private LocalDate flightDate;
   private String firstName;
   private String lastName;

   public String getFlightDesignator() {
      return flightDesignator;
   }

   public void setFlightDesignator(String designator) {
      if (designator == null || designator.length() < 4 || designator.length() > 6) {
        throw new IllegalArgumentException("Invalid flight designator");
      }
      this.flightDesignator = designator;
   }

   public LocalDate getFlightDate() {
      return flightDate;
   }

   public void setFlightDate(LocalDate date) {
      flightDate = date;
   }

   public String getFirstName() {
      return firstName;
   }

   public void setFirstName(String fn) {
      firstName = fn;
   }

   public String getLastName() {
      return lastName;
   }

   public void setLastName(String ln) {
      lastName = ln;
   }

   @Override
   public String toString() {
      return "SeatReservation{flightDesignator=" + flightDesignator
              + ",flightDate=" + flightDate
              + ",firstName=" + firstName
              + ",lastName=" + lastName
              + "}";
   }

}
