package com.virtuzo.abhishek;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.virtuzo.abhishek.ApiCalls.GetBusinessLogo;
import com.virtuzo.abhishek.ApiCalls.GetCreditNotes;
import com.virtuzo.abhishek.ApiCalls.GetCustomers;
import com.virtuzo.abhishek.ApiCalls.GetDeliveryOrders;
import com.virtuzo.abhishek.ApiCalls.GetEwayBillReasons;
import com.virtuzo.abhishek.ApiCalls.GetEwayBills;
import com.virtuzo.abhishek.ApiCalls.GetGSTCategory;
import com.virtuzo.abhishek.ApiCalls.GetGroupProducts;
import com.virtuzo.abhishek.ApiCalls.GetHSCCodes;
import com.virtuzo.abhishek.ApiCalls.GetHSNfromFile;
import com.virtuzo.abhishek.ApiCalls.GetInvoices;
import com.virtuzo.abhishek.ApiCalls.GetLatestApkVersion;
import com.virtuzo.abhishek.ApiCalls.GetPaymentMode;
import com.virtuzo.abhishek.ApiCalls.GetProducts;
import com.virtuzo.abhishek.ApiCalls.GetPurchases;
import com.virtuzo.abhishek.ApiCalls.GetSalesReturns;
import com.virtuzo.abhishek.ApiCalls.GetStates;
import com.virtuzo.abhishek.ApiCalls.GetSuppliers;
import com.virtuzo.abhishek.ApiCalls.SendBrands;
import com.virtuzo.abhishek.ApiCalls.SendCreditNote;
import com.virtuzo.abhishek.ApiCalls.SendCustomers;
import com.virtuzo.abhishek.ApiCalls.SendDeliveryOrder;
import com.virtuzo.abhishek.ApiCalls.SendErrorLogs;
import com.virtuzo.abhishek.ApiCalls.SendEwayBill;
import com.virtuzo.abhishek.ApiCalls.SendInvoices;
import com.virtuzo.abhishek.ApiCalls.SendPayments;
import com.virtuzo.abhishek.ApiCalls.SendProductCategory;
import com.virtuzo.abhishek.ApiCalls.SendProducts;
import com.virtuzo.abhishek.ApiCalls.SendPurchaseInvoices;
import com.virtuzo.abhishek.ApiCalls.SendPurchaseReturns;
import com.virtuzo.abhishek.ApiCalls.SendSalesReturns;
import com.virtuzo.abhishek.ApiCalls.SendSuppliers;
import com.virtuzo.abhishek.Views.Activity.ListActivity.DraftInvoiceListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.HsnCodeListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.PurchaseListForPurchaseReturnActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.PurchaseReturnListActivity;
import com.virtuzo.abhishek.Views.Activity.Others.ComingSoonActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.CreditNoteListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.CustomerListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.DeliveryOrderListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.InvoiceListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.InvoiceListForPaymentActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.InvoiceListForSalesReturnActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewCustomerActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewDeliveryOrderActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewInvoiceActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewPurchaseActivity;
import com.virtuzo.abhishek.Views.Activity.NewFormActivity.NewSupplierActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.ProductListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.PurchaseListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.SalesReturnListActivity;
import com.virtuzo.abhishek.Views.Activity.ListActivity.SupplierListActivity;
import com.virtuzo.abhishek.Views.Activity.Others.WebPortalActivity;
import com.virtuzo.abhishek.Views.Activity.Others.WifiActivity;
import com.virtuzo.abhishek.database.DatabaseHandler;
import com.virtuzo.abhishek.modal.GroupProductItem;
import com.virtuzo.abhishek.modal.MyFunctions;
import com.virtuzo.abhishek.modal.Product;

import org.json.JSONException;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

import static com.virtuzo.abhishek.modal.MyFunctions.Dowloadimage;

public class DashBoardScreen extends AppCompatActivity {

    public static final String PREFS_NAME = "LoginPrefs";
    private static final String TAG = DashBoardScreen.class.getSimpleName();
    FrameLayout frameText, framedateAndTime, frameSimReplacement;
    FrameLayout showInvoicePayment, showNewCustomer, showNewSupplier, showNewSalesReturn, showPurchaseReturn, newPurchase, newInvoice, newDeliveryOrder;
    TextView dateAndTime, txtUserName, txtGSTNumber, txtTopUp, salesMTDTextView;
    String strTopUp = "", LastName = "", DateAndTime = "",
            OutletID = "", TabCode = "", FirstName = "", GSTNumber = "", BusinessId = "", RoleID = "",
            SalesMTDString = "", SalesTodayString = "", CollectionsMTDString = "", OutletTypeID = "",
            CollectionsTodayString = "", InvoicesMTDString = "", InvoicesTodayString = "",
            ClientCode = "", UserName = "", Password = "",
            Post = "", LoginID = "", DistributorID = "", ClientTypeID = "", strMobileNumber = "", strEmailId = "",
            strTraiffResult = "", strTraiffId = "", strTraiffCode = "", responseCode = "", strLoginId = "", strDistributorID = "",
            emailid = "", strFirstname = "", strPost = "", TotalTopup = "";
    double SalesMTD, SalesToday, CollectionsMTD, CollectionsToday, InvoicesMTD, InvoicesToday;
    final int PRODUCT_LIST_ITEM = 0, CUSTOMER_LIST_ITEM = 1, SUPPLIER_LIST_ITEM = 2, INVOICE_LIST_ITEM = 3;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle drawer_toggle;
    Toolbar toolbar_dashbord;
    NavigationView navigationView;
    ProgressDialog progressDialog;
    public static final String SharedPref_LatestProductID = "LatestProductID";
    public static final String SharedPref_LatestCustomerID = "LatestCustomerID";
    public static final String SharedPref_LatestSupplierID = "LatestSupplierID";
    TextView lastSyncedTextView, outletNameTextView;
    Button syncNowButton;
    SharedPreferences setting;
    SharedPreferences.Editor editor;
    ImageView  logoView;
    SyncNow syncNow;
    CustomViewGroup disableView;

    LinearLayout leftDashboardLayout, rightDashboardLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board_screen);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        syncNowButton = (Button) findViewById(R.id.syncNowButton);

        setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        editor = setting.edit();
        if (!setting.getBoolean("logged", false)) {
            Intent intent = new Intent(DashBoardScreen.this, UserLoginApi.Login.class);
            startActivity(intent);
            DashBoardScreen.this.finish();
        }
        FirstName = setting.getString("FirstName", "");
        UserName = setting.getString("UserName", "");
        Password = setting.getString("Password", "");
        ClientCode = setting.getString("ClientCode", "");
        GSTNumber = setting.getString("GSTNumber", "");
        BusinessId = setting.getString("BusinessId", "");
        TabCode = setting.getString("TabCode", "");
        OutletID = setting.getString("OutletID", "");
        RoleID = setting.getString("RoleID", "");
        OutletTypeID = setting.getString("OutletTypeID", "");
        SalesMTDString = setting.getString("SalesMTD", "");
        SalesTodayString = setting.getString("SalesToday", "");
        CollectionsMTDString = setting.getString("CollectionsMTD", "");
        CollectionsTodayString = setting.getString("CollectionsToday", "");
        InvoicesMTDString = setting.getString("InvoicesMTD", "");
        InvoicesTodayString = setting.getString("InvoicesToday", "");
//        showScreenDetails();

        leftDashboardLayout = (LinearLayout) findViewById(R.id.leftDashboardLayout);
        rightDashboardLayout = (LinearLayout) findViewById(R.id.rightDashboardLayout);

        getSupportActionBar().hide();
        toolbar_dashbord = (Toolbar) findViewById(R.id.tool_bar);
        drawerLayout= (DrawerLayout)findViewById(R.id.drawer_layout);
        navigationView=(NavigationView)findViewById(R.id.design_navigation_view);
        drawer_toggle = new ActionBarDrawerToggle(
                this, drawerLayout,toolbar_dashbord, R.string.open, R.string.close);
        drawerLayout.setDrawerListener(drawer_toggle);
        drawer_toggle.syncState();

        NavigationView menu_navigation_view = (NavigationView) findViewById(R.id.menu_navigation_view);

        menu_navigation_view.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.product_list:
                        Intent intentShowProductList = new Intent(getApplicationContext(), ProductListActivity.class);
                        intentShowProductList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
                        startActivity(intentShowProductList);
                        break;

                    case R.id.customer_list:
                        Intent intentShowCustomerList = new Intent(getApplicationContext(), CustomerListActivity.class);
                        intentShowCustomerList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
                        startActivity(intentShowCustomerList);
                        break;

                    case R.id.supplier_list:
                        Intent intentShowSupplierList = new Intent(getApplicationContext(), SupplierListActivity.class);
                        intentShowSupplierList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
                        startActivity(intentShowSupplierList);
                        break;

                    case R.id.invoice_list:
                        Intent intentShowInvoiceList = new Intent(getApplicationContext(), InvoiceListActivity.class);
                        intentShowInvoiceList.putExtra("BusinessID", BusinessId);
                        startActivity(intentShowInvoiceList);
                        break;

                    case R.id.delivery_order_list:
                        Intent intentShowDeliveryOrderList = new Intent(getApplicationContext(), DeliveryOrderListActivity.class);
                        startActivity(intentShowDeliveryOrderList);
                        break;

                    case R.id.purchase_list:
                        Intent intentPurchaseList = new Intent(getApplicationContext(), PurchaseListActivity.class);
                        startActivity(intentPurchaseList);
                        break;

                    case R.id.salesreturn_list:
                        Intent intentSalesReturnList = new Intent(getApplicationContext(), SalesReturnListActivity.class);
                        startActivity(intentSalesReturnList);
                        break;

                    case R.id.purchasereturn_list:
                        Intent intentPurchaseReturnList = new Intent(getApplicationContext(), PurchaseReturnListActivity.class);
                        startActivity(intentPurchaseReturnList);
                        break;

//                    case R.id.creditnote_list:
//                        Intent intentCreditNoteList = new Intent(getApplicationContext(), CreditNoteListActivity.class);
//                        intentCreditNoteList.putExtra("NoteType", "Credit Note");
//                        startActivity(intentCreditNoteList);
//                        break;
//
//                    case R.id.debitnote_list:
//                        Intent intentDebitNoteList = new Intent(getApplicationContext(), CreditNoteListActivity.class);
//                        intentDebitNoteList.putExtra("NoteType", "Debit Note");
//                        startActivity(intentDebitNoteList);
//                        break;

                    case R.id.hsn_code_list:
                        Intent intentHSNCodeList = new Intent(getApplicationContext(), HsnCodeListActivity.class);
                        intentHSNCodeList.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
                        startActivity(intentHSNCodeList);
                        break;

                    case R.id.web_portal:
                        Intent intentShowWebPortal = new Intent(getApplicationContext(), WebPortalActivity.class);
                        intentShowWebPortal.putExtra("WebLink", WebPortalActivity.WEB_PORTAL);
                        intentShowWebPortal.putExtra("ClientCode", ClientCode);
                        intentShowWebPortal.putExtra("UserName", UserName);
                        intentShowWebPortal.putExtra("Password", Password);
                        startActivity(intentShowWebPortal);
                        break;

                    case R.id.admin_login:
                        adminLogin();
                        break;

                    case R.id.update_app:
                       showOkDialog("Under Development");
                       break;

//                        InstallAPK downloadAndInstall = new InstallAPK();
//                        downloadAndInstall.setContext(DashBoardScreen.this);
//                        downloadAndInstall.execute("http://gstmadeeasy.net/apk/update.apk");
//                        break;

                    case R.id.logout_btn:
                        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
                        final View view = getLayoutInflater().inflate(R.layout.confirm_dialog, null);
                        builder.setView(view);
                        final AlertDialog dialog = builder.create();
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        TextView confirmMessageTextView = (TextView) view.findViewById(R.id.confirmMessage);
                        confirmMessageTextView.setText("Are you sure ?");
                        Button yesButton = (Button) view.findViewById(R.id.yesClick);
                        Button noButton = (Button) view.findViewById(R.id.noClick);
                        yesButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                                // logout
                                logoutUser();
                            }
                        });
                        noButton.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
                return false;
            }
        });

        newInvoice = (FrameLayout) findViewById(R.id.newInvoice);
        newDeliveryOrder = (FrameLayout) findViewById(R.id.newDeliveryOrder);
        newPurchase = (FrameLayout) findViewById(R.id.newPurchase);
        showInvoicePayment = (FrameLayout) findViewById(R.id.showInvoicePayment);
        showNewCustomer = (FrameLayout) findViewById(R.id.showNewCustomer);
        showNewSupplier = (FrameLayout) findViewById(R.id.showNewSupplier);
        showNewSalesReturn = (FrameLayout) findViewById(R.id.showSalesReturn);
        showPurchaseReturn = (FrameLayout) findViewById(R.id.showPurchaseReturn);

        View header = menu_navigation_view.getHeaderView(0);
        TextView userName = (TextView) header.findViewById(R.id.nav_username);
        TextView gstNumber = (TextView) header.findViewById(R.id.nav_gstnumber);
        userName.setText(FirstName);
        gstNumber.setText("GSTIN - " + GSTNumber);

        TextView versionTextView = (TextView) findViewById(R.id.versionTextView);
        versionTextView.setText("GSTMadeEasy " + getAppVersion());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setCanceledOnTouchOutside(false);

        lastSyncedTextView = (TextView) findViewById(R.id.lastSyncedTextView);
        String date = DatabaseHandler.getInstance().getTableLastSynced(DatabaseHandler.LIST_CODE_INVOICE_LIST);
        updateLastSynced(date);

        if(setting.getBoolean("firstrun", false) || setting.getBoolean("sync", false)) {
            if(!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setMessage("Sync Begins");
            }
            new SyncNow().execute();
        }

        String TabCode = setting.getString("TabCode", "");
        StringBuilder builder = new StringBuilder();
        builder.append(TabCode);
        builder.append("00000");
        // create tab generated customer id
        if(setting.getString(DashBoardScreen.SharedPref_LatestProductID, "").isEmpty()) {
            editor.putString(DashBoardScreen.SharedPref_LatestProductID, builder.toString());
        }
        if(setting.getString(DashBoardScreen.SharedPref_LatestCustomerID, "").isEmpty()) {
            editor.putString(DashBoardScreen.SharedPref_LatestCustomerID, builder.toString());
        }
        if(setting.getString(DashBoardScreen.SharedPref_LatestSupplierID, "").isEmpty()) {
            editor.putString(DashBoardScreen.SharedPref_LatestSupplierID, builder.toString());
        }

        editor.putBoolean("sync", false);
        editor.commit();


        newInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewInvoice = new Intent(DashBoardScreen.this, NewInvoiceActivity.class);
                Log.i("DashBoard", "new invoice clicked");
                startActivity(intentNewInvoice);
            }
        });

        newDeliveryOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewDeliveryOrder = new Intent(DashBoardScreen.this, NewDeliveryOrderActivity.class);
                Log.i("DashBoard", "new delivery order clicked");
                startActivity(intentNewDeliveryOrder);
            }
        });

        newPurchase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentNewPurchase = new Intent(DashBoardScreen.this, NewPurchaseActivity.class);
                Log.i("DashBoard", "new purchase clicked");
                startActivity(intentNewPurchase);
            }
        });

        showInvoicePayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShowInvoiceList = new Intent(getApplicationContext(), InvoiceListForPaymentActivity.class);
                intentShowInvoiceList.putExtra("BusinessID", BusinessId);
                startActivity(intentShowInvoiceList);
            }
        });

        showNewCustomer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShowNewCustomer = new Intent(getApplicationContext(), NewCustomerActivity.class);
                intentShowNewCustomer.putExtra("BusinessID", BusinessId);
                intentShowNewCustomer.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
                startActivity(intentShowNewCustomer);
            }
        });

        showNewSupplier.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShowNewSupplier = new Intent(getApplicationContext(), NewSupplierActivity.class);
                intentShowNewSupplier.putExtra("BusinessID", BusinessId);
                intentShowNewSupplier.putExtra(MyFunctions.CLASS_NAME, MyFunctions.CLASS_DASHBOARD);
                startActivity(intentShowNewSupplier);
            }
        });

        showNewSalesReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentSalesReturnList = new Intent(getApplicationContext(), InvoiceListForSalesReturnActivity.class);
                startActivity(intentSalesReturnList);
            }
        });

        showPurchaseReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intentShowPurchaseReturn = new Intent(getApplicationContext(), PurchaseListForPurchaseReturnActivity.class);
                startActivity(intentShowPurchaseReturn);
            }
        });

       // Dowloadimage(DashBoardScreen.this, setting.getString("Logo",null));
        logoView= (ImageView)findViewById(R.id.logo_view);
        outletNameTextView= (TextView)findViewById(R.id.tv_outletname);

//        if(null==MyFunctions.getImage()) {
            outletNameTextView.setText(setting.getString("OutletName", ""));
//        }

        logoView.setImageBitmap(MyFunctions.getImage());

//        if(isUpdateAvailable()){
//            if(new ConnectionDetector(this).isConnectingToInternet()) {
//                showUpdateDialog("Update Available", "oncreate");
//            }
//        }

        disableStatusBar();

    }

    private void disableStatusBar() {

        /*---------------------------For Status Bar----------------------------*/

        WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));

        WindowManager.LayoutParams localLayoutParams = new WindowManager.LayoutParams();
        localLayoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        localLayoutParams.gravity = Gravity.TOP;
        localLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |

                // this is to enable the notification to receive touch events
                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |

                // Draws over status bar
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;

        localLayoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        localLayoutParams.height = (int) (40 * getResources().getDisplayMetrics().scaledDensity);
        localLayoutParams.format = PixelFormat.TRANSPARENT;

        disableView = new CustomViewGroup(this);
        manager.addView(disableView, localLayoutParams);

          /*---------------------------For Navigation Bar----------------------------*/

//        WindowManager manager2 = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
//
//        WindowManager.LayoutParams localLayoutParams2 = new WindowManager.LayoutParams();
//        localLayoutParams2.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
//        localLayoutParams2.gravity = Gravity.BOTTOM;
//        localLayoutParams2.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE |
//
//                // this is to enable the notification to receive touch events
//                WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL |
//
//                // Draws over status bar
//                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN;
//
//        localLayoutParams2.width = WindowManager.LayoutParams.MATCH_PARENT;
//        localLayoutParams2.height = (int) (50 * getResources().getDisplayMetrics().scaledDensity);
//        localLayoutParams2.format = PixelFormat.TRANSPARENT;
//
//        blockingNavigationBarView = new CustomViewGroup(this);
//        manager2.addView(blockingNavigationBarView, localLayoutParams2);
//
//
//
//        MyFunctions.setWindowFull(DashBoardScreen.this);



    }

    private void updateLastSynced(String date) {
        lastSyncedTextView.setText(date);
    }

    private void hideSystemUI() {
        // Set the IMMERSIVE flag.
        // Set the content to appear under the system bars so that the content
        // doesn't resize when the system bars hide and show.
        findViewById(android.R.id.content).setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);
    }

    public void logoutUser() {
        SharedPreferences setting = getSharedPreferences(DashBoardScreen.PREFS_NAME, 0);
        SharedPreferences.Editor editor = setting.edit();
        editor.remove("logged");
        editor.commit();
        Intent intentLogin = new Intent(DashBoardScreen.this, UserLoginApi.Login.class);
        startActivity(intentLogin);
        DashBoardScreen.this.finish();
    }

    public String getAppVersion() {
        PackageInfo pInfo;
        String version = "";
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return version;
    }

    private boolean updateAvailable() {
        PackageInfo pInfo;
        String version;
        try {
            pInfo = this.getPackageManager().getPackageInfo(getPackageName(), 0);
            version = pInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            //request version
            //compare version
            //update available return true
            //else false;
        }
        return false;
    }

    public void adminLogin() {
        final String pin= setting.getString("AdminPin", MyFunctions.ADMINLOGIN_PIN);
        final EditText adminLoginPin = new EditText(this);
        adminLoginPin.setInputType(InputType.TYPE_CLASS_NUMBER);

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(adminLoginPin);
        alert.setTitle("Setup")
                .setMessage("Enter Pin to Login")
                .setPositiveButton("Login", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        String enteredPin= adminLoginPin.getText().toString();
                        if(enteredPin.equals(pin)){
                            MyFunctions.toggleKeyboard(DashBoardScreen.this);
                            showAdminDialog();
                        } else {
                            Toast.makeText(DashBoardScreen.this, "Incorrect Password", Toast.LENGTH_SHORT).show();
                        }
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {}
                })
                .show();
    }

    public void showAdminDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
        final View view = getLayoutInflater().inflate(R.layout.admin_login_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        Button removeKioskButton = (Button) view.findViewById(R.id.removeKioskButton);
        Button goToSettingsButton = (Button) view.findViewById(R.id.goToSettingsButton);
        Button backButton = (Button) view.findViewById(R.id.backButton);
        removeKioskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getPackageManager().clearPackagePreferredActivities(getPackageName());
                Toast.makeText(DashBoardScreen.this, "Kiosk Mode Disabled", Toast.LENGTH_SHORT).show();
            }
        });
        goToSettingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private boolean isUpdateAvailable() {
        String version=setting.getString("Version",getAppVersion());
        if(!version.equals(getAppVersion())){
            return true;
        }
        return false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public void syncNowButtonClick(View view) throws JSONException {
        if (checkInternetConnection()) {
            if(!progressDialog.isShowing()) {
                progressDialog.show();
                progressDialog.setMessage("Sync Begins");
            }
            syncNow = new SyncNow();
            syncNow.execute();
        }
    }

    public boolean checkInternetConnection() {
        ConnectivityManager cm =(ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        }
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        showOkDialog("Internet not available");
        return false;
    }

    private void storeImage(Bitmap image) {
        File pictureFile = getOutputMediaFile();
        if (pictureFile == null) {
            Log.d(TAG,
                    "Error creating media file, check storage permissions: ");// e.getMessage());
            return;
        }
        try {
            FileOutputStream fos = new FileOutputStream(pictureFile);
            image.compress(Bitmap.CompressFormat.PNG, 90, fos);
            fos.close();
        } catch (FileNotFoundException e) {
            Log.d(TAG, "File not found: " + e.getMessage());
        } catch (IOException e) {
            Log.d(TAG, "Error accessing file: " + e.getMessage());
        }
    }

    private File getOutputMediaFile(){
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File path =
                Environment.getExternalStoragePublicDirectory
                        (
                                // Environment.DIRECTORY_PICTURES
                                Environment.DIRECTORY_DOCUMENTS + "/gstMadeEasy/"
                        );

        // Make sure the path directory exists.
        if (!path.exists()) {
            // Make it, if it doesn't exit
            path.mkdirs();
        }

        File file = new File(path, "OutletLogo.jpg");
        return file;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Toast.makeText(this, "Barcode Scanner detected", Toast.LENGTH_LONG).show();
        if(newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            ((InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE))
                    .showInputMethodPicker();
            Toast.makeText(this, "Barcode Scanner detected. Please turn OFF Hardware/Physical keyboard to enable softkeyboard to function.", Toast.LENGTH_SHORT).show();
        }
    }

    public void wifiButtomClick(View view) {
//        startActivity(new Intent(WifiManager.ACTION_PICK_WIFI_NETWORK));
        startActivity(new Intent(this, WifiActivity.class));
    }

    public void SavedDraftsListClick(View view) {
        Toast.makeText(this, "Drafts List", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, DraftInvoiceListActivity.class));
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        logoView.setImageBitmap(MyFunctions.getImage());
        refreshScreenDetails();
    }

    private void showScreenDetails() {
        SalesMTD = Double.parseDouble(SalesMTDString);
        CollectionsMTD = Double.parseDouble(CollectionsMTDString);
        InvoicesMTD = Double.parseDouble(InvoicesMTDString);
        SalesToday = Double.parseDouble(SalesTodayString);
        CollectionsToday = Double.parseDouble(CollectionsTodayString);
        InvoicesToday = Double.parseDouble(InvoicesTodayString);

        setScreenDetails();
    }

    private void refreshScreenDetails() {

        if (setting.getBoolean(MyFunctions.HideDashboardDetails, false)) {
            leftDashboardLayout.setVisibility(View.INVISIBLE);
            rightDashboardLayout.setVisibility(View.INVISIBLE);
        } else {
            leftDashboardLayout.setVisibility(View.VISIBLE);
            rightDashboardLayout.setVisibility(View.VISIBLE);
        }

        ArrayList<Double> dashboardDetails = DatabaseHandler.getInstance().getDashboardDetails();
        SalesMTD = dashboardDetails.get(DatabaseHandler.DASHBOARD_DATA_SalesMTD);
        CollectionsMTD = dashboardDetails.get(DatabaseHandler.DASHBOARD_DATA_CollectionMTD);
        InvoicesMTD = dashboardDetails.get(DatabaseHandler.DASHBOARD_DATA_InvoicesMTD);
        SalesToday = dashboardDetails.get(DatabaseHandler.DASHBOARD_DATA_SalesToday);
        CollectionsToday = dashboardDetails.get(DatabaseHandler.DASHBOARD_DATA_CollectionToday);
        InvoicesToday = dashboardDetails.get(DatabaseHandler.DASHBOARD_DATA_InvoicesToday);

        setScreenDetails();
    }

    private void setScreenDetails() {
        TextView SalesMTDTextView = (TextView) findViewById(R.id.txtSalesMTD);
        TextView SalesTodayTextView = (TextView) findViewById(R.id.txtSalesToday);
        TextView CollectionMTDTextView = (TextView) findViewById(R.id.txtCollectionMTD);
        TextView CollectionTodayTextView = (TextView) findViewById(R.id.txtCollectionToday);
        TextView InvoicesMTDTextView = (TextView) findViewById(R.id.txtInvoicesMTD);
        TextView InvoicesTodayTextView = (TextView) findViewById(R.id.txtInvoicesToday);

        // without commas
//        String rupeeSymbol = "\u20B9";
//        SalesMTDTextView.setText("Sales MTD : " + rupeeSymbol + String.format("%.2f", this.SalesMTD));
//        SalesTodayTextView.setText("Sales Today : " + rupeeSymbol + String.format("%.2f", this.SalesToday));
//        CollectionMTDTextView.setText("Collection MTD : " + rupeeSymbol + String.format("%.2f", this.CollectionsMTD));
//        CollectionTodayTextView.setText("Collection Today : " + rupeeSymbol + String.format("%.2f", this.CollectionsToday));

        // with Rs. as rupee symbol
//        Locale locale = new Locale("en", "in");
//        SalesMTDTextView.setText("Sales MTD : " + NumberFormat.getCurrencyInstance(locale).format(this.SalesMTD));
//        SalesTodayTextView.setText("Sales Today : " + NumberFormat.getCurrencyInstance(locale).format(this.SalesToday));
//        CollectionMTDTextView.setText("Collection MTD : " + NumberFormat.getCurrencyInstance(locale).format(this.CollectionsMTD));
//        CollectionTodayTextView.setText("Collection Today : " + NumberFormat.getCurrencyInstance(locale).format(this.CollectionsToday));

        Locale locale = new Locale("en","IN");
        DecimalFormat decimalFormat = (DecimalFormat) DecimalFormat.getCurrencyInstance(locale);
        DecimalFormatSymbols dfs = DecimalFormatSymbols.getInstance(locale);
        dfs.setCurrencySymbol("\u20B9");
        decimalFormat.setDecimalFormatSymbols(dfs);
        SalesMTDTextView.setText("Sales MTD : " + decimalFormat.format(this.SalesMTD));
        SalesTodayTextView.setText("Sales Today : " + decimalFormat.format(this.SalesToday));
        CollectionMTDTextView.setText("Collection MTD : " + decimalFormat.format(this.CollectionsMTD));
        CollectionTodayTextView.setText("Collection Today : " + decimalFormat.format(this.CollectionsToday));

        InvoicesMTDTextView.setText("Invoices MTD : " + (int)this.InvoicesMTD);
        InvoicesTodayTextView.setText("Invoices Today : " + (int)this.InvoicesToday);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (disableView !=null) {
            WindowManager manager = ((WindowManager) getApplicationContext().getSystemService(Context.WINDOW_SERVICE));
            manager.removeView(disableView);
        }
        if(progressDialog!=null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void openVirtuzoIn(View view) {
        Intent intentShowWebPortal = new Intent(getApplicationContext(), WebPortalActivity.class);
        intentShowWebPortal.putExtra("WebLink", WebPortalActivity.VIRTUZO_IN);
        startActivity(intentShowWebPortal);
    }

    private void errorDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        progressDialog.dismiss();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText("Error occured");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    private void errorCodeDialog(String errorCode, String exception) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
        final View view = getLayoutInflater().inflate(R.layout.error_code_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
//        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        progressDialog.dismiss();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        TextView errorCodeTextView = (TextView) view.findViewById(R.id.errorCodeTextView);
        errorCodeTextView.setMovementMethod(new ScrollingMovementMethod());
        okMessageTextView.setText("Error occured");
        errorCodeTextView.setText("Error Code : " + errorCode + "\n" + exception);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
    }

    public void updateProgressDialogMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                progressDialog.setMessage(message);
            }
        });
    }

    private void successDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        progressDialog.dismiss();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText("Synced Successfully!!");
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                outletNameTextView.setText(setting.getString("OutletName", ""));
                logoView.setImageBitmap(MyFunctions.getImage());
                dialog.dismiss();
            }
        });
    }

    public void showOkDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
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

    public void showUpdateDialog(String heading,String from) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
        final View view = getLayoutInflater().inflate(R.layout.ok_alert_dialog, null);
        builder.setView(view);
        final AlertDialog dialog = builder.create();
        if(from.equalsIgnoreCase("sync")) {
            dialog.setCanceledOnTouchOutside(false);
        }
        dialog.show();
        Button okButton = (Button) view.findViewById(R.id.okClick);
        okButton.setText("UPDATE");
        TextView okMessageTextView = (TextView) view.findViewById(R.id.okMessage);
        okMessageTextView.setText(heading);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                        InstallAPK downloadAndInstall = new InstallAPK();
                        downloadAndInstall.setContext(DashBoardScreen.this);
                        String apkpath=setting.getString("ApkPath","");
