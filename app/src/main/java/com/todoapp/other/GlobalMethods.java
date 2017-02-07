package com.todoapp.other;


import android.content.Context;

import android.widget.Toast;

public class GlobalMethods {

    public static void showMessage(Context context, String TEXT) {
        Toast.makeText(context, TEXT, Toast.LENGTH_SHORT).show();
    }





}
