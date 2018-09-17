package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Adapters.PaymentAddedAdapter;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Payment;
import com.virtuzo.abhishek.modal.PaymentMode;

import java.util.ArrayList;

public class MultipleInvoicePaymentActivity extends AppCompatActivity {

    final int CHOOSE_MODE_OPTION = 0, E_VOUCHER_OPTION = 1, CASH_OPTION = 2, CREDIT_CARD_OPTION = 3, WALLET_OPTION = 4,
            DEBIT_CARD_OPTION = 5, CREDIT_NOTE_OPTION = 6;
    ArrayList<Payment> paymentArrayList;
    Button payNowButton, payLaterButton;
    String InvoiceSeries, TabCode, PaymentReceiptCode = "RCPT";
    String invoiceNumber, invoiceDate;
    double totalAmount, masterSalesID;
    TextView invoiceNumberTextView, totalAmountTextView;
    TextView paidAmountTextView;
    String receiptNumber;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    String callingActivity;
    CheckBox email_check, sms_check;
    PaymentAddedAdapter paymentAddedAdapter;
    RecyclerView recyclerView;
    String rupeeSymbol = "\u20B9";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multiple_invoice_payment);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.actionBarTitle);
        title.setText("Invoice Payment");

//        String s = getCallingActivity().getClassName();
//        Log.i("Calling Activity", s);
//        String s1 = NewInvoiceActivity.class.getName();
//        Log.i("Another Call Activity", s1);
        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        InvoiceSeries = setting.getString("InvoiceSeries", "");
        TabCode = setting.getString("TabCode", "");
        callingActivity = getIntent().getExtras().getString(MyFunctions.CLASS_NAME, "");

        payNowButton = (Button) findViewById(R.id.btn_paynow);
        payLaterButton = (Button) findViewById(R.id.btn_paylater);

        // upper screen
        invoiceNumberTextView = (TextView) findViewById(R.id.invoiceNumberTextView);
        totalAmountTextView = (TextView) findViewById(R.id.totalAmountTextView);
        paidAmountTextView = (TextView) findViewById(R.id.paidAmountTextView);

        invoiceNumber = getIntent().getExtras().getString("salesReturnInvoiceNumber", "");
        invoiceDate = getIntent().getExtras().getString("invoiceDate", "");
        totalAmount = getIntent().getExtras().getDouble("totalAmount");
        Log.i("Total Amount", totalAmount+"");
        Log.i("Invoice Number", invoiceNumber);
        masterSalesID = getIntent().getExtras().getDouble("masterSalesID");
        invoiceNumberTextView.setText(invoiceNumber);
        totalAmountTextView.setText("Payable Amount : " + rupeeSymbol + " " + String.format("%.2f", totalAmount));
        paymentArrayList = new ArrayList<>();
        updatePaidAmount();

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final double paidAmount = amountAlreadyPaid();
                if (paidAmount == 0.0){
                    showOkDialog("No Payment Done");
                    return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MultipleInvoicePaymentActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.invoice_payment_confirm_dialog, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Button yesButton = (Button) view.findViewById(R.id.yesClick);
                Button noButton = (Button) view.findViewById(R.id.noClick);
                TextView paymentAmountTextView = (TextView) view.findViewById(R.id.paymentAmount);
                final double finalPayAmount = paidAmount;
                paymentAmountTextView.setText(rupeeSymbol + " " + String.format("%.2f", finalPayAmount));

                if(finalPayAmount > totalAmount) {
                    TextView balanceAmountTextView = (TextView) view.findViewById(R.id.balanceAmount);
                    balanceAmountTextView.setVisibility(View.VISIBLE);
                    balanceAmountTextView.setText("Balance to return : " + rupeeSymbol + " " + String.format("%.2f", finalPayAmount - totalAmount));
                }

                yesButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        double extraAmount = finalPayAmount - totalAmount;
                        if(extraAmount > 0.0) {
                            for (Payment payment : paymentArrayList) {
                                if(payment.getPaymentTypeID() == CASH_OPTION) {
                                    payment.setAmount(payment.getAmount() - extraAmount);
                                    extraAmount = 0.0;
                                    break;
                                }
                            }
                        }
                        if (extraAmount > 0.0) {
                            Payment payment = createPaymentObject(-1*extraAmount, CASH_OPTION, "", "");
                            paymentArrayList.add(payment);
                        }
                        for (Payment payment : paymentArrayList) {
                            DatabaseHandler.getInstance().addNewPayment(payment);
                        }
                        goBack(paidAmount);
                    }
                });

                noButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });
            }
        });

        payLaterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double payAmount = 0.0;
                sendIntent(payAmount);
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.paymentListRecyclerView);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(this, 1);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        paymentAddedAdapter = new PaymentAddedAdapter(paymentArrayList, new PaymentAddedAdapter.OnClickListener() {
            @Override
            public void onItemClick(Payment payment) {
                updateCurrentPayment(payment);
            }

            @Override
            public void onCancelItem(int position) {

            }
        });
        recyclerView.setAdapter(paymentAddedAdapter);

        receiptNumber = createReceiptNumber();
        Log.i("Receipt Number", receiptNumber);

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();

//        email_check=(CheckBox)findViewById(R.id.email_checkbox);
//        sms_check=(CheckBox)findViewById(R.id.sms_checkbox);
//
//        email_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(email_check.isChecked()){
//                    if( new ConnectionDetector(getApplicationContext()).isConnectingToInternet()){
//
//                    }
//                    else {
//                        email_check.setChecked(false);
//                        showOkDialog("Please Check Your Internet Connectivity");
//                    }
//                }
//            }
//        });
//
//        sms_check.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if(sms_check.isChecked()){
//                    if( new ConnectionDetector(getApplicationContext()).isConnectingToInternet()){
//
//                    }
//                    else {
//                        sms_check.setChecked(false);
//                        showOkDialog("Please Check Your Internet Connectivity");
//                    }
//                }
//            }
//        });

    }

    private boolean isInvalidPayAmountEntered(double payAmount) {
        if(payAmount > amountLeftToPay()) {
            showOkDialog("Amount entered is greater than pay amount");
            return true;
        }
        return false;
    }

    private double amountLeftToPay() {
        return totalAmount - amountAlreadyPaid();
    }

    private double amountAlreadyPaid() {
        double total = 0.0;
        for (Payment payment : paymentArrayList) {
            total += payment.getAmount();
        }
        return total;
    }

    private void updatePaidAmount() {
        double paidAmount = amountAlreadyPaid();
        paidAmountTextView.setText("Total Paid : " + rupeeSymbol + " " + String.format("%.2f", paidAmount));
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    private void goBack(final double paidAmount) {
        if(callingActivity.equals(MyFunctions.CLASS_INVOICELIST_FOR_PAYMENT)) {
            if(paymentArrayList.size() > 0) {
                String receiptString = getReceiptString(paidAmount);
                Log.i("Print", receiptString);
                printThroughPrinter(receiptString);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(MultipleInvoicePaymentActivity.this);
            final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
            builder.setView(view);
            final AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();
            Button okButton = (Button) view.findViewById(R.id.okClick);
            TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
            okMessageTextView.setText("Payment Done Successfully!!");
            okButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    sendIntent(paidAmount);
                }
            });
        } else if(callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE) || callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE_FOR_DO)) {
            sendIntent(paidAmount);
        }
    }

    private void sendIntent(double paidAmount) {
        Intent intent = new Intent();
//        intent.putExtra("paidAmount", paidAmount - balanceReturned);
        intent.putExtra("cashTendered", paidAmount);
//        intent.putExtra("balanceReturned", balanceReturned);
        intent.putExtra("masterSalesID", masterSalesID);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private String getReceiptString(double paidAmount) {
        StringBuilder builder = new StringBuilder();
        int lengthOfScreen = 48;
        int leftLength = 24;
        int rightLength = 24;
        String nextLine = "\n";
        builder.append(nextLine);
        String paymentReceiptString = "Payment Receipt";
        builder.append(MyFunctions.makeStringCentreAlign(paymentReceiptString, lengthOfScreen));
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", lengthOfScreen));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Payment Receipt Number :", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(paymentArrayList.get(0).getReceiptNumber(), rightLength));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Invoice Number : ", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(paymentArrayList.get(0).getInvoiceNumber(), rightLength));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Date : ", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(MyFunctions.getCurrentDateTime(), rightLength));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign(MyFunctions.drawLine("-", lengthOfScreen/2), lengthOfScreen));
        builder.append(nextLine);
        builder.append(nextLine);

        for (Payment payment : paymentArrayList) {
            builder.append(MyFunctions.makeStringLeftAlign("Payment Mode : ", leftLength));
            builder.append(MyFunctions.makeStringRightAlign(payment.getPaymentMode(), rightLength));
            builder.append(nextLine);
            builder.append(MyFunctions.makeStringLeftAlign("Amount : ", leftLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", payment.getAmount()), rightLength));
            builder.append(nextLine);
            switch (payment.getPaymentTypeID()) {
                case CREDIT_CARD_OPTION:
                case DEBIT_CARD_OPTION:
                    builder.append(MyFunctions.makeStringLeftAlign("Card Number : ", leftLength));
                    builder.append(MyFunctions.makeStringRightAlign(payment.getCreditCardNumber(), rightLength));
                    builder.append(nextLine);
                    builder.append(MyFunctions.makeStringLeftAlign("Receipt Number : ", leftLength));
                    builder.append(MyFunctions.makeStringRightAlign(payment.getReferenceNumber(), rightLength));
                    builder.append(nextLine);
                    break;

                case WALLET_OPTION:
                    builder.append(MyFunctions.makeStringLeftAlign("Mobile Number : ", leftLength));
                    builder.append(MyFunctions.makeStringRightAlign(payment.getCreditCardNumber(), rightLength));
                    builder.append(nextLine);
                    break;

                case E_VOUCHER_OPTION:
                    builder.append(MyFunctions.makeStringLeftAlign("Reference Number : ", leftLength));
                    builder.append(MyFunctions.makeStringRightAlign(payment.getCreditCardNumber(), rightLength));
                    builder.append(nextLine);
                    break;
            }
            builder.append(nextLine);
        }

        builder.append(MyFunctions.makeStringCentreAlign(MyFunctions.drawLine("-", lengthOfScreen/2), lengthOfScreen));
        builder.append(nextLine);
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Cash Tendered", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", paidAmount), rightLength));
        builder.append(nextLine);

        if(paidAmount - totalAmount > 0) {
            builder.append(MyFunctions.makeStringLeftAlign("Balance Returned", leftLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", -1*(paidAmount - totalAmount)), rightLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.drawLine("-", lengthOfScreen));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your business***",48));
        builder.append(nextLine);
        builder.append(nextLine);
        return builder.toString();
    }

    private String createReceiptNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append(this.InvoiceSeries);
        builder.append("-");
        builder.append(this.TabCode);
        builder.append("/");
        builder.append(this.PaymentReceiptCode);
        builder.append("/");
        String previousReceiptNumber = DatabaseHandler.getInstance().getPreviousPaymentReceiptNumber(builder.toString());
        if(previousReceiptNumber == null) {
            builder.append("0000001");
        } else {
            String seriesNumberString = previousReceiptNumber.substring(previousReceiptNumber.length() - 7);
            int seriesNumber = Integer.parseInt(seriesNumberString);
            seriesNumber++;
            seriesNumberString = "0000000" + seriesNumber;
            seriesNumberString = seriesNumberString.substring(seriesNumberString.length() - 7);
            builder.append("" + seriesNumberString);
        }
        return builder.toString();
    }

    private boolean isPaymentAgainstSyncedInvoice() {
        if(DatabaseHandler.getInstance().isInvoiceSynced(this.invoiceNumber)) {
            return true;
        }
        return false;
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(MultipleInvoicePaymentActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(MultipleInvoicePaymentActivity.this,mHandler);
        u_infor = new int[5][2];
        u_infor[0][0] = 0x1CBE;
        u_infor[0][1] = 0x0003;
        u_infor[1][0] = 0x1CB0;
        u_infor[1][1] = 0x0003;
        u_infor[2][0] = 0x0483;
        u_infor[2][1] = 0x5740;
        u_infor[3][0] = 0x0493;
        u_infor[3][1] = 0x8760;
        u_infor[4][0] = 0x0471;
        u_infor[4][1] = 0x0055;
    }

    private boolean CheckUsbPermission(){
        if( dev != null ){
            if( usbCtrl.isHasPermission(dev)){
                return true;
            }
        }

        Toast.makeText(MultipleInvoicePaymentActivity.this, getString(R.string.usb_msg_conn_state),
                Toast.LENGTH_SHORT).show();
        return false;
    }

    private void connectThroughPrinter() {
        usbCtrl.close();
        int  i = 0;
        for( i = 0 ; i < 5 ; i++ ){
            dev = usbCtrl.getDev(u_infor[i][0],u_infor[i][1]);
            if(dev != null)
                break;
        }
        if( dev != null ){
            if( !(usbCtrl.isHasPermission(dev))){
                usbCtrl.getPermission(dev);
            }else{
                Toast.makeText(MultipleInvoicePaymentActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
//                printThroughPrinter(receiptString);
            }
        }
    }

    private void printThroughPrinter(String receiptString) {
        byte isHasPaper;
        byte[] cmd = null;
        if( dev != null ){
            if( usbCtrl.isHasPermission(dev)){
                Toast.makeText(MultipleInvoicePaymentActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(MultipleInvoicePaymentActivity.this, "The printer has no paper",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String txt_msg = receiptString;
                if( CheckUsbPermission() == true ){
                    usbCtrl.sendMsg(txt_msg, "GBK", dev);
                    usbCtrl.cutPaper(dev, 100);
                    usbCtrl.openCashBox(dev);
                }
            }
        }
        usbCtrl.close();
    }

    public void showOkDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MultipleInvoicePaymentActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText(heading);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    @Override
    public void onBackPressed() {
    }

    public void addNewPayment(final int paymentMode) {
        double amountLeftToPay = amountLeftToPay();
        if (amountLeftToPay <= 0.0) {
            showOkDialog("Payment Done");
            return;
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MultipleInvoicePaymentActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_invoice_payment, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button saveClick = (Button) view.findViewById(R.id.saveClick);
        Button cancelClick = (Button) view.findViewById(R.id.cancelClick);
        final EditText amountEditText = (EditText) view.findViewById(R.id.amountEditText);
        final EditText cardNumberEditText = (EditText) view.findViewById(R.id.cardNumberEditText);
        final EditText receiptNumberEditText = (EditText) view.findViewById(R.id.receiptNumberEditText);
        amountEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        cardNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        receiptNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });

        TextView paymentTitle = (TextView) view.findViewById(R.id.paymentTitle);
        TextView cardNumberTextView = (TextView) view.findViewById(R.id.cardNumberTextView);
        TextView receiptNumberTextView = (TextView) view.findViewById(R.id.receiptNumberTextView);

        amountEditText.setText(String.format("%.2f", amountLeftToPay));
        amountEditText.selectAll();

        switch (paymentMode) {
            case CASH_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_CASH + " Payment");
                cardNumberTextView.setVisibility(View.GONE);
                cardNumberEditText.setVisibility(View.GONE);
                receiptNumberTextView.setVisibility(View.GONE);
                receiptNumberEditText.setVisibility(View.GONE);
                break;

            case CREDIT_CARD_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_CREDITCARD + " Payment");
                break;

            case DEBIT_CARD_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_DEBITCARD + " Payment");
                break;

            case WALLET_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_WALLET + " Payment");
                cardNumberTextView.setText("Mobile Number");
                cardNumberEditText.setHint("Enter Mobile Number");
                receiptNumberTextView.setVisibility(View.GONE);
                receiptNumberEditText.setVisibility(View.GONE);
                break;

            case E_VOUCHER_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_eVOUCHER + " Payment");
                cardNumberTextView.setText("Reference Number");
                cardNumberEditText.setHint("Enter Reference Number");
                receiptNumberTextView.setVisibility(View.GONE);
                receiptNumberEditText.setVisibility(View.GONE);
                break;
        }

        saveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double payAmount = MyFunctions.parseDouble(amountEditText.getText().toString());
                String payAmountString = String.format("%.2f", payAmount);
                payAmount = MyFunctions.parseDouble(payAmountString);

                if(payAmount == 0) {
                    showOkDialog("Enter valid amount");
                    return;
                }

