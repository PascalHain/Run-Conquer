package com.pascalhain.runconquer;

import android.app.Application;
import android.content.SharedPreferences;

import androidx.preference.PreferenceManager;

import org.osmdroid.config.Configuration;

import java.io.File;

import com.pascalhain.runconquer.data.repository.RunConquerRepositoryImpl;

public class RunConquerApp extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Configuration.getInstance().load(this, prefs);
        Configuration.getInstance().setUserAgentValue(getPackageName());
        File basePath = new File(getCacheDir(), "osmdroid");
        File tileCache = new File(basePath, "tiles");
        Configuration.getInstance().setOsmdroidBasePath(basePath);
        Configuration.getInstance().setOsmdroidTileCache(tileCache);
        RunConquerRepositoryImpl.initialize(this);
    }
}
