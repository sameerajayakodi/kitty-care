package org.samee.lk.skypos.dto;

public class OrderDetailDTO {

        private int itemId;
        private int orderQty;
        private double total;

    public OrderDetailDTO(int itemId ,  int orderQty, double total) {
        this.total = total;
        this.orderQty = orderQty;
        this.itemId = itemId;
    }

    public int getItemId() {
            return itemId;
        }

        public void setItemId(int itemId) {
            this.itemId = itemId;
        }

        public int getOrderQty() {
            return orderQty;
        }

        public void setOrderQty(int orderQty) {
            this.orderQty = orderQty;
        }

        public double getTotal() {
            return total;
        }

        public void setTotal(double total) {
            this.total = total;
        }

}
