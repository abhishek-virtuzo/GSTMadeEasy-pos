package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 2/5/2018.
 */

public class EwayBillReason {

    private int ID;
    private String Reason;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    @Override
    public String toString() {
        return getReason();
    }
}
