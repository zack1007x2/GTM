package com.google.android.tagmanager.examples.gtmsample;

import java.lang.reflect.Array;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.android.tagmanager.examples.helloworld.R;

/**
 * An {@link Activity} that reads background and text color from a local Json
 * file and applies those colors to text view.
 */
public class MainActivity extends Activity {
	private static final String TAG = "GTMExample";
	private static final String CONTAINER_ID = "GTM-5BQR5J";
	private static final String BACKGROUND_COLOR_KEY = "background-color";
	private static final String TEXT_COLOR_KEY = "text-color";
	private static final String NAME_KEY = "name";
	private static final String MONEY_KEY = "money";
	private TextView tvName, tvMoney;

	// Set to false for release build.
	private static final Boolean DEVELOPER_BUILD = true;
	private ContainerHolder mContainerHolder = null;
	public static Context mContext;
	public DataLayer mDatalayer;
	private static final String SCREEN_NAME = "Main Screen";
	private long pastTime;

	private void setContainerHolder(ContainerHolder containerHolder,
			long pastTime) {
		this.mContainerHolder = containerHolder;
		ContainerHolderSingleton.setContainerHolder(mContainerHolder);
		mContainerHolder
				.setContainerAvailableListener(new ContainerLoadedCallback());
		this.pastTime = pastTime;
		if (mDatalayer != null) {
			Log.d("Zack", "PUSH EVENT Time past = " + pastTime);
			
			mDatalayer.push(DataLayer.mapOf("Var", "reFreshbtn", "Category",
					"AsyncTask", "Time", pastTime, "Label",
					"LoadContainerTime", "btnRefresh", "isClick"));
		}

	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		if (DEVELOPER_BUILD) {
			StrictMode.enableDefaults();
		}
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mDatalayer = TagManager.getInstance(this).getDataLayer();
		 TagManager mTagManager = TagManager.getInstance(this);

		
		
		
		init();
		new DownloadContainerTask(this).execute(CONTAINER_ID);
	}

	private void init() {
		tvName = (TextView) findViewById(R.id.hello_world);
		tvMoney = (TextView) findViewById(R.id.tvMoney);
		tvName.setBackgroundColor(Color.MAGENTA);
		tvName.setTextColor(Color.WHITE);
	}

	private void updateUI() {
		tvName.setBackgroundColor(getColor(BACKGROUND_COLOR_KEY));
		tvName.setTextColor(getColor(TEXT_COLOR_KEY));
		tvName.setText(getName(NAME_KEY));
		if (BlueToothHandler.isBlueToothOn()) {
			tvMoney.setText(getMoney(MONEY_KEY));
		} else {
			tvMoney.setText("This is Tag Test " + Handler123.getKey1()
					+ Handler123.getKey2());
		}

	}

	private String getMoney(String key) {
		String money = "0";
		if (mContainerHolder != null) {
			money = mContainerHolder.getContainer().getString(key);
		}
		return money;
	}

	/**
	 * Returns an integer representing a color.
	 */
	private int getColor(String key) {
		String colorName = "";
		if (mContainerHolder != null) {
			colorName = mContainerHolder.getContainer().getString(key);
		}
		return colorFromColorName(colorName);
	}

	private String getName(String key) {
		String Name = "";
		if (mContainerHolder != null) {
			Name = mContainerHolder.getContainer().getString(key);
		}
		return Name;
	}

	/**
	 * Looks up the externalized string resource and displays it in a pop-up
	 * dialog box.
	 *
	 * @param stringKey
	 */
	private void displayErrorToUser(int stringKey) {
		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Error");
		alertDialog.setMessage(getResources().getString(stringKey));
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.show();
	}

	public void StartSecondActivity(View view) {
		Intent intent = new Intent();
		intent.setClass(this, Second.class);
		startActivity(intent);
	}

