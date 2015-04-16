package com.google.android.tagmanager.examples.helloworld;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.Container.FunctionCallMacroCallback;
import com.google.android.gms.tagmanager.Container.FunctionCallTagCallback;
import com.google.android.gms.tagmanager.ContainerHolder.ContainerAvailableListener;
import com.google.android.gms.tagmanager.TagManager;

/**
 * An {@link Activity} that reads background and text color from a local
 * Json file and applies those colors to text view.
 */
public class MainActivity extends Activity {
    private static final String TAG = "GTMExample";
    private static final String CONTAINER_ID = "GTM-5BQR5J";
    private static final String BACKGROUND_COLOR_KEY = "background-color";
    private static final String TEXT_COLOR_KEY = "text-color";
    private static final String NAME_KEY = "name";
    private static final String MONEY_KEY = "money";
    private static final long TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS = 2000;
    

    // Set to false for release build.
    private static final Boolean DEVELOPER_BUILD = true;
    private ContainerHolder mContainerHolder = null;

    private void setContainerHolder(ContainerHolder containerHolder) {
      this.mContainerHolder = containerHolder;
      ContainerLoadedCallback.registerCallbacksForContainer(mContainerHolder.getContainer());
      this.mContainerHolder.setContainerAvailableListener(new ContainerHolder.ContainerAvailableListener() {

		@Override
		public void onContainerAvailable(ContainerHolder containerHolder,
				String containerVersion) {
			mContainerHolder = containerHolder;
			
		}
      });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (DEVELOPER_BUILD) {
            StrictMode.enableDefaults();
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
//        new DownloadContainerTask(this).execute(CONTAINER_ID);
        
        TagManager tagManager = TagManager.getInstance(this);

        // Modify the log level of the logger to print out not only
        // warning and error messages, but also verbose, debug, info messages.
        tagManager.setVerboseLoggingEnabled(true);

        PendingResult<ContainerHolder> pending =
                tagManager.loadContainerPreferNonDefault(CONTAINER_ID,
                R.raw.gtm_5bqr5j_json);
        

        // The onResult method will be called as soon as one of the following happens:
        //     1. a saved container is loaded
        //     2. if there is no saved container, a network container is loaded
        //     3. the 2-second timeout occurs
        pending.setResultCallback(new ResultCallback<ContainerHolder>() {
            @Override
            public void onResult(ContainerHolder containerHolder) {
                ContainerHolderSingleton.setContainerHolder(containerHolder);
                Container container = containerHolder.getContainer();
                mContainerHolder = containerHolder;
                if (!containerHolder.getStatus().isSuccess()) {
                    Log.e("CuteAnimals", "failure loading container");
                    displayErrorToUser(R.string.load_error);
                    return;
                }
                ContainerHolderSingleton.setContainerHolder(containerHolder);
                ContainerLoadedCallback.registerCallbacksForContainer(container);
                containerHolder.setContainerAvailableListener(new ContainerLoadedCallback());
            }
        }, TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS, TimeUnit.MILLISECONDS);
        
        
    }

    private void updateUI() {
        TextView textView = (TextView) findViewById(R.id.hello_world);
        TextView tvMoney = (TextView) findViewById(R.id.tvMoney);
        textView.setBackgroundColor(getColor(BACKGROUND_COLOR_KEY));
        textView.setTextColor(getColor(TEXT_COLOR_KEY));
        textView.setText(getName(NAME_KEY));
        tvMoney.setText(getMoney(MONEY_KEY));
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
     * Looks up the externalized string resource and displays it in a pop-up dialog box.
     *
     * @param stringKey
     */
    private void displayErrorToUser(int stringKey) {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Error");
        alertDialog.setMessage(getResources().getString(stringKey));
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
        });
        alertDialog.show();
    }

    public void colorButtonClicked(@SuppressWarnings("unused") View view) {
        Log.i(TAG, "colorButtonClicked");
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setTitle("Getting Info");
        // The container holder might have not been set at this moment. For an example that shows
        // how to use a splash screen to guarantee that the container holder will be initialized,
        // see cuteanimals example.
        if (mContainerHolder != null) {
        	
          alertDialog.setMessage(BACKGROUND_COLOR_KEY + " = "
                  + mContainerHolder.getContainer().getString(BACKGROUND_COLOR_KEY)
                  + " " + TEXT_COLOR_KEY + " = "
                  + mContainerHolder.getContainer().getString(TEXT_COLOR_KEY));
        } else {
          alertDialog.setMessage("The container isn't ready. Using default application values");

        }
        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE,
                "OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
        });
        alertDialog.show();
        
        
    }

    public void refreshButtonClicked(@SuppressWarnings("unused") View view) {
        Log.i(TAG, "refreshButtonClicked");
        
        if (mContainerHolder != null) {
        	mContainerHolder.refresh();
        	updateUI();
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

    // This AsyncTask class will set the Container Holder object once this task is completed.
    private class DownloadContainerTask extends AsyncTask<String, Void, Boolean> {
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
            PendingResult<ContainerHolder> pending = tagManager.loadContainerPreferNonDefault(
            		CONTAINER_ID, DEFAULT_CONTAINER_RESOURCE_ID);

            mContainerHolder = pending.await(TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS,
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
            setContainerHolder(mContainerHolder);
            updateUI();
        }
    }
    
    
    private static class ContainerLoadedCallback implements ContainerHolder.ContainerAvailableListener {
        @Override
        public void onContainerAvailable(ContainerHolder containerHolder, String containerVersion) {
            // We load each container when it becomes available.
            Container container = containerHolder.getContainer();
            registerCallbacksForContainer(container);
        }

        public static void registerCallbacksForContainer(Container container) {
            // Register two custom function call macros to the container.
            container.registerFunctionCallMacroCallback("increment", new CustomMacroCallback());
            container.registerFunctionCallMacroCallback("mod", new CustomMacroCallback());
            // Register a custom function call tag to the container.
            container.registerFunctionCallTagCallback("custom_tag", new CustomTagCallback());
            
        }
    }
    
    
    private static class CustomTagCallback implements FunctionCallTagCallback {
        @Override
        public void execute(String tagName, Map<String, Object> parameters) {
            // The code for firing this custom tag.
            Log.i("CuteAnimals", "Custom function call tag :" + tagName + " is fired.");
        }
    }
    
    
    private static class CustomMacroCallback implements FunctionCallMacroCallback {
        private int numCalls;

        @Override
        public Object getValue(String name, Map<String, Object> parameters) {
            if ("increment".equals(name)) {
                return ++numCalls;
            } else if ("mod".equals(name)) {
                return (Long) parameters.get("key1") % Integer.valueOf((String) parameters.get("key2"));
               
            } else {
                throw new IllegalArgumentException("Custom macro name: " + name + " is not supported.");
            }
        }
    }
}
