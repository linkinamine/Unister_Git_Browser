package com.unister.gitquery;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.unister.gitquery.Data.Constants;

/**
 * Created by Mohamed El Amine on 28/05/2016.
 */
public class SplashScreen extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {

                Intent i = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(
                        R.anim.fade_in, R.anim.fade_out);

                finish();
            }
        }, Constants.SPLASH_TIME_OUT);
    }
}
