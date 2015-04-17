package com.google.android.tagmanager.examples.helloworld;

import java.util.Map;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.tagmanager.Container.FunctionCallMacroCallback;

public class BlueToothHandler implements FunctionCallMacroCallback {
	private static boolean isBlueToothOn;
	

	@Override
	public Object getValue(String arg0, Map<String, Object> arg1) {
		BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
		
		Log.d("Zack",mBluetoothAdapter.isEnabled()+"");
		isBlueToothOn = mBluetoothAdapter.isEnabled();
		return mBluetoothAdapter.isEnabled();
	}
	
	public static boolean isBlueToothOn(){
		return isBlueToothOn;
	}

}
