package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 12/2/2017.
 */

public class MasterSales {

    //            [SalesID] [bigint] IDENTITY(1,1) NOT NULL,
    double SalesID;
    //            [SalesReceiptNumber] [varchar](50) NULL,
    String SalesReceiptNumber;
    //            [CustomerID] [bigint] NULL,
    String CustomerID;
    //            [BaseAmount] [decimal](18, 2) NULL,
    double BaseAmount;
    //            [TotalDiscountAmount] [decimal](18, 2) NULL,
    double TotalDiscountAmount;
    //            [TaxAmount] [decimal](18, 2) NULL,
    double TaxAmount;
    //            [TotalAmount] [decimal](18, 2) NULL,
    double TotalAmount;
    //            [ReferenceNumber] [varchar](50) NULL,
    String ReferenceNumber;
    //            [LoginID] [bigint] NULL,
    double LoginID;
    //            [SalesDtTm] [datetime] NULL,
    String SalesDtTm;
    //            [Gender] [varchar](1) NULL,
    String Gender;
    //            [isHold] [bit] NULL,\
    boolean isHold;
    //            [isActive] [bit] NULL,
    boolean isActive;
    //            [OutletID] [int] NULL,
    double OutletID;
    //            [CustomerType] [char](1) NULL,
    String CustomerType;
    //            [Balance] [decimal](18, 2) NULL,
    double Balance;
    //            [PaidAmount] [decimal](18, 2) NULL,
    double PaidAmount;
    //            [TaxOnPaidAmount] [decimal](18, 2) NULL,
    double TaxOnPaidAmount;
    //            [CGST] [decimal](18, 2) NULL,
    double CGST;
    //            [SGST] [decimal](18, 2) NULL,
    double SGST;
    //            [IGST] [decimal](18, 2) NULL,
    double IGST;
    //            [DiscountTypeID] [int] NULL,
    int DiscountTypeID;
    //            [Discount] [decimal](18, 2) NULL,
    double Discount;
    //            [CESS] [decimal](18, 2) NULL,
    double CESS;
    //            [DueDate] [datetime] NULL,
    String DueDate; //  date time
    //            [CustomerName] [varchar](100) NULL,
    String CustomerName;
    //            [TermsCondition] [varchar](max) NULL,
    String TermsCondition;
    //            [CustomerGSTN] [varchar](15) NULL,
    String CustomerGSTN;
    //            [CustomerEmail] [varchar](50) NULL,
    String CustomerEmail;
    //            [CustomerMobile] [varchar](10) NULL,
    String CustomerMobile;
    //            [CustomerAddress] [varchar](max) NULL,
    String CustomerAddress;
    //            [OutletAddress] [varchar](500) NULL,
    String OutletAddress;
    //            [OutletLogo] [varchar](100) NULL,
    String OutletLogo;
    //            [OutletGSTN] [varchar](15) NULL,
    String OutletGSTN;
    //            [ShipAddress] [varchar](500) NULL,
    String ShipAddress;
    //            [TransportCharge] [decimal](18, 2) NULL,
    double TransportCharge;
    //            [InsuranceCharge] [decimal](18, 2) NULL,
    double InsuranceCharge;
    //            [PackingCharge] [decimal](18, 2) NULL,
    double PackingCharge;
    //            [BillAddress] [decimal](18, 2) NULL
    String BillAddress;

    String SalesDate;

    double SubTotal;

    int eWayBillRequired = 0;

    int ShippingStateID;

    String DONumber = "";

    int synced = 0; // not synced, 1 when the invoice comes from the server

    public double getSalesID() {
        return SalesID;
    }

    public void setSalesID(double salesID) {
        SalesID = salesID;
    }

    public String getSalesReceiptNumber() {
        return SalesReceiptNumber;
    }

