package org.samee.lk.skypos;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;

import java.awt.event.ActionEvent;
import java.net.URL;
import java.util.ResourceBundle;

public class ProductAddViewController implements Initializable {

    @FXML
    public ChoiceBox<String> choiceBox;
    public Label myLabel;

    private String[]  category = {"Pet Item","Pet Food","Medicine"};

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        choiceBox.getItems().addAll(category);
        choiceBox.setOnAction(this::getCategory);
    }

    private void getCategory(javafx.event.ActionEvent actionEvent) {
        String category = choiceBox.getValue();
        myLabel.setText(category);
    }




}
