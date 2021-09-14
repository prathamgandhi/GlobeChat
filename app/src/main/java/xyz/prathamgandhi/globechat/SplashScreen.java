package xyz.prathamgandhi.globechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences.Editor;


public class SplashScreen extends AppCompatActivity {
    private final int SPLASH_DISPLAY_LENGTH = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Handler h = new Handler();
        h.postDelayed(() -> {
            Intent intent = new Intent(SplashScreen.this, NamePage.class);
            SplashScreen.this.startActivity(intent);
            SplashScreen.this.finish();
        }, SPLASH_DISPLAY_LENGTH);

    }
}