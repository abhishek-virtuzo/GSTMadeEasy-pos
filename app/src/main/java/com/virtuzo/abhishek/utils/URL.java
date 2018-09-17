package com.virtuzo.abhishek.utils;

/**
 * Created by sbhar on 11/05/2016.
 */
public interface URL {

//    String GSTMadeEasyURL = "http://virtuzo.in/GSTMadeEasyAPI/POS_API.svc";
    String GSTMadeEasyURL="http://API.gstmadeeasy.net/POS_API.svc";

    String GSTUserLoginURL = GSTMadeEasyURL + "/ValidateUser";

    String GSTSendBrandsURL = GSTMadeEasyURL +"/SaveBrand";

    String GSTSendProductCategoryURL = GSTMadeEasyURL +"/SaveProductCategory";

    String GSTSendProductURL = GSTMadeEasyURL +"/SaveProduct";

    String GSTGetProductsURL = GSTMadeEasyURL + "/GetProducts";

    String GSTGetGroupProductsURL = "http://180.179.221.40/GSTAdminAPI/POS_API.svc/GetGroupProducts";

    String GSTGetOutletLogoURL = "http://180.179.221.40/GSTAdminAPI/POS_API.svc/GetLogo";

    String GSTGetHSNCodesURL = GSTMadeEasyURL +"/GetHSN";

    String GSTGetCustomersURL = GSTMadeEasyURL + "/GetCustomers";

    String GSTGetSuppliersURL = GSTMadeEasyURL + "/GetSuppliers";

    String GSTGetStatesURL = GSTMadeEasyURL + "/GetState";

    String GetGSTCategoryURL = GSTMadeEasyURL + "/GetGSTCategory";

    String GSTGetPaymentModeURL = GSTMadeEasyURL + "/GetPaymentMode";

    String GSTSendInvoicesURL = GSTMadeEasyURL + "/SaveInvoice";

    String GSTSendCustomersURL = GSTMadeEasyURL + "/SaveCustomer";

    String GSTSendPaymentsURL = GSTMadeEasyURL + "/SavePayment";

    String GSTGetInvoicesURL = GSTMadeEasyURL + "/GetInvoices";

    String GSTSendPurchasesURL = GSTMadeEasyURL + "/SavePurchase";

    String GSTGetPurchasesURL = GSTMadeEasyURL + "/GetPurchases";

    String GSTSendSuppliersURL = GSTMadeEasyURL + "/SaveSupplier";

    String GSTLatestApkURL = GSTMadeEasyURL + "/GetLatestApkVersion";

    String GSTSendEWayBillInfoURL = GSTMadeEasyURL + "/SaveEWayBillInfo";

    String GSTGetEWayBillReasonsURL = GSTMadeEasyURL + "/GetEWayBillReasons";

    String GSTGetEWayBillsURL = GSTMadeEasyURL + "/GetEWayBill";

    String GSTSendLogsURL = GSTMadeEasyURL + "/SaveLog";

    String GSTSendDeliveryOrdersURL = GSTMadeEasyURL + "/SaveDeliveryOrder";

    String GSTGetDeliveryOrdersURL = GSTMadeEasyURL + "/GetDeliveryOrders";

    String GSTGetSalesReturnsURL = GSTMadeEasyURL + "/GetSalesReturns";

    String GSTSendSalesReturnsURL = GSTMadeEasyURL + "/SaveSalesReturn";

    String GSTSendCreditNotesURL = GSTMadeEasyURL + "/SaveCreditDebitNote";

    String GSTGetCreditNotesURL = GSTMadeEasyURL + "/GetCreditDebitNotes";

    String GSTGetPurchaseReturnsURL = GSTMadeEasyURL + "/GetPurchaseReturns";

    String GSTSendPurchaseReturnsURL = GSTMadeEasyURL + "/SavePurchaseReturn";


//    String GSTSendDeliveryOrdersURL = "http://testapi.gstmadeeasy.net/POS_API.svc"+"/SaveDeliveryOrder";
//
//    String GSTGetDeliveryOrdersURL= "http://testapi.gstmadeeasy.net/POS_API.svc"+"/GetDeliveryOrders";
// http://virtuzo.in/GSTMadeEasyAPI/POS_API.svc/SavePurchase?BusinessId=1&Purchases=
}
