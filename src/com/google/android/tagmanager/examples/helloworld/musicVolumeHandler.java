package com.google.android.tagmanager.examples.helloworld;

import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.util.Log;

import com.google.android.gms.tagmanager.Container.FunctionCallMacroCallback;

public class musicVolumeHandler implements FunctionCallMacroCallback {

	private AudioManager audioManager;

	public musicVolumeHandler(Context context) {
		audioManager = (AudioManager) context
				.getSystemService(Context.AUDIO_SERVICE);
	}

	@Override
	public Object getValue(String arg0, Map<String, Object> arg1) {
		
		Log.d("Zack", "VOLUME = "+audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
		return audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);

	}

}
