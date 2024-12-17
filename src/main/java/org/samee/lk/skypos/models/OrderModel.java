package org.samee.lk.skypos.models;

import org.samee.lk.skypos.dto.OrderDTO;
import org.samee.lk.skypos.dto.OrderDetailDTO;

import java.sql.*;

public class OrderModel {
    public static boolean checkout(OrderDTO orderDTO) throws ClassNotFoundException, SQLException {

        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/kitty_care","root", "acpt");
        connection.setAutoCommit(false);
        PreparedStatement preparedStatement = connection.prepareStatement("insert into orders (amount) values (?)", Statement.RETURN_GENERATED_KEYS);

        preparedStatement.setObject(1,orderDTO.getOrderAmount());

        int i = preparedStatement.executeUpdate();
        if (i >= 0) {
            ResultSet generatedKeys = preparedStatement.getGeneratedKeys();
            if (generatedKeys.next()) {
                int id = generatedKeys.getInt(1);

                for(OrderDetailDTO dto:orderDTO.getOrderDetailDTO()){

                    PreparedStatement preparedStatement1 = connection.prepareStatement("INSERT INTO order_detail(oid,iid,qty,price) values(?,?,?,?)");
                    preparedStatement1.setObject(1,id);
                    System.out.println(dto.getItemId());
                    preparedStatement1.setObject(2,dto.getItemId());
                    preparedStatement1.setObject(3,dto.getOrderQty());
                    preparedStatement1.setObject(4,dto.getTotal());
                    int orderDetailsSave = preparedStatement1.executeUpdate();

                    if (orderDetailsSave > 0) {
                        PreparedStatement preparedStatement2 = connection.prepareStatement("update item set qty = qty - ? where id = ?");
                        preparedStatement2.setObject(1,dto.getOrderQty());
                        preparedStatement2.setObject(2,dto.getItemId());
                        int itemTableUpdate = preparedStatement2.executeUpdate();
                        if (itemTableUpdate <= 0) {
                            connection.rollback();
                            connection.setAutoCommit(true);
                            return  false;
                        }


                    }else {
                        connection.rollback();
                        connection.setAutoCommit(true);
                        return false;
                    }
                }
            }connection.commit();
            connection.setAutoCommit(true);
            return true;
        }else {
            connection.rollback();
            connection.setAutoCommit(true);
            return false;
        }



    }
}
