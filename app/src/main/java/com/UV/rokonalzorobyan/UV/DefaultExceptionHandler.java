package com.UV.rokonalzorobyan.UV;

import android.app.Activity;
import android.widget.Toast;
public class DefaultExceptionHandler implements Thread.UncaughtExceptionHandler {
    Activity activity;
    public DefaultExceptionHandler(Activity activity) {
        this.activity = activity;
    }
    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        }
}