package org.samee.lk.skypos.controllers;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.animation.PauseTransition;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
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
import org.samee.lk.skypos.models.ViewItemModel;
import org.samee.lk.skypos.tm.ItemTM;
import org.samee.lk.skypos.tm.OrderTM;


import java.awt.*;
import java.awt.print.PrinterException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javax.swing.*;

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
    public TextField nameInput;
    private double subTotal = 0;
    ItemTM itemTM;
    String formatted;
    ArrayList<OrderDetailDTO> orderDetailsDto = null;
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
        itemsTable.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.isPrimaryButtonDown() && mouseEvent.getClickCount() == 1) {
                    // Get the selected item as an ItemDTO
                   itemTM= itemsTable.getSelectionModel().getSelectedItem();


                    idInput.setText(String.valueOf(itemTM.getId()));
                    itemNameLbl.setText(itemTM.getName());
                    categoryLbl.setText(itemTM.getCategory());
                    qtyLbl.setText(String.valueOf(itemTM.getQty()));
                    priceLbl.setText(String.valueOf(itemTM.getPrice()));
                }
            }
        });

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
        if(!orderQtyInput.getText().isEmpty() && !itemNameLbl.getText().isEmpty() && !categoryLbl.getText().isEmpty() && !qtyLbl.getText().isEmpty() && !priceLbl.getText().isEmpty()){

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
                itemNameLbl.setText("");
                categoryLbl.setText("");
                qtyLbl.setText("");
                priceLbl.setText("");

                idInput.clear();
                orderQtyInput.clear();
                orderQtyInput.clear();

               

            }else {
                showErrorAlert("Please enter valid order quantity");
            }


        }else {
            showErrorAlert("Please enter valid order quantity");
        }

    }

    public void checkoutAction(ActionEvent actionEvent) throws SQLException, ClassNotFoundException {
        OrderDTO orderDTO = new OrderDTO(formatted, subTotal, orderDetailsDto);

        if (!orderDetailsDto.isEmpty()) {
            boolean status = OrderModel.checkout(orderDTO);
            if (status) {
                printReceipt();
                clearCheckoutForm();
                refreshItemTable();
                showSuccessAlert("Order checked out successfully");
            } else {
                showErrorAlert("Order checkout failed");
            }
        } else {
            showErrorAlert("Checkout failed - cart is empty");
        }
    }

    private void printReceipt() {
        String receiptContent = generateReceiptContent();
        saveReceipt(receiptContent);
        printToDefaultPrinter(receiptContent);
    }

    private String generateReceiptContent() {
        StringBuilder receipt = new StringBuilder();
        receipt.append("\n");
        receipt.append("          SKY POS SYSTEM          \n");
        receipt.append("     No. 123, Main Street, City   \n");
        receipt.append("       Tel: +94 11 2223334        \n");
        receipt.append("     Email: skypos@gmail.com      \n");
        receipt.append("\n");
        receipt.append("────────────────────────────────────\n");
        receipt.append("Receipt No: ").append(System.currentTimeMillis() % 10000).append("\n");
        receipt.append("Date: ").append(formatted).append("\n");
        receipt.append("Time: ").append(LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"))).append("\n");
        receipt.append("────────────────────────────────────\n");
        receipt.append("\n");
        receipt.append(String.format("%-4s %-20s %4s %8s\n", "No.", "Item", "Qty", "Amount"));
        receipt.append("────────────────────────────────────\n");

        int itemNo = 1;
        for (OrderTM order : ordersDetails) {
            receipt.append(String.format("%-4d %-20s %4d %8.2f\n",
                    itemNo++,
                    truncateString(order.getItemName(), 20),
                    order.getOrderQty(),
                    order.getTotalPrice()));
        }

        receipt.append("\n");
        receipt.append("────────────────────────────────────\n");
        receipt.append(String.format("%-29s %8.2f\n", "Sub Total:", subTotal));
        receipt.append(String.format("%-29s %8.2f\n", "Discount (0%):", 0.00));
        receipt.append(String.format("%-29s %8.2f\n", "Net Total:", subTotal));
        receipt.append("────────────────────────────────────\n");
        receipt.append("\n");
        receipt.append("          Thank You!               \n");
        receipt.append("       Please Come Again!          \n");
        receipt.append("\n");
        receipt.append("     Software By: Your Company     \n");
        receipt.append("────────────────────────────────────\n");

        return receipt.toString();
    }

    private String truncateString(String str, int length) {
        if (str.length() > length) {
            return str.substring(0, length - 3) + "...";
        }
        return str;
    }

    private void saveReceipt(String content) {
        String fileName = "receipts/receipt-" + System.currentTimeMillis() + ".txt";
        File directory = new File("receipts");
        if (!directory.exists()) {
            directory.mkdir();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(fileName))) {
            writer.print(content);
        } catch (IOException e) {
            showErrorAlert("Error saving receipt: " + e.getMessage());
        }
    }

    private void printToDefaultPrinter(String content) {
        JTextArea textArea = new JTextArea(content);
        textArea.setFont(new Font("Monospaced", Font.PLAIN, 10));
        try {
            textArea.print();
        } catch (PrinterException e) {
            showErrorAlert("Error printing receipt: " + e.getMessage());
        }
    }

    private void clearCheckoutForm() {
        itemNameLbl.setText("");
        categoryLbl.setText("");
        qtyLbl.setText("");
        priceLbl.setText("");
        ordersDetails.clear();
        orderDetailTable.setItems(FXCollections.observableArrayList(ordersDetails));
        idInput.clear();
        orderQtyInput.clear();
        subTotal = 0;
        totalPriceLbl.setText(String.valueOf(subTotal));
    }
    public void cancelOrder(ActionEvent actionEvent) {

        ordersDetails.clear();
        orderDetailsDto.clear();


        subTotal = 0;
        totalPriceLbl.setText(String.valueOf(subTotal));


        orderDetailTable.setItems(FXCollections.observableArrayList(ordersDetails));


        idInput.clear();
        orderQtyInput.clear();
        itemNameLbl.setText("");
        categoryLbl.setText("");
        qtyLbl.setText("");
        priceLbl.setText("");


        showSuccessAlert("Order cancelled successfully");
    }

    private void refreshItemTable() {
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

    public void refreshItemName(ActionEvent actionEvent) {
        nameInput.setText("");
    }
}
