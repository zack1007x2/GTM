package com.google.android.tagmanager.examples.gtmsample;

import com.google.android.gms.tagmanager.DataLayer;
import com.google.android.gms.tagmanager.TagManager;
import com.google.android.tagmanager.examples.helloworld.R;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class Second extends Activity{
	private Button btReturn;
	private static final String SCREEN_NAME = "Second Screen";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.second);
		btReturn= (Button)findViewById(R.id.btReturn);
		btReturn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		
	}
	
	@Override
	public void onStart() {
		super.onStart();
		Log.d("Zack","PUSH   SecondScreenOpen");
		DataLayer mDataLayer = TagManager.getInstance(this).getDataLayer();
		mDataLayer.push(DataLayer.mapOf("event", "openScreen", "screenName",
				SCREEN_NAME));
	}

}
