package com.virtuzo.abhishek.Views.Activity.Others;

import android.Manifest;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import android.content.DialogInterface;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.support.v7.app.AlertDialog;

//import com.kongqw.permissionslibrary.PermissionsManager;
//import com.kongqw.wifilibrary.WiFiManager;
//import com.kongqw.wifilibrary.listener.OnWifiConnectListener;
//import com.kongqw.wifilibrary.listener.OnWifiEnabledListener;
//import com.kongqw.wifilibrary.listener.OnWifiScanResultsListener;
import com.virtuzo.abhishek.PermissionsManager;
import com.virtuzo.abhishek.R;
import com.virtuzo.abhishek.Views.Activity.ListActivity.ErrorLogsListActivity;
import com.virtuzo.abhishek.WifiLibrary.listener.OnWifiConnectListener;
import com.virtuzo.abhishek.WifiLibrary.listener.OnWifiEnabledListener;
import com.virtuzo.abhishek.WifiLibrary.listener.OnWifiScanResultsListener;
import com.virtuzo.abhishek.WifiLibrary.WiFiManager;
import com.virtuzo.abhishek.Views.Adapters.WifiListAdapter;
import com.virtuzo.abhishek.modal.MyFunctions;

//import kong.qingwei.kqwwifimanagerdemo.adapter.WifiListAdapter;
//import kong.qingwei.kqwwifimanagerdemo.view.ConnectWifiDialog;

public class WifiActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener, OnWifiScanResultsListener, OnWifiConnectListener, OnWifiEnabledListener {

    private static final String TAG = "WifiActivity";

    private ListView mWifiList;
    private SwipeRefreshLayout mSwipeLayout;
    private PermissionsManager mPermissionsManager;


    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };
    private final int GET_WIFI_LIST_REQUEST_CODE = 0;
    private WiFiManager mWiFiManager;
    private WifiListAdapter mWifiListAdapter;
    private SwitchCompat switchCompat;
    private FrameLayout frameLayout;
    boolean isKeyboardShown = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi);

//        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        getSupportActionBar().hide();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setPadding(0,0,0,0);//for tab otherwise give space in tab
        toolbar.setContentInsetsAbsolute(0,0);
        TextView heading = (TextView) toolbar.findViewById(R.id.actionBarTitle);
        heading.setText("Wifi Settings");

        initView();



        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    mWiFiManager.startScan();
                    mWiFiManager.openWiFi();
                } else {
                    mSwipeLayout.setRefreshing(false);
                    mWiFiManager.closeWiFi();
                }
            }
        });

        mSwipeLayout.setOnRefreshListener(this);
        mWifiList.setEmptyView(findViewById(R.id.empty_view));
        mWifiListAdapter = new WifiListAdapter(getApplicationContext());
        mWifiList.setAdapter(mWifiListAdapter);
        mWifiList.setOnItemClickListener(this);
        mWifiList.setOnItemLongClickListener(this);

        mWiFiManager = WiFiManager.getInstance(getApplicationContext());

        mPermissionsManager = new PermissionsManager(this) {
            @Override
            public void authorized(int requestCode) {

                if (GET_WIFI_LIST_REQUEST_CODE == requestCode) {

                    List<ScanResult> scanResults = mWiFiManager.getScanResults();
                    refreshData(scanResults);
                }
            }

            @Override
            public void noAuthorization(int requestCode, String[] lacksPermissions) {
            }

            @Override
            public void ignore() {

                List<ScanResult> scanResults = mWiFiManager.getScanResults();
                refreshData(scanResults);
            }
        };

        mPermissionsManager.checkPermissions(GET_WIFI_LIST_REQUEST_CODE, PERMISSIONS);

        if(!mWiFiManager.isWifiEnabled()){
            mSwipeLayout.setRefreshing(false);
        }
    }


    private void initView() {
        // WIFI switch
        switchCompat = (SwitchCompat) findViewById(R.id.switch_wifi);

        frameLayout = (FrameLayout) findViewById(R.id.fl_wifi);
        mSwipeLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);

        mWifiList = (ListView) findViewById(R.id.wifi_list);
    }

    @Override
    protected void onResume() {
        super.onResume();

        mWiFiManager.setOnWifiEnabledListener(this);
        mWiFiManager.setOnWifiScanResultsListener(this);
        mWiFiManager.setOnWifiConnectListener(this);

        switchCompat.setChecked(mWiFiManager.isWifiEnabled());
    }

    @Override
    protected void onPause() {
        super.onPause();

        mWiFiManager.removeOnWifiEnabledListener();
        mWiFiManager.removeOnWifiScanResultsListener();
        mWiFiManager.removeOnWifiConnectListener();
    }

    public void refreshData(List<ScanResult> scanResults) {

        if(mWiFiManager.isWifiEnabled()) {
            mSwipeLayout.setRefreshing(false);
            // 刷新界面
            mWifiListAdapter.refreshData(scanResults);

            Snackbar.make(mWifiList, "WIFI list refresh success", Snackbar.LENGTH_SHORT).show();
        }else{
            mSwipeLayout.setRefreshing(false);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        mPermissionsManager.recheckPermissions(requestCode, permissions, grantResults);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        final ScanResult scanResult = (ScanResult) mWifiListAdapter.getItem(position);

        WiFiManager wiFiManager= WiFiManager.getInstance(getApplicationContext());
        if(scanResult.BSSID.equals(wiFiManager.getConnectionInfo().getBSSID())){


            final String ssid = scanResult.SSID;
            new AlertDialog.Builder(this)
                    .setTitle(ssid)
                    .setItems(new String[]{"Disconnect", "Delete"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
                                    Log.i(TAG, "onClick: connectionInfo :" + connectionInfo.getSSID());
                                    if (mWiFiManager.addDoubleQuotation(ssid).equals(connectionInfo.getSSID())) {
                                        mWiFiManager.disconnectWifi(connectionInfo.getNetworkId());
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Currently not connected [ " + ssid + " ]", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                case 1:
                                    WifiConfiguration wifiConfiguration = mWiFiManager.getConfigFromConfiguredNetworksBySsid(ssid);
                                    if (null != wifiConfiguration) {
                                        boolean isDelete = mWiFiManager.deleteConfig(wifiConfiguration.networkId);
                                        Toast.makeText(getApplicationContext(), isDelete ? "successfully deleted！" : "Other applications configured network without ROOT permissions can not be deleted！", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "Did not save the network！", Toast.LENGTH_SHORT).show();
                                    }
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
                    .show();

        }
        else {

            final EditText et_password = new EditText(this);
            et_password.setHint("Enter Password");
            //final String[] password = new String[1];

            AlertDialog passworDialog = new AlertDialog.Builder(this)
                    .setTitle(scanResult.SSID)

                    .setView(et_password)
                    .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {




                            String password = et_password.getText().toString();


                            switch (mWiFiManager.getSecurityMode(scanResult)) {
                                case WPA:
                                case WPA2:

                                    mWiFiManager.connectWPA2Network(scanResult.SSID, password);
//                new ConnectWifiDialog(this) {
//
//                    @Override
//                    public void connect(String password) {
//                        mWiFiManager.connectWPA2Network(scanResult.SSID, password);
//                    }
//                }.setSsid(scanResult.SSID).show();
                                    break;
                                case WEP:

                                    mWiFiManager.connectWEPNetwork(scanResult.SSID, password);
//                new ConnectWifiDialog(this) {
//
//                    @Override
//                    public void connect(String password) {
//                        mWiFiManager.connectWEPNetwork(scanResult.SSID, password);
//                    }
//                }.setSsid(scanResult.SSID).show();
                                    break;
                                case OPEN:
                                    mWiFiManager.connectOpenNetwork(scanResult.SSID);
                                    break;
                            }


                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {

                        }
                    })
                    .show();

        }

    }

    public void backButtonPressed(View view) {
        super.onBackPressed();
    }

    public void logsButton(View view) {
//        Intent intentShowErrorLogs = new Intent(getApplicationContext(), ErrorLogsListActivity.class);
//        startActivity(intentShowErrorLogs);
        startActivity(new Intent(getApplicationContext(), SettingsActivity.class));
    }

    public void switchKeyboardButton(View view) {
        MyFunctions.toggleKeyboard(this);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        ScanResult scanResult = (ScanResult) mWifiListAdapter.getItem(position);
        final String ssid = scanResult.SSID;
        new AlertDialog.Builder(this)
                .setTitle(ssid)
                .setItems(new String[]{"Disconnect", "Delete"}, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                WifiInfo connectionInfo = mWiFiManager.getConnectionInfo();
                                Log.i(TAG, "onClick: connectionInfo :" + connectionInfo.getSSID());
                                if (mWiFiManager.addDoubleQuotation(ssid).equals(connectionInfo.getSSID())) {
                                    mWiFiManager.disconnectWifi(connectionInfo.getNetworkId());
                                } else {
                                    Toast.makeText(getApplicationContext(), "Currently not connected [ " + ssid + " ]", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            case 1:
                                WifiConfiguration wifiConfiguration = mWiFiManager.getConfigFromConfiguredNetworksBySsid(ssid);
                                if (null != wifiConfiguration) {
                                    boolean isDelete = mWiFiManager.deleteConfig(wifiConfiguration.networkId);
                                    Toast.makeText(getApplicationContext(), isDelete ? "successfully deleted！" : "Other applications configured network without ROOT permissions can not be deleted！", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getApplicationContext(), "Did not save the network！", Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                break;
                        }
                    }
                })
                .show();
        return true;
    }



    @Override
    public void onRefresh() {

        mWiFiManager.startScan();
    }

    @Override
    public void onScanResults(List<ScanResult> scanResults) {
        refreshData(scanResults);
    }

    @Override
    public void onWiFiConnectLog(String log) {
        Log.i(TAG, "onWiFiConnectLog: " + log);
        Snackbar.make(mWifiList, "WIFI connecting : " + log, Snackbar.LENGTH_SHORT).show();
    }


    @Override
    public void onWiFiConnectSuccess(String SSID) {
        Log.i(TAG, "onWiFiConnectSuccess:  [ " + SSID + " ] connection succeeded");
        Toast.makeText(getApplicationContext(), SSID + "  connection succeeded", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWiFiConnectFailure(String SSID) {
        Log.i(TAG, "onWiFiConnectFailure:  [ " + SSID + " ] Connection failed");
        Toast.makeText(getApplicationContext(), SSID + "  Connection failed! Please reconnect！", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onWifiEnabled(boolean enabled) {
        switchCompat.setChecked(enabled);
        frameLayout.setVisibility(enabled ? View.VISIBLE : View.GONE);
    }


}
