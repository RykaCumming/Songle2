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
import android.support.v4.content.ContextCompat;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;





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
    public static final String WIFI = "Wi-Fi";
    public static final String ANY = "Any";
    private static final String URL = "http://stackoverflow.com/feeds/tag?tagnames=android&sort=newest";
    private static final String URLSong1Map1 = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/map5.kml";

   // public static final String  = "String1";

    public static final String TAG = "NetworkActivity";

    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;
    public static String sPref = null;

    private NetworkFragment mNetworkFragment;
    SongleXmlParser songleXmlParser = new SongleXmlParser();
    private boolean mDownloading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_network);
        Intent intent = getIntent();
        String file = intent.getStringExtra("file");
        Log.i("urlmadeit",file);
        mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(), file);
      //  mNetworkFragment = NetworkFragment.getInstance(getFragmentManager(), "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/01/map5.kml");

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
      //  Log.e("NetworkActivity",result);
        if (!result.contains("kml xmlns"))
        {
            Intent intent = new Intent(NetworkActivity.this, ScrollingActivity.class);
            intent.putExtra("Resultxml", result);
            startActivity(intent);
        }
        else
        {
              Log.i("NetworkActivitykml",result);
              Intent intent = new Intent(NetworkActivity.this, MapsActivity.class);
              intent.putExtra("Resultkml", result);
              startActivity(intent);
        }
   //     InputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));
   //     ArrayList<SongleXmlParser.Entry> parsed = songleXmlParser.parse(stream);
  //      Log.e("NetworkActivity",parsed.get(0).getArtist());


    /*
        SongleXmlParser songleXmlParser =new SongleXmlParser();
        InputStream stream = new ByteArrayInputStream(result.getBytes(StandardCharsets.UTF_8.name()));
        try {
            songleXmlParser.parse(stream);
//            Log.e("",songleXmlParser.parse(stream).get(0).artist);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("NetworkActivity",result);
        */
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

    // Uses AsyncTask to download the XML feed from stackoverflow.com.
    public void loadPage() {

        if((sPref.equals(ANY)) && (wifiConnected || mobileConnected)) {
            new DownloadXmlTask().execute(URL);
        }
        else if ((sPref.equals(WIFI)) && (wifiConnected)) {
            new DownloadXmlTask().execute(URL);
        } else {
            // show error
        }
    }
}
