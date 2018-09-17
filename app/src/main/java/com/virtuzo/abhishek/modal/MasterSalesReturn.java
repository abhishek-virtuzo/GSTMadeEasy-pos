package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/7/2018.
 */

public class MasterSalesReturn {

//    CREATE TABLE [dbo].[mSalesReturn](
//            [SaleReturnID] [bigint] IDENTITY(1,1) NOT NULL,
//	          [SalesReturnDate] [datetime] NULL,
//            [CustomerID] [bigint] NULL,
//            [SaleID] [bigint] NULL,
//            [OutletID] [int] NULL,
//            [CreditAmount] [decimal](18, 2) NULL,
//            [IsActive] [bit] NULL,
//            [CreatedBy] [bigint] NULL,
//            [CreatedDttm] [smalldatetime] NULL,
//            [ModifiedBy] [bigint] NULL,
//            [ModifiedDtTm] [smalldatetime] NULL)

    private long SaleReturnID;
    private String SalesReturnDate;
    private String CustomerID;
    private int OutletID;
	private double CreditAmount;
    private boolean IsActive;
    private double CreatedBy; // CreatedBy - LoginID
    private String CreatedDttm;
    private String CustomerName;
    private String CustomerEmail;
    private String CustomerMobile;
    private String CustomerLandline;
    private String InvoiceNumber;
    private String SalesReturnNumber;
    private String Notes;
    private int SalesID = 0;
    private int synced = 0;

    public long getSaleReturnID() {
        return SaleReturnID;
    }

    public void setSaleReturnID(long saleReturnID) {
        SaleReturnID = saleReturnID;
    }

    public String getSalesReturnDate() {
        return SalesReturnDate;
    }

    public void setSalesReturnDate(String salesReturnDate) {
        SalesReturnDate = salesReturnDate;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public int getOutletID() {
        return OutletID;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public double getCreditAmount() {
        return CreditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        CreditAmount = creditAmount;
    }

    public boolean isActive() {
        return IsActive;
    }

    public void setActive(boolean active) {
        IsActive = active;
    }

    public double getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(double createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDttm() {
        return CreatedDttm;
    }

    public void setCreatedDttm(String createdDttm) {
        CreatedDttm = createdDttm;
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

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public String getSalesReturnNumber() {
        return SalesReturnNumber;
    }

    public void setSalesReturnNumber(String salesReturnNumber) {
        SalesReturnNumber = salesReturnNumber;
    }

    public String getNotes() {
        return Notes;
    }

    public void setNotes(String notes) {
        Notes = notes;
    }

    public int getSalesID() {
        return SalesID;
    }

    public void setSalesID(int salesID) {
        SalesID = salesID;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
