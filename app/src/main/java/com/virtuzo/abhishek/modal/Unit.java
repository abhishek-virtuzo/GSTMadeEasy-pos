package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/15/2018.
 */

public class Unit {
//            "ID" : 2,
//            "IsActive" : true,
//            "Unit" : Meter

    private int ID;
    private boolean isActive;
    private String Unit;
    private int synced = 0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    @Override
    public String toString() {
        return this.getUnit();
    }
}
