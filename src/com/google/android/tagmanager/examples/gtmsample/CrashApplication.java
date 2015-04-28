package com.google.android.tagmanager.examples.gtmsample;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;

public class CrashApplication extends Application {

	private DataLayer CrashDataLayer;
	private Context mContext;
	

	@Override
	public void onCreate() {
		super.onCreate();

		mContext = this;
		

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
				CrashDataLayer = TagManager.getInstance(activity)
						.getDataLayer();
				CrashHandler crashHandler = CrashHandler.getInstance();
				crashHandler.init(mContext, CrashDataLayer);
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

}
