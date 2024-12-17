package org.samee.lk.skypos.tm;

public class OrderTM {
    private String itemName;
    private String category;
    private int orderQty;
    private double itemPrice;
    private double totalPrice;

    public OrderTM(String itemName, String category, int orderQty, double itemPrice, double totalPrice) {
        this.itemName = itemName;
        this.category = category;
        this.orderQty = orderQty;
        this.itemPrice = itemPrice;
        this.totalPrice = totalPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getOrderQty() {
        return orderQty;
    }

    public void setOrderQty(int orderQty) {
        this.orderQty = orderQty;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }
}
