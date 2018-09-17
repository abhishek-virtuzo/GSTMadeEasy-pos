package com.virtuzo.abhishek.modal;

/**
 * Created by virtuzo on 6/21/2018.
 */

public class GroupProductDetailInInvoice {

    private long ID;
    private double SalesID;
    private String InvoiceNumber;
    private long GroupProductID;
    private int GroupProductQuantity;
    private int Synced = 0;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public double getSalesID() {
        return SalesID;
    }

    public void setSalesID(double salesID) {
        SalesID = salesID;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public long getGroupProductID() {
        return GroupProductID;
    }

    public void setGroupProductID(long groupProductID) {
        GroupProductID = groupProductID;
    }

    public int getGroupProductQuantity() {
        return GroupProductQuantity;
    }

    public void setGroupProductQuantity(int groupProductQuantity) {
        GroupProductQuantity = groupProductQuantity;
    }

    public int getSynced() {
        return Synced;
    }

    public void setSynced(int synced) {
        Synced = synced;
    }
}
