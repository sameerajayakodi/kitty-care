package org.samee.lk.skypos.tm;

public class LowStockTM {
    private String lowItemName;

    public String getLowItemName() {
        return lowItemName;
    }

    public LowStockTM(String lowItemName) {
        this.lowItemName = lowItemName;
    }

    public void setLowItemName(String lowItemName) {
        this.lowItemName = lowItemName;
    }
}
