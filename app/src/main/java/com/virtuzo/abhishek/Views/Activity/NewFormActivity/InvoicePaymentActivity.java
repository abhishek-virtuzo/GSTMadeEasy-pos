package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.ConnectionDetector;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ListActivity.InvoiceListForPaymentActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Payment;
import com.virtuzo.abhishek.modal.PaymentMode;

import java.util.ArrayList;

public class InvoicePaymentActivity extends AppCompatActivity {

    LinearLayout cashlayout,cclayout,dclayout,evlayout;
    final int CHOOSE_MODE_OPTION = 0, E_VOUCHER_OPTION = 1, CASH_OPTION = 2, CREDIT_CARD_OPTION = 3, WALLET_OPTION = 4,
            DEBIT_CARD_OPTION = 5, CREDIT_NOTE_OPTION = 6;
    ArrayList payment_mode_list;
    Button payNowButton, payLaterButton;
    Spinner spinner;
    String InvoiceSeries, TabCode, PaymentReceiptCode = "RCPT";
    String invoiceNumber, invoiceDate;
    double totalAmount, masterSalesID;
    TextView invoiceNumberTextView, totalAmountTextView, balanceAmountTextView;
    EditText payAmountEditText, creditCardNumberEditText, creditCardReceiptNumberEditText;
    EditText debitCardNumberEditText, debitCardReceiptNumberEditText;
    RadioGroup radioGroup;
    String receiptNumber;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    String callingActivity;
    CheckBox email_check, sms_check;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invoice_payment);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.actionBarTitle);
        title.setText("Invoice Payment");

        String s = getCallingActivity().getClassName();
        Log.i("Calling Activity", s);
        String s1 = NewInvoiceActivity.class.getName();
        Log.i("Another Call Activity", s1);
        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        InvoiceSeries = setting.getString("InvoiceSeries", "");
        TabCode = setting.getString("TabCode", "");
        callingActivity =  getCallingActivity().getClassName();

        cashlayout=(LinearLayout) findViewById(R.id.cash_layout);
        cclayout=(LinearLayout)findViewById(R.id.credit_layout);
        dclayout=(LinearLayout)findViewById(R.id.debit_layout);
        evlayout=(LinearLayout)findViewById(R.id.evoucher_layout);
        payNowButton = (Button) findViewById(R.id.btn_paynow);
        payLaterButton = (Button) findViewById(R.id.btn_paylater);

        // upper screen
        invoiceNumberTextView = (TextView) findViewById(R.id.invoiceNumber);
        totalAmountTextView = (TextView) findViewById(R.id.totalAmount);
        payAmountEditText = (EditText) findViewById(R.id.payAmount);

        // cash layout
        balanceAmountTextView = (TextView) findViewById(R.id.balanceAmount);
        radioGroup = (RadioGroup) findViewById(R.id.radioGroup);
        spinner = (Spinner) findViewById(R.id.payment_method);

        // credit card layout
        creditCardNumberEditText = (EditText) findViewById(R.id.cc_number);
        creditCardReceiptNumberEditText = (EditText) findViewById(R.id.cc_receipt);
        creditCardReceiptNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });

        // debit card layout
        debitCardNumberEditText = (EditText) findViewById(R.id.dc_number);
        debitCardReceiptNumberEditText = (EditText) findViewById(R.id.dc_receipt);
        debitCardReceiptNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });

        invoiceNumber = getIntent().getExtras().getString("salesReturnInvoiceNumber", "");
        invoiceDate = getIntent().getExtras().getString("invoiceDate", "");
        totalAmount = getIntent().getExtras().getDouble("totalAmount");
        Log.i("Total Amount", totalAmount+"");
        Log.i("Invoice Number", invoiceNumber);
        masterSalesID = getIntent().getExtras().getDouble("masterSalesID");
        invoiceNumberTextView.setText(invoiceNumber);
        totalAmountTextView.setText("Pay : " + String.format("%.2f", totalAmount));
        payAmountEditText.setText(String.format("%.2f", totalAmount));
        payAmountEditText.selectAll();
        payAmountEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //TODO handle totoal amount error when enter .
                double payAmount = 0.0;
                String payAmountString = payAmountEditText.getText().toString();
                if (MyFunctions.StringLength(payAmountString) > 0) {
                    payAmount = MyFunctions.parseDouble(payAmountString);
                }
                double balanceAmount = totalAmount - payAmount;
                balanceAmountTextView.setText(String.format("%.2f", balanceAmount));
//                if (payAmount > totalAmount) {
//                    radioGroup.setVisibility(View.VISIBLE);
//                } else {
//                    radioGroup.setVisibility(View.INVISIBLE);
//                }
                radioGroup.setVisibility(View.INVISIBLE);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        payNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int position = spinner.getSelectedItemPosition();
                if(payAmountEditText.getText().length() == 0) {
                    showOkDialog("Enter valid amount");
                    return;
                }
                double payAmount = MyFunctions.parseDouble(payAmountEditText.getText().toString());
                String payAmountString = String.format("%.2f", payAmount);
                payAmount = MyFunctions.parseDouble(payAmountString);

                if(payAmount == 0) {
                    showOkDialog("Enter valid amount");
                    return;
                }

                switch (position) {
                    case CASH_OPTION:
//                        int id = radioGroup.getCheckedRadioButtonId();
//                        int TRANSACTION_MODE_NONE = -1, TRANSACTION_MODE_DEPOSIT = 0, TRANSACTION_MODE_RETURN = 1;
//                        if(payAmount > totalAmount) {
//                            if (id == TRANSACTION_MODE_NONE) {
//                                showOkDialog("Select Transaction Type");
//                                return;
//                            }
//                        }
//                        View radioButton = radioGroup.findViewById(id);
//                        int radioId = radioGroup.indexOfChild(radioButton);
//                        if(radioId == TRANSACTION_MODE_RETURN) {
//                            if(payAmount > totalAmount) {
//                                payAmount = totalAmount;
//                            }
//                        }
                        break;

                    case CREDIT_CARD_OPTION:
                        if(isInvalidPayAmountEntered(payAmount)) {
                            return;
                        }
                        String ccNumber = creditCardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(ccNumber) == 0) {
                            showOkDialog("Enter Credit Card Number");
                            return;
                        } else if(MyFunctions.StringLength(ccNumber) > 16) {
                            showOkDialog("Credit Card Number cannot be greater than 16");
                            return;
                        }
                        String ccReceipt = creditCardReceiptNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(ccReceipt) == 0) {
                            showOkDialog("Enter Credit Card Receipt Number");
                            return;
                        }
                        break;

                    case DEBIT_CARD_OPTION:
                        if(isInvalidPayAmountEntered(payAmount)) {
                            return;
                        }
                        String dcNumber = debitCardNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(dcNumber) == 0) {
                            showOkDialog("Enter Debit Card Number");
                            return;
                        } else if(MyFunctions.StringLength(dcNumber) > 16) {
                            showOkDialog("Debit Card Number cannot be greater than 16");
                            return;
                        }
                        String dcReceipt = debitCardReceiptNumberEditText.getText().toString();
                        if(MyFunctions.StringLength(dcReceipt) == 0) {
                            showOkDialog("Enter Debit Card Receipt Number");
                            return;
                        }
                        break;

                    case E_VOUCHER_OPTION:
                        if(isInvalidPayAmountEntered(payAmount)) {
                            return;
                        }
                        showOkDialog("Payment Option unavailable");
                        return;

                    case WALLET_OPTION:
                        if(isInvalidPayAmountEntered(payAmount)) {
                            return;
                        }
                        showOkDialog("Payment Option unavailable");
                        return;

                    case CREDIT_NOTE_OPTION:
                        if(isInvalidPayAmountEntered(payAmount)) {
                            return;
                        }
                        showOkDialog("Payment Option unavailable");
                        return;
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(InvoicePaymentActivity.this);
                final View view = getLayoutInflater().inflate(R.layout.invoice_payment_confirm_dialog, null);
                builder.setView(view);
                final AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                Button yesButton = (Button) view.findViewById(R.id.yesClick);
                Button noButton = (Button) view.findViewById(R.id.noClick);
                TextView paymentAmountTextView = (TextView) view.findViewById(R.id.paymentAmount);
                String rupeeSymbol = "\u20B9";
                final double finalPayAmount = payAmount;
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
                        // invoice confirmed
                        choosePaymentOption(position, finalPayAmount);
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
                sendIntent(payAmount, 0.0);
            }
        });

        payment_mode_list = new ArrayList<PaymentMode>();
        payment_mode_list.add(new PaymentMode(0, "--Choose payment mode--"));
        payment_mode_list.addAll(DatabaseHandler.getInstance().getPaymentModeListFromDB());
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, payment_mode_list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinner.setAdapter(adapter);
        spinner.setSelection(CASH_OPTION);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                cashlayout.setVisibility(View.GONE);
                cclayout.setVisibility(View.GONE);
                dclayout.setVisibility(View.GONE);
                evlayout.setVisibility(View.GONE);
                payNowButton.setVisibility(View.VISIBLE);

                switch(position) {
                    case CHOOSE_MODE_OPTION:
                        payNowButton.setVisibility(View.GONE);
                        break;

                    case CASH_OPTION:
                        cashlayout.setVisibility(View.VISIBLE);
                        break;

                    case CREDIT_CARD_OPTION:
                        cclayout.setVisibility(View.VISIBLE);
                        break;

                    case DEBIT_CARD_OPTION:
                        dclayout.setVisibility(View.VISIBLE);
                        break;

                    case E_VOUCHER_OPTION:
                        evlayout.setVisibility(View.VISIBLE);
                        payNowButton.setVisibility(View.INVISIBLE);
                        break;

                    case CREDIT_NOTE_OPTION:
                        payNowButton.setVisibility(View.INVISIBLE);
                        showOkDialog("Option Unavailable");
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing, just another required interface callback
            }

        });

        receiptNumber = createReceiptNumber();
        Log.i("Receipt Number", receiptNumber);

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();

        email_check=(CheckBox)findViewById(R.id.email_checkbox);
        sms_check=(CheckBox)findViewById(R.id.sms_checkbox);

        email_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(email_check.isChecked()){
                    if( new ConnectionDetector(getApplicationContext()).isConnectingToInternet()){

                    }
                    else {
                        email_check.setChecked(false);
                        showOkDialog("Please Check Your Internet Connectivity");
                    }
                }
            }
        });

        sms_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(sms_check.isChecked()){
                    if( new ConnectionDetector(getApplicationContext()).isConnectingToInternet()){

                    }
                    else {
                        sms_check.setChecked(false);
                        showOkDialog("Please Check Your Internet Connectivity");
                    }
                }
            }
        });

    }

    private boolean isInvalidPayAmountEntered(double payAmount) {
        if(payAmount > totalAmount) {
            showOkDialog("Amount entered is greater than total amount");
            return true;
        }
        return false;
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    private void goBack(Payment payment, final double paidAmount, final double balanceAmount, boolean isCashPayment) {
        if(callingActivity.equals(InvoiceListForPaymentActivity.class.getName())) {
            if(payment != null) {
                String receiptString = getReceiptString(payment, paidAmount, balanceAmount, isCashPayment);
                Log.i("Print", receiptString);
                printThroughPrinter(receiptString);
            }
            AlertDialog.Builder builder = new AlertDialog.Builder(InvoicePaymentActivity.this);
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
                    sendIntent(paidAmount, balanceAmount);
                }
            });
        } else if(callingActivity.equals(NewInvoiceActivity.class.getName()) || callingActivity.equals(NewSalesInvoiceFromDOActivity.class.getName())) {
            sendIntent(paidAmount, balanceAmount);
        }
    }

    private void sendIntent(double paidAmount, double balanceReturned) {
        Intent intent = new Intent();
        intent.putExtra("paidAmount", paidAmount - balanceReturned);
        intent.putExtra("cashTendered", paidAmount);
        intent.putExtra("balanceReturned", balanceReturned);
        intent.putExtra("masterSalesID", masterSalesID);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    private String getReceiptString(Payment payment, double paidAmount, double balanceAmount, boolean isCashPayment) {
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
        builder.append(MyFunctions.makeStringLeftAlign("Receipt Number : ", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(payment.getReceiptNumber(), rightLength));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Invoice Number : ", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(payment.getInvoiceNumber(), rightLength));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Date : ", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(MyFunctions.getCurrentDateTime(), rightLength));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Payment Mode : ", leftLength));
        builder.append(MyFunctions.makeStringRightAlign(payment.getPaymentMode(), rightLength));
        builder.append(nextLine);

        if(isCashPayment) {
            builder.append(MyFunctions.makeStringLeftAlign("Cash Tendered : ", leftLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", paidAmount), rightLength));
            builder.append(nextLine);
            builder.append(MyFunctions.makeStringLeftAlign("Balance Returned : ", leftLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", balanceAmount), rightLength));
            builder.append(nextLine);
        } else {
            builder.append(MyFunctions.makeStringLeftAlign("Amount : ", leftLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", payment.getAmount()), rightLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.drawLine("-", lengthOfScreen));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your business***",48));
        builder.append(nextLine);
        builder.append(nextLine);
        return  builder.toString();
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

    private Payment createPayment(double payAmount, String paymentMode,String cardNumber, String referenceNumber) {
        Payment payment = new Payment();
        payment.setInvoiceNumber(this.invoiceNumber);
        payment.setReceiptNumber(this.receiptNumber);
        payment.setPaymentMode(paymentMode);
        payment.setAmount(payAmount);
        payment.setReferenceNumber(referenceNumber);
        payment.setPaymentTypeID(((PaymentMode)spinner.getSelectedItem()).getPaymentTypeID());
        payment.setCreditCardNumber(cardNumber);
        payment.setCreditCardType("");
        if(isPaymentAgainstSyncedInvoice()) {
            payment.setSynced(2);
        }
        if(callingActivity.equals(InvoiceListForPaymentActivity.class.getName())) {
            payment.setPaymentDtTm(MyFunctions.getCurrentDateTime());
        } else if(callingActivity.equals(NewInvoiceActivity.class.getName()) || callingActivity.equals(NewSalesInvoiceFromDOActivity.class.getName())) {
            payment.setPaymentDtTm(invoiceDate);
        }
        return payment;
    }

    private boolean isPaymentAgainstSyncedInvoice() {
        if(DatabaseHandler.getInstance().isInvoiceSynced(this.invoiceNumber)) {
            return true;
        }
        return false;
    }

    private void choosePaymentOption(int paymentOption, double payAmount) {
        switch (paymentOption) {
            case CASH_OPTION:
                cashPayment(payAmount);
                break;

            case CREDIT_CARD_OPTION:
                creditCardPayment(payAmount);
                break;

            case DEBIT_CARD_OPTION:
                debitCardPayment(payAmount);
                break;

            case E_VOUCHER_OPTION:
                eVoucherPayment(payAmount);
                break;

            case CREDIT_NOTE_OPTION:
                creditNotePayment(payAmount);
                break;
        }
    }

    private void cashPayment(double payAmount) {
        double balance = payAmount - totalAmount;
        if(balance > 0.0) {
            Payment payment = createPayment(totalAmount, "CASH", "", "");
            DatabaseHandler.getInstance().addNewPayment(payment);
            goBack(payment, payAmount, balance, true);
        } else {
            Payment payment = createPayment(payAmount, "CASH", "", "");
            DatabaseHandler.getInstance().addNewPayment(payment);
            goBack(payment, payAmount, 0.0, true);
        }
    }

    private void creditCardPayment(double payAmount) {
        String ccNumber = creditCardNumberEditText.getText().toString();
        String ccReceipt = creditCardReceiptNumberEditText.getText().toString();
        Payment payment = createPayment(payAmount, "CREDIT CARD", ccNumber, ccReceipt);
        DatabaseHandler.getInstance().addNewPayment(payment);
        goBack(payment, payAmount, 0.0, false);
    }

    private void debitCardPayment(double payAmount) {
        String dcNumber = debitCardNumberEditText.getText().toString();
        String dcReceipt = debitCardReceiptNumberEditText.getText().toString();
        Payment payment = createPayment(payAmount, "DEBIT CARD", dcNumber, dcReceipt);
        DatabaseHandler.getInstance().addNewPayment(payment);
        goBack(payment, payAmount, 0.0, false);
    }

    private void eVoucherPayment(double payAmount) {
    }

    private void creditNotePayment(double payAmount) {
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(InvoicePaymentActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(InvoicePaymentActivity.this,mHandler);
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

        Toast.makeText(InvoicePaymentActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(InvoicePaymentActivity.this, getString(R.string.usb_msg_getpermission),
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
                Toast.makeText(InvoicePaymentActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(InvoicePaymentActivity.this, "The printer has no paper",
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
        AlertDialog.Builder builder = new AlertDialog.Builder(InvoicePaymentActivity.this);
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
}