//                PaymentMode paymentMode = (PaymentMode) paymentModeSpinner.getSelectedItem();
//                if(paymentMode.getPaymentTypeID() == 0) {
//                    showOkDialog("Enter valid payment mode");
//                    return;
//                }
                switch (paymentMode) {
                    case CASH_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        break;

                    case CREDIT_CARD_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String ccNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(ccNumber) == 0) {
                            showOkDialog("Enter Credit Card Number");
                            return;
                        } else if(MyFunctions.StringLength(ccNumber) > 16) {
                            showOkDialog("Credit Card Number cannot be greater than 16");
                            return;
                        }
                        String ccReceipt = receiptNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(ccReceipt) == 0) {
                            showOkDialog("Enter Credit Card Receipt Number");
                            return;
                        }
                        break;

                    case DEBIT_CARD_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String dcNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(dcNumber) == 0) {
                            showOkDialog("Enter Debit Card Number");
                            return;
                        } else if(MyFunctions.StringLength(dcNumber) > 16) {
                            showOkDialog("Debit Card Number cannot be greater than 16");
                            return;
                        }
                        String dcReceipt = receiptNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(dcReceipt) == 0) {
                            showOkDialog("Enter Debit Card Receipt Number");
                            return;
                        }
                        break;

                    case E_VOUCHER_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String refNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(refNumber) == 0) {
                            showOkDialog("Enter Reference Number");
                            return;
                        } else if(MyFunctions.StringLength(refNumber) > 15) {
                            showOkDialog("Reference Number cannot be greater than 15");
                            return;
                        }
                        break;

                    case WALLET_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String mobNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(mobNumber) == 0) {
                            showOkDialog("Enter Mobile Number");
                            return;
                        } else if(MyFunctions.StringLength(mobNumber) > 10) {
                            showOkDialog("Mobile Number cannot be greater than 10");
                            return;
                        }
//                        showOkDialog("Payment Option unavailable");
//                        return;
                        break;

                    case CREDIT_NOTE_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        showOkDialog("Payment Option unavailable");
                        return;
                }

                String cardNumber = cardNumberEditText.getText().toString();
                String referenceNumber = receiptNumberEditText.getText().toString();
                Payment payment = createPaymentObject(payAmount, paymentMode, cardNumber, referenceNumber);
                paymentArrayList.add(payment);
                updatePaidAmount();
                paymentAddedAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void updateCurrentPayment(final Payment payment) {
        final int paymentMode = payment.getPaymentTypeID();
        double amount = payment.getAmount();

        AlertDialog.Builder builder = new AlertDialog.Builder(MultipleInvoicePaymentActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.dialog_invoice_payment, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button saveClick = (Button) view.findViewById(R.id.saveClick);
        Button cancelClick = (Button) view.findViewById(R.id.cancelClick);
        final EditText amountEditText = (EditText) view.findViewById(R.id.amountEditText);
        final EditText cardNumberEditText = (EditText) view.findViewById(R.id.cardNumberEditText);
        final EditText receiptNumberEditText = (EditText) view.findViewById(R.id.receiptNumberEditText);
        amountEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        cardNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        receiptNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });

        TextView paymentTitle = (TextView) view.findViewById(R.id.paymentTitle);
        TextView cardNumberTextView = (TextView) view.findViewById(R.id.cardNumberTextView);
        TextView receiptNumberTextView = (TextView) view.findViewById(R.id.receiptNumberTextView);

        switch (paymentMode) {
            case CASH_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_CASH + " Payment");
                cardNumberTextView.setVisibility(View.GONE);
                cardNumberEditText.setVisibility(View.GONE);
                receiptNumberTextView.setVisibility(View.GONE);
                receiptNumberEditText.setVisibility(View.GONE);
                break;

            case CREDIT_CARD_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_CREDITCARD + " Payment");
                break;

            case DEBIT_CARD_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_DEBITCARD + " Payment");
                break;

            case WALLET_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_WALLET + " Payment");
                cardNumberTextView.setText("Mobile Number");
                cardNumberEditText.setHint("Enter Mobile Number");
                receiptNumberTextView.setVisibility(View.GONE);
                receiptNumberEditText.setVisibility(View.GONE);
                break;

            case E_VOUCHER_OPTION:
                paymentTitle.setText(PaymentMode.PAYMENT_eVOUCHER + " Payment");
                cardNumberTextView.setText("Voucher Number");
                cardNumberEditText.setHint("Enter Voucher Number");
                receiptNumberTextView.setVisibility(View.GONE);
                receiptNumberEditText.setVisibility(View.GONE);
                break;
        }

        amountEditText.setText(String.format("%.2f", amount));
        cardNumberEditText.setText(payment.getCreditCardNumber());
        receiptNumberEditText.setText(payment.getReferenceNumber());
        amountEditText.selectAll();

        saveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                double payAmount = MyFunctions.parseDouble(amountEditText.getText().toString());
                String payAmountString = String.format("%.2f", payAmount);
                payAmount = MyFunctions.parseDouble(payAmountString);

                if(payAmount == 0) {
                    showOkDialog("Enter valid amount");
                    return;
                }

//                PaymentMode paymentMode = (PaymentMode) paymentModeSpinner.getSelectedItem();
//                if(paymentMode.getPaymentTypeID() == 0) {
//                    showOkDialog("Enter valid payment mode");
//                    return;
//                }
                switch (paymentMode) {
                    case CASH_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        break;

                    case CREDIT_CARD_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String ccNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(ccNumber) == 0) {
                            showOkDialog("Enter Credit Card Number");
                            return;
                        } else if(MyFunctions.StringLength(ccNumber) > 16) {
                            showOkDialog("Credit Card Number cannot be greater than 16");
                            return;
                        }
                        String ccReceipt = receiptNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(ccReceipt) == 0) {
                            showOkDialog("Enter Credit Card Receipt Number");
                            return;
                        }
                        break;

                    case DEBIT_CARD_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String dcNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(dcNumber) == 0) {
                            showOkDialog("Enter Debit Card Number");
                            return;
                        } else if(MyFunctions.StringLength(dcNumber) > 16) {
                            showOkDialog("Debit Card Number cannot be greater than 16");
                            return;
                        }
                        String dcReceipt = receiptNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(dcReceipt) == 0) {
                            showOkDialog("Enter Debit Card Receipt Number");
                            return;
                        }
                        break;

                    case E_VOUCHER_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String refNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(refNumber) == 0) {
                            showOkDialog("Enter Reference Number");
                            return;
                        } else if(MyFunctions.StringLength(refNumber) > 15) {
                            showOkDialog("Reference Number cannot be greater than 15");
                            return;
                        }
                        break;

                    case WALLET_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        String mobNumber = cardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(mobNumber) == 0) {
                            showOkDialog("Enter Mobile Number");
                            return;
                        } else if(MyFunctions.StringLength(mobNumber) > 10) {
                            showOkDialog("Mobile Number cannot be greater than 10");
                            return;
                        }
