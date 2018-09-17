package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 2/16/2018.
 */

public class MasterDeliveryOrder {

//    CREATE TABLE [dbo].[mDeliveryOrder](
//            [ID] [bigint] IDENTITY(1,1) NOT NULL,
//            [DeliveryDate] [smalldatetime] NULL,
//            [DONumber] [varchar](50) NULL,
//            [Customer_ID] [bigint] NULL,
//            [OutletID] [bigint] NULL,
//            [IsActive] [bit] NULL,
//            [CreatedBy] [bigint] NULL,
//            [CreatedDtTm] [smalldatetime] NULL,
//            [ModifiedBy] [bigint] NULL,
//            [ModifiedDtTm] [smalldatetime] NULL,
//            [Status] [int] NULL,
//            [CustomerName] [varchar](50) NULL,
//            [CustomerEmail] [varchar](50) NULL,
//            [CustomerContactPerson] [varchar](50) NULL,
//            [CustomerMobile] [varchar](10) NULL,
//            [CustomerLandline] [varchar](10) NULL,
//            [CustomerBillAddress] [varchar](500) NULL,
//            [CustomerBillStateID] [int] NULL,
//            [CustomerShipAdrress] [varchar](500) NULL,
//            [CustomerShipStateID] [int] NULL)

    private int ID;
    private String DeliveryDate;
    private String DONumber;
    private String Customer_ID;
    private long OutletID;
    private boolean IsActive;
    private long CreatedBy;
    private String CreatedDtTm;
    private int Status;
    private String CustomerName;
    private String CustomerEmail;
    private String CustomerContactPerson;
    private String CustomerMobile;
    private String CustomerLandline;
    private String CustomerBillAddress;
    private int CustomerBillStateID;
    private String CustomerShipAddress;
    private int CustomerShipStateID;
    private int TotalQuantity = 0;
    private double TotalAmount = 0;
    private int synced = 0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getDONumber() {
        return DONumber;
    }

    public void setDONumber(String DONumber) {
        this.DONumber = DONumber;
    }

    public String getCustomer_ID() {
        return Customer_ID;
    }

    public void setCustomer_ID(String customer_ID) {
        Customer_ID = customer_ID;
    }

    public long getOutletID() {
        return OutletID;
    }

    public void setOutletID(long outletID) {
        OutletID = outletID;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public long getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(long createdBy) {
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

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerEmail() {
        return CustomerEmail;
    }

    public void setCustomerEmail(String customerEmail) {
        CustomerEmail = customerEmail;
    }

    public String getCustomerContactPerson() {
        return CustomerContactPerson;
    }

    public void setCustomerContactPerson(String customerContactPerson) {
        CustomerContactPerson = customerContactPerson;
    }

    public String getCustomerMobile() {
        return CustomerMobile;
    }

    public void setCustomerMobile(String customerMobile) {
        CustomerMobile = customerMobile;
    }

    public String getCustomerLandline() {
        return CustomerLandline;
    }

    public void setCustomerLandline(String customerLandline) {
        CustomerLandline = customerLandline;
    }

    public String getCustomerBillAddress() {
        return CustomerBillAddress;
    }

    public void setCustomerBillAddress(String customerBillAddress) {
        CustomerBillAddress = customerBillAddress;
    }

    public int getCustomerBillStateID() {
        return CustomerBillStateID;
    }

    public void setCustomerBillStateID(int customerBillStateID) {
        CustomerBillStateID = customerBillStateID;
    }

    public String getCustomerShipAddress() {
        return CustomerShipAddress;
    }

    public void setCustomerShipAddress(String customerShipAddress) {
        CustomerShipAddress = customerShipAddress;
    }

    public int getCustomerShipStateID() {
        return CustomerShipStateID;
    }

    public void setCustomerShipStateID(int customerShipStateID) {
        CustomerShipStateID = customerShipStateID;
    }

    public int getTotalQuantity() {
        return TotalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        TotalQuantity = totalQuantity;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
