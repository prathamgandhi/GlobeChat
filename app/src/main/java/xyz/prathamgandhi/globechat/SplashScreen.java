package xyz.prathamgandhi.globechat;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.content.SharedPreferences.Editor;


public class SplashScreen extends AppCompatActivity {
    public final String PREFS_NAME = "MyPrefsFile";
    private final int SPLASH_DISPLAY_LENGTH = 500;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        Editor editor = settings.edit();
        Runnable go_to_namepage;
        if(settings.getBoolean("first_launch", true)) {

            go_to_namepage = new Runnable() {
                @Override
                public void run() {
                    Intent nameIntent = new Intent(SplashScreen.this, NamePage.class);
                    SplashScreen.this.startActivity(nameIntent);
                    SplashScreen.this.finish();
                }
            };
            editor.putBoolean("first_launch", false);
            editor.apply();
        }
        else {
            go_to_namepage = new Runnable() {
                @Override
                public void run() {
                    Intent mainIntent = new Intent(SplashScreen.this, MainActivity.class);
                    SplashScreen.this.startActivity(mainIntent);
                    SplashScreen.this.finish();
                }

            };
        }
        Handler h = new Handler();
        h.postDelayed(go_to_namepage, SPLASH_DISPLAY_LENGTH);
    }

}