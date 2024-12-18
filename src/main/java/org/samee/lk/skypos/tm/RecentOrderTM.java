package org.samee.lk.skypos.tm;

public class RecentOrderTM {
    private String orderDate;
    private double amount;

    public RecentOrderTM(String orderDate, double amount) {
        this.orderDate = orderDate;
        this.amount = amount;
    }

    public String getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(String orderDate) {
        this.orderDate = orderDate;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }
}
