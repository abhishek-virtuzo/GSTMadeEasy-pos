package com.virtuzo.abhishek.modal;

/**
 * Created by Aman Bansal on 20-12-2017.
 */

public class PaymentMode {

    private int PaymentTypeID;
    private String PaymentTypeName;
    //            E_VOUCHER_OPTION = 1, CASH_OPTION = 2, CREDIT_CARD_OPTION = 3, WALLET_OPTION = 4,
//                    DEBIT_CARD_OPTION = 5, CREDIT_NOTE_OPTION = 6;
    public static String PAYMENT_CASH = "Cash";
    public static String PAYMENT_CREDITCARD = "Credit Card";
    public static String PAYMENT_DEBITCARD = "Debit Card";
    public static String PAYMENT_eVOUCHER = "e-Voucher";
    public static String PAYMENT_WALLET = "E-wallet";
    public static String PAYMENT_CREDITNOTE = "Credit Note";
    public static final int CHOOSE_MODE_OPTION = 0, E_VOUCHER_OPTION = 1, CASH_OPTION = 2, CREDIT_CARD_OPTION = 3, WALLET_OPTION = 4,
            DEBIT_CARD_OPTION = 5, CREDIT_NOTE_OPTION = 6;

    public PaymentMode() {
    }

    public PaymentMode(int paymentTypeID, String paymentTypeName) {
        PaymentTypeID = paymentTypeID;
        PaymentTypeName = paymentTypeName;
    }

    public int getPaymentTypeID() {
        return PaymentTypeID;
    }

    public void setPaymentTypeID(int paymentTypeID) {
        PaymentTypeID = paymentTypeID;
    }

    public String getPaymentTypeName() {
        return PaymentTypeName;
    }

    public void setPaymentTypeName(String paymentTypeName) {
        PaymentTypeName = paymentTypeName;
    }

    @Override
    public String toString() {
        return getPaymentTypeName();
    }
}