	public void colorButtonClicked(@SuppressWarnings("unused") View view) {
		Log.d("Zack", "PUSH  GetInfoBtnClicked");
		DataLayer mDataLayer = TagManager.getInstance(this).getDataLayer();
		mDataLayer.push(DataLayer.mapOf("event", "getinfo", // Event, Name of
															// Open Screen
															// Event.
				"Label", "null", "Value", "GetInfoBtn"));

		AlertDialog alertDialog = new AlertDialog.Builder(this).create();
		alertDialog.setTitle("Getting Info");
		// The container holder might have not been set at this moment. For an
		// example that shows
		// how to use a splash screen to guarantee that the container holder
		// will be initialized,
		// see cuteanimals example.
		if (mContainerHolder != null) {

			alertDialog
					.setMessage(BACKGROUND_COLOR_KEY
							+ " = "
							+ mContainerHolder.getContainer().getString(
									BACKGROUND_COLOR_KEY)
							+ " "
							+ TEXT_COLOR_KEY
							+ " = "
							+ mContainerHolder.getContainer().getString(
									TEXT_COLOR_KEY));
		} else {
			alertDialog
					.setMessage("The container isn't ready. Using default application values");

		}
		alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "OK",
				new DialogInterface.OnClickListener() {
					@Override
					public void onClick(DialogInterface dialog, int which) {
					}
				});
		alertDialog.show();

	}

	public void refreshButtonClicked(@SuppressWarnings("unused") View view) {
		Log.i(TAG, "refreshButtonClicked");

		if (mContainerHolder != null) {

			// mContainerHolder.refresh();
			mDatalayer.push(DataLayer.EVENT_KEY, "custom_tag");
			ContainerHolderSingleton.getContainerHolder().refresh();
			// Map<String, Object> (this).getDataLayer().push(map);
			updateUI();

			String a[] = new String[1];
			System.out.print(a[3]);

		}
	}

	public int colorFromColorName(String colorName) {
		try {
			return Color.parseColor(colorName);
		} catch (Exception e) {
			return Color.BLACK;
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	// This AsyncTask class will set the Container Holder object once this task
	// is completed.
	private class DownloadContainerTask extends
			AsyncTask<String, Void, Boolean> {
		long StartTime = System.currentTimeMillis();
		private static final long TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS = 2000;
		private static final int DEFAULT_CONTAINER_RESOURCE_ID = R.raw.gtm_5bqr5j_json;

		private Activity mActivity;
		private ContainerHolder mContainerHolder;

		public DownloadContainerTask(Activity activity) {
			this.mActivity = activity;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			String containerId = params[0];

			TagManager tagManager = TagManager.getInstance(mActivity);
			tagManager.setVerboseLoggingEnabled(true);

			PendingResult<ContainerHolder> pending = tagManager
					.loadContainerPreferNonDefault(CONTAINER_ID,
							DEFAULT_CONTAINER_RESOURCE_ID);

			mContainerHolder = pending.await(
					TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS,
					TimeUnit.MILLISECONDS);
			if (!mContainerHolder.getStatus().isSuccess()) {
				Log.e("HelloWorld", "failure loading container");
				displayErrorToUser(R.string.load_error);
				return false;
			}
			Log.e("HelloWorld", "success");
			return true;

		}

		@Override
		protected void onPostExecute(Boolean result) {
			if (result) {
				long endTime = System.currentTimeMillis();
				long pastTime = endTime - StartTime;
				setContainerHolder(mContainerHolder, pastTime);
			}
		}
	}

	private static class ContainerLoadedCallback implements
			ContainerHolder.ContainerAvailableListener {
		@Override
		public void onContainerAvailable(ContainerHolder containerHolder,
				String containerVersion) {
			// We load each container when it becomes available.
			Container container = containerHolder.getContainer();
			container.registerFunctionCallMacroCallback("bluetoothstate",
					new BlueToothHandler());

			container.registerFunctionCallTagCallback("custom_tag",
					new Handler123());

			// container.registerFunctionCallTagCallback("custom_tag", new
			// Handlergoogle());
		}

	}

	@Override
	public void onStart() {
		super.onStart();
		Log.d("Zack", "PUSH   ScreenOpen");
		if(mDatalayer!=null)
			mDatalayer.push(DataLayer.mapOf("event", "openScreen", "screenName",
				SCREEN_NAME));
	}

	@Override
	public void onStop() {
		super.onStop();
	}

	public boolean isOnline() {
		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo netInfo = cm.getActiveNetworkInfo();
		if (netInfo != null && netInfo.isConnectedOrConnecting()) {
			return true;
		}
		return false;
	}

}
