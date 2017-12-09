package com.fatec.labs.fatec_firebase.Common;

import android.app.Application;
import android.content.Context;


public class App extends Application {

    private static Context mContext;

    public App(Context mContext) {
        if(this.mContext == null){
            this.mContext = mContext;
        }
    }

    public static Context getContext() {
        return mContext;
    }
}
