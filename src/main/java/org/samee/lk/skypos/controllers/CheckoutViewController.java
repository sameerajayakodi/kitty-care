package org.samee.lk.skypos.controllers;

import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.samee.lk.skypos.dto.ItemDTO;
import org.samee.lk.skypos.dto.OrderDTO;
import org.samee.lk.skypos.dto.OrderDetailDTO;
import org.samee.lk.skypos.models.ItemModel;
import org.samee.lk.skypos.models.OrderModel;
import org.samee.lk.skypos.tm.ItemTM;
import org.samee.lk.skypos.tm.OrderTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;
public class CheckoutViewController implements Initializable {

    public TableView<ItemTM> itemsTable;
    public Label itemNameLbl;
    public Label categoryLbl;
    public Label qtyLbl;
    public Label priceLbl;
    public TextField idInput;

    public TableView<OrderTM> orderDetailTable;
    public ArrayList<OrderTM> ordersDetails;
    public Label datetime;
    public TextField orderQtyInput;
    public Label totalPriceLbl;
    public Button logout;
    public Button removeItem;
    public Button updateItem;
    public Button addItems;
    public Button viewItem;
    public Button analytics;
    private double subTotal = 0;
    String formatted;
    ArrayList<OrderDetailDTO> orderDetailsDto = null;
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
//Date and Time

        LocalDateTime current = LocalDateTime.now();

        // Format the date and time
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatted = current.format(formatter);

        datetime.setText(formatted);

    ArrayList<ItemTM> items = new ArrayList<>();
        try {
            items = ItemModel.loadItems();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException(e);
        }

        itemsTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("id"));
        itemsTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("name"));
        itemsTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("category"));
        itemsTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qty"));
        itemsTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("price"));



        itemsTable.setItems(FXCollections.observableArrayList(items));
        ordersDetails= new ArrayList<>();

        orderDetailsDto = new ArrayList<>();
        orderDetailTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemName"));
        orderDetailTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("category"));
        orderDetailTable.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("orderQty"));
        orderDetailTable.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("itemPrice"));
        orderDetailTable.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
    }



    public void searchItem(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
       if( !idInput.getText().isEmpty()){
           int id = Integer.parseInt(idInput.getText());

           if(id>0 ){
               ItemDTO itemDTO =  ItemModel.searchById(id);
               itemNameLbl.setText(itemDTO.getName());
               categoryLbl.setText(itemDTO.getCategory());
               qtyLbl.setText(String.valueOf(itemDTO.getQty()));
               priceLbl.setText(String.valueOf(itemDTO.getPrice()));
           }
       }else {
           showErrorAlert("Please enter a valid ID");
       }





    }

    public void addonCart(ActionEvent actionEvent) {
        if(!orderQtyInput.getText().isEmpty()){
            int itemID = Integer.parseInt(idInput.getText());
            String itemName = itemNameLbl.getText();
            String category = categoryLbl.getText();
            int avilableQuantity = Integer.parseInt(qtyLbl.getText());
            int qty = Integer.parseInt(orderQtyInput.getText());
            if(qty>0 && qty<=avilableQuantity){
                double price = Double.parseDouble(priceLbl.getText());
                double total = qty * price;
                OrderTM order = new OrderTM(itemName, category, qty, price, total);
                ordersDetails.add(order);
                orderDetailsDto.add(new OrderDetailDTO(itemID,qty ,total));
                int newQty = Integer.parseInt(qtyLbl.getText()) - qty;;
                qtyLbl.setText(String.valueOf(newQty));
                subTotal += total;
                totalPriceLbl.setText(String.valueOf(subTotal));

                orderDetailTable.setItems(FXCollections.observableArrayList(ordersDetails));
            }else {
                showErrorAlert("Please enter valid order quantity");
            }


        }else {
            showErrorAlert("Please enter valid order quantity");
        }

    }

    public void checkoutAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {

        OrderDTO orderDTO = new OrderDTO(formatted,subTotal,orderDetailsDto);

        boolean status = OrderModel.checkout(orderDTO);
        if (status){

            refreshTable();
            showSuccessAlert("order checked out successfully");
        }
        showErrorAlert("order checked out failed");
        System.out.println("checkoutButton");

    }

    public void cancelOrder(ActionEvent actionEvent) {
        showSuccessAlert("Order cancelled");
    }

    private void refreshTable() {
        try {
            ArrayList<ItemTM> tms = ItemModel.loadItems();
            itemsTable.setItems(FXCollections.observableArrayList(tms));
        } catch (ClassNotFoundException | SQLException e) {
           showErrorAlert("Error refreshing table: " + e.getMessage());
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


    public void logoutPageLoad(ActionEvent actionEvent) {

    }

    public void removeItemLoad(ActionEvent actionEvent) {
    }

    public void updateItemLoad(ActionEvent actionEvent) {
    }

    public void addItemsLoad(ActionEvent actionEvent) {
    }

    public void viewItemPageLoad(ActionEvent actionEvent) {
    }

    public void analyticsLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/analytics-view/analytics-view.fxml"));
        Stage stage = (Stage) analytics.getScene().getWindow();

        stage.setScene(new Scene(mainPageRoot));

        stage.show();
    }
}