//                        showOkDialog("Payment Option unavailable");
//                        return;
                        break;

                    case CREDIT_NOTE_OPTION:
//                        if(isInvalidPayAmountEntered(payAmount)) {
//                            return;
//                        }
                        showOkDialog("Payment Option unavailable");
                        return;
                }

                String cardNumber = cardNumberEditText.getText().toString();
                String referenceNumber = receiptNumberEditText.getText().toString();
                payment.setAmount(payAmount);
                payment.setCreditCardNumber(cardNumber);
                payment.setReferenceNumber(referenceNumber);
                updatePaidAmount();
                paymentAddedAdapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        cancelClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private Payment createPaymentObject(double payAmount, int paymentMode, String cardNumber, String referenceNumber) {
        Payment payment = new Payment();
        payment.setInvoiceNumber(this.invoiceNumber);
        payment.setReceiptNumber(this.receiptNumber);
        switch (paymentMode) {
            case E_VOUCHER_OPTION:
                payment.setPaymentMode(PaymentMode.PAYMENT_eVOUCHER);
                break;

            case CASH_OPTION:
                payment.setPaymentMode(PaymentMode.PAYMENT_CASH);
                break;

            case CREDIT_CARD_OPTION:
                payment.setPaymentMode(PaymentMode.PAYMENT_CREDITCARD);
                break;

            case WALLET_OPTION:
                payment.setPaymentMode(PaymentMode.PAYMENT_WALLET);
                break;

            case DEBIT_CARD_OPTION:
                payment.setPaymentMode(PaymentMode.PAYMENT_DEBITCARD);
                break;

            case CREDIT_NOTE_OPTION:
                payment.setPaymentMode(PaymentMode.PAYMENT_CREDITNOTE);
                break;

        }
        payment.setAmount(payAmount);
        payment.setReferenceNumber(referenceNumber);
        payment.setPaymentTypeID(paymentMode);
        payment.setCreditCardNumber(cardNumber);
        payment.setCreditCardType("");
        if(isPaymentAgainstSyncedInvoice()) {
            payment.setSynced(2);
        }
        if(callingActivity.equals(MyFunctions.CLASS_INVOICELIST_FOR_PAYMENT)) {
            payment.setPaymentDtTm(MyFunctions.getCurrentDateTime());
        } else if(callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE) || callingActivity.equals(MyFunctions.CLASS_NEW_INVOICE_FOR_DO)) {
            payment.setPaymentDtTm(invoiceDate);
        }
        return payment;
    }

    public void cashButtonClick(View view) {
        boolean cashDone = false;
        for (Payment payment : paymentArrayList) {
            if (payment.getPaymentTypeID() == CASH_OPTION) {
                updateCurrentPayment(payment);
                cashDone = true;
                break;
            }
        }
        if (!cashDone) {
            addNewPayment(CASH_OPTION);
        }
    }

    public void creditCardButtonClick(View view) {
        addNewPayment(CREDIT_CARD_OPTION);
    }

    public void debitCardButtonClick(View view) {
        addNewPayment(DEBIT_CARD_OPTION);
    }

    public void walletButtonClick(View view) {
        addNewPayment(WALLET_OPTION);
    }

    public void voucherButtonClick(View view) {
        addNewPayment(E_VOUCHER_OPTION);
    }
}
