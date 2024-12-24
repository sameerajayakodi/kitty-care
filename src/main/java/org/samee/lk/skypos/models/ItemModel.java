package org.samee.lk.skypos.models;



import org.samee.lk.skypos.db.DB;
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

    public static boolean updateItem(ItemDTO itemDTO) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care","root", "acpt");
        PreparedStatement preparedStatement = connection.prepareStatement("UPDATE item SET name = ?,category = ?, qty = ?, price = ? WHERE id = ?");
        preparedStatement.setObject(1,itemDTO.getName());
        preparedStatement.setObject(2,itemDTO.getCategory());
        preparedStatement.setObject(3,itemDTO.getQty());
        preparedStatement.setObject(4,itemDTO.getPrice());
        preparedStatement.setInt(5,itemDTO.getId());
        int i = preparedStatement.executeUpdate();
        if (i > 0) {
            return true;
        }else {
            return false;
        }
    }

    public static Object[] removeItem(ItemDTO itemDTO) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care", "root", "acpt");
        PreparedStatement checkStmt = connection.prepareStatement("SELECT COUNT(*) FROM order_detail WHERE iid = ?");
        checkStmt.setObject(1, itemDTO.getId());
        ResultSet rs = checkStmt.executeQuery();
        if (rs.next() && rs.getInt(1) > 0) {
            return new Object[]{false, "Item cannot be deleted as it is referenced in the Order Details."};
        }
        PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM item WHERE id = ?");
        deleteStmt.setObject(1, itemDTO.getId());
        int rowsAffected = deleteStmt.executeUpdate();
        if (rowsAffected > 0) {
            return new Object[]{true, "Item deleted successfully."};
        } else {
            return new Object[]{false, "Item deletion failed. The item might not exist."};
        }
    }

    public static boolean addNewItem(ItemDTO itemDTO) throws ClassNotFoundException, SQLException {
        boolean status = false;
        Connection connection = DB.getDbConnection();
        String sql = "INSERT INTO item (name, category, qty, price) VALUES (?, ?, ?, ?)";
        PreparedStatement preparedStatement = connection.prepareStatement(sql);

        preparedStatement.setString(1, itemDTO.getName());
        preparedStatement.setString(2, itemDTO.getCategory());
        preparedStatement.setInt(3, itemDTO.getQty());
        preparedStatement.setDouble(4, itemDTO.getPrice());

        int i= preparedStatement.executeUpdate();
        if (i > 0) {
            status = true;
        }else {
            status = false;
        }
return status;
    }




}
