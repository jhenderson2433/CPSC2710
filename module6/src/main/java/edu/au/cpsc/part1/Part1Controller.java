/*
 * Project: Module 6 - Part 1 (Flight Part1 App)
 * Author: Javontea Henderson
 * Email: jdh0204@auburn.edu
 * Date: 2026-02-15
 */

package edu.au.cpsc.part1;

import javafx.beans.binding.Bindings;
import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.fxml.Initializable;


import java.net.URL;
import java.util.ResourceBundle;

public class Part1Controller implements Initializable {


    @FXML
    private TextField messageTextField, echoTextField, firstBidirectionalTextField, secondBidirectionalTextField;

    @FXML
    private ImageView secretOverlayImageView;

    @FXML
    private Slider secretSlider;

    @FXML
    private CheckBox selectMeCheckBox;

    @FXML
    private Label selectMeLabel;

    @FXML
    private TextField tweetTextField;

    @FXML
    private Label numberOfCharactersLabel, validityLabel;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        // 1) messageTextField -> echoTextField (one-way)
        echoTextField.textProperty().bind(messageTextField.textProperty());

        // 2) bi-directional binding between first and second text fields
        firstBidirectionalTextField.textProperty()
                .bindBidirectional(secondBidirectionalTextField.textProperty());

        // 3) slider controls opacity of image
        secretOverlayImageView.opacityProperty().bind(secretSlider.valueProperty());

        // 4) checkbox selected state shown as true/false in label
        selectMeLabel.textProperty().bind(selectMeCheckBox.selectedProperty().asString());

        // 5) number of characters in tweetTextField
        numberOfCharactersLabel.textProperty()
                .bind(tweetTextField.textProperty().length().asString());

        // 6) validity label: "Valid" if <= 10 chars else "Invalid"
        validityLabel.textProperty().bind(
                javafx.beans.binding.Bindings
                        .when(tweetTextField.textProperty().length().lessThanOrEqualTo(10))
                        .then("Valid")
                        .otherwise("Invalid")
        );
    }
}