package com.virtuzo.abhishek.modal;

/**
 * Created by virtuzo on 6/14/2018.
 */

public class GroupProductItem {

//    CREATE TABLE [dbo].[GroupProducts](
//            [ID] [int] IDENTITY(1,1) NOT NULL,
//        	[ProductID] [bigint] NULL,
//            [ItemID] [bigint] NULL,
//            [Quantity] [int] NULL,
//            [UnitID] [bigint] NULL,
//            [Price] [decimal](18, 2) NULL,
//            [Weightage] [decimal](18, 2) NULL,
//            [isActive] [bit] NULL

    private long ID;
    private long ProductID; // Combo Product ID
    private long ItemID; // Individual Product ID
    private String ItemName;
    private String ItemCode;
    private int Quantity;
    private long UnitID;
    private double Price;
    private double Weightage;
    private int IsActive;
    private int Synced = 0;

    private double Tax;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getProductID() {
        return ProductID;
    }

    public void setProductID(long productID) {
        ProductID = productID;
    }

    public long getItemID() {
        return ItemID;
    }

    public void setItemID(long itemID) {
        ItemID = itemID;
    }

    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemCode() {
        return ItemCode;
    }

    public void setItemCode(String itemCode) {
        ItemCode = itemCode;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public long getUnitID() {
        return UnitID;
    }

    public void setUnitID(long unitID) {
        UnitID = unitID;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getWeightage() {
        return Weightage;
    }

    public void setWeightage(double weightage) {
        Weightage = weightage;
    }

    public int getIsActive() {
        return IsActive;
    }

    public void setIsActive(int isActive) {
        this.IsActive = isActive;
    }

    public int getSynced() {
        return Synced;
    }

    public void setSynced(int synced) {
        Synced = synced;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double tax) {
        Tax = tax;
    }
}
