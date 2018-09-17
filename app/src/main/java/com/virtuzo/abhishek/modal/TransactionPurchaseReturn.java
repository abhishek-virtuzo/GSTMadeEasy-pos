package com.virtuzo.abhishek.modal;

/**
 * Created by AbhishekAggarwal on 3/23/2018.
 */

public class TransactionPurchaseReturn {

    private double PurchaseDetailID;
    private long PurchaseReturnID;
    private double ProductID;
    private String ProductCode;
    private long UnitID;
    private int ReturnQty;
    private double UnitPrice;
    private double ReturnAmount;
    private double ReturnTaxAmount;
    private String ProductName;
    private double CGST;
    private double SGST;
    private double IGST;
    private double CESS;

    public double getPurchaseDetailID() {
        return PurchaseDetailID;
    }

    public void setPurchaseDetailID(double purchaseDetailID) {
        PurchaseDetailID = purchaseDetailID;
    }

    public long getPurchaseReturnID() {
        return PurchaseReturnID;
    }

    public void setPurchaseReturnID(long purchaseReturnID) {
        PurchaseReturnID = purchaseReturnID;
    }

    public double getProductID() {
        return ProductID;
    }

    public void setProductID(double productID) {
        ProductID = productID;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public long getUnitID() {
        return UnitID;
    }

    public void setUnitID(long unitID) {
        UnitID = unitID;
    }

    public double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        UnitPrice = unitPrice;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getReturnQty() {
        return ReturnQty;
    }

    public void setReturnQty(int returnQty) {
        ReturnQty = returnQty;
    }

    public double getReturnAmount() {
        return ReturnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        ReturnAmount = returnAmount;
    }

    public double getReturnTaxAmount() {
        return ReturnTaxAmount;
    }

    public void setReturnTaxAmount(double returnTaxAmount) {
        ReturnTaxAmount = returnTaxAmount;
    }

    public double getCGST() {
        return CGST;
    }

    public void setCGST(double CGST) {
        this.CGST = CGST;
    }

    public double getSGST() {
        return SGST;
    }

    public void setSGST(double SGST) {
        this.SGST = SGST;
    }

    public double getIGST() {
        return IGST;
    }

    public void setIGST(double IGST) {
        this.IGST = IGST;
    }

    public double getCESS() {
        return CESS;
    }

    public void setCESS(double CESS) {
        this.CESS = CESS;
    }

}