    public void setSalesReceiptNumber(String salesReceiptNumber) {
        SalesReceiptNumber = salesReceiptNumber;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public double getBaseAmount() {
        return BaseAmount;
    }

    public void setBaseAmount(double baseAmount) {
        BaseAmount = baseAmount;
    }

    public double getTotalDiscountAmount() {
        return TotalDiscountAmount;
    }

    public void setTotalDiscountAmount(double totalDiscountAmount) {
        TotalDiscountAmount = totalDiscountAmount;
    }

    public double getTaxAmount() {
        return TaxAmount;
    }

    public void setTaxAmount(double taxAmount) {
        TaxAmount = taxAmount;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getReferenceNumber() {
        return ReferenceNumber;
    }

    public void setReferenceNumber(String referenceNumber) {
        ReferenceNumber = referenceNumber;
    }

    public double getLoginID() {
        return LoginID;
    }

    public void setLoginID(double loginID) {
        LoginID = loginID;
    }

    public String getSalesDtTm() {
        return SalesDtTm;
    }

    public void setSalesDtTm(String salesDtTm) {
        SalesDtTm = salesDtTm;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public boolean isHold() {
        return isHold;
    }

    public void setHold(boolean hold) {
        isHold = hold;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public double getOutletID() {
        return OutletID;
    }

    public void setOutletID(double outletID) {
        OutletID = outletID;
    }

    public String getCustomerType() {
        return CustomerType;
    }

    public void setCustomerType(String customerType) {
        CustomerType = customerType;
    }

    public double getBalance() {
        return Balance;
    }

    public void setBalance(double balance) {
        Balance = balance;
    }

    public double getPaidAmount() {
        return PaidAmount;
    }

    public void setPaidAmount(double paidAmount) {
        PaidAmount = paidAmount;
    }

    public double getTaxOnPaidAmount() {
        return TaxOnPaidAmount;
    }

    public void setTaxOnPaidAmount(double taxOnPaidAmount) {
        TaxOnPaidAmount = taxOnPaidAmount;
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

    public int getDiscountTypeID() {
        return DiscountTypeID;
    }

    public void setDiscountTypeID(int discountTypeID) {
        DiscountTypeID = discountTypeID;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public double getCESS() {
        return CESS;
    }

    public void setCESS(double CESS) {
        this.CESS = CESS;
    }

    public String getDueDate() {
        return DueDate;
    }

    public void setDueDate(String dueDate) {
        DueDate = dueDate;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getTermsCondition() {
        return TermsCondition;
    }

    public void setTermsCondition(String termsCondition) {
        TermsCondition = termsCondition;
    }

    public String getCustomerGSTN() {
        return CustomerGSTN;
    }

    public void setCustomerGSTN(String customerGSTN) {
        CustomerGSTN = customerGSTN;
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

    public String getCustomerAddress() {
        return CustomerAddress;
    }

    public void setCustomerAddress(String customerAddress) {
        CustomerAddress = customerAddress;
    }

    public String getOutletAddress() {
        return OutletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        OutletAddress = outletAddress;
    }

    public String getOutletLogo() {
        return OutletLogo;
    }

    public void setOutletLogo(String outletLogo) {
        OutletLogo = outletLogo;
    }

    public String getOutletGSTN() {
        return OutletGSTN;
    }

    public void setOutletGSTN(String outletGSTN) {
        OutletGSTN = outletGSTN;
    }

    public String getShipAddress() {
        return ShipAddress;
    }

    public void setShipAddress(String shipAddress) {
        ShipAddress = shipAddress;
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

    public String getBillAddress() {
        return BillAddress;
    }

    public void setBillAddress(String billAddress) {
        BillAddress = billAddress;
    }

    public String getSalesDate() {
        return SalesDate;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double subTotal) {
        SubTotal = subTotal;
    }

    public void setSalesDate(String salesDate) {
        SalesDate = salesDate;
    }

    public int geteWayBillRequired() {
        return eWayBillRequired;
    }

    public void seteWayBillRequired(int eWayBillRequired) {
        this.eWayBillRequired = eWayBillRequired;
    }

    public int getShippingStateID() {
        return ShippingStateID;
    }

    public void setShippingStateID(int shippingStateID) {
        ShippingStateID = shippingStateID;
    }

    public String getDONumber() {
        return DONumber;
    }

    public void setDONumber(String DONumber) {
        this.DONumber = DONumber;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    public double getOtherCharges() {
        return getTransportCharge() + getInsuranceCharge() + getPackingCharge();
    }
}
