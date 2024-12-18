package org.samee.lk.skypos.tm;

public class OverStockTM {
    private String overItemName;

    public OverStockTM(String overItemName) {
        this.overItemName = overItemName;
    }

    public String getOverItemName() {
        return overItemName;
    }

    public void setOverItemName(String overItemName) {
        this.overItemName = overItemName;
    }
}
