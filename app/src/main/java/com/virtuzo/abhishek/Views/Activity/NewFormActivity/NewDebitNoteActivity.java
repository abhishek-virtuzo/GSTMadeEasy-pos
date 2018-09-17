package com.virtuzo.abhishek.Views.Activity.NewFormActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hoin.usbsdk.PrintPic;
import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ListActivity.SupplierListActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.CreditDebitNote;
import com.virtuzo.abhishek.modal.Supplier;
import com.virtuzo.abhishek.modal.MyFunctions;

import java.util.Calendar;

public class NewDebitNoteActivity extends AppCompatActivity {

    private static final int CHOOSE_CUSTOMER_RESPONSE_CODE = 105;
    private Supplier supplierSelected = null;
    Button chooseSupplierButton;
    TextView supplierDetailsTextView;
    LinearLayout amountLinearLayout;
    String InvoiceSeries, TabCode, LoginID, OutletID;
    EditText amountEditText, reasonEditText;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(NewDebitNoteActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_debit_note);

        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        InvoiceSeries = setting.getString("InvoiceSeries", "");
        TabCode = setting.getString("TabCode", "");
        LoginID = setting.getString("UserID", "");
        OutletID = setting.getString("OutletID", "");

        getSupportActionBar().hide();
        Toolbar toolbar_new_credit_note = (Toolbar) findViewById(R.id.toolbar_new_credit_note);
        toolbar_new_credit_note.setPadding(0, 0, 0, 0);//for tab otherwise give space in tab
        toolbar_new_credit_note.setContentInsetsAbsolute(0, 0);
        TextView heading = (TextView) toolbar_new_credit_note.findViewById(R.id.actionBarTitle);
        heading.setText("Create new Debit Note");
        toolbar_new_credit_note.setBackground(new ColorDrawable(ContextCompat.getColor(getApplicationContext(), android.R.color.holo_blue_bright)));

