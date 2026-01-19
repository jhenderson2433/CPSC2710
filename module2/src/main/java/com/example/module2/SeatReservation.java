/*
 * Project: Module 2
 * Author: Javontea Henderson
 * Email: jdh0204@auburn.edu
 * Date: 1/19/2026
 * Description: Flight reservation app
 */

package com.example.module2;

import java.time.LocalDate;

public class SeatReservation {

   private String flightDesignator;
   private LocalDate flightDate;
   private String firstName;
   private String lastName;

   // New fields required by the assignment
   private int numberOfBags;
   private boolean flyingWithInfant;
   private boolean flyingWithTravelInsurance;

   public SeatReservation() {
      // default constructor
   }

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
      this.flightDate = date;
   }

   public String getFirstName() {
      return firstName;
   }

   // Updated per assignment: 2–15 characters
   public void setFirstName(String fn) {
      if (fn == null || fn.length() < 2 || fn.length() > 15) {
         throw new IllegalArgumentException("First name must be 2-15 characters.");
      }
      this.firstName = fn;
   }

   public String getLastName() {
      return lastName;
   }

   // Updated per assignment: 2–15 characters
   public void setLastName(String ln) {
      if (ln == null || ln.length() < 2 || ln.length() > 15) {
         throw new IllegalArgumentException("Last name must be 2-15 characters.");
      }
      this.lastName = ln;
   }

   // numberOfBags getter/setter
   public int getNumberOfBags() {
      return numberOfBags;
   }

   public void setNumberOfBags(int numberOfBags) {
      this.numberOfBags = numberOfBags;
   }

   // flyingWithInfant getter + two setter-ish methods
   public boolean isFlyingWithInfant() {
      return flyingWithInfant;
   }

   public void makeFlyingWithInfant() {
      flyingWithInfant = true;
   }

   public void makeNotFlyingWithInfant() {
      flyingWithInfant = false;
   }

   // travel insurance getter + two setter-ish methods
   public boolean hasTravelInsurance() {
      return flyingWithTravelInsurance;
   }

   public void makeFlyingWithTravelInsurance() {
      flyingWithTravelInsurance = true;
   }

   public void makeNotFlyingWithTravelInsurance() {
      flyingWithTravelInsurance = false;
   }

   @Override
   public String toString() {
      return "SeatReservation{flightDesignator='" + flightDesignator
              + "', flightDate=" + flightDate
              + ", firstName='" + firstName
              + "', lastName='" + lastName
              + "', numberOfBags=" + numberOfBags
              + ", flyingWithInfant=" + flyingWithInfant
              + ", flyingWithTravelInsurance=" + flyingWithTravelInsurance
              + "}";
   }
}
