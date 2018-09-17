package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 12/2/2017.
 */

public class MasterPurchase {

//    CREATE TABLE [dbo].[mPurchase](
//            [PurchaseID] [bigint] IDENTITY(1,1) NOT NULL,
//[PurchaseDate] [smalldatetime] NULL,
//            [InvoiceNumber] [varchar](50) NULL,
//            [OutletID] [bigint] NULL,
//            [IsActive] [bit] NULL,
// ??            [CreatedBy] [bigint] NULL,
//            [CreatedDtTm] [smalldatetime] NULL,
// ??           [ModifiedBy] [bigint] NULL,
// ??           [ModifiedDtTm] [smalldatetime] NULL,
// ??           [Status] [int] NULL,
//            [SupplierID] [bigint] NULL,
//            [TransportCharge] [decimal](18, 2) NULL,
//            [InsuranceCharge] [decimal](18, 2) NULL,
//            [PackingCharge] [decimal](18, 2) NULL,
//    private String PurchaseReceiptNumber;
//    private double BaseAmount;
//    private double TotalDiscountAmount;
//    private double TaxAmount;
//    private double TotalAmount;
//    private String Gender;
//    private boolean isHold;
//    private String SupplierType;
//    private double Balance;
//    private double PaidAmount;
//    private double TaxOnPaidAmount;
//    private double CGST;
//    private double SGST;
//    private double IGST;
//    private int DiscountTypeID;
//    private double Discount;
//    private double CESS;
//    private String DueDate;
//    private String SupplierName;
//    private String TermsCondition;
//    private String SupplierGSTN;
//    private String SupplierEmail;
//    private String SupplierMobile;
//    private String SupplierAddress;
//    private String OutletAddress;
//    private String OutletLogo;
//    private String OutletGSTN;
//    private String ShipAddress;
//    private String BillAddress;
//private boolean isActive; // IsActive
//    Hi,
//
//    Please find below API for Saving Purchase
//
//    http://Virtuzo.in/GSTMadeEasyAPI/POS_API.svc/SavePurchase?BusinessId=1&Purchases=
// {"Purchases":[{"PurchaseDate":"29 Dec 2017","InvoiceNumber" : "12020","OutletID":5,"SupplierID": 24,"CreatedBy": 7,
// "TransportCharge":0.00,"InsuranceCharge":0.00,"PackingCharge":0.00,"CreatedDtTm":"29 Dec 2017","PurchaseID":23,
// "TransactionPurchase" : [{"PurchaseDetailID":40,"ProductID":177,"Quantity":10,"UnitID":1,"UnitPrice":10.00,"Amount":100.00,"Status":7}]}]}


//[CGST] [decimal](18, 2) NULL,
//            [SGST] [decimal](18, 2) NULL,
//            [IGST] [decimal](18, 2) NULL,
//            [CESS] [decimal](18, 2) NULL

    private double PurchaseID; // Purchase ID
    private String SupplierID; // SupplierID
    private String InvoiceNumber; // InvoiceNumber
    private double CreatedBy; // CreatedBy - LoginID
    private String CreatedDtTm; // CreatedDtTm
    private double OutletID; // OutletID
    private double TransportCharge; // TransportCharge
    private double InsuranceCharge; // InsuranceCharge
    private double PackingCharge; // PackingCharge
    private String PurchaseDate; // PurchaseDate
    private double CGST;
    private double SGST;
    private double IGST;
    private double CESS;
    private double SubTotal;
    private double GrandTotal;
    private String SupplierName;
    private String SupplierEmail;
    private String SupplierMobile;
    private int synced = 0;

    public double getPurchaseID() {
        return PurchaseID;
    }

    public void setPurchaseID(double purchaseID) {
        PurchaseID = purchaseID;
    }

    public String getSupplierID() {
        return SupplierID;
    }

    public void setSupplierID(String supplierID) {
        SupplierID = supplierID;
    }

    public double getOutletID() {
        return OutletID;
    }

    public void setOutletID(double outletID) {
        OutletID = outletID;
    }

    public double getTransportCharge() {
        return TransportCharge;
    }

    public void setTransportCharge(double transportCharge) {
        TransportCharge = transportCharge;
    }

    public double getInsuranceCharge() {
        return InsuranceCharge;
    }

    public void setInsuranceCharge(double insuranceCharge) {
        InsuranceCharge = insuranceCharge;
    }

    public double getPackingCharge() {
        return PackingCharge;
    }

    public void setPackingCharge(double packingCharge) {
        PackingCharge = packingCharge;
    }

    public String getPurchaseDate() {
        return PurchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        PurchaseDate = purchaseDate;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public double getCreatedBy() {
        return CreatedBy;
    }

    public void setCreatedBy(double createdBy) {
        CreatedBy = createdBy;
    }

    public String getCreatedDtTm() {
        return CreatedDtTm;
    }

    public void setCreatedDtTm(String createdDtTm) {
        CreatedDtTm = createdDtTm;
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

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double subTotal) {
        SubTotal = subTotal;
    }

    public double getGrandTotal() {
        return GrandTotal;
    }

    public void setGrandTotal(double grandTotal) {
        GrandTotal = grandTotal;
    }

    public String getSupplierName() {
        return SupplierName;
    }

    public void setSupplierName(String supplierName) {
        SupplierName = supplierName;
    }

    public String getSupplierEmail() {
        return SupplierEmail;
    }

    public void setSupplierEmail(String supplierEmail) {
        SupplierEmail = supplierEmail;
    }

    public String getSupplierMobile() {
        return SupplierMobile;
    }

    public void setSupplierMobile(String supplierMobile) {
        SupplierMobile = supplierMobile;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

}
