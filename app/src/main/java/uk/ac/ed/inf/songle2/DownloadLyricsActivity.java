package uk.ac.ed.inf.songle2;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DownloadLyricsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_lyrics);
        Intent intent = getIntent();
        String kmlfile = intent.getStringExtra("Resultkml");
//        final String lyricsurl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+ FivePageActivity.songno + "/words.txt";
        final String lyricsurl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+ FivePageActivity.songno + "/words.txt";
        Intent intent2 =new Intent(DownloadLyricsActivity.this, NetworkActivity.class);
        intent2.putExtra("file",lyricsurl);
        intent2.putExtra("kmlfromDownloadLyricActivity",kmlfile);
        startActivity(intent2);
    }
}
