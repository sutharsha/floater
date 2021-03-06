package com.brkn.floater;

import android.app.*;
import android.content.*;
import android.graphics.*;
import android.media.*;
import android.os.*;
import android.util.*;
import android.view.*;
import android.widget.*;
import android.provider.*;

public class FloatingViewService extends Service {

    final String MIMETYPE_TEXT_PLAIN = "text/plain";
    private WindowManager mWindowManager;
    private View mFloatingView;
	private TextView stateTextView;

    public FloatingViewService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //Inflate the floating view layout we created
        mFloatingView = LayoutInflater.from(this).inflate(R.layout.layout_floating_widget, null);

        //Add the view to the window.
        //        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
        //                WindowManager.LayoutParams.WRAP_CONTENT,
        //                WindowManager.LayoutParams.WRAP_CONTENT,
        //                WindowManager.LayoutParams.TYPE_PHONE,
        //                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
        //                PixelFormat.TRANSLUCENT);

        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_PHONE;
        }

        final WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);

        //Specify the view position
        params.gravity = Gravity.TOP | Gravity.START;        //Initially view will be added to top-left corner
        params.x = 0;
        params.y = 100;

        //Add the view to the window
        mWindowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        mWindowManager.addView(mFloatingView, params);

        //The root element of the collapsed view layout
        final View collapsedView = mFloatingView.findViewById(R.id.collapse_view);
        //The root element of the expanded view layout
        final View expandedView = mFloatingView.findViewById(R.id.expanded_container);

        //Set the close button
        ImageView closeButtonCollapsed = (ImageView) mFloatingView.findViewById(R.id.close_btn);
        closeButtonCollapsed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //close the service and remove the from from the window
                stopSelf();
            }
        });

        //Set the view while floating view is expanded.
        //Set the play button.
        ImageView playButton = (ImageView) mFloatingView.findViewById(R.id.play_btn);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  Toast.makeText(FloatingViewService.this, "Playing the song.", Toast.LENGTH_LONG).show();
				String command = "navigate home by public transport";
				Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
				intent.setClassName("com.google.android.googlequicksearchbox", "com.google.android.googlequicksearchbox.SearchActivity");
				intent.putExtra("query", command);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK); //necessary if launching from Service
				startActivity(intent);	
				
				*/
				/*
				String title = "Bring me to life";

				//Intent intent = new Intent(MediaStore.INTENT_ACTION_MEDIA_PLAY_FROM_SEARCH);
				
				//Intent intent = new Intent("com.google.android.music");
				
				Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.music");
				//startActivity(intent);*
				
				//intent.putExtra(MediaStore.EXTRA_MEDIA_FOCUS,
						//		MediaStore.Audio.Artists.ENTRY_CONTENT_TYPE);
						
				intent.putExtra(MediaStore.EXTRA_MEDIA_ARTIST,"Evanescence");
				intent.putExtra(MediaStore.EXTRA_MEDIA_ALBUM,"Fallen");
				
				intent.putExtra(MediaStore.EXTRA_MEDIA_TITLE,title);               
				intent.putExtra(SearchManager.QUERY,title);
				if (intent.resolveActivity(getPackageManager()) != null) {
					startActivity(intent);
				}
				*/
				
				
				//Intent i = getPackageManager().getLaunchIntentForPackage("com.google.android.music");
				
				Intent i = new Intent("com.android.music.musicservicecommand");
				//i.putExtra("command", "play");
				i.putExtra("command", MediaPlayer.CMDPLAY);
				sendBroadcast(i);

				AudioManager manager = (AudioManager)getSystemService(Context.AUDIO_SERVICE);
				if(manager.isMusicActive())
				{
					// do something - or do it not
					Toast.makeText(FloatingViewService.this, "Music active", Toast.LENGTH_SHORT).show();
					
					//Toast.makeText(FloatingViewService.this, "Music active", Toast.LENGTH_LONG).show();
					
				}else{
					Intent intent = getPackageManager().getLaunchIntentForPackage("com.google.android.music");
					startActivity(intent);
				}
				
            }
        });

        //Set the next button.
        ImageView nextButton = (ImageView) mFloatingView.findViewById(R.id.next_btn);
        nextButton.setOnClickListener(new View.OnClickListener() {

            //private String MIMETYPE_TEXT_PLAIN = "text/*";

            @Override
            public void onClick(View v) {
                //Toast.makeText(FloatingViewService.this, "Playing next song.", Toast.LENGTH_LONG).show();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                String pasteData = "grf";

                // If it does contain data, decide if you can handle the data.
                if (!(clipboard.hasPrimaryClip())) {

                } else if (!(clipboard.getPrimaryClipDescription().hasMimeType(MIMETYPE_TEXT_PLAIN))) {

                    // since the clipboard has data but it is not plain text

                } else {

                    //since the clipboard contains plain text.
                    ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

                    // Gets the clipboard as text.
                    pasteData = item.getText().toString();
                    Toast.makeText(FloatingViewService.this, pasteData + "Playing next song.", Toast.LENGTH_LONG).show();
                }
				
				
				Intent i = new Intent("com.android.music.musicservicecommand");
				i.putExtra("command", MediaPlayer.CMDNEXT);
				sendBroadcast(i);

            }
        });

        //Set the pause button.
        ImageView prevButton = (ImageView) mFloatingView.findViewById(R.id.prev_btn);
        prevButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(FloatingViewService.this, "Playing previous song.", Toast.LENGTH_LONG).show();
                startService(new Intent(FloatingViewService.this, SampleAccessibilityService.class));
				
				Intent i = new Intent("com.android.music.musicservicecommand");
				i.putExtra("command", MediaPlayer.CMDPREVIOUS);
				sendBroadcast(i);
				
            }
        });

        //Set the close button
        ImageView closeButton = (ImageView) mFloatingView.findViewById(R.id.close_button);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                collapsedView.setVisibility(View.VISIBLE);
                expandedView.setVisibility(View.GONE);
            }
        });

        //Open the application on thi button click
        ImageView openButton = (ImageView) mFloatingView.findViewById(R.id.open_button);
        openButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Open the application  click.
                Intent intent = new Intent(FloatingViewService.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);

                //close the service and remove view from the view hierarchy
                stopSelf();
            }
        });

        //Drag and move floating view using user's touch action.
        mFloatingView.findViewById(R.id.root_container).setOnTouchListener(new View.OnTouchListener() {
            private int initialX;
            private int initialY;
            private float initialTouchX;
            private float initialTouchY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:

                        //remember the initial position.
                        initialX = params.x;
                        initialY = params.y;

                        //get the touch location
                        initialTouchX = event.getRawX();
                        initialTouchY = event.getRawY();
                        return true;
                    case MotionEvent.ACTION_UP:
                        int Xdiff = (int) (event.getRawX() - initialTouchX);
                        int Ydiff = (int) (event.getRawY() - initialTouchY);

                        //The check for Xdiff <10 && YDiff< 10 because sometime elements moves a little while clicking.
                        //So that is click event.
                        if (Xdiff < 10 && Ydiff < 10) {
                            if (isViewCollapsed()) {
                                //When user clicks on the image view of the collapsed layout,
                                //visibility of the collapsed layout will be changed to "View.GONE"
                                //and expanded view will become visible.
                                collapsedView.setVisibility(View.GONE);
                                expandedView.setVisibility(View.VISIBLE);
                            }
                        }
                        return true;
                    case MotionEvent.ACTION_MOVE:
                        //Calculate the X and Y coordinates of the view.
                        params.x = initialX + (int) (event.getRawX() - initialTouchX);
                        params.y = initialY + (int) (event.getRawY() - initialTouchY);

                        //Update the layout with new X & Y coordinate
                        mWindowManager.updateViewLayout(mFloatingView, params);
                        return true;
                }
                return false;
            }
        });
		
		
		stateTextView = mFloatingView.findViewById(R.id.stateTv);
		stateTextView.setText("hdhd");
		
		
		IntentFilter musicPauseFilter = new IntentFilter(
			"android.media.action.CLOSE_AUDIO_EFFECT_CONTROL_SESSION");
		IntentFilter musicPlayFilter = new IntentFilter(
			"android.media.action.OPEN_AUDIO_EFFECT_CONTROL_SESSION");

		registerReceiver(musicPlay, musicPlayFilter);
		registerReceiver(musicPause, musicPauseFilter);
    }

    /**
     * Detect if the floating view is collapsed or expanded.
     *
     * @return true if the floating view is collapsed.
     */
    private boolean isViewCollapsed() {
        return mFloatingView == null || mFloatingView.findViewById(R.id.collapse_view).getVisibility() == View.VISIBLE;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mFloatingView != null) {
            mWindowManager.removeView(mFloatingView);
        }
    }
	
	
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
