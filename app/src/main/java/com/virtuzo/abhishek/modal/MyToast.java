package com.virtuzo.abhishek.modal;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by Abhishek Aggarwal on 12/14/2017.
 */

public class MyToast {

    public static Toast toast;

    public static void showToast (String message, Context context){ //"Toast toast" is declared in the class
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.show();
    }
}
