/*
 * Project: Module 2
 * Author: Javontea Henderson
 * Email: jdh0204@auburn.edu
 * Date: 1/19/2026
 * Description: Flight reservation app
 */
package com.example.module2;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.time.LocalDate;

public class SeatReservationApplication extends Application {

    private SeatReservation seatReservation;

    private TextField flightDesignatorField;
    private DatePicker flightDatePicker;
    private TextField firstNameField;
    private TextField lastNameField;
    private TextField bagsField;
    private CheckBox infantCheckBox;
    private CheckBox insuranceCheckBox;
    private TextField passengersField;

    @Override
    public void start(Stage stage) {

        // Create model object
        seatReservation = new SeatReservation();
        seatReservation.setFlightDesignator("AA123");
        seatReservation.setFlightDate(LocalDate.now());
        seatReservation.setFirstName("John");
        seatReservation.setLastName("Doe");
        seatReservation.setNumberOfBags(1);

        // Controls
        flightDesignatorField = new TextField();
        flightDatePicker = new DatePicker();
        firstNameField = new TextField();
        lastNameField = new TextField();
        bagsField = new TextField();
        infantCheckBox = new CheckBox();
        insuranceCheckBox = new CheckBox();

        passengersField = new TextField("1");
        passengersField.setEditable(false);

        // Layout
        GridPane grid = new GridPane();
        grid.setPadding(new Insets(10));
        grid.setHgap(10);
        grid.setVgap(10);

        grid.add(new Label("Flight designator:"), 0, 0);
        grid.add(flightDesignatorField, 1, 0);

        grid.add(new Label("Flight date:"), 0, 1);
        grid.add(flightDatePicker, 1, 1);

        grid.add(new Label("First name:"), 0, 2);
        grid.add(firstNameField, 1, 2);

        grid.add(new Label("Last name:"), 0, 3);
        grid.add(lastNameField, 1, 3);

        grid.add(new Label("Bags:"), 0, 4);
        grid.add(bagsField, 1, 4);

        grid.add(new Label("Flying with infant?"), 0, 5);
        grid.add(infantCheckBox, 1, 5);

        grid.add(new Label("Travel insurance?"), 0, 6);
        grid.add(insuranceCheckBox, 1, 6);

        grid.add(new Label("Number of passengers:"), 0, 7);
        grid.add(passengersField, 1, 7);

        // Buttons
        Button cancelButton = new Button("Cancel");
        Button saveButton = new Button("Save");

        HBox buttonBox = new HBox(10, cancelButton, saveButton);
        buttonBox.setAlignment(Pos.TOP_RIGHT);
        buttonBox.setPadding(new Insets(10));

        BorderPane root = new BorderPane();
        root.setCenter(grid);
        root.setBottom(buttonBox);

        // Events
        infantCheckBox.setOnAction(e -> {
            if (infantCheckBox.isSelected()) {
                passengersField.setText("2");
            } else {
                passengersField.setText("1");
            }
        });

        cancelButton.setOnAction(e -> {
            System.out.println("Cancel clicked");
            Platform.exit();
        });

        saveButton.setOnAction(e -> {
            try {
                seatReservation.setFlightDesignator(flightDesignatorField.getText());
                seatReservation.setFlightDate(flightDatePicker.getValue());
                seatReservation.setFirstName(firstNameField.getText());
                seatReservation.setLastName(lastNameField.getText());
                seatReservation.setNumberOfBags(Integer.parseInt(bagsField.getText()));

                if (infantCheckBox.isSelected()) {
                    seatReservation.makeFlyingWithInfant();
                } else {
                    seatReservation.makeNotFlyingWithInfant();
                }

                if (insuranceCheckBox.isSelected()) {
                    seatReservation.makeFlyingWithTravelInsurance();
                } else {
                    seatReservation.makeNotFlyingWithTravelInsurance();
                }

                System.out.println(seatReservation);
                Platform.exit();

            } catch (IllegalArgumentException ex) {
                System.out.println("Error: " + ex.getMessage());
            }
        });

        updateUI();

        stage.setTitle(seatReservation.getFirstName() + " " +
                seatReservation.getLastName() + "'s Seat Reservation App");

        stage.setScene(new Scene(root, 400, 420));
        stage.show();
    }

    private void updateUI() {
        flightDesignatorField.setText(seatReservation.getFlightDesignator());
        flightDatePicker.setValue(seatReservation.getFlightDate());
        firstNameField.setText(seatReservation.getFirstName());
        lastNameField.setText(seatReservation.getLastName());
        bagsField.setText(String.valueOf(seatReservation.getNumberOfBags()));
        infantCheckBox.setSelected(seatReservation.isFlyingWithInfant());
        insuranceCheckBox.setSelected(seatReservation.hasTravelInsurance());
    }

    public static void main(String[] args) {
        launch(args);
    }
}
