package org.samee.lk.skypos.controllers;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.samee.lk.skypos.models.AnalyticsModel;
import org.samee.lk.skypos.tm.LowStockTM;
import org.samee.lk.skypos.tm.OverStockTM;
import org.samee.lk.skypos.tm.RecentOrderTM;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class AnalyticsViewController implements Initializable {
    public Button logout;
    public Button removeItem;
    public Button updateItem;
    public Button addItems;
    public Button viewItem;
    public Button analytics;
    public Button checkoutpageloadBtn;
    public Label datetime;
    public Label totalOrders;
    public TableView itemsTable;
    public Label totalRevenue;
    public Label totalItems;
    public Label itemsSold;
    public LineChart salesLineChart;
    public TableView<RecentOrderTM> recentOrderTable;
    public TextField idInput;
    public Label itemNameLbl;
    public Label categoryLbl;
    public Label qtyLbl;
    public Label priceLbl;
    public Label idLbl;
    String formatted;
    public TableView <LowStockTM>lowStockTable;
    public TableView<OverStockTM> overStockTable;


    public void logoutPageLoad(ActionEvent actionEvent) {
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

    public void analyticsLoad(ActionEvent actionEvent) {
    }

    public void checkoutPageLoad(ActionEvent actionEvent) throws IOException {
        Parent mainPageRoot = FXMLLoader.load(getClass().getResource("/org/samee/lk/skypos/checkout-view/checkout-view.fxml"));
        Stage stage = (Stage) checkoutpageloadBtn.getScene().getWindow();

        stage.setScene(new Scene(mainPageRoot));

        stage.show();
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        LocalDateTime current = LocalDateTime.now();


        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        formatted = current.format(formatter);

        datetime.setText(formatted);
        try {

            int[] analyticsData = AnalyticsModel.loadAnalytics();


            totalOrders.setText(String.valueOf(analyticsData[0]));
            totalRevenue.setText(String.valueOf(analyticsData[1]));
            itemsSold.setText(String.valueOf(analyticsData[2]));
            totalItems.setText(String.valueOf(analyticsData[3]));


            Map<String, Double> salesData = AnalyticsModel.loadSalesInsights();


            LineChart.Series<String, Number> series = new LineChart.Series<>();
            series.setName("Revenue");


            for (Map.Entry<String, Double> entry : salesData.entrySet()) {
                series.getData().add(new LineChart.Data<>(entry.getKey(), entry.getValue()));
            }


            salesLineChart.getData().add(series);

            List<RecentOrderTM> recentOrders = AnalyticsModel.getRecentOrders();

            recentOrderTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderDate"));
            recentOrderTable.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("amount"));
            recentOrderTable.setItems(FXCollections.observableArrayList(recentOrders));



            List<LowStockTM> lowStockTMS = AnalyticsModel.getLowStockItems();
            lowStockTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("lowItemName"));
            lowStockTable.setItems(FXCollections.observableArrayList(lowStockTMS));



            List<OverStockTM> overStockTMS = AnalyticsModel.getOverStockItems();
            overStockTable.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("overItemName"));
            overStockTable.setItems(FXCollections.observableArrayList(overStockTMS));

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public void searchItem(ActionEvent actionEvent) {
    }
}
