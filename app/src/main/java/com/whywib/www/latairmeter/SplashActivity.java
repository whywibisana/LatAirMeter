package com.whywib.www.latairmeter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

public class SplashActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		Thread timer = new Thread() {
            @Override
            public void run() {
                long startTime = System.currentTimeMillis();
            
                
                long now = System.currentTimeMillis();
                if (now - startTime < 4000){
                    try {
                        sleep(4000 - (now - startTime));
                    } catch (InterruptedException iEx){
                        
                    }
                }
                
                SplashActivity.this.finish();
               
                startActivity(new Intent(SplashActivity.this.getApplicationContext(), LoginActivity.class));
            }
        };
        timer.start();
	}
}
