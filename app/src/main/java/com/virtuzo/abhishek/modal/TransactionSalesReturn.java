package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/7/2018.
 */

public class TransactionSalesReturn {

//    CREATE TABLE [dbo].[tSalesReturn](
//            [SalesReturnDetailID] [bigint] NOT NULL,
//        	[SalesReturnID] [bigint] NULL,
//            [ProductID] [bigint] NULL,
//            [ReturnQty] [int] NULL,
//            [UnitID] [tinyint] NULL,
//            [ReturnPrice] [decimal](18, 2) NULL,
//            [ReturnAmount] [decimal](18, 2) NULL

    private long SalesReturnDetailID;
    private long SalesReturnID;
    private long ProductID;
    private String ProductCode;
    private int UnitID;
    private double ReturnPrice;
    private int ReturnQty;
    private double ReturnTaxRate;
    private double ReturnTaxAmount;
    private double ReturnAmount;
    private String ProductName;
    private int ReturnType;
    int synced = 0;

    public long getSalesReturnDetailID() {
        return SalesReturnDetailID;
    }

    public void setSalesReturnDetailID(long salesReturnDetailID) {
        SalesReturnDetailID = salesReturnDetailID;
    }

    public long getSalesReturnID() {
        return SalesReturnID;
    }

    public void setSalesReturnID(long salesReturnID) {
        SalesReturnID = salesReturnID;
    }

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public double getReturnPrice() {
        return ReturnPrice;
    }

    public void setReturnPrice(double returnPrice) {
        ReturnPrice = returnPrice;
    }

    public int getReturnQty() {
        return ReturnQty;
    }

    public void setReturnQty(int returnQty) {
        ReturnQty = returnQty;
    }

    public double getReturnTaxRate() {
        return ReturnTaxRate;
    }

    public void setReturnTaxRate(double returnTaxRate) {
        ReturnTaxRate = returnTaxRate;
    }

    public double getReturnTaxAmount() {
        return ReturnTaxAmount;
    }

    public void setReturnTaxAmount(double returnTaxAmount) {
        ReturnTaxAmount = returnTaxAmount;
    }

    public double getReturnAmount() {
        return ReturnAmount;
    }

    public void setReturnAmount(double returnAmount) {
        ReturnAmount = returnAmount;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public int getReturnType() {
        return ReturnType;
    }

    public void setReturnType(int returnType) {
        ReturnType = returnType;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
