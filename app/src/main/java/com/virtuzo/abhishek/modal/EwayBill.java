package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 2/5/2018.
 */

public class EwayBill {
//    CREATE TABLE [dbo].[tEwayBillNumber](
//            [ID] [bigint] IDENTITY(1,1) NOT NULL,
//	`         [SalesID] [bigint] NULL,
//            [InvoiceNumber] [varchar](50) NULL,
//            [ReasonID] [int] NULL,
//            [DocumentNo] [varchar](50) NULL,
//            [VehicleNo] [varchar](50) NULL,
//            [isActive] [bit] NULL
//    )

//     \"ID\": 1,
//             \"SalesID\": 113,
//             \"InvoiceNumber\": \"GST-bjg/1718/Feb-08/002\",
//             \"ReasonID\": 2,
//             \"DocumentNo\": \"2222\",
//             \"VehicleNo\": \"HR12 2222\",
//             \"EWayBillNo\": null,
//             \"EWayDocumentPath\": null,
//             \"isActive\": true,
//             \"CreatedBy\": null,
//             \"CreatedDtTm\": null

    private long ID;
    private long SalesID;
    private String InvoiceNumber="";
    private int ReasonID;
    private String DocumentNo;
    private String VehicleNo;
    private boolean isActive;
    private double CreatedBy; // CreatedBy - LoginID
    private String CreatedDtTm; // CreatedDtTm
    private String EWayBillNo; // EWayBillNo
    private String EWayDocumentPath=""; // EWayDocumentPath
    private String DONumber="";
    private int synced = 0;

    public long getID() {
        return ID;
    }

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getSalesID() {
        return SalesID;
    }

    public void setSalesID(long salesID) {
        SalesID = salesID;
    }

    public String getInvoiceNumber() {
        return InvoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        InvoiceNumber = invoiceNumber;
    }

    public int getReasonID() {
        return ReasonID;
    }

    public void setReasonID(int reasonID) {
        ReasonID = reasonID;
    }

    public String getDocumentNo() {
        return DocumentNo;
    }

    public void setDocumentNo(String documentNo) {
        DocumentNo = documentNo;
    }

    public String getVehicleNo() {
        return VehicleNo;
    }

    public void setVehicleNo(String vehicleNo) {
        VehicleNo = vehicleNo;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
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

    public String getEWayBillNo() {
        return EWayBillNo;
    }

    public void setEWayBillNo(String EWayBillNo) {
        this.EWayBillNo = EWayBillNo;
    }

    public String getEWayDocumentPath() {
        return EWayDocumentPath;
    }

    public void setEWayDocumentPath(String EWayDocumentPath) {
        this.EWayDocumentPath = EWayDocumentPath;
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
}
