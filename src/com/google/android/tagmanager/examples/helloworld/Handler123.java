package com.google.android.tagmanager.examples.helloworld;

import java.util.Map;

import android.util.Log;

import com.google.android.gms.tagmanager.Container.FunctionCallTagCallback;

public class Handler123 implements FunctionCallTagCallback {
	private static String key1;
	private static String key2;

	@Override
	public void execute(String arg0, Map<String, Object> arg1) {
		Log.d("Zack", arg0);
		Log.d("Zack",arg1.get("key1").toString());
		Log.d("Zack",arg1.get("key2").toString());
		key1 = arg1.get("key1").toString();
		key2 = arg1.get("key2").toString();
	}
	
	public static String getKey1(){
		return key1;
	}
	
	public static String getKey2(){
		return key2;
	}
}
