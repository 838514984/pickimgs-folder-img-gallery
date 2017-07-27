package com.example.administrator.imagepicker.application;

import android.app.Activity;
import android.app.Application;

import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class MyApplication extends Application {
    public static MyApplication INSTANCE;
    public ArrayList<Activity> activities=new ArrayList<>();
    @Override
    public void onCreate() {
        super.onCreate();
        INSTANCE=this;
    }

    public void addActivity(Activity activity){
        activities.add(activity);
    }

    public void finishRangActivity(Class startClass,Class finishClass){
        boolean isFinish=false;
        for (Activity activity:activities){
            if (activity.getClass()==startClass){
                isFinish=true;
            }
            if (isFinish){
                activity.finish();
            }
            if (finishClass.getClass()==finishClass)
                return;
        }
    }
}
