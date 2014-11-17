package com.syncostyle.cordova.betteraudio;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import java.util.concurrent.ExecutorService;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;

public class BetterAudio extends CordovaPlugin {
	private Context context;

	public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) throws JSONException {
		this.context = this.cordova.getActivity().getBaseContext();

		if (action.equals("play")) {
			final String uri = data.getString(0);

			this.cordova.getThreadPool().execute(new Runnable() {
				public void run() {
					Log.v("BetterAudio", "Uri is " + uri);

					MediaPlayer mp = new MediaPlayer();

					mp.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
						public void onPrepared(MediaPlayer m) {
							AudioManager am = (AudioManager)BetterAudio.this.context.getSystemService("audio");
							am.requestAudioFocus(null, 5, 3);
							m.start();
						}
					});

					mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
						public void onCompletion(MediaPlayer m) {
							AudioManager a = (AudioManager)BetterAudio.this.context.getSystemService("audio");
							a.abandonAudioFocus(null);
							m.release();
						}
					});

					mp.setOnErrorListener(new MediaPlayer.OnErrorListener() {
						public boolean onError(MediaPlayer m, int errorCode, int more) {
							m.stop();
							AudioManager a = (AudioManager)BetterAudio.this.context.getSystemService("audio");
							a.abandonAudioFocus(null);
							Log.v("BetterAudio", "Uh oh: " + errorCode + " / " + more);
							return false;
						}
					});

					AssetFileDescriptor fd = null;
					try {
						fd = BetterAudio.this.cordova.getActivity().getAssets().openFd(uri);
						mp.setDataSource(fd.getFileDescriptor(), fd.getStartOffset(), fd.getLength());
						mp.prepare();
					} catch (Exception e) {
						callbackContext.error(e.getLocalizedMessage());
					}
				}
			});
		}
		return true;
	}
}