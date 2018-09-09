package com.brkn.floater;

import android.app.*;
import android.content.*;
import android.os.*;
import android.util.*;
import android.widget.*;

public class MediaPlayer extends Activity
{

	public static final String SERVICECMD = "com.android.music.musicservicecommand";
	public static final String CMDNAME = "command";
	public static final String CMDTOGGLEPAUSE = "togglepause";
	public static final String CMDSTOP = "stop";
	public static final String CMDPAUSE = "pause";
	public static final String CMDPLAY = "play";
	public static final String CMDPREVIOUS = "previous";
	public static final String CMDNEXT = "next";
	/** Called when the activity is first created. */

	private TextView stateTextView;
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.media_player);
		IntentFilter iF = new IntentFilter();
		iF.addAction("com.android.music.metachanged");
		iF.addAction("com.android.music.playstatechanged");
		iF.addAction("com.android.music.playbackcomplete");
		iF.addAction("com.android.music.queuechanged");

		//registerReceiver(mReceiver, iF);

		stateTextView = (TextView)findViewById(R.id.stateTv);
		stateTextView.setText("hdhd");

		Toast.makeText(this, "booo", Toast.LENGTH_LONG).show();


		IntentFilter musicPauseFilter = new IntentFilter(
			"android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION");
		IntentFilter musicPlayFilter = new IntentFilter(
			"android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION");

		registerReceiver(musicPlay, musicPlayFilter);
		registerReceiver(musicPause, musicPauseFilter);
	}

	private BroadcastReceiver mReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent)
		{
			String action = intent.getAction();
			String cmd = intent.getStringExtra("command");
			Log.d("mIntentReceiver.onReceive ", action + " / " + cmd);

			String artist = intent.getStringExtra("artist");
			String album = intent.getStringExtra("album");
			String track = intent.getStringExtra("track");
			Log.d("Music", artist + ":" + album + ":" + track);

			stateTextView.setText(action + " / " + cmd + " / " + artist + ":" + album + ":" + track);
		}
	};

	// Broadcast Receiver for Music play.
    private BroadcastReceiver musicPlay = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.v("gaurav", "Music play started");
			stateTextView.setText("Music play started");
		}
    };

    // Broadcast Receiver for Music pause.
    private BroadcastReceiver musicPause = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // TODO Auto-generated method stub
            Log.v("gaurav", "Music paused");
			stateTextView.setText("Music paused");
		}
    };
}
