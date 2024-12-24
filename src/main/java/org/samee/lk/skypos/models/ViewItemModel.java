package org.samee.lk.skypos.models;

import org.samee.lk.skypos.tm.ItemTM;

import java.sql.*;
import java.util.ArrayList;

public class ViewItemModel {
    public static ArrayList<ItemTM> SearchItem(String searchName) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care", "root", "acpt");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, category, qty, price FROM item WHERE id like ? OR name LIKE ? OR category LIKE ?");
        preparedStatement.setString(1, "%" + searchName + "%");
        preparedStatement.setString(2, "%" + searchName + "%");
        preparedStatement.setString(3, "%" + searchName + "%");
        ResultSet resultSet = preparedStatement.executeQuery();
        ArrayList<ItemTM> tms = new ArrayList<>();
        while (resultSet.next()) {
            tms.add(new ItemTM(resultSet.getInt(1), resultSet.getString(2), resultSet.getString(3), resultSet.getInt(4), resultSet.getDouble(5)));
        }
        return tms;
    }

}
