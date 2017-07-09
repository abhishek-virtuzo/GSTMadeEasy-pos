package com.virtuzoconsultancyservicespvtltd.ahprepaid.modal;

/**
 * Created by Eiraj on 7/7/2017.
 */

public class PlanClass {

    long TariffCode;
    String ProductDescription;
    double RechargeAmount;
    String CurrencySymbol;
    double StateTax;
    double Regulatory;
    double Discount;
    String PlanDescription;
    double TotalAmount;

    public long getTariffCode() {
        return TariffCode;
    }

    public void setTariffCode(long tariffCode) {
        TariffCode = tariffCode;
    }

    public String getProductDescription() {
        return ProductDescription;
    }

    public void setProductDescription(String productDescription) {
        ProductDescription = productDescription;
    }

    public double getRechargeAmount() {
        return RechargeAmount;
    }

    public void setRechargeAmount(double rechargeAmount) {
        RechargeAmount = rechargeAmount;
    }

    public String getCurrencySymbol() {
        return CurrencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        CurrencySymbol = currencySymbol;
    }

    public double getStateTax() {
        return StateTax;
    }

    public void setStateTax(double stateTax) {
        StateTax = stateTax;
    }

    public double getRegulatory() {
        return Regulatory;
    }

    public void setRegulatory(double regulatory) {
        Regulatory = regulatory;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public String getPlanDescription() {
        return PlanDescription;
    }

    public void setPlanDescription(String planDescription) {
        PlanDescription = planDescription;
    }

    public double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        TotalAmount = totalAmount;
    }
}
