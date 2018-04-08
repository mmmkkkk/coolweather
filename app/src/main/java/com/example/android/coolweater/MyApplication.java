package com.example.android.coolweater;

import android.app.Application;

import com.raizlabs.android.dbflow.config.FlowManager;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
//        FlowManager.init(FlowConfig.builder()
//                .addDatabaseConfig(DatabaseConfig.builder(AppDatabase.class)
//                        .databaseName("AppDatabase")  //自定义数据库名
//                        .build())
//                .build())
        FlowManager.init(this);
    }
}
