package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 2/16/2018.
 */

public class TransactionDeliveryOrder {

//    CREATE TABLE [dbo].[mDeliveryOrderDetail](
//            [ID] [bigint] IDENTITY(1,1) NOT NULL,
//            [DOID] [bigint] NULL,
//            [ProductID] [bigint] NULL,
//            [Quantity] [int] NULL,
//            [UnitID] [tinyint] NULL,
//            [IsActive] [bit] NULL,
//            [CreatedBy] [bigint] NULL,
//            [CreatedDtTm] [smalldatetime] NULL,
//            [ModifiedBy] [bigint] NULL,
//            [ModifiedDtTm] [smalldatetime] NULL,
//            [Status] [int] NULL)
//            [ProductCode] [varchar](50) NULL,

    private int ID;
    private int DOID;
    private long ProductID;
    private int Quantity;
    private int UnitID;
    private boolean IsActive;
    private int CreatedBy;
    private String CreatedDtTm;
    private int Status;
    private String ProductCode;
    private String ProductName;
    private double Amount;
    private int synced = 0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getDOID() {
        return DOID;
    }

    public void setDOID(int DOID) {
        this.DOID = DOID;
    }

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public int getUnitID() {
        return UnitID;
    }

    public void setUnitID(int unitID) {
        UnitID = unitID;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public int getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(int createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDtTm() {
        return CreatedDtTm;
    }

    public void setCreatedDtTm(String createdDtTm) {
        CreatedDtTm = createdDtTm;
    }

    public int getStatus() {
        return Status;
    }

    public void setStatus(int status) {
        Status = status;
    }

    public String getProductCode() {
        return ProductCode;
    }

    public void setProductCode(String productCode) {
        ProductCode = productCode;
    }

    public String getProductName() {
        return ProductName;
    }

    public void setProductName(String productName) {
        ProductName = productName;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
