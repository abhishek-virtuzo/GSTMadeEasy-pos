package com.virtuzo.abhishek.Views.Activity.ViewActivity;

import android.app.AlertDialog;
import android.content.SharedPreferences;
import android.hardware.usb.UsbDevice;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.hoin.usbsdk.PrintPic;
import com.hoin.usbsdk.UsbController;
import com.virtuzo.abhishek.DashBoardScreen;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.CreditDebitNote;
import com.virtuzo.abhishek.modal.MasterPurchaseReturn;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.TransactionPurchaseReturn;

import java.util.ArrayList;

public class PurchaseReturnViewActivity extends AppCompatActivity {

    String FirstName, OutletName, OutletAddress, OutletGSTNumber,
            ContactNumber, StateName, LoginID="";
    String receiptString;
    private int[][] u_infor;
    UsbController usbCtrl = null;
    UsbDevice dev = null;
    SharedPreferences setting;
    String SR_ID;
    MasterPurchaseReturn masterPurchaseReturn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchasereturn_view);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        TextView title = (TextView) findViewById(R.id.actionBarTitle);
        title.setText("Purchase Return Details");

        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        FirstName = setting.getString("FirstName", "");
        OutletName = setting.getString("OutletName", "");
        OutletAddress = setting.getString("Address", "");
        ContactNumber = setting.getString("ContactNumber", "");
        OutletGSTNumber = setting.getString("GSTNumber","");
        StateName = setting.getString("StateName", "");
        LoginID = setting.getString("UserID", "");

        SR_ID = getIntent().getExtras().getString("CN_ID");

        masterPurchaseReturn = DatabaseHandler.getInstance().getMasterPurchaseReturnRecord(SR_ID);
        ArrayList<TransactionPurchaseReturn> tArray = DatabaseHandler.getInstance().getPurchaseReturnListDetailsFromDB(SR_ID);
        TextView invoiceReceiptTextView = (TextView) findViewById(R.id.invoiceReceiptTextView);
        receiptString = getPurchaseReturnReceipt(masterPurchaseReturn, tArray);
        CreditDebitNote creditNote = DatabaseHandler.getInstance().getCreditNoteFromSalesReturn(masterPurchaseReturn.getPurchaseReturnNumber());
        if (creditNote != null) {
            receiptString += getCreditNoteReceipt(creditNote);
        }
        invoiceReceiptTextView.setText(receiptString);

        // print receipt code
        initializePrinterConfiguration();
        connectThroughPrinter();
    }

    private String getPurchaseReturnReceipt(MasterPurchaseReturn masterPurchaseReturn, ArrayList<TransactionPurchaseReturn> transactionPurchaseReturns) {
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
        builder.append(MyFunctions.makeStringCentreAlign("Purchase Return", 48));
        builder.append(nextLine);
        builder.append(nextLine);
        builder.append("Purchase Return No. : " + masterPurchaseReturn.getPurchaseReturnNumber());
        builder.append(nextLine);
        builder.append("Purchase Invoice No. : " + masterPurchaseReturn.getInvoiceNumber());
        builder.append(nextLine);
        builder.append("Date : " + masterPurchaseReturn.getPurchaseReturnDate());
        builder.append(nextLine);
        builder.append("Cashier : " + this.FirstName);
        builder.append(nextLine);
        builder.append("Supplier : " + masterPurchaseReturn.getSupplierName());
        if(MyFunctions.StringLength(masterPurchaseReturn.getSupplierMobile()) > 0) {
            builder.append(" ( " + masterPurchaseReturn.getSupplierMobile() + " )");
        }
        builder.append(nextLine);
        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        String itemNameHeading = "ItemName";
        int itemNameHeadingLength = 11;
        builder.append(MyFunctions.makeStringLeftAlign(itemNameHeading, itemNameHeadingLength));

        String qtyHeading = "Rtn Qty";
        int qtyHeadingLength = 7;
        builder.append(MyFunctions.makeStringRightAlign(qtyHeading, qtyHeadingLength));

        String priceHeading = "Price";
        int priceHeadingLength = 10;
        builder.append(MyFunctions.makeStringRightAlign(priceHeading, priceHeadingLength));

        String gstHeading = "Tax";
        int gstHeadingLength = 9;
        builder.append(MyFunctions.makeStringRightAlign(gstHeading, gstHeadingLength));

        String amountHeading = "Amount";
        int amountHeadingLength = 11;
        builder.append(MyFunctions.makeStringRightAlign(amountHeading, amountHeadingLength));

        builder.append(nextLine);
        for(TransactionPurchaseReturn transactionPurchaseReturn : transactionPurchaseReturns) {
            builder.append(transactionPurchaseReturn.getProductName());
            builder.append(nextLine);

            builder.append(MyFunctions.makeStringLeftAlign("  ", itemNameHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(transactionPurchaseReturn.getReturnQty() + "", qtyHeadingLength));

            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionPurchaseReturn.getUnitPrice()), priceHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionPurchaseReturn.getReturnTaxAmount()), gstHeadingLength));
            builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", transactionPurchaseReturn.getReturnAmount()), amountHeadingLength));
            builder.append(nextLine);

        }

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);

        int leftAlignLength = 36;
        int rightAlignLength = 12;
        builder.append(MyFunctions.makeStringLeftAlign("Debit Amount", leftAlignLength));
        builder.append(MyFunctions.makeStringRightAlign(String.format("%.2f", masterPurchaseReturn.getDebitAmount()), rightAlignLength));
        builder.append(nextLine);

        builder.append(MyFunctions.drawLine("-", 48));
        builder.append(nextLine);
        builder.append(MyFunctions.makeStringLeftAlign("Note :- " + masterPurchaseReturn.getNotes(), 48));
        builder.append(nextLine);
        builder.append(nextLine);
//        builder.append(MyFunctions.makeStringCentreAlign("***Thank-you for your visit***",48));
//        builder.append(nextLine);
//        builder.append(nextLine);
//        builder.append(nextLine);

        return builder.toString();
    }

    private String getCreditNoteReceipt(CreditDebitNote creditNote) {
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
                    Toast.makeText(PurchaseReturnViewActivity.this, getString(R.string.usb_msg_getpermission),
                            Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }
    };

    private void initializePrinterConfiguration() {
        usbCtrl = new UsbController(PurchaseReturnViewActivity.this,mHandler);
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

        Toast.makeText(PurchaseReturnViewActivity.this, getString(R.string.usb_msg_conn_state),
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
                Toast.makeText(PurchaseReturnViewActivity.this, getString(R.string.usb_msg_getpermission),
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
                Toast.makeText(PurchaseReturnViewActivity.this, getString(R.string.usb_msg_getpermission),
                        Toast.LENGTH_SHORT).show();
                isHasPaper = usbCtrl.revByte(dev);
                if( isHasPaper == 0x38 ){
                    Toast.makeText(PurchaseReturnViewActivity.this, "The printer has no paper",
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
        AlertDialog.Builder builder = new AlertDialog.Builder(PurchaseReturnViewActivity.this);
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
