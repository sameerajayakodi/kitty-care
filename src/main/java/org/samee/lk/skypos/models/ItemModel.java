package org.samee.lk.skypos.models;



import org.samee.lk.skypos.dto.ItemDTO;
import org.samee.lk.skypos.dto.OrderDTO;
import org.samee.lk.skypos.tm.ItemTM;

import java.sql.*;
import java.util.ArrayList;

public class ItemModel {
    public  static ArrayList<ItemTM> loadItems() throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care","root", "acpt");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, category, qty, price FROM item");
        ResultSet resultSet =  preparedStatement.executeQuery();
        ArrayList<ItemTM> tms = new ArrayList();
        while (resultSet.next()) {
            tms.add(new ItemTM(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getInt(4),resultSet.getDouble(5)));
        }

        return tms;


    }
    public static ItemDTO searchById(int id) throws ClassNotFoundException, SQLException {
        ItemDTO itemDTO = null;
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care","root", "acpt");
        PreparedStatement preparedStatement = connection.prepareStatement("SELECT id, name, category, qty, price FROM item where id = ?");
        preparedStatement.setInt(1, id);
        ResultSet resultSet = preparedStatement.executeQuery();
        if (resultSet.next()) {
            itemDTO = new ItemDTO(resultSet.getInt(1),resultSet.getString(2),resultSet.getString(3),resultSet.getInt(4),resultSet.getDouble(5));
        }
        return itemDTO;
    }



}
