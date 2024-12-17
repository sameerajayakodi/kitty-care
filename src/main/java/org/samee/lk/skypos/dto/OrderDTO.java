package org.samee.lk.skypos.dto;

import java.util.ArrayList;

public class OrderDTO {
private String orderDate;
private double orderAmount;
private ArrayList <OrderDetailDTO> orderDetailDTO;

    public OrderDTO(String orderDate, double orderAmount, ArrayList<OrderDetailDTO> orderDetailDTO) {
        this.orderDate = orderDate;
        this.orderAmount = orderAmount;
        this.orderDetailDTO = orderDetailDTO;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(double orderAmount) {
        this.orderAmount = orderAmount;
    }

    public ArrayList<OrderDetailDTO> getOrderDetailDTO() {
        return orderDetailDTO;
    }

    public void setOrderDetailDTO(ArrayList<OrderDetailDTO> orderDetailDTO) {
        this.orderDetailDTO = orderDetailDTO;
    }
}
