package com.virtuzo.abhishek.modal;

/**
 * Created by Abhishek Aggarwal on 1/15/2018.
 */

public class HSCCode {

//            \"ID\": 1,
//            \"HSCCode\": \"1011010\",
//            \"Description\": \"LIVE HORSES, ASSES,MULES AND HINNIES PURE-BRED BREEDING ANIMALS HORSES\",
//            \"CGST\": 9.00,
//            \"SGST\": 9.00,
//            \"IGST\": 18.00,
//            \"CESS\": 0.00,
//            \"IsActive\": true,
//            \"CreatedBy\": 0,
//            \"Condition\": null,
//            \"Remarks\": null,
//            \"RedioHSN\": null

    private int ID;
    private String HSCCode;
    private String Description;
    private double CGST;
    private double SGST;
    private double IGST;
    private double CESS;
    private boolean IsActive;
    private int CreatedBy;
    private String Condition;
    private String Remarks;
    private String RedioHSN;
    private int synced = 0;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getHSCCode() {
        return HSCCode;
    }

    public void setHSCCode(String HSCCode) {
        this.HSCCode = HSCCode;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
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

    public String getCondition() {
        return Condition;
    }

    public void setCondition(String condition) {
        Condition = condition;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    public String getRedioHSN() {
        return RedioHSN;
    }

    public void setRedioHSN(String redioHSN) {
        RedioHSN = redioHSN;
    }

    public int getSynced() {
        return synced;
    }

    public void setSynced(int synced) {
        this.synced = synced;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        if(this.getID() != 0) {
            stringBuilder.append(String.format("%.2f", this.getIGST()) + "% - ");
            stringBuilder.append(this.getHSCCode() + " - ");
        }
        stringBuilder.append(this.getDescription());
        return stringBuilder.toString();
    }
}
