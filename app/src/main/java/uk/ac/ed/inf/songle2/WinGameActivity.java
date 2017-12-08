package uk.ac.ed.inf.songle2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

public class WinGameActivity extends AppCompatActivity {

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(WinGameActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_win_game);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
        Intent intent =getIntent();
        String title = intent.getStringExtra("title");
        String artist = intent.getStringExtra("artist");
        String url = intent.getStringExtra("url");
        TextView textview = findViewById(R.id.textView5);
        textview.setText("Congratulations! The song was "+"\n"+title+"\n"+" by "+"\n"+artist+"."+"\n\n"+url);
    }

}
