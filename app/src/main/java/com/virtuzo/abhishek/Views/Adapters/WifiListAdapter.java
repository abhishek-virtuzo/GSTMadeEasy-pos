package com.virtuzo.abhishek.Views.Adapters;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

//import com.kongqw.wifilibrary.WiFiManager;
import com.virtuzo.abhishek.WifiLibrary.WiFiManager;

import com.virtuzo.abhishek.R;
import java.util.ArrayList;
import java.util.List;

//import kong.qingwei.kqwwifimanagerdemo.R;

/**
 * Created by kqw on 2016/8/2.
 * Wifi列表的数据适配器
 */
public class WifiListAdapter extends BaseAdapter {

    private static final String TAG = "WifiListAdapter";
    private List<ScanResult> scanResults;
    private Context mContext;

    public WifiListAdapter(Context context) {
        mContext = context.getApplicationContext();
        this.scanResults = new ArrayList<>();
    }

    public void refreshData(List<ScanResult> scanResults) {
        if (null != scanResults) {
            Log.i(TAG, "refreshData 1 : " + scanResults.size());

            scanResults = WiFiManager.excludeRepetition(scanResults);
            Log.i(TAG, "refreshData 2 : " + scanResults.size());

            this.scanResults.clear();

            this.scanResults.addAll(scanResults);
        }

        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return scanResults.size();
    }

    @Override
    public Object getItem(int position) {
        return scanResults.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);
            convertView = layoutInflater.inflate(R.layout.item_wifi, null);
            holder = new ViewHolder();
            holder.ssid = (TextView) (convertView).findViewById(R.id.ssid);
            holder.status=(TextView)(convertView).findViewById(R.id.status);
            convertView.setTag(holder);

        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        ScanResult scanResult = scanResults.get(position);
        String connected="";

        WiFiManager wiFiManager= WiFiManager.getInstance(mContext);
        if(scanResult.BSSID.equals(wiFiManager.getConnectionInfo().getBSSID())){
            connected="Connected";
        }


        holder.ssid.setText("Wifi Name：" + scanResult.SSID +"\n Signal Strength：" + WifiManager.calculateSignalLevel(scanResult.level, 5) + "/5\nEncryption：" + scanResult.capabilities);
        holder.status.setText(connected);
        return convertView;
    }

    private class ViewHolder {
        private TextView ssid, status;
    }
}
