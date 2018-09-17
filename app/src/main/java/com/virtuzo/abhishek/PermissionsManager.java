package com.virtuzo.abhishek;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;

/**
 * Created by Aman Bansal on 09-01-2018.
 */

public abstract class PermissionsManager {


    private static final String PACKAGE_URL_SCHEME = "package:";
    private Activity mTargetActivity;

    public abstract void authorized(int var1);

    public abstract void noAuthorization(int var1, String[] var2);

    public abstract void ignore();

    public PermissionsManager(Activity targetActivity) {
        this.mTargetActivity = targetActivity;
    }

    public void checkPermissions(int requestCode, String... permissions) {
        if(Build.VERSION.SDK_INT >= 23) {
            ArrayList<String> lacks = new ArrayList();
            String[] lacksPermissions = permissions;
            int var5 = permissions.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                String permission = lacksPermissions[var6];
                if(ContextCompat.checkSelfPermission(this.mTargetActivity.getApplicationContext(), permission) == -1) {
                    lacks.add(permission);
                }
            }

            if(!lacks.isEmpty()) {
                lacksPermissions = new String[lacks.size()];
                lacksPermissions = (String[])lacks.toArray(lacksPermissions);
                ActivityCompat.requestPermissions(this.mTargetActivity, lacksPermissions, requestCode);
            } else {
                this.authorized(requestCode);
            }
        } else {
            this.ignore();
        }

    }

    public void recheckPermissions(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        int[] var4 = grantResults;
        int var5 = grantResults.length;

        for(int var6 = 0; var6 < var5; ++var6) {
            int grantResult = var4[var6];
            if(grantResult == -1) {
                this.noAuthorization(requestCode, permissions);
                return;
            }
        }

        this.authorized(requestCode);
    }

    public static void startAppSettings(Context context) {
        Intent intent = new Intent("android.settings.APPLICATION_DETAILS_SETTINGS");
        intent.setFlags(268435456);
        intent.setData(Uri.parse("package:" + context.getPackageName()));
        context.startActivity(intent);
    }
}
