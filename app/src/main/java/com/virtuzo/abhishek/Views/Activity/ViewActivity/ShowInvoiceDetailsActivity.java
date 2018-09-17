package com.virtuzo.abhishek.Views.Activity.ViewActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.hoin.usbsdk.PrintPic;
import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.EwayBill;
import com.virtuzo.abhishek.modal.EwayBillReason;
import com.virtuzo.abhishek.modal.GSTCategory;
import com.virtuzo.abhishek.modal.GroupProductDetailInInvoice;
import com.virtuzo.abhishek.modal.MasterSales;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.TransactionSales;

import java.util.ArrayList;

public class ShowInvoiceDetailsActivity extends AppCompatActivity {

    String FirstName, OutletName, OutletAddress, OutletGSTNumber,
            ContactNumber, StateName, LoginID="", GSTCategoryID;
    String receiptString;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    SharedPreferences setting;
    LinearLayout noEwayBillLayout, yesEwayBillLayout;
    String invoiceNumber;
    MasterSales msSales;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_invoice_detals);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.actionBarTitle);
        title.setText("Invoice");

        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        FirstName = setting.getString("FirstName", "");
        OutletName = setting.getString("OutletName", "");
        OutletAddress = setting.getString("Address", "");
        ContactNumber = setting.getString("ContactNumber", "");
        OutletGSTNumber = setting.getString("GSTNumber","");
        StateName = setting.getString("StateName", "");
        LoginID = setting.getString("UserID", "");
        GSTCategoryID = setting.getString("GSTCategoryID", "");


        invoiceNumber = getIntent().getExtras().getString("salesInvoiceNumber");

        msSales = DatabaseHandler.getInstance().getMasterSalesRecord(invoiceNumber);
        ArrayList<TransactionSales> tArray = DatabaseHandler.getInstance().getInvoiceDetailsFromDB(msSales.getReferenceNumber());
        ArrayList<GroupProductDetailInInvoice> groupProductDetails = DatabaseHandler.getInstance().getGroupProductDetails(msSales.getReferenceNumber());
        TextView invoiceReceiptTextView = (TextView) findViewById(R.id.invoiceReceiptTextView);
        receiptString = getReceipt(msSales, tArray, groupProductDetails);
        invoiceReceiptTextView.setText(receiptString);

        refreshEwayBillDetails();

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();
    }

    private void refreshEwayBillDetails() {
        final EwayBill ewayBill = DatabaseHandler.getInstance().getEwayBillRecordFromSalesInvoice(invoiceNumber);
        noEwayBillLayout = (LinearLayout) findViewById(R.id.noEwayBillLayout);
        yesEwayBillLayout = (LinearLayout) findViewById(R.id.yesEwayBillLayout);
        if(ewayBill != null) {
            // ewaybill present
            yesEwayBillLayout.setVisibility(View.VISIBLE);
            noEwayBillLayout.setVisibility(View.GONE);
            TextView ewayBillDetails = (TextView) findViewById(R.id.ewayBillDetails);
            Button updateEwayBillButton = (Button) findViewById(R.id.updateEwayBillButton);
            updateEwayBillButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateEwayBill(ewayBill);
                }
            });
            ewayBillDetails.setText(getEwayBillDetails(ewayBill));
        } else {
            yesEwayBillLayout.setVisibility(View.GONE);
            noEwayBillLayout.setVisibility(View.VISIBLE);
            Button createEwayBillButton = (Button) findViewById(R.id.createEwayBillButton);
            createEwayBillButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    createEwayBill();
                }
            });
        }
    }

    private void updateEwayBill(final EwayBill previousEwayBill) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowInvoiceDetailsActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.separate_ewaybill_creation_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button saveClick = (Button) view.findViewById(R.id.saveClick);
        saveClick.setText("Update");
        Button cancelClick = (Button) view.findViewById(R.id.cancelClick);
        final EditText ewayBillNumberEditText = (EditText) view.findViewById(R.id.ewayBillNumberEditText);
        final EditText documentNumberEditText = (EditText) view.findViewById(R.id.documentNumberEditText);
        final EditText vehicleNumberEditText = (EditText) view.findViewById(R.id.vehicleNumberEditText);
        ewayBillNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        documentNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        vehicleNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        final Spinner reasonSpinner = (Spinner) view.findViewById(R.id.reasonSpinner);
        ArrayList<EwayBillReason> reasonList = new ArrayList<>();
        EwayBillReason demoEwayBillReason = new EwayBillReason();
        demoEwayBillReason.setReason("-- Choose Reason --");
        reasonList.add(demoEwayBillReason);
        reasonList.addAll(DatabaseHandler.getInstance().getEWayBillReasonsList());
        ArrayAdapter adapter = new ArrayAdapter(ShowInvoiceDetailsActivity.this, android.R.layout.simple_spinner_item, reasonList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(adapter);

        // setfield
        ewayBillNumberEditText.setText(previousEwayBill.getEWayBillNo());
        documentNumberEditText.setText(previousEwayBill.getDocumentNo());
        vehicleNumberEditText.setText(previousEwayBill.getVehicleNo());
        for (int i = 0; i < reasonList.size(); i++) {
            EwayBillReason reason = reasonList.get(i);
            if(reason.getID() == previousEwayBill.getReasonID()){
                reasonSpinner.setSelection(i);
            }
        }

        saveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save eway bill form
                if(reasonSpinner.getSelectedItemPosition() == 0) {
                    showOkDialog("Select Reason");
                    return;
                }
                EwayBill ewayBill = new EwayBill();
                ewayBill.setID(previousEwayBill.getID());
                ewayBill.setSalesID(previousEwayBill.getSalesID());
                ewayBill.setInvoiceNumber(previousEwayBill.getInvoiceNumber());
                ewayBill.setDONumber(previousEwayBill.getDONumber());
                EwayBillReason reason = (EwayBillReason) reasonSpinner.getSelectedItem();
                ewayBill.setReasonID(reason.getID());
                ewayBill.setDocumentNo(documentNumberEditText.getText().toString());
                ewayBill.setVehicleNo(vehicleNumberEditText.getText().toString());
                ewayBill.setEWayBillNo(ewayBillNumberEditText.getText().toString());
                ewayBill.setCreatedBy(MyFunctions.parseDouble(LoginID));
                ewayBill.setCreatedDtTm(MyFunctions.getCurrentDateTime());
                if(previousEwayBill.getSynced() != 0) {
                    ewayBill.setSynced(2); // for update
                } else {
                    ewayBill.setSynced(0);
                }
                DatabaseHandler.getInstance().updatePreviousEWayBill(ewayBill);

                refreshEwayBillDetails();
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

    private void createEwayBill() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowInvoiceDetailsActivity.this);
        final View view = getLayoutInflater().inflate(R.layout.separate_ewaybill_creation_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button saveClick = (Button) view.findViewById(R.id.saveClick);
        Button cancelClick = (Button) view.findViewById(R.id.cancelClick);
        final EditText ewayBillNumberEditText = (EditText) view.findViewById(R.id.ewayBillNumberEditText);
        final EditText documentNumberEditText = (EditText) view.findViewById(R.id.documentNumberEditText);
        final EditText vehicleNumberEditText = (EditText) view.findViewById(R.id.vehicleNumberEditText);
        ewayBillNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        documentNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        vehicleNumberEditText.setFilters(new InputFilter[] { MyFunctions.filter });
        final Spinner reasonSpinner = (Spinner) view.findViewById(R.id.reasonSpinner);
        ArrayList<EwayBillReason> reasonList = new ArrayList<>();
        EwayBillReason demoEwayBillReason = new EwayBillReason();
        demoEwayBillReason.setReason("-- Choose Reason --");
        reasonList.add(demoEwayBillReason);
        reasonList.addAll(DatabaseHandler.getInstance().getEWayBillReasonsList());
        ArrayAdapter adapter = new ArrayAdapter(ShowInvoiceDetailsActivity.this, android.R.layout.simple_spinner_item, reasonList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        reasonSpinner.setAdapter(adapter);

        saveClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save eway bill form
                if(reasonSpinner.getSelectedItemPosition() == 0) {
                    showOkDialog("Select Reason");
                    return;
                }
                EwayBill ewayBill = new EwayBill();
                ewayBill.setInvoiceNumber(invoiceNumber);
                EwayBillReason reason = (EwayBillReason) reasonSpinner.getSelectedItem();
                ewayBill.setReasonID(reason.getID());
                ewayBill.setDONumber(msSales.getDONumber());
                ewayBill.setDocumentNo(documentNumberEditText.getText().toString());
                ewayBill.setVehicleNo(vehicleNumberEditText.getText().toString());
                ewayBill.setEWayBillNo(ewayBillNumberEditText.getText().toString());
                ewayBill.setCreatedBy(MyFunctions.parseDouble(LoginID));
                ewayBill.setCreatedDtTm(MyFunctions.getCurrentDateTime());
                ewayBill.setSynced(0);
                DatabaseHandler.getInstance().addNewEWayBill(ewayBill);

                refreshEwayBillDetails();
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

    private String getEwayBillDetails(EwayBill ewayBill) {
        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        builder.append("E-Way Bill :");
        builder.append(nextLine);
        builder.append(nextLine);

        int leftAlignLength = 20;
        int rightAlignLength = 28;

        builder.append(MyFunctions.makeStringLeftAlign("Reason", leftAlignLength));
        String reason = DatabaseHandler.getInstance().getEWayBillReason(ewayBill.getReasonID());
        builder.append(MyFunctions.makeStringRightAlign(reason, rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("E-way Bill Number", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(ewayBill.getEWayBillNo(), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Vehicle Number", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(ewayBill.getVehicleNo(), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("MTR Number", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(ewayBill.getDocumentNo(), rightAlignLength));
        builder.append(nextLine);
        return builder.toString();
    }

    private String getReceipt(MasterSales masterSales, ArrayList<TransactionSales> transactionSalesArrayList, ArrayList<GroupProductDetailInInvoice> groupProductDetails) {
        StringBuilder builder = new StringBuilder();
        String nextLine = "\n";
        builder.append(nextLine);
        builder.append(this.OutletName);
        builder.append(nextLine);
        String address[] = this.OutletAddress.split("### ");
        for(String str : address) {
            builder.append(str);
            builder.append(nextLine);
        }
        builder.append("Tel : " + this.ContactNumber);
        builder.append(nextLine);

        builder.append("Outlet State: " + StateName);
        builder.append(nextLine);
        if(MyFunctions.StringLength(this.OutletGSTNumber) > 0) {
            builder.append("GST No. " + this.OutletGSTNumber);
            builder.append(nextLine);
        }
        builder.append(nextLine);
        int OutletGSTCategoryID = MyFunctions.parseInt(this.GSTCategoryID);
        if (OutletGSTCategoryID == GSTCategory.COMPOSITE_SCHEME) {
            builder.append(MyFunctions.makeStringCentreAlign(getString(R.string.composite_scheme_receipt_heading), 48));
        } else {
            builder.append(MyFunctions.makeStringCentreAlign("Tax Invoice/Bill Of Supply", 48));
        }
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("Invoice No. : " + masterSales.getReferenceNumber());
        builder.append(nextLine);
        builder.append("Date : " + masterSales.getSalesDate());
        builder.append(nextLine);
        builder.append("Cashier : " + this.FirstName);
        builder.append(nextLine);
        builder.append("Customer : " + masterSales.getCustomerName());
        if(MyFunctions.StringLength(masterSales.getCustomerMobile()) > 0) {
            builder.append(" ( " + masterSales.getCustomerMobile() + " )");
        }
        builder.append(nextLine);
        if(!masterSales.getCustomerID().equals("0")) {
            if (MyFunctions.StringLength(masterSales.getBillAddress()) > 0) {
                builder.append(masterSales.getBillAddress());
                builder.append(nextLine);
            }
            if(MyFunctions.StringLength(masterSales.getCustomerGSTN()) > 0) {
                builder.append("GST Number : " + masterSales.getCustomerGSTN());
                builder.append(nextLine);
            }
        }
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        String itemNameHeading = "ITEM NAME";
        int itemNameHeadingLength = 14;
        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));

        String qtyHeading = "QTY";
        int qtyHeadingLength = 5;
        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));

        String priceHeading = "PRICE";
        int priceHeadingLength = 10;
        builder.append(MyFunctions.makeStringRightAlign(priceHeading, priceHeadingLength));

        String gstHeading = "GST";
        int gstHeadingLength = 8;
        builder.append(MyFunctions.makeStringRightAlign(gstHeading, gstHeadingLength));

        String amountHeading = "AMOUNT";
        int amountHeadingLength = 11;
        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));

        double savedDiscount=0;

        builder.append(nextLine);
        for(TransactionSales transactionSales : transactionSalesArrayList) {
            builder.append(transactionSales.getItemName());
            builder.append(nextLine);

            savedDiscount+= transactionSales.getDiscountAmount();
            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(transactionSales.getQty()+"", qtyHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getSalesPriceWithDiscount()), priceHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getTaxAmount()
                    / transactionSales.getQty()), gstHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionSales.getTotalAmount()), amountHeadingLength));
            builder.append(nextLine);

//            builder.append(MyFunctions.makeStringRightAlign("(" + Math.round((transactionSales.getTaxAmount() / transactionSales.getQty()
//                            * 100)
//                            / transactionSales.getSalesPriceWithDiscount())
//                            + "%)"
//                    , 48 - amountHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign("(" + String.format("%.2f", transactionSales.getTaxRate()) + "%)"
                    , 48 - amountHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(" ", amountHeadingLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int leftAlignLength = 36;
        int rightAlignLength = 12;
        builder.append(MyFunctions.makeStringLeftAlign("Total Amt.", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getSubTotal()), rightAlignLength));
        builder.append(nextLine);

//        builder.append(MyFunctions.makeStringLeftAlign("Discount", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
//        builder.append(nextLine);

        if(masterSales.getOtherCharges() > 0) {
            builder.append(MyFunctions.makeStringLeftAlign("Other Charges", leftAlignLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getOtherCharges()), rightAlignLength));
            builder.append(nextLine);
        }

        builder.append(MyFunctions.makeStringLeftAlign("Total GST", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getTaxAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("You Saved", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign( String.format("%.2f", savedDiscount), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Cash Discount", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Total (incl. GST)", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getTotalAmount()), rightAlignLength));
        builder.append(nextLine);

//        builder.append(MyFunctions.makeStringLeftAlign("Discount", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign("-" + String.format("%.2f", masterSales.getTotalDiscountAmount()), rightAlignLength));
//        builder.append(nextLine);

        double roundOffTotalAmount = Math.round(masterSales.getTotalAmount());

        double roundOffAmount = roundOffTotalAmount - masterSales.getTotalAmount();

        builder.append(MyFunctions.makeStringLeftAlign("Rounding of Amt.", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", roundOffAmount), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Net Payable Amt.", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", roundOffTotalAmount), rightAlignLength));
        builder.append(nextLine);

//        builder.append(MyFunctions.makeStringLeftAlign("Cash Tendered", leftAlignLength));
//        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterSales.getPaidAmount()), rightAlignLength));
//        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Balance", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", roundOffTotalAmount - masterSales.getPaidAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int GST_Length = 16; // 48/3
        builder.append("GST Summary");
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("CGST", GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign("SGST", GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign("IGST", GST_Length));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getCGST()), GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getSGST()), GST_Length));
        builder.append(MyFunctions.makeStringCentreAlign(String.format("%.2f", masterSales.getIGST()), GST_Length));
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("Terms and Conditions :- " + masterSales.getTermsCondition(), 48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
        builder.append(nextLine);

//        Gson gson = new Gson();
//        for (GroupProductDetailInInvoice groupProductDetail : groupProductDetails) {
//            builder.append(nextLine);
//            builder.append(gson.toJson(groupProductDetail));
//        }

        builder.append(nextLine);
        builder.append(nextLine);

        return builder.toString();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(ShowInvoiceDetailsActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(ShowInvoiceDetailsActivity.this,mHandler);
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

        Toast.makeText(ShowInvoiceDetailsActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(ShowInvoiceDetailsActivity.this, getString(R.string.usb_msg_getpermission),
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
                Toast.makeText(ShowInvoiceDetailsActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(ShowInvoiceDetailsActivity.this, "The printer has no paper",
                            Toast.LENGTH_SHORT).show();
                    return;
                }
                String txt_msg = receiptString;
                if( CheckUsbPermission() == true ){

//                    if(null!=MyFunctions.getImage()) {
//
//                        String imagePath = Environment.getExternalStorageDirectory()+"/GSTMadeEasy/logo.png";
//
//                        printImage(imagePath);
//                    }
                    usbCtrl.sendMsg(txt_msg, "GBK", dev);
                    usbCtrl.cutPaper(dev, 100);
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

    public void backButton(View view) {
        super.onBackPressed();
    }

    public void printButton(View view) {
        printThroughPrinter(receiptString);
    }

    public void showOkDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ShowInvoiceDetailsActivity.this);
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

}
