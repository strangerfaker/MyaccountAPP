package com.example.myaccountapp;

import android.app.Application;

import com.example.myaccountapp.db.DBManager;

/* 表示全局应用的类*/
//在此处调用初始化数据库方法
public class UniteApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        // 初始化数据库
        DBManager.initDB(getApplicationContext());
    }
}

