package uk.ac.ed.inf.songle2;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static MediaPlayer mediaPlayer;
    public static boolean shouldPlay=false;
    @Override
    public void onBackPressed()
    {
        // exit app regardless of position in stack
        moveTaskToBack(true);
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Intent intent =getIntent();
        if (intent.getStringExtra("NetworkActivity")!=null) {
            //If the message with tag "NetworkActivity" is not null, this means that this activity has just been called from the Network Activity
            //The only case that this will happen is if the network activity fails i.e. there is no internet connection
            Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection.", Snackbar.LENGTH_LONG);
            snackbar.show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(this);
        boolean MUSIC_ALLOWED = settings.getBoolean("key_pref_music",true);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bensoundukulele);
        if (MUSIC_ALLOWED&&!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()){
             mediaPlayer.stop();
             mediaPlayer.release();
        }

    }
    public void sendMessage(View view) {
            Intent intent = new Intent(this, NetworkActivity.class);
            intent.putExtra("file", "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml");
            startActivity(intent);
    }
    public void HowToPlayMessage(View view) {
        Intent intent = new Intent(this, HowToPlayActivity.class);
        shouldPlay=true;
        startActivity(intent);
    }
    public void SettingsMessage(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);

        startActivity(intent);
    }


}
