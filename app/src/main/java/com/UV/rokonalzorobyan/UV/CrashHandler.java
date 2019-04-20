package com.UV.rokonalzorobyan.UV;

import android.content.Context;
import android.content.Intent;
public class CrashHandler implements
        java.lang.Thread.UncaughtExceptionHandler {
    private final Context myContext;
    public CrashHandler(Context context) {
        myContext = context;
    }

    public void uncaughtException(Thread thread, Throwable exception) {
        Intent intent = new Intent(myContext, MainActivity.class);
        myContext.startActivity(intent);
        System.exit(0);
    }
}