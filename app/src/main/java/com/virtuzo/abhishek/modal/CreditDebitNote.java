package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/7/2018.
 */

public class CreditDebitNote {

//    CREATE TABLE [dbo].[mCreditNote](
//            [CreditNoteID] [bigint] NULL,
//            [SalesID] [bigint] NULL,
//            [CustomerID] [bigint] NULL,
//            [Amount] [decimal](18, 2) NULL,
//            [Reason] [text] NULL,
//            [isActive] [bit] NULL,
//            [CreatedBy] [bigint] NULL,
//            [CreatedDtTm] [smalldatetime] NULL,
//            [ModifiedBy] [bigint] NULL,
//            [ModifiedDtTm] [smalldatetime] NULL

    private long CreditNoteID;
    private String NoteNumber;
    private String CustomerID;
    private String CustomerName;
    private String CustomerEmail;
    private String CustomerMobile;
    private String CustomerLandline;
    private String InvoiceNumber;
    private String SalesReturnNumber;
    private int OutletID;
    private double Amount;
    private String Reason;
    private long CreatedBy; // Login ID
    private String CreatedDtTm;
    private long ModifiedBy;
    private String ModifiedDtTm;
    private int SalesID = 0;
    private String NoteType;
    private String NoteDate;
    private int synced = 0;

    public long getCreditNoteID() {
        return CreditNoteID;
    }

    public void setCreditNoteID(long creditNoteID) {
        CreditNoteID = creditNoteID;
    }

    public String getNoteNumber() {
        return NoteNumber;
    }

    public void setNoteNumber(String NoteNumber) {
        this.NoteNumber = NoteNumber;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
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

    public int getOutletID() {
        return OutletID;
    }

    public void setOutletID(int outletID) {
        OutletID = outletID;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
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

    public long getModifiedBy() {
        return ModifiedBy;
    }

    public void setModifiedBy(long modifiedBy) {
        ModifiedBy = modifiedBy;
    }

    public String getModifiedDtTm() {
        return ModifiedDtTm;
    }

    public void setModifiedDtTm(String modifiedDtTm) {
        ModifiedDtTm = modifiedDtTm;
    }

    public int getSalesID() {
        return SalesID;
    }

    public void setSalesID(int salesID) {
        SalesID = salesID;
    }

    public String getNoteType() {
        return NoteType;
    }

    public void setNoteType(String noteType) {
        NoteType = noteType;
    }

    public String getNoteDate() {
        return NoteDate;
    }

    public void setNoteDate(String noteDate) {
        NoteDate = noteDate;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }
}
