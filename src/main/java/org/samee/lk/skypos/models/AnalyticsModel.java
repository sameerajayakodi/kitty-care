package org.samee.lk.skypos.models;

import org.samee.lk.skypos.tm.LowStockTM;
import org.samee.lk.skypos.tm.OverStockTM;
import org.samee.lk.skypos.tm.RecentOrderTM;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnalyticsModel {


    public static int[] loadAnalytics() throws SQLException, ClassNotFoundException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care", "root", "acpt");


        int totalOrders = 0;
        int totalRevenue = 0;
        int itemsSold = 0;
        int totalItems = 0;


        String totalOrdersQuery = "SELECT COUNT(*) AS total_orders FROM orders";
        String totalRevenueQuery = "SELECT SUM(amount) AS total_revenue FROM orders";
        String itemsSoldQuery = "SELECT SUM(qty) AS items_sold FROM order_detail";
        String totalItemsQuery = "SELECT COUNT(DISTINCT id) AS total_items FROM item";


        PreparedStatement preparedStatement = connection.prepareStatement(totalOrdersQuery);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            totalOrders = resultSet.getInt("total_orders");
        }


        preparedStatement = connection.prepareStatement(totalRevenueQuery);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            totalRevenue = resultSet.getInt("total_revenue");
        }


        preparedStatement = connection.prepareStatement(itemsSoldQuery);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            itemsSold = resultSet.getInt("items_sold");
        }


        preparedStatement = connection.prepareStatement(totalItemsQuery);
        resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            totalItems = resultSet.getInt("total_items");
        }


        resultSet.close();
        preparedStatement.close();
        connection.close();


        return new int[]{totalOrders, totalRevenue, itemsSold, totalItems};
    }



    public static Map<String, Double> loadSalesInsights() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care", "root", "acpt");
        Map<String, Double> salesData = new LinkedHashMap<>();

        String query = "SELECT DATE(order_date) AS order_date, SUM(amount) AS total_revenue " +
                "FROM orders " +
                "WHERE amount > 0 " +
                "GROUP BY DATE(order_date) " +
                "ORDER BY DATE(order_date) ASC";


        PreparedStatement preparedStatement = connection.prepareStatement(query);
         ResultSet resultSet = preparedStatement.executeQuery();

        while (resultSet.next()) {
            String date = resultSet.getString("order_date");
            double revenue = resultSet.getDouble("total_revenue");
            salesData.put(date, revenue);
        }

        return salesData;
    }


    public static ArrayList<RecentOrderTM> getRecentOrders() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care", "root", "acpt");
        String query = "SELECT * FROM orders ORDER BY order_date DESC LIMIT 5";
        ArrayList<RecentOrderTM> recentOrders = new ArrayList<>();

        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {

                Timestamp orderDate = rs.getTimestamp("order_date");


                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                String formattedDate = sdf.format(orderDate);


                recentOrders.add(new RecentOrderTM(formattedDate, rs.getDouble("amount")));
            }
        }
        return recentOrders;
    }


    public static List<LowStockTM> getLowStockItems() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care", "root", "acpt");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM item WHERE qty > 0 AND qty < 20");
      ResultSet resultSet =   preparedStatement.executeQuery();
      List<LowStockTM> lowStockItems = new ArrayList<>();
      while (resultSet.next()) {
          lowStockItems.add(new LowStockTM(resultSet.getString("name")));
      }

return  lowStockItems;
    }

    public static List<OverStockTM> getOverStockItems() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care", "root", "acpt");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT name FROM item WHERE qty = 0");
        ResultSet resultSet =   preparedStatement.executeQuery();
        List<OverStockTM> overStockItems = new ArrayList<>();
        while (resultSet.next()) {
            overStockItems.add(new OverStockTM(resultSet.getString("name")));
        }
    return  overStockItems;
    }
}
