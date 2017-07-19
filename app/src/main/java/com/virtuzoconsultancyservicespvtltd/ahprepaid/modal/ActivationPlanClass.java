package com.virtuzoconsultancyservicespvtltd.ahprepaid.modal;

/**
 * Created by Eiraj on 7/7/2017.
 */

public class ActivationPlanClass {

    int TariffID;
    int TariffCode;
    String TariffPlan;
    double Amount;
    int TariffTypeID;
    int ValidityDays;
    double Regulatory;
    int Months;

    public int getTariffID() {
        return TariffID;
    }

    public void setTrariffID(int tariffID) {
        TariffID = tariffID;
    }

    public int getTariffCode() {
        return TariffCode;
    }

    public void setTariffCode(int tariffCode) {
        TariffCode = tariffCode;
    }

    public String getTariffPlan() {
        return TariffPlan;
    }

    public void setTariffPlan(String tariffPlan) {
        TariffPlan = tariffPlan;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getTariffTypeID() {
        return TariffTypeID;
    }

    public void setTariffTypeID(int tariffTypeID) {
        TariffTypeID = tariffTypeID;
    }

    public int getValidityDays() {
        return ValidityDays;
    }

    public void setValidityDays(int validityDays) {
        ValidityDays = validityDays;
    }

    public double getRegulatory() {
        return Regulatory;
    }

    public void setRegulatory(double regulatory) {
        Regulatory = regulatory;
    }

    public String getMonths() {
        return String.valueOf(Months);
    }

    public void setMonths(int months) {
        Months = months;
    }
}
