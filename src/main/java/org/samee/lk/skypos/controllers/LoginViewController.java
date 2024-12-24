package org.samee.lk.skypos.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginViewController {
    public TextField usernameField;
    public PasswordField passwordField;
    public Button loginButton;
    public Label errorLbl;

    public void handleLogin(ActionEvent actionEvent) throws IOException {

        errorLbl.setText("");
        errorLbl.setTextFill(Color.RED);


        if (!usernameField.getText().isEmpty() && !passwordField.getText().isEmpty()) {
            if (usernameField.getText().equals("admin") && passwordField.getText().equals("admin")) {

                errorLbl.setText("Login successful!");
                errorLbl.setTextFill(Color.GREEN);


                Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/checkout-view/checkout-view.fxml"));
                Stage stage = (Stage) loginButton.getScene().getWindow();
                stage.setScene(new Scene(mainPageRoot));
                stage.show();
                return;
            } else {

                errorLbl.setText("Invalid username or password");
            }
        } else {

            errorLbl.setText("Please fill in all fields");
        }


        errorLbl.setTextFill(Color.RED);
    }
}
