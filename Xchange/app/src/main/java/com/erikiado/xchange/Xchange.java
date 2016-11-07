package com.erikiado.xchange;

import android.app.Application;

import com.facebook.accountkit.AccountKit;

/**
 * Created by erikiado on 9/21/16.
 */

public class Xchange extends Application{
    @Override
    public void onCreate(){
        super.onCreate();
        AccountKit.initialize(getApplicationContext());
    }
}
