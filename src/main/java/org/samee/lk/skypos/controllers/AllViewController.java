package org.samee.lk.skypos.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import org.samee.lk.skypos.models.ViewItemModel;
import org.samee.lk.skypos.tm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AllViewController implements Initializable {

    public Button removeItem;
    public Button addItems;
    public Button logout;
    public Button viewItem;
    public Button analytics;

    public Label datetime;







    public Label itemNameLbl;
    public Label categoryLbl;
    public Label qtyLbl;
    public Label priceLbl;
    public Button checkoutPageloadBtn;
    public Button updateItem;
    public TableView<ItemTM> itemsTable;
    public TextField idInput;
    public Label idLbl;
    public TextField nameInput;
    ItemTM itemTM;

    String formatted;


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
                itemTM = itemsTable.getSelectionModel().getSelectedItem();
                try {
                    loadUpdatePage(itemTM);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

    }


    private void loadUpdatePage(ItemTM item) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/samee/lk/skypos/update-view/update-view.fxml"));
        Parent updatePageRoot = loader.load();


        UpdateController updateController = loader.getController();
        updateController.setItemData(item);


        Stage stage = (Stage) itemsTable.getScene().getWindow();
        stage.setScene(new Scene(updatePageRoot));
        stage.show();
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

    public void searchItem(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String itemName = nameInput.getText();
        ArrayList<ItemTM> itemTMS = ViewItemModel.SearchItem(itemName);
        itemsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        itemsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("category"));
        itemsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        itemsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));



        itemsTable.setItems(FXCollections.observableArrayList(itemTMS));
    }

    public void updateItemLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/update-view/update-view.fxml"));
        Stage stage = (Stage) updateItem.getScene().getWindow();
        stage.setScene(new Scene(mainPageRoot));
        stage.show();
    }
}
