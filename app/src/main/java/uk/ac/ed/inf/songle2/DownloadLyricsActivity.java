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
        String url = intent.getStringExtra("kml_url");
        String kmlfile = intent.getStringExtra("Resultkml");
        String entry =intent.getStringExtra("entry");
        String[] splitentry = entry.split("\\|\\|\\|");
        final String lyricsurl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+splitentry[0] + "/words.txt";
        Intent intent2 =new Intent(DownloadLyricsActivity.this, NetworkActivity.class);
        intent2.putExtra("file",lyricsurl);
        intent2.putExtra("kmlfromDownloadLyricActivity",kmlfile);
        intent2.putExtra("entry",entry);
        intent2.putExtra("kml_url", url);
        startActivity(intent2);
    }
}
