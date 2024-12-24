package org.samee.lk.skypos.controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.samee.lk.skypos.dto.ItemDTO;
import org.samee.lk.skypos.models.ItemModel;
import org.samee.lk.skypos.models.ViewItemModel;
import org.samee.lk.skypos.tm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    public Button logout;
    public Button removeItem;
    public Button addItems;
    public Button viewItem;
    public Button analytics;
    public Button checkoutPageloadBtn;
    public Label datetime;
    public TableView<ItemTM> itemsTable;
    public Button updateItem;
    public TextField nameInput;
    public Label itemId;
    @FXML
    private TextField idInput;
    @FXML
    private TextField tonameInput;
    @FXML
    private TextField categoryInput;
    @FXML
    private TextField qtyInput;
    @FXML
    private TextField priceInput;
    ItemTM itemTM;
    String formatted;
    public void setItemData(ItemTM item) {
        itemId.setText(String.valueOf(item.getId()));
       tonameInput.setText(item.getName());
        categoryInput.setText(item.getCategory());
        qtyInput.setText(String.valueOf(item.getQty()));
        priceInput.setText(String.valueOf(item.getPrice()));
    }
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        LocalDateTime current = LocalDateTime.now();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatted = current.format(formatter);

        datetime.setText(formatted);


        itemsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        itemsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("category"));
        itemsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        itemsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));

        nameInput.textProperty().addListener((observable, oldValue, newValue) -> {
            try {
                ArrayList<ItemTM> itemTMS = ViewItemModel.SearchItem(newValue);
                itemsTable.setItems(FXCollections.observableArrayList(itemTMS));
            } catch (ClassNotFoundException | SQLException e) {
                e.printStackTrace();
            }
        });

        try {
            ArrayList<ItemTM> itemTMS = ViewItemModel.SearchItem("");
            itemsTable.setItems(FXCollections.observableArrayList(itemTMS));
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }






        itemsTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1 && !itemsTable.getSelectionModel().isEmpty()) {
                itemTM = (ItemTM) itemsTable.getSelectionModel().getSelectedItem();
                itemId.setText(String.valueOf(itemTM.getId()));
                tonameInput.setText(itemTM.getName());
                categoryInput.setText(itemTM.getCategory());
                qtyInput.setText(String.valueOf(itemTM.getQty()));
                priceInput.setText(String.valueOf(itemTM.getPrice()));

            }
        });

    }


    public void saveItem(ActionEvent actionEvent) {
    }

    public void cancelUpdate(ActionEvent actionEvent) {
    }

    public void logoutPageLoad() {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to logout or Cancel to stay.");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/assets/confirmAlert.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                Parent loginPageRoot = null;
                try {
                    loginPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/login-view/login-view.fxml"));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                Stage stage = (Stage) logout.getScene().getWindow();
                stage.setScene(new Scene(loginPageRoot));
                stage.show();
            }
        });
    }

    public void removeItemLoad(ActionEvent actionEvent) {
    }

    public void addItemsLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/add-view/add-view.fxml"));
        Stage stage = (Stage) addItems.getScene().getWindow();
        stage.setScene(new Scene(mainPageRoot));
        stage.show();
    }

    public void viewItemPageLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/all-view/all-view.fxml"));
        Stage stage = (Stage) viewItem.getScene().getWindow();
        stage.setScene(new Scene(mainPageRoot));
        stage.show();
    }

    public void analyticsLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/analytics-view/analytics-view.fxml"));
        Stage stage = (Stage) analytics.getScene().getWindow();
        stage.setScene(new Scene(mainPageRoot));
        stage.show();
    }

    public void checkoutPageLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/checkout-view/checkout-view.fxml"));
        Stage stage = (Stage) checkoutPageloadBtn.getScene().getWindow();

        stage.setScene(new Scene(mainPageRoot));

        stage.show();
    }

    public void searchItem(ActionEvent actionEvent) {
    }

    public void updateItemLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/update-view/update-view.fxml"));
        Stage stage = (Stage) updateItem.getScene().getWindow();
        stage.setScene(new Scene(mainPageRoot));
        stage.show();
    }


    public void updateItemAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        boolean status = ItemModel.updateItem(new ItemDTO(Integer.parseInt(itemId.getText()), tonameInput.getText(), categoryInput.getText(), Integer.parseInt(qtyInput.getText()), Double.parseDouble(priceInput.getText())));
        if (status){
            showSuccessAlert(tonameInput.getText()+" Updated Successfully");
            itemId.setText("");
            tonameInput.setText("");
            categoryInput.setText("");
            qtyInput.setText("");
            priceInput.setText("");
            refreshItemTable();
        }else {
            showErrorAlert(tonameInput.getText()+" Update Failed!");
        }
    }



    public  void showSuccessAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION, message, ButtonType.OK);

        alert.getDialogPane().getStylesheets().add(getClass().getResource("/assets/successAlert.css").toExternalForm());
        alert.setTitle("Success");
        alert.setHeaderText("Successful!");

        alert.show();
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> alert.close());
        pause.play();
    }

    public  void showErrorAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR, message, ButtonType.OK);
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/assets/errorAlert.css").toExternalForm());
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.show();
        PauseTransition pause = new PauseTransition(Duration.seconds(2));
        pause.setOnFinished(event -> alert.close());
        pause.play();
    }

    private void refreshItemTable() {
        try {
            ArrayList<ItemTM> tms = ItemModel.loadItems();
            itemsTable.setItems(FXCollections.observableArrayList(tms));
        } catch (ClassNotFoundException | SQLException e) {
            showErrorAlert("Error refreshing table: " + e.getMessage());
        }
    }

    public void removeItemAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Logout Confirmation");
        alert.setHeaderText("Are you sure you want to logout?");
        alert.setContentText("Click OK to logout or Cancel to stay.");
        alert.getDialogPane().getStylesheets().add(getClass().getResource("/assets/confirmAlert.css").toExternalForm());
        alert.getDialogPane().getStyleClass().add("dialog-pane");
        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                int id = Integer.parseInt(itemId.getText());
                boolean status = false;
                try {
                    status = ItemModel.removeItem(id);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                if(status){
                    showSuccessAlert("Item removed successfully");
                    refreshItemTable();
                }else{
                    showErrorAlert("Item removal Failed!");
                }
            }
        });






    }

}
