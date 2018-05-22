package com.clarusapps.SwamiVivekananda;



import com.google.android.gcm.GCMRegistrar;

import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.Menu;
import android.widget.ImageView;

public class SplashScreen extends Activity {
	ImageView imgView;
	private static final int SPLASH_DISPLAY_TIME = 3000; 
	private static String SENDERID="648660927131";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        DatabaseHelper db=MyApplication.getDataBase();
        
       // db.closeDB();
       new Thread(){
			public void run(){
				GCMRegistrar.checkDevice(SplashScreen.this);
				GCMRegistrar.checkManifest(SplashScreen.this);
				final String regId = GCMRegistrar.getRegistrationId(SplashScreen.this);
				if (regId.equals("")) {
				  GCMRegistrar.register(SplashScreen.this, SENDERID);
				  Log.v("Now registered", "Registaration id:"+GCMRegistrar.getRegistrationId(SplashScreen.this));
				} else {
				  Log.v("Already registered", "Registaration id:"+regId);
				}
			}
			
		}.start();
		
        new Handler().postDelayed(new Runnable() {
			public void run() {
				Intent mainIntent = new Intent(SplashScreen.this,
						MyTabActivity.class);
				SplashScreen.this.startActivity(mainIntent);

				SplashScreen.this.finish();				
				 
				overridePendingTransition(R.anim.mainfadein,
						R.anim.splashfadeout);
			}
		}, SPLASH_DISPLAY_TIME);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
