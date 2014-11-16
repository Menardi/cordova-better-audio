/*
 * Author: Gearoid Moroney (gearoid@syncostyle.com)
 */

package com.syncostyle.cordova.betteraudio;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaResourceApi;
import org.json.JSONArray;
import org.json.JSONException;

import com.syncostyle.c25k.R;

import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.content.res.Resources;
import android.util.Log;
import android.net.Uri;

public class BetterAudio extends CordovaPlugin {

	/**
	 * 	Executes the request and returns PluginResult
     *
     * 	@param action		Action to execute
     * 	@param data			JSONArray of arguments to the plugin
     *  @param callbackContext	The callback context used when calling back into JavaScript.
     *
     *  @return				A PluginRequest object with a status
     * */
    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext) {
		context = this.cordova.getActivity().getBaseContext();

    	if(action.equals("play")) {
    		String type;
			try {
				CordovaResourceApi resourceApi = webView.getResourceApi();
				type = data.getString(0);
				Log.d("BetterAudio", "Type is " + type);

	    		Uri audioUri = resourceApi.remapUri(Uri.parse("file:///android_asset/www/audio/" + type + ".ogg"));
	    		Log.d("BetterAudio", "Uri is " + audioUri.toString());

				// Attempt to get audio focus
				AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
				am.requestAudioFocus(null, AudioManager.STREAM_NOTIFICATION, AudioManager.AUDIOFOCUS_GAIN_TRANSIENT_MAY_DUCK);

				// Play the notification anyway, regardless of focus
				MediaPlayer mp = MediaPlayer.create(context, audioUri);
				mp.start();

				// Set the completion listener
				mp.setOnCompletionListener(new OnCompletionListener() {
					public void onCompletion(MediaPlayer m) {
						AudioManager a = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
						a.abandonAudioFocus(null);
						m.release();
					}
				});
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return false;
			}
		}
		return true;
	}

	private Context context;
}
