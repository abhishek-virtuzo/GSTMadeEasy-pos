package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 12/4/2017.
 */

public class TransactionPurchase {

    // {"Purchases":[{"PurchaseDate":"29 Dec 2017","InvoiceNumber" : "12020","OutletID":5,"SupplierID": 24,"CreatedBy": 7,
// "TransportCharge":0.00,"InsuranceCharge":0.00,"PackingCharge":0.00,"CreatedDtTm":"29 Dec 2017","PurchaseID":23,
// "TransactionPurchase" :
// [{"PurchaseDetailID":40,"ProductID":177,"Quantity":10,"UnitID":1,"UnitPrice":10.00,"Amount":100.00,"Status":7}]}]}

//    private double PurchasePersonID;
//    private int ItemTypeID;
//    private String RefNumber;
//    private double Discount;
//    private double TaxAmount;
//    private double SalesCommission;
//    private int CommissionTypeID;
//    private double Outlet_SalesSubID;
//    private int DiscountType;
//    private double CGST;
//    private double SGST;
//    private double IGST;
//    private double CESS;
//    private String ItemName;
//"PurchaseDetailID":40,"ProductID":177,"Quantity":10,"UnitID":1,"UnitPrice":10.00,"Amount":100.00,"Status":7
    private double PurchaseDetailID;
    private double PurchaseID;
    private double ProductID;
    private long UnitID;
    private int Quantity;
    private double UnitPrice;
    private double Amount;
    private int Status = 7;
    private String ProductName;
    private String Unit;
    private double iCGST;
    private double iSGST;
    private double iIGST;
    private double iCESS;
    private String ProductCode;
    private int synced = 0;

    public double getPurchaseID() {
        return PurchaseID;
    }

    public void setPurchaseID(double purchaseID) {
        PurchaseID = purchaseID;
    }

    public double getPurchaseDetailID() {
        return PurchaseDetailID;
    }

    public void setPurchaseDetailID(double purchaseDetailID) {
        PurchaseDetailID = purchaseDetailID;
    }

    public double getProductID() {
        return ProductID;
    }

    public void setProductID(double productID) {
        ProductID = productID;
    }

    public long getUnitID() {
        return UnitID;
    }

    public void setUnitID(long unitID) {
        UnitID = unitID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public double getiCGST() {
        return iCGST;
    }

    public void setiCGST(double iCGST) {
        this.iCGST = iCGST;
    }

    public double getiSGST() {
        return iSGST;
    }

    public void setiSGST(double iSGST) {
        this.iSGST = iSGST;
    }

    public double getiIGST() {
        return iIGST;
    }

    public void setiIGST(double iIGST) {
        this.iIGST = iIGST;
    }

    public double getiCESS() {
        return iCESS;
    }

    public void setiCESS(double iCESS) {
        this.iCESS = iCESS;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