//                        String apkpath= "https://gstmadeeasy.net/apk/update.apk";
                        if(!apkpath.equals("")){
                            downloadAndInstall.execute(apkpath);
                            //   TODO change link
                            //   downloadAndInstall.execute("https://pos.gstmadeeasy.net/apk/version/v10/update.apk");
                        }
            }
        });
    }

    public void showSuccessDataLoadedMessage(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                successfulDataLoadedDialog(message);
            }
        });
    }

    public void successfulDataLoadedDialog(String heading) {
        AlertDialog.Builder builder = new AlertDialog.Builder(DashBoardScreen.this);
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
//                if(null==MyFunctions.getImage()) {
                    outletNameTextView.setText(setting.getString("OutletName", ""));

//                }else{
//                    outletNameTextView.setText("");
//                }
                logoView.setImageBitmap(MyFunctions.getImage());
                dialog.dismiss();
            }
        });
    }

    private class LoadHSN extends AsyncTask<String, String, String> implements GetHSNfromFile.CallBack, GetHSCCodes.CallBack {

        GetHSNfromFile getHSNfromFile;
        GetHSCCodes getHSCCodes;

        @Override
        protected String doInBackground(String... params) {
//            getHSNfromFile = new GetHSNfromFile(DashBoardScreen.this, this);
            getHSCCodes = new GetHSCCodes(BusinessId, this);
            return null;
        }

        @Override
        public void afterGetHSNCodes() {
            Log.e("HSN Timer", "inserted into database");

            progressDialog.dismiss();
            Dowloadimage(setting.getString("Logo",null));

            showSuccessDataLoadedMessage("Data loaded successfully");
        }

        @Override
        public void afterGetHSCCodes() {
            Log.e("HSN Timer", "inserted into database");

            progressDialog.dismiss();
            Dowloadimage(setting.getString("Logo",null));

            showSuccessDataLoadedMessage("Data loaded successfully");
        }
    }

    private class SyncNow extends AsyncTask<String, String, String> implements SendCustomers.CallBack, GetCustomers.CallBack, SendInvoices.CallBack, SendPayments.CallBack, GetInvoices.CallBack, SendPurchaseInvoices.CallBack, GetProducts.CallBack, SendSuppliers.CallBack, GetPurchases.CallBack, GetSuppliers.CallBack, GetLatestApkVersion.CallBack, SendProductCategory.CallBack, SendBrands.CallBack, SendProducts.CallBack, SendEwayBill.CallBack, GetEwayBills.CallBack, SendErrorLogs.CallBack, SendDeliveryOrder.CallBack, GetStates.CallBack, GetEwayBillReasons.CallBack, GetGSTCategory.CallBack, GetPaymentMode.CallBack, GetDeliveryOrders.CallBack, SendSalesReturns.CallBack, SendCreditNote.CallBack, GetSalesReturns.CallBack, GetCreditNotes.CallBack, SendPurchaseReturns.CallBack, GetGroupProducts.CallBack, GetBusinessLogo.CallBack {

        double totalCount = 0.30;
        double counter = 0;

        String syncingMessage = "Please wait, this may take a while ";

        SendErrorLogs sendErrorLogs;
        SendProductCategory sendProductCategory;
        SendBrands sendBrands;
        SendProducts sendProducts;
        GetProducts getProducts;
        GetGroupProducts getGroupProducts;
        SendCustomers sendCustomers;
        GetCustomers getCustomers;
        SendDeliveryOrder sendDeliveryOrder;
        GetDeliveryOrders getDeliveryOrders;
        SendInvoices sendInvoices;
        SendPayments sendPayments;
        GetInvoices getInvoices;
        SendEwayBill sendEwayBill;
        GetEwayBills getEwayBills;
        SendSalesReturns sendSalesReturns;
        GetSalesReturns getSalesReturns;
        SendCreditNote sendCreditNote;
        GetCreditNotes getCreditNotes;
        SendSuppliers sendSuppliers;
        SendPurchaseInvoices sendPurchaseInvoices;
        GetPurchases getPurchases;
        GetSuppliers getSuppliers;
        GetLatestApkVersion getLatestApkVersion;
        GetStates getStates;
        GetGSTCategory getGSTCategory;
        GetEwayBillReasons getEwayBillReasons;
        GetPaymentMode getPaymentMode;
        GetBusinessLogo getBusinessLogo;

        SendPurchaseReturns sendPurchaseReturns;

        @Override
        protected String doInBackground(String... params) {

            getLatestApkVersion= new GetLatestApkVersion(DashBoardScreen.this, this);

//            sendBrands = new SendBrands(BusinessId, this);
//            sendProducts = new SendProducts(BusinessId, this);

//            sendCustomers = new SendCustomers(BusinessId, TabCode, this);
//            getCustomers = new GetCustomers(BusinessId, this);
//            sendInvoices = new SendInvoices(BusinessId, TabCode, this);
//            sendPayments = new SendPayments(BusinessId, TabCode, this);
//            getInvoices = new GetInvoices(BusinessId, OutletID, this);
//            sendPurchaseInvoices = new SendPurchaseInvoices(BusinessId, TabCode, this);
//            sendSalesReturns = new SendSalesReturns(BusinessId, TabCode, this);

//            sendCreditNote = new SendCreditNote(BusinessId, TabCode, this);
//            getSalesReturns = new GetSalesReturns(BusinessId, OutletID, RoleID, OutletTypeID, this);

//            sendPurchaseReturns = new SendPurchaseReturns(BusinessId, TabCode, this);

            return strTraiffResult;
        }

        @Override
        public void afterGetLatestApkVersion() {

//            String version=setting.getString("Version","");
//            !version.equals(getAppVersion())
            if(isUpdateAvailable() && !setting.getBoolean("firstrun", false)){
                new SyncBeforeUpdate().execute();
//                    cancel(true);
//
//                if(!isCancelled()){
//                    return;}

            }else {
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendErrorLogs = new SendErrorLogs(BusinessId, TabCode, this);
                }
            }
        }

        @Override
        public void afterSendErrorLogs() {
            // next api call
            counter++;
            updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
            if (checkInternetConnection()) {
                sendProductCategory = new SendProductCategory(BusinessId, this);
//                getBusinessLogo = new GetBusinessLogo(BusinessId, OutletID, this, DashBoardScreen.this);
            }
        }

        @Override
        public void afterSendProductCategory() {
            if(sendProductCategory.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().productCategoriesSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendBrands = new SendBrands(BusinessId, this);
                }
            } else {
                errorCodeDialog(sendProductCategory.errorCode, sendProductCategory.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendProductCategory.errorCode, sendProductCategory.apiResponse);
            }
        }

        @Override
        public void afterSendBrands() {
            if(sendBrands.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().brandsSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendProducts = new SendProducts(BusinessId, this);
                }
            } else {
                errorCodeDialog(sendBrands.errorCode, sendBrands.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendBrands.errorCode, sendBrands.apiResponse);
            }
        }

        @Override
        public void afterSendProducts() {
            if(sendProducts.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
//                DatabaseHandler.getInstance().productListSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getProducts = new GetProducts(Integer.parseInt(BusinessId), this);
                }
            } else {
                errorCodeDialog(sendProducts.errorCode, sendProducts.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendProducts.errorCode, sendProducts.apiResponse);
            }
        }

        @Override
        public void afterGetProducts() {
            if(getProducts.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateProductList(getProducts.productList);
                DatabaseHandler.getInstance().insertLastSynced(DatabaseHandler.LIST_CODE_PRODUCT_LIST);
                DatabaseHandler.getInstance().updateProductCategoryList(getProducts.productCategoryArrayList);
                DatabaseHandler.getInstance().updateBrandList(getProducts.brandArrayList);
                DatabaseHandler.getInstance().updateProductUnitList(getProducts.unitArrayList);

                // Dummy Group Product Added
//                Product dummyProduct = new Product();
//                dummyProduct.setProductID(7000);
//                dummyProduct.setProductCode("COMBO 121");
//                dummyProduct.setProductName("South Indian Platter Combo");
//                dummyProduct.setBrandID(0);
//                dummyProduct.setBrand("");
//                dummyProduct.setBarcode("1234567");
//                dummyProduct.setSalesPrice(1000);
//                dummyProduct.setHSNID(0);
//                dummyProduct.setHSNCode("");
//                dummyProduct.setUnitID(0);
//                dummyProduct.setUnit("");
//                dummyProduct.setTAX(5);
//                dummyProduct.setProductCategoryId(71);
//                dummyProduct.setProductSubCategoryId(0);
//                dummyProduct.setIsGroupProduct(1);
//                dummyProduct.setSynced(1);
//
//                GroupProductItem groupProductItem1 = new GroupProductItem();
//                groupProductItem1.setID(1);
//                groupProductItem1.setProductID(7000);
//                groupProductItem1.setItemID(0); // for now
//                groupProductItem1.setItemName("Masala Dosa");
//                groupProductItem1.setItemCode("MD 101");
//                groupProductItem1.setQuantity(4);
//                groupProductItem1.setUnitID(0);
//                groupProductItem1.setPrice(400);
//                groupProductItem1.setSynced(1);
//
//                GroupProductItem groupProductItem2 = new GroupProductItem();
//                groupProductItem2.setID(2);
//                groupProductItem2.setProductID(7000);
//                groupProductItem2.setItemID(0); // for now
//                groupProductItem2.setItemName("Onion Uttapam");
//                groupProductItem2.setItemCode("OU 101");
//                groupProductItem2.setQuantity(3);
//                groupProductItem2.setUnitID(0);
//                groupProductItem2.setPrice(600);
//                groupProductItem2.setSynced(1);
//
//                ArrayList<GroupProductItem> groupProductItems = new ArrayList<>();
//                groupProductItems.add(groupProductItem1);
//                groupProductItems.add(groupProductItem2);
//
//                DatabaseHandler.getInstance().addNewProduct(dummyProduct);
//                DatabaseHandler.getInstance().updateGroupProductItemList(groupProductItems);

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getGroupProducts = new GetGroupProducts(BusinessId, this);
                }
            } else {
                errorCodeDialog(getProducts.errorCode, getProducts.apiResponse);
                MyFunctions.errorOccuredInApiCall(getProducts.getClass().getSimpleName(), getProducts.apiResponse);
            }
        }

        @Override
        public void afterGetGroupProducts() {
            if (MyFunctions.SUCCESS_MESSAGE.equals(getGroupProducts.resultMessage)) {
                DatabaseHandler.getInstance().updateGroupProductItemList(getGroupProducts.groupProductItems);

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendCustomers = new SendCustomers(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(getGroupProducts.errorCode, getGroupProducts.apiResponse);
                MyFunctions.errorOccuredInApiCall(getGroupProducts.getClass().getSimpleName(), getGroupProducts.apiResponse);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        public void afterSendCustomers() {
            if (sendCustomers.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
//                DatabaseHandler.getInstance().customerListSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getCustomers = new GetCustomers(BusinessId, this);
                }
            } else {
                errorCodeDialog(sendCustomers.errorCode, sendCustomers.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendCustomers.errorCode, sendCustomers.apiResponse);
            }
        }

        @Override
        public void afterGetCustomers() {
            if(getCustomers.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateCustomerList(getCustomers.customerList);
                DatabaseHandler.getInstance().insertLastSynced(DatabaseHandler.LIST_CODE_CUSTOMER_LIST);
                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendDeliveryOrder = new SendDeliveryOrder(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(getCustomers.errorCode, getCustomers.apiResponse);
                MyFunctions.errorOccuredInApiCall(getCustomers.errorCode, getCustomers.apiResponse);
            }
        }

        @Override
        public void afterSendDeliveryOrder() {
            if(sendDeliveryOrder.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getDeliveryOrders = new GetDeliveryOrders(BusinessId, OutletID, RoleID, OutletTypeID, this);
                }
            } else {
                errorCodeDialog(sendDeliveryOrder.errorCode, sendDeliveryOrder.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendDeliveryOrder.errorCode, sendDeliveryOrder.apiResponse);
            }

        }

        @Override
        public void afterGetDeliveryOrders() {
            if(getDeliveryOrders.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateMasterDeliveryOrderList(getDeliveryOrders.masterDeliveryOrderArrayList);
                DatabaseHandler.getInstance().updateTransactionDeliveryOrderList(getDeliveryOrders.transactionDeliveryOrderArrayList);

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendInvoices = new SendInvoices(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(getDeliveryOrders.errorCode, getDeliveryOrders.apiResponse);
                MyFunctions.errorOccuredInApiCall(getDeliveryOrders.errorCode, getDeliveryOrders.apiResponse);
            }
        }

        @Override
        public void afterSendInvoices() {
            if (sendInvoices.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                // SYNC = 1, already done in the api

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendPayments = new SendPayments(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(sendInvoices.errorCode, sendInvoices.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendInvoices.errorCode, sendInvoices.apiResponse);
            }
        }

        @Override
        public void afterSendPayments() {
            if (sendPayments.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
//                DatabaseHandler.getInstance().paymentsSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
//                DatabaseHandler.getInstance().salesInvoiceSyncSuccessful();
                if (checkInternetConnection()) {
                    getInvoices = new GetInvoices(BusinessId, OutletID, RoleID, OutletTypeID, this);
                }
            } else {
                errorCodeDialog(sendPayments.errorCode, sendPayments.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendPayments.errorCode, sendPayments.apiResponse);
            }
        }

        @Override
        public void afterGetInvoices() {
            if(getInvoices.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateMasterSalesList(getInvoices.masterSalesArrayList);
                String dateTime = DatabaseHandler.getInstance().insertLastSynced(DatabaseHandler.LIST_CODE_INVOICE_LIST);
                updateLastSynced(dateTime);
                DatabaseHandler.getInstance().updateTransactionSalesList(getInvoices.transactionSalesArrayList);
                DatabaseHandler.getInstance().updatePaymentList(getInvoices.paymentArrayList);
                refreshScreenDetails();

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendEwayBill = new SendEwayBill(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(getInvoices.errorCode, getInvoices.apiResponse);
                MyFunctions.errorOccuredInApiCall(getInvoices.errorCode, getInvoices.apiResponse);
            }
        }

        @Override
        public void afterSendEwayBill() {
            if(sendEwayBill.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
//                DatabaseHandler.getInstance().eWayBillsSyncSuccessful();

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getEwayBills = new GetEwayBills(BusinessId, this);
                }
            }else {
                errorCodeDialog(sendEwayBill.errorCode, sendEwayBill.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendEwayBill.errorCode, sendEwayBill.apiResponse);
            }
        }

        @Override
        public void afterGetEwayBills() {
            if(getEwayBills.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
                DatabaseHandler.getInstance().updateEwayBillsList(getEwayBills.ewayBillArrayList);

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendSalesReturns = new SendSalesReturns(BusinessId, TabCode, this);
                }
            }else {
                errorCodeDialog(getEwayBills.errorCode, getEwayBills.apiResponse);
                MyFunctions.errorOccuredInApiCall(getEwayBills.errorCode, getEwayBills.apiResponse);
            }
        }

        @Override
        public void afterSendSalesReturnInvoices() {
            if (sendSalesReturns.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getSalesReturns = new GetSalesReturns(BusinessId, OutletID, RoleID, OutletTypeID, this);
                }
            } else {
                errorCodeDialog(sendSalesReturns.errorCode, sendSalesReturns.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendSalesReturns.errorCode, sendSalesReturns.apiResponse);
            }
        }

        @Override
        public void afterGetSalesReturn() {
            if (getSalesReturns.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateMasterSalesReturnList(getSalesReturns.masterSalesReturnArrayList);
                DatabaseHandler.getInstance().updateTransactionSalesReturnList(getSalesReturns.transactionSalesReturnArrayList);

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendSuppliers = new SendSuppliers(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(getSalesReturns.errorCode, getSalesReturns.apiResponse);
                MyFunctions.errorOccuredInApiCall(getSalesReturns.errorCode, getSalesReturns.apiResponse);
            }
        }

        @Override
        public void afterSendSuppliers() {
            if(sendSuppliers.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
//                DatabaseHandler.getInstance().supplierListSyncSuccessful();

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getSuppliers = new GetSuppliers(Integer.parseInt(BusinessId), this);
                }
            } else {
                errorCodeDialog(sendSuppliers.errorCode, sendSuppliers.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendSuppliers.errorCode, sendSuppliers.apiResponse);
            }
        }

        @Override
        public void afterGetSuppliers() {
            if(getSuppliers.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateSupplierList(getSuppliers.supplierList);
                DatabaseHandler.getInstance().insertLastSynced(DatabaseHandler.LIST_CODE_SUPPLIER_LIST);
                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendPurchaseInvoices = new SendPurchaseInvoices(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(getSuppliers.errorCode, getSuppliers.apiResponse);
                MyFunctions.errorOccuredInApiCall(getSuppliers.errorCode, getSuppliers.apiResponse);
            }
        }

        @Override
        public void afterSendPurchaseInvoices() {
            if (sendPurchaseInvoices.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getPurchases = new GetPurchases(BusinessId, OutletID, RoleID, OutletTypeID, this);
                }
            } else {
                errorCodeDialog(sendPurchaseInvoices.errorCode, sendPurchaseInvoices.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendPurchaseInvoices.errorCode, sendPurchaseInvoices.apiResponse);
            }
        }

        @Override
        public void afterGetPurchases() {
            if(getPurchases.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateMasterPurchaseList(getPurchases.masterPurchaseArrayList);
                DatabaseHandler.getInstance().updateTransactionPurchaseList(getPurchases.transactionPurchaseArrayList);

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendPurchaseReturns = new SendPurchaseReturns(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(getPurchases.errorCode, getPurchases.apiResponse);
                MyFunctions.errorOccuredInApiCall(getPurchases.errorCode, getPurchases.apiResponse);
            }
        }

        @Override
        public void afterSendPurchaseReturnInvoices() {
            if (sendPurchaseReturns.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    sendCreditNote = new SendCreditNote(BusinessId, TabCode, this);
                }
            } else {
                errorCodeDialog(sendPurchaseReturns.errorCode, sendPurchaseReturns.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendPurchaseReturns.errorCode, sendPurchaseReturns.apiResponse);
            }
        }

        @Override
        public void afterSendCreditNote() {
            if(sendCreditNote.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getCreditNotes = new GetCreditNotes(BusinessId, OutletID, RoleID, OutletTypeID, this);
                }
            } else {
                errorCodeDialog(sendCreditNote.errorCode, sendCreditNote.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendCreditNote.errorCode, sendCreditNote.apiResponse);
            }
        }

        @Override
        public void afterGetCreditNotes() {
            if(getCreditNotes.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
                DatabaseHandler.getInstance().updateCreditNoteList(getCreditNotes.creditNoteArrayList);

                // next api call
                counter++;
                updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
                if (checkInternetConnection()) {
                    getStates = new GetStates(this);
                }
            }else {
                errorCodeDialog(getCreditNotes.errorCode, getCreditNotes.apiResponse);
                MyFunctions.errorOccuredInApiCall(getCreditNotes.errorCode, getCreditNotes.apiResponse);
            }
        }

        @Override
        public void afterGetStates() {
            if(getStates.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateStateList(getStates.stateList);
            } else {
                MyFunctions.errorOccuredInApiCall(getStates.errorCode, getStates.apiResponse);
            }
            counter++;
            updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
            if (checkInternetConnection()) {
                getEwayBillReasons = new GetEwayBillReasons(BusinessId, this);
            }
        }

        @Override
        public void afterGetEwayBillReasons() {
            if(getEwayBillReasons.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateEwayBillReasonsList(getEwayBillReasons.ewayBillReasonArrayList);
            } else {
                MyFunctions.errorOccuredInApiCall(getEwayBillReasons.errorCode, getEwayBillReasons.apiResponse);
            }
            counter++;
            updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
            if (checkInternetConnection()) {
                getGSTCategory = new GetGSTCategory(Integer.parseInt(BusinessId), this);
            }
        }

        @Override
        public void afterGetGSTCategory(){
            if(getGSTCategory.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updateGSTCategoryList(getGSTCategory.gstCategoryList);
            } else {
                MyFunctions.errorOccuredInApiCall(getGSTCategory.errorCode, getGSTCategory.apiResponse);
            }
            counter++;
            updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
            getBusinessLogo = new GetBusinessLogo(BusinessId, OutletID, this, DashBoardScreen.this);
        }

        @Override
        public void afterGetBusinessLogo() {
            counter++;
            updateProgressDialogMessage(syncingMessage + " (" + (int)(counter/totalCount) + "%)");
            getPaymentMode= new GetPaymentMode(Integer.parseInt(BusinessId), this);
        }

        @Override
        public void afterGetPaymentMode() {
            if(getPaymentMode.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().updatePaymentModeList(getPaymentMode.paymentModeList);
            } else {
                MyFunctions.errorOccuredInApiCall(getPaymentMode.errorCode, getPaymentMode.apiResponse);
            }

            if(setting.getBoolean("firstrun", false) || setting.getBoolean("sync", false)) {
                editor.putBoolean("firstrun", false);
                editor.putBoolean("sync", false);
                editor.commit();
                new LoadHSN().execute();
            } else {
                Dowloadimage(setting.getString("Logo",null));

                // the end of syncing
                progressDialog.dismiss();
                successDialog();
            }
        }

    }

    private class SyncBeforeUpdate extends AsyncTask<String, String, String> implements SendCustomers.CallBack, SendInvoices.CallBack, SendPayments.CallBack, SendPurchaseInvoices.CallBack, SendSuppliers.CallBack, SendProductCategory.CallBack, SendProducts.CallBack, SendBrands.CallBack, SendEwayBill.CallBack, SendDeliveryOrder.CallBack, SendSalesReturns.CallBack, SendCreditNote.CallBack, SendErrorLogs.CallBack {

        int totalCount = 14;
        int counter = 0;

        SendErrorLogs sendErrorLogs;
        SendProductCategory sendProductCategory;
        SendBrands sendBrands;
        SendProducts sendProducts;
        SendCustomers sendCustomers;
        SendDeliveryOrder sendDeliveryOrder;
        SendInvoices sendInvoices;
        SendPayments sendPayments;
        SendEwayBill sendEwayBill;
        SendSalesReturns sendSalesReturns;
        SendCreditNote sendCreditNote;
        SendSuppliers sendSuppliers;
        SendPurchaseInvoices sendPurchaseInvoices;

        @Override
        protected String doInBackground(String... params) {
          //  progressDialog.setMessage("Syncing...");
//            progressDialog.setMessage("Send Customers");
            counter++;
            updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
            sendErrorLogs = new SendErrorLogs(BusinessId, TabCode, this);
//            sendCustomers = new SendCustomers(BusinessId, TabCode, this);

//            getCustomers = new GetCustomers(BusinessId, this);
//            sendInvoices = new SendInvoices(BusinessId, TabCode, this);
//            sendPayments = new SendPayments(BusinessId, TabCode, this);
//            getInvoices = new GetInvoices(BusinessId, OutletID, this);
            return strTraiffResult;
        }

        @Override
        public void afterSendErrorLogs() {
            counter++;
            updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
            sendProductCategory = new SendProductCategory(BusinessId, this);
        }

        @Override
        public void afterSendProductCategory() {
            if(sendProductCategory.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().productCategoriesSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendBrands = new SendBrands(BusinessId, this);
            } else {
                errorCodeDialog(sendProductCategory.errorCode, sendProductCategory.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendProductCategory.errorCode, sendProductCategory.apiResponse);
            }
        }

        @Override
        public void afterSendBrands() {
            if(sendBrands.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                DatabaseHandler.getInstance().brandsSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendProducts = new SendProducts(BusinessId, this);
            } else {
                errorCodeDialog(sendBrands.errorCode, sendBrands.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendBrands.errorCode, sendBrands.apiResponse);
            }
        }

        @Override
        public void afterSendProducts() {
            if(sendProducts.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
//                DatabaseHandler.getInstance().productListSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendCustomers = new SendCustomers(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendProducts.errorCode, sendProducts.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendProducts.errorCode, sendProducts.apiResponse);
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }

        @Override
        public void afterSendCustomers() {
            if (sendCustomers.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
//                DatabaseHandler.getInstance().customerListSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendDeliveryOrder = new SendDeliveryOrder(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendCustomers.errorCode, sendCustomers.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendCustomers.errorCode, sendCustomers.apiResponse);
            }
        }

        @Override
        public void afterSendDeliveryOrder() {
            if(sendDeliveryOrder.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {

                // call next api
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendInvoices = new SendInvoices(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendDeliveryOrder.errorCode, sendDeliveryOrder.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendDeliveryOrder.errorCode, sendDeliveryOrder.apiResponse);
            }
        }

        @Override
        public void afterSendInvoices() {
            if (sendInvoices.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                // SYNC = 1, already done in the api

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendPayments = new SendPayments(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendInvoices.errorCode, sendInvoices.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendInvoices.errorCode, sendInvoices.apiResponse);
            }
        }

        @Override
        public void afterSendPayments() {
            if (sendPayments.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
//                DatabaseHandler.getInstance().paymentsSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendEwayBill= new SendEwayBill(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendPayments.errorCode, sendPayments.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendPayments.errorCode, sendPayments.apiResponse);
            }
        }

        @Override
        public void afterSendEwayBill() {
            if(sendEwayBill.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
//                DatabaseHandler.getInstance().eWayBillsSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendSalesReturns = new SendSalesReturns(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendEwayBill.errorCode, sendEwayBill.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendEwayBill.errorCode, sendEwayBill.apiResponse);
            }
        }

        @Override
        public void afterSendSalesReturnInvoices() {
            if(sendSalesReturns.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
//                DatabaseHandler.getInstance().eWayBillsSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendCreditNote = new SendCreditNote(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendSalesReturns.errorCode, sendSalesReturns.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendSalesReturns.errorCode, sendSalesReturns.apiResponse);
            }
        }

        @Override
        public void afterSendCreditNote() {
            if(sendCreditNote.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
//                DatabaseHandler.getInstance().eWayBillsSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendSuppliers= new SendSuppliers(BusinessId, TabCode, this);
            } else {
                errorCodeDialog(sendCreditNote.errorCode, sendCreditNote.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendCreditNote.errorCode, sendCreditNote.apiResponse);
            }
        }

        @Override
        public void afterSendSuppliers() {
            if(sendSuppliers.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)){
//                DatabaseHandler.getInstance().supplierListSyncSuccessful();

                // call next API
                counter++;
                updateProgressDialogMessage("Syncing (" + counter + "/" + totalCount + ")");
                sendPurchaseInvoices = new SendPurchaseInvoices(BusinessId, TabCode, this);

            } else {
                errorCodeDialog(sendSuppliers.errorCode, sendSuppliers.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendSuppliers.errorCode, sendSuppliers.apiResponse);
            }
        }

        @Override
        public void afterSendPurchaseInvoices() {
            if (sendPurchaseInvoices.resultMessage.equals(MyFunctions.SUCCESS_MESSAGE)) {
                progressDialog.dismiss();
                showUpdateDialog("Update Available", "sync");
            } else {
                errorCodeDialog(sendPurchaseInvoices.errorCode, sendPurchaseInvoices.apiResponse);
                MyFunctions.errorOccuredInApiCall(sendPurchaseInvoices.errorCode, sendPurchaseInvoices.apiResponse);
            }
        }

    }

    public class InstallAPK extends AsyncTask<String,Void,Void> {

        ProgressDialog downloadingprogress = new ProgressDialog(DashBoardScreen.this);
        int status = 0;
        private Context context;

        public void setContext(Context context){
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            downloadingprogress.setMessage("Downloading Update");
            downloadingprogress.setCancelable(false);
            downloadingprogress.setCanceledOnTouchOutside(false);
            downloadingprogress.show();
        }

        @Override
        protected Void doInBackground(String... arg0) {
            int count;
            try {
                URL url = new URL(arg0[0]);

//                HttpsURLConnection c = (HttpsURLConnection) url.openConnection();
//                c.setRequestMethod("GET");
//                c.setDoOutput(true);
//                c.connect();

                File sdcard = Environment.getExternalStorageDirectory();
                File myDir = new File(sdcard,"/GSTMadeEasy");
                myDir.mkdirs();
                File outputFile = new File(myDir, "update.apk");
                if(outputFile.exists()){
                    outputFile.delete();
                }

                InputStream is = url.openStream();

                DataInputStream dis = new DataInputStream(is);

                byte[] buffer = new byte[1024];
                int length;

                FileOutputStream fos = new FileOutputStream(outputFile);
                while ((length = dis.read(buffer))>0) {
                    fos.write(buffer, 0, length);
                }

//                FileOutputStream fos = new FileOutputStream(outputFile);
//                InputStream is = c.getInputStream();
//                byte[] buffer = new byte[1024];
//                int len1 = 0;
//                while ((len1 = is.read(buffer)) != -1) {
//                    fos.write(buffer, 0, len1);
//                }
                fos.flush();
                fos.close();
                is.close();

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(new File(sdcard,"/GSTMadeEasy/update.apk")), "application/vnd.android.package-archive");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); // without this flag android returned a intent error!
                context.startActivity(intent);

                saveInFile();
                editor.putBoolean("sync", true);
                editor.commit();
            } catch (FileNotFoundException fnfe) {
                status = 1;
                Log.e("File", "FileNotFoundException! " + fnfe);
            } catch(Exception e) {
                Log.e("UpdateAPP", "Exception " + e);
            }
            return null;
        }

        public void onPostExecute(Void unused) {
            downloadingprogress.hide();
            if(status == 1) {
                Toast.makeText(context, "Not Available", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void saveInFile() {
        try {
            File sd = Environment.getExternalStorageDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "/data/data/" + getPackageName() + "/databases/" + DatabaseHandler.DATABASE_NAME;
                String backupDBPath = "db_backup.db";
                File currentDB = new File(currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                }
            }
        } catch (Exception e) {
            MyFunctions.errorOccuredCatchInSavingDbBackup(e, "Dashboard Screen");
        }
    }

}
