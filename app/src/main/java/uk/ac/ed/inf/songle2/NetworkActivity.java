package uk.ac.ed.inf.songle2;


import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;






import android.app.FragmentManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.EditText;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class NetworkActivity extends FragmentActivity implements DownloadCallback {

    private Intent main_Intent;
    private NetworkFragment mNetworkFragment;//= new NetworkFragment();// = new NetworkFragment();
    private boolean mDownloading = false;
    public static boolean ismobileallowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        Intent intent = getIntent();
        String file = intent.getStringExtra("file");
        String words = intent.getStringExtra("lyrics");
        main_Intent=intent;
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean MOBILE_ALLOWED = sharedPrefs.getBoolean("key_pref_mobile",false);
        ismobileallowed=MOBILE_ALLOWED;

//        Log.i("",String.valueOf(MOBILE_ALLOWED));
//        Bundle bundle = new Bundle();
//        bundle.putBoolean("key",MOBILE_ALLOWED);
//        mNetworkFragment.setArguments(bundle);


        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(), file,MOBILE_ALLOWED);
        startDownload();
    }

    private void startDownload() {
        if (!mDownloading && mNetworkFragment != null) {
            // Execute the async download.
            mNetworkFragment.startDownload(this);
            mDownloading = true;
        }
    }
    @Override
    public void updateFromDownload(String result) throws UnsupportedEncodingException,XmlPullParserException,IOException  {
        if (result==null) {
            Intent intent = new Intent(NetworkActivity.this, MainActivity.class);
            intent.putExtra("NetworkActivity","NetworkActivity");
            startActivity(intent);
        }
        if (result.contains("<?xml version=") && !(result.contains("kml xmlns")))
        {
            //result is xml -> take this string to the scrolling activity where it will be parsed and displayed
            Intent intent = new Intent(NetworkActivity.this, ScrollingActivity.class);
            intent.putExtra("Resultxml",result);
            startActivity(intent);
        }
        else if (result.contains("kml xmlns"))
        {
            //result is kml ->take this string to the download lyrics activity which will in turn come back here and to download lyrics
            Intent fromdifficulty = getIntent();
            String entry = fromdifficulty.getStringExtra("entry");
            String url =fromdifficulty.getStringExtra("file");
            // get the kml url and entry from the FivePageActivity (VeryEasy,Easy,Normal etc.)


            Intent todownlyrcs = new Intent(NetworkActivity.this, DownloadLyricsActivity.class);
            todownlyrcs.putExtra("kml_url",url);
            todownlyrcs.putExtra("Resultkml", result);
            todownlyrcs.putExtra("entry",entry);
            //^ we need to bring along the kml url, the downloaded kml string (result) and the entry
            startActivity(todownlyrcs);
        }
        else
        {
            //result is Lyrics
            Intent fromdownlyrcs = getIntent();
            String entry =fromdownlyrcs.getStringExtra("entry");
            String url = fromdownlyrcs.getStringExtra("kml_url");
            String kmlfile = main_Intent.getStringExtra("kmlfromDownloadLyricActivity");
            Intent tomap = new Intent(NetworkActivity.this, MapsActivity.class);
            tomap.putExtra("ResultLyrics",result);
            tomap.putExtra("Resultkmlfromdown",kmlfile);
            tomap.putExtra("entry",entry);
            tomap.putExtra("kml_url",url);
            startActivity(tomap);
        }
     }

    @Override
    public NetworkInfo getActiveNetworkInfo() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo;
    }

    @Override
    public void onProgressUpdate(int progressCode, int percentComplete) {
        switch(progressCode) {
            // You can add UI behavior for progress updates here.
            case Progress.ERROR:
            //...
                break;
            case Progress.CONNECT_SUCCESS:
            //...
                break;
            case Progress.GET_INPUT_STREAM_SUCCESS:
            //...
                break;
            case Progress.PROCESS_INPUT_STREAM_IN_PROGRESS:
            //...
                break;
            case Progress.PROCESS_INPUT_STREAM_SUCCESS:
            //...
                break;
        }
    }

    @Override
    public void finishDownloading() {
        mDownloading = false;
        if (mNetworkFragment != null) {
            mNetworkFragment.cancelDownload();
        }
    }

}
