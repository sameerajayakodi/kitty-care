package org.samee.lk.skypos.controllers;

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
import org.samee.lk.skypos.models.ViewItemModel;
import org.samee.lk.skypos.tm.ItemTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddViewController implements Initializable {
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
    public TextField itemNameInput;
    public TextField quantityInput;
    public TextField searchInput;

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


    public void logoutPageLoad(ActionEvent actionEvent) {
    }

    public void updateItemLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/update-view/update-view.fxml"));
        Stage stage = (Stage) updateItem.getScene().getWindow();
        stage.setScene(new Scene(mainPageRoot));
        stage.show();
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

    public void addItemAction(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/add-view/add-view.fxml"));
        Stage stage = (Stage) addItems.getScene().getWindow();

        stage.setScene(new Scene(mainPageRoot));

        stage.show();
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

        searchInput.textProperty().addListener((observable, oldValue, newValue) -> {
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

    public void searchItem(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        String itemName = searchInput.getText();
        ArrayList<ItemTM> itemTMS = ViewItemModel.SearchItem(itemName);
        itemsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        itemsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("category"));
        itemsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        itemsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));



        itemsTable.setItems(FXCollections.observableArrayList(itemTMS));
    }
}
