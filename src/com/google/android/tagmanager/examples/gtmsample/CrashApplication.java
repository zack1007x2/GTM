package com.google.android.tagmanager.examples.gtmsample;

import java.util.ArrayList;
import java.util.EventObject;
import java.util.concurrent.TimeUnit;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.android.tagmanager.examples.gtmsample.MainActivity.MyListener;

import android.app.Activity;
import android.app.Application;
import android.app.Application.ActivityLifecycleCallbacks;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class CrashApplication extends Application implements MyListener{

	private CrashHandler crashHandler;
	private static final String CONTAINER_ID = "GTM-5BQR5J";
	private TagManager tagManager;
	private DataLayer mDataLayer;

	@Override
	public void onCreate() {
		super.onCreate();

		crashHandler = CrashHandler.getInstance();
		crashHandler.init(this);

		this.registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {

			@Override
			public void onActivityCreated(Activity activity,
					Bundle savedInstanceState) {

			}

			@Override
			public void onActivityStarted(Activity activity) {
			}

			@Override
			public void onActivityResumed(Activity activity) {
//				crashHandler.setTagManager(TagManager.getInstance(activity));
//				crashHandler.setcurActivity(activity);
			}

			@Override
			public void onActivityPaused(Activity activity) {
			}

			@Override
			public void onActivityStopped(Activity activity) {
			}

			@Override
			public void onActivitySaveInstanceState(Activity activity,
					Bundle outState) {
			}

			@Override
			public void onActivityDestroyed(Activity activity) {

			}
		});
	}

	@Override
	public void callback(DataLayer mDatalayer) {
		this.mDataLayer = mDatalayer;
		crashHandler.setContainerHolder(mDataLayer);
	}
	
	
}