        chooseSupplierButton = (Button) findViewById(R.id.chooseSupplierButton);
        supplierDetailsTextView = (TextView) findViewById(R.id.supplierDetailsTextView);
        amountLinearLayout = (LinearLayout) findViewById(R.id.amountLinearLayout);
        amountEditText = (EditText) findViewById(R.id.amountEditText);
        reasonEditText = (EditText) findViewById(R.id.reasonEditText);
        
        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();

    }

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(NewDebitNoteActivity.this,mHandler);
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

        Toast.makeText(NewDebitNoteActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(NewDebitNoteActivity.this, getString(R.string.usb_msg_getpermission),
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
                Toast.makeText(NewDebitNoteActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(NewDebitNoteActivity.this, "The printer has no paper",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String txt_msg = receiptString;
                if( CheckUsbPermission() == true ){

//                    if(null!=MyFunctions.getImage()) {
//
//                        String imagePath = Environment.getExternalStoragePublicDirectory
//                                (
//                                        // Environment.DIRECTORY_PICTURES
//                                        Environment.DIRECTORY_DOCUMENTS + "/GSTMadeEasy/"
//                                ) + "/logo.png";
//                        printImage(imagePath);
//                    }
                    usbCtrl.sendMsg(txt_msg, "GBK", dev);
                    usbCtrl.cutPaper(dev, 100);
//                    if(openCashDrawer) {
//                        usbCtrl.openCashBox(dev);
//                    }
                }
            }
        }
        usbCtrl.close();
    }

    private void printImage(String imagePath) {
        int i = 0,s = 0,j = 0,index = 0;
        byte[] temp = new byte[56];
        byte[] sendData = null;
        PrintPic pg = new PrintPic();
        pg.initCanvas(384);
        pg.initPaint();
        pg.drawImage(100, 0, imagePath);
        sendData = pg.printDraw();

        for( i = 0 ; i < pg.getLength() ; i++ ){  //Ã¿¸ôÒ»ÐÐ¼ÓÉÏ°üÍ·£¬·¢ËÍÒ»´ÎÊý¾Ý
            s = 0;
            temp[s++] = 0x1D;
            temp[s++] = 0x76;
            temp[s++] = 0x30;
            temp[s++] = 0x00;
            temp[s++] = (byte)(pg.getWidth() / 8);
            temp[s++] = 0x00;
            temp[s++] = 0x01;
            temp[s++] = 0x00;
            for( j = 0 ; j < (pg.getWidth() / 8) ; j++ )
                temp[s++] = sendData[index++];
            usbCtrl.sendByte(temp, dev);
        }
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void showOkDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDebitNoteActivity.this);
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

    public void chooseSupplierButtonClick(View view) {
        Intent intentShowSupplierList = new Intent(getApplicationContext(), SupplierListActivity.class);
        intentShowSupplierList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_NEW_CREDITNOTE);
        startActivityForResult(intentShowSupplierList, CHOOSE_CUSTOMER_RESPONSE_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CHOOSE_CUSTOMER_RESPONSE_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                String json = data.getStringExtra("supplier");
                Gson gson = new Gson();
                supplierSelected = gson.fromJson(json, Supplier.class);

                chooseSupplierButton.setText("Choose different Supplier");
                amountLinearLayout.setVisibility(View.VISIBLE);
                setSupplierOnScreen();
            }
        }
    }

    private void setSupplierOnScreen() {
        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        builder.append(nextLine);
        builder.append("Supplier : ");
        if (supplierSelected.getEntityType().equalsIgnoreCase("Individual")) {
            builder.append(supplierSelected.getContactPerson());
        } else {
            builder.append(supplierSelected.getEntityName());
        }
        builder.append(nextLine);
        builder.append("Contact Number : ");
        builder.append(supplierSelected.getContactNumber());

        supplierDetailsTextView.setText(builder.toString());
    }

    private CreditDebitNote createDebitNoteObject(double creditAmount, String reason) {
        CreditDebitNote creditNote = new CreditDebitNote();
        creditNote.setNoteNumber(createNewDebitNoteNumber());
        creditNote.setCustomerID(supplierSelected.getSupplierId());
        if(supplierSelected.getEntityType().equalsIgnoreCase("Individual")) {
            creditNote.setCustomerName(supplierSelected.getContactPerson());
        } else {
            creditNote.setCustomerName(supplierSelected.getEntityName());
        }
        creditNote.setNoteType("Debit Note");
        creditNote.setCustomerEmail(supplierSelected.getContactEmailId());
        creditNote.setCustomerMobile(supplierSelected.getContactNumber());
        creditNote.setCustomerLandline(supplierSelected.getContactLandline());
        creditNote.setInvoiceNumber("");
        creditNote.setSalesReturnNumber("");
        creditNote.setOutletID(Integer.parseInt(OutletID));
        creditNote.setAmount(creditAmount);
        creditNote.setReason(reason);
        creditNote.setCreatedBy(MyFunctions.parseInt(LoginID));
        creditNote.setCreatedDtTm(MyFunctions.getCurrentDateTime());
        creditNote.setNoteDate(creditNote.getCreatedDtTm());
        creditNote.setModifiedBy(0);
        creditNote.setModifiedDtTm("");
        creditNote.setSalesID(0);
        creditNote.setSynced(0);
        return creditNote;
    }
    
    private String createNewDebitNoteNumber() {
        StringBuilder builder = new StringBuilder();
        builder.append("DN");
//        builder.append(this.InvoiceSeries);
        builder.append(this.TabCode);
        builder.append("/");
        builder.append(this.dateFormatInInvoiceNumber());
        builder.append("/");
        String previousDebitNoteNumber = DatabaseHandler.getInstance().getPreviousCreditNoteNumber(builder.toString());
        if(previousDebitNoteNumber == null) {
            builder.append("001");
        } else {
            String seriesNumberString = previousDebitNoteNumber.substring(previousDebitNoteNumber.length() - 3);
            int seriesNumber = Integer.parseInt(seriesNumberString);
            seriesNumber++;
            seriesNumberString = "000" + seriesNumber;
            seriesNumberString = seriesNumberString.substring(seriesNumberString.length() - 3);
            builder.append("" + seriesNumberString);
        }
        return builder.toString();
    }

    private String getDebitNoteReceipt(CreditDebitNote creditNote) {
        int lengthOfScreen = 46;
        String rupeeSymbol = "Rs.";
//        String rupeeSymbol = "\u20B9";

        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        String borderLine = "|" + MyFunctions.drawLine(" ", lengthOfScreen) + "|";

//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(nextLine);
        // 1
        builder.append(" ");
        builder.append(MyFunctions.drawLine("-", lengthOfScreen));
        builder.append(" ");
        builder.append(nextLine);
        // 2
        builder.append(borderLine);
        builder.append(nextLine);
        // 3
        builder.append("|");
        builder.append(MyFunctions.makeStringCentreAlign("**" + creditNote.getNoteType() + "**", lengthOfScreen));
        builder.append("|");
        builder.append(nextLine);
        // 4
        builder.append(borderLine);
        builder.append(nextLine);
        // 5
        builder.append("|");
        builder.append(MyFunctions.makeStringCentreAlign("Voucher No. - " + creditNote.getNoteNumber(), lengthOfScreen));
        builder.append("|");
        builder.append(nextLine);
        // 6
        builder.append("|");
        builder.append(MyFunctions.makeStringCentreAlign("Amount - " + rupeeSymbol + String.format("%.2f", creditNote.getAmount()), lengthOfScreen));
        builder.append("|");
        builder.append(nextLine);
        // 7
        builder.append(borderLine);
        builder.append(nextLine);
        // 8
        builder.append(" ");
        builder.append(MyFunctions.drawLine("-", lengthOfScreen));
        builder.append(" ");
        builder.append(nextLine);
//
//        builder.append(nextLine);
//        builder.append(nextLine);

        return builder.toString();
    }

    @NonNull
    private String dateFormatInInvoiceNumber() {
        int invoiceDay, invoiceMonth, invoiceYear;
        Calendar calendar = Calendar.getInstance();
        invoiceYear = calendar.get(Calendar.YEAR);
        invoiceMonth = calendar.get(Calendar.MONTH) + 1;
        invoiceDay = calendar.get(Calendar.DAY_OF_MONTH);
        
        StringBuilder builder = new StringBuilder();
        int invoiceYearTwoDigit = invoiceYear % 100;
        builder.append("" + invoiceYearTwoDigit);

        String invoiceMonthString = "00" + invoiceMonth;
        invoiceMonthString = invoiceMonthString.substring(invoiceMonthString.length() - 2);
        builder.append(invoiceMonthString);

        String invoiceDayString = "00" + invoiceDay;
        invoiceDayString = invoiceDayString.substring(invoiceDayString.length() - 2);
        builder.append(invoiceDayString);

        return builder.toString();
    }

    public void onSaveButtonClick(View v) {
        String amountString = amountEditText.getText().toString();
        double amount = MyFunctions.parseDouble(amountString);
        amountString = String.format("%.2f", amount);
        amount = MyFunctions.parseDouble(amountString);
        if (amount == 0) {
            showOkDialog("Enter valid amount");
            return;
        }
        
        String reason = reasonEditText.getText().toString();
        if (MyFunctions.StringLength(reason) == 0) {
            showOkDialog("Enter reason");
            return;
        }
        
        CreditDebitNote creditNote = createDebitNoteObject(amount, reason);
        DatabaseHandler.getInstance().addNewCreditNote(creditNote);
        String creditNoteReceipt = getDebitNoteReceipt(creditNote);
        printThroughPrinter(creditNoteReceipt);

        // sales return save
        AlertDialog.Builder builder = new AlertDialog.Builder(NewDebitNoteActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText("Debit Note Created!!");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                NewDebitNoteActivity.super.onBackPressed();
            }
        });

    }
}
