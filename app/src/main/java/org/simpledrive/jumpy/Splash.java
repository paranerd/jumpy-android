package org.simpledrive.jumpy;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;

public class Splash extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        
        Thread logoTimer = new Thread() {
        	public void run() {
        		try {
        			sleep(1000);
        			Intent menuIntent = new Intent("org.simpledrive.jumpy.MENU");
        			startActivity(menuIntent);
        		} catch (InterruptedException e) {
					e.printStackTrace();
				}

        		finally {
        			finish();
        		}
        	}
        };
        logoTimer.start();
    }
}
