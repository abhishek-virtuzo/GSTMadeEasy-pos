package com.virtuzo.abhishek.Views.Activity.ViewActivity;

import android.app.AlertDialog;
import android.content.Intent;
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

import com.hoin.usbsdk.PrintPic;
import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewSalesInvoiceFromDOActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.EwayBill;
import com.virtuzo.abhishek.modal.EwayBillReason;
import com.virtuzo.abhishek.modal.MasterDeliveryOrder;
import com.virtuzo.abhishek.modal.MasterSales;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;
import com.virtuzo.abhishek.modal.State;
import com.virtuzo.abhishek.modal.TransactionDeliveryOrder;

import java.util.ArrayList;

public class DeliveryOrderViewActivity extends AppCompatActivity {

    String FirstName, OutletName, OutletAddress, OutletGSTNumber,
            ContactNumber, StateName, LoginID="";
    String receiptString;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    SharedPreferences setting;
    LinearLayout noEwayBillLayout, yesEwayBillLayout;
    LinearLayout noSalesInvoiceLayout, yesSalesInvoiceLayout;

    TextView tv_DeliveryOrder,tv_DeliveryOrderDetails;
    ArrayList<TransactionDeliveryOrder> listOfTransactionDeliveryOrder;
    String DO_ID, doNumber;
    MasterDeliveryOrder masterDeliveryOrder;
    String invoiceNumber = "";
    String eWayBillNo = "";
    TextView invoiceReceiptTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_order_view);

        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        FirstName = setting.getString("FirstName", "");
        OutletName = setting.getString("OutletName", "");
        OutletAddress = setting.getString("Address", "");
        ContactNumber = setting.getString("ContactNumber", "");
        OutletGSTNumber = setting.getString("GSTNumber","");
        StateName = setting.getString("StateName", "");
        LoginID = setting.getString("UserID", "");

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        listOfTransactionDeliveryOrder = new ArrayList<>();

        Intent intent = getIntent();
        DO_ID = intent.getExtras().getString("DO_ID", "0");

        listOfTransactionDeliveryOrder.clear();
        masterDeliveryOrder = DatabaseHandler.getInstance().getMasterDeliveryOrderRecord(DO_ID);
        doNumber = masterDeliveryOrder.getDONumber();
        // purchaseID= String.valueOf(masterPurchase.getPurchaseID());

        listOfTransactionDeliveryOrder=DatabaseHandler.getInstance().getDeliveryOrderListDetailsFromDB(DO_ID);

        invoiceReceiptTextView = (TextView) findViewById(R.id.invoiceReceiptTextView);

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();

    }

    @Override
    protected void onResume() {
        super.onResume();

        refreshSalesInvoiceDetails();
        refreshEwayBillDetails();
        receiptString = getReceipt(masterDeliveryOrder, listOfTransactionDeliveryOrder);
        invoiceReceiptTextView.setText(receiptString);
    }

    private void refreshSalesInvoiceDetails() {
        final MasterSales masterSales = DatabaseHandler.getInstance().getMasterSalesRecordFromDO(doNumber);
        noSalesInvoiceLayout = (LinearLayout) findViewById(R.id.noSalesInvoiceLayout);
        yesSalesInvoiceLayout = (LinearLayout) findViewById(R.id.yesSalesInvoiceLayout);
        if(masterSales != null) {
            // invoice present
            invoiceNumber = masterSales.getReferenceNumber();
            yesSalesInvoiceLayout.setVisibility(View.VISIBLE);
            noSalesInvoiceLayout.setVisibility(View.GONE);
            Button showSalesInvoiceButton = (Button) findViewById(R.id.showSalesInvoiceButton);
            showSalesInvoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // show invoice details
                    Intent intentShowInvoiceDetails = new Intent(getApplicationContext(), ShowInvoiceDetailsActivity.class);
                    intentShowInvoiceDetails.putExtra("salesInvoiceNumber", masterSales.getReferenceNumber());
                    startActivity(intentShowInvoiceDetails);
                }
            });
        } else {
            yesSalesInvoiceLayout.setVisibility(View.GONE);
            noSalesInvoiceLayout.setVisibility(View.VISIBLE);
            Button createSalesInvoiceButton = (Button) findViewById(R.id.createSalesInvoiceButton);
            createSalesInvoiceButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // create invoie through DO
                    Intent intent = new Intent(DeliveryOrderViewActivity.this, NewSalesInvoiceFromDOActivity.class);
                    intent.putExtra("MasterDO_ID", DO_ID);
                    startActivity(intent);
                }
            });
        }
    }

    private void refreshEwayBillDetails() {
        final EwayBill ewayBill = DatabaseHandler.getInstance().getEwayBillRecordFromDO(doNumber);
        noEwayBillLayout = (LinearLayout) findViewById(R.id.noEwayBillLayout);
        yesEwayBillLayout = (LinearLayout) findViewById(R.id.yesEwayBillLayout);
        if(ewayBill != null) {
            // ewaybill present
            eWayBillNo = ewayBill.getEWayBillNo();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryOrderViewActivity.this);
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
        ArrayAdapter adapter = new ArrayAdapter(DeliveryOrderViewActivity.this, android.R.layout.simple_spinner_item, reasonList);
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
                EwayBillReason reason = (EwayBillReason) reasonSpinner.getSelectedItem();
                ewayBill.setReasonID(reason.getID());
                ewayBill.setDocumentNo(documentNumberEditText.getText().toString());
                ewayBill.setVehicleNo(vehicleNumberEditText.getText().toString());
                ewayBill.setEWayBillNo(ewayBillNumberEditText.getText().toString());
                ewayBill.setCreatedBy(MyFunctions.parseDouble(LoginID));
                ewayBill.setCreatedDtTm(MyFunctions.getCurrentDateTime());
                ewayBill.setDONumber(masterDeliveryOrder.getDONumber());
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryOrderViewActivity.this);
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
        ArrayAdapter adapter = new ArrayAdapter(DeliveryOrderViewActivity.this, android.R.layout.simple_spinner_item, reasonList);
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
                ewayBill.setDONumber(doNumber);
                EwayBillReason reason = (EwayBillReason) reasonSpinner.getSelectedItem();
                ewayBill.setReasonID(reason.getID());
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

    private String getReceipt(MasterDeliveryOrder masterDeliveryOrder, ArrayList<TransactionDeliveryOrder> transactionDeliveryOrderArrayList) {
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

        builder.append("Outlet State: " + this.StateName);
        builder.append(nextLine);
        if(MyFunctions.StringLength(this.OutletGSTNumber) > 0) {
            builder.append("GST No. " + this.OutletGSTNumber);
            builder.append(nextLine);
        }
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringCentreAlign("Delivery Order", 48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("DO No. : " + masterDeliveryOrder.getDONumber());
        builder.append(nextLine);
        builder.append("Date : " + masterDeliveryOrder.getDeliveryDate());
        builder.append(nextLine);
        if(MyFunctions.StringLength(eWayBillNo) > 0) {
            builder.append("E-way Bill No. : " + eWayBillNo);
            builder.append(nextLine);
        }
        builder.append("Cashier : " + this.FirstName);
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("Customer : " + masterDeliveryOrder.getCustomerName());
        if(MyFunctions.StringLength(masterDeliveryOrder.getCustomerMobile()) > 0) {
            builder.append(" ( " + masterDeliveryOrder.getCustomerMobile() + " )");
        }
        builder.append(nextLine);
        builder.append("Bill Address : " + masterDeliveryOrder.getCustomerBillAddress());
        builder.append(nextLine);
        State billState = DatabaseHandler.getInstance().getStateFromId(masterDeliveryOrder.getCustomerBillStateID());
        builder.append("Bill State : " + billState.getStateName());
        builder.append(nextLine);
        builder.append("Ship Address : " + masterDeliveryOrder.getCustomerShipAddress());
        builder.append(nextLine);
        State shipState = DatabaseHandler.getInstance().getStateFromId(masterDeliveryOrder.getCustomerShipStateID());
        builder.append("Ship State : " + shipState.getStateName());
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        String itemNameHeading = "ItemName(ItemCode)";
        int itemNameHeadingLength = 28;
        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));

        String qtyHeading = "QTY";
        int qtyHeadingLength = 6;
        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));

        String amountHeading = "AMOUNT";
        int amountHeadingLength = 14;
        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));

        builder.append(nextLine);

        String rupeeSymbol = "\u20B9";
        int totalQuantity = 0;
        double totalAmount = 0.0;

        for(TransactionDeliveryOrder transactionDeliveryOrder : transactionDeliveryOrderArrayList) {
            Product product = DatabaseHandler.getInstance().getProductRecordFromDeliveryOrder(transactionDeliveryOrder);
            if (transactionDeliveryOrder.getAmount() == 0) {
                if(product != null) {
                    transactionDeliveryOrder.setAmount(product.getSalesPrice() * transactionDeliveryOrder.getQuantity());
                }
            }

            builder.append(transactionDeliveryOrder.getProductName() + " (" + transactionDeliveryOrder.getProductCode() + ")");
            builder.append(nextLine);
            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(transactionDeliveryOrder.getQuantity()+"", qtyHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionDeliveryOrder.getAmount()), amountHeadingLength));

            builder.append(nextLine);

            totalQuantity += transactionDeliveryOrder.getQuantity();
            totalAmount += transactionDeliveryOrder.getAmount();
        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        if(masterDeliveryOrder.getTotalQuantity() == 0) {
            masterDeliveryOrder.setTotalQuantity(totalQuantity);
        }
        if(masterDeliveryOrder.getTotalAmount() == 0.0) {
            masterDeliveryOrder.setTotalAmount(totalAmount);
        }

        int leftAlignLength = 32;
        int rightAlignLength = 16;

        builder.append(MyFunctions.makeStringLeftAlign("Total Quantity ", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(masterDeliveryOrder.getTotalQuantity()+"", rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.makeStringLeftAlign("Total Amount ", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterDeliveryOrder.getTotalAmount()), rightAlignLength));
        builder.append(nextLine);

//        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
//        builder.append(nextLine);

        builder.append(nextLine);
        builder.append(nextLine);

        return builder.toString();
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UsbController.USB_CONNECTED:
                    Toast.makeText(DeliveryOrderViewActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(DeliveryOrderViewActivity.this,mHandler);
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

        Toast.makeText(DeliveryOrderViewActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(DeliveryOrderViewActivity.this, getString(R.string.usb_msg_getpermission),
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
                Toast.makeText(DeliveryOrderViewActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(DeliveryOrderViewActivity.this, "The printer has no paper",
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DeliveryOrderViewActivity.this);
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

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

}
