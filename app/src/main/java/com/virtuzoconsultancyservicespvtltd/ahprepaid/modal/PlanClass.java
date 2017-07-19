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
    double Regulatry;
    double Discount;
    String PlanDescription;
    double TotalAmount;
    int Month;

    public int getMonth() {
        return Month;
    }

    public void setMonth(int month) {
        Month = month;
    }

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

    public double getRegulatry() {
        return Regulatry;
    }

    public void setRegulatry(double Regulatry) {
        Regulatry = Regulatry;
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
