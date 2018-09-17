package com.virtuzo.abhishek.modal;

import java.util.ArrayList;

/**
 * Created by virtuzo on 9/4/2018.
 */

public class DraftInvoice {

    int DraftID;
    String CurrentDate;
    MasterSales masterSales;
    ArrayList<ProductWithQuantity> ProductList;

    Customer customer;

    public int getDraftID() {
        return DraftID;
    }

    public void setDraftID(int draftID) {
        DraftID = draftID;
    }

    public String getCurrentDate() {
        return CurrentDate;
    }

    public void setCurrentDate(String currentDate) {
        CurrentDate = currentDate;
    }

    public MasterSales getMasterSales() {
        return masterSales;
    }

    public void setMasterSales(MasterSales masterSales) {
        this.masterSales = masterSales;
    }

    public ArrayList<ProductWithQuantity> getProductList() {
        return ProductList;
    }

    public void setProductList(ArrayList<ProductWithQuantity> productList) {
        ProductList = productList;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }
}
