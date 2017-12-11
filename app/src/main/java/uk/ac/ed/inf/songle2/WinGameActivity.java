package uk.ac.ed.inf.songle2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class WinGameActivity extends AppCompatActivity {
private MediaPlayer mediaPlayer;
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WinGameActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_game);
        mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.vesperia94hooray);
        mediaPlayer.start();
        Intent intent =getIntent();
        String num = intent.getStringExtra("num");
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        String url = intent.getStringExtra("url");
        String difficulty = intent.getStringExtra("difficulty");
        TextView textview = findViewById(R.id.textView5);
        Button return_home = findViewById(R.id.button7);
        return_home.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WinGameActivity.this, MainActivity.class);
                startActivity(intent);

            }
        });

        SharedPreferences settings = getSharedPreferences("mySettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(num+"NumberWin",num);
        editor.putString(num+"DifficultyWin",difficulty);
        editor.apply();

        textview.setText("Congratulations! \n\n\n You completed the game on the "+difficulty+" difficulty."+ "\n"+ "The song was "+"\n"+title+"\n"+" by "+"\n"+artist+"."+"\n\n"+url);
    }

}
