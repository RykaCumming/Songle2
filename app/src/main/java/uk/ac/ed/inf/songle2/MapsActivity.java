package uk.ac.ed.inf.songle2;

import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.common.api.GoogleApiClient;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationServices;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;import android.os.Vibrator;
import android.widget.TextView;

import java.util.Set;
import java.util.concurrent.TimeUnit;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,LocationListener
{
    private List<Marker> mMarkers = new ArrayList<Marker>();
    private ArrayList<SongleKmlParser.Entry> thelist; //static?
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private boolean mLocationPermissionGranted = false;
    private Location mLastLocation;
    private static final String TAG ="MapsActivity";
    private ArrayList<String> wordlist = new ArrayList<>();
    private Set<String> wordlistset = new HashSet<>();
    private String kml_url;
    private String global_lyrics;
    private Set<String> global_saved_words;

    //-------------Information about the current song
    private String global_entry;
    private String num;
    private String artist;
    private String title;
    private String url;

    private String difficulty;
    private boolean timer_allowed;

    private MediaPlayer mediaPlayer;

    SongleKmlParser songleKmlParser = new SongleKmlParser();
    ParseTask mParseTask= new ParseTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        Intent intent = getIntent();
        kml_url = intent.getStringExtra("kml_url");
        Log.i("kml_url",kml_url);
        String kmlfile = intent.getStringExtra("Resultkmlfromdown");
        String lyrics = intent.getStringExtra("ResultLyrics");
        global_lyrics=lyrics;
        String entry =intent.getStringExtra("entry");
        String[] entrysplit = entry.split("\\|\\|\\|");
        num=entrysplit[0];
        artist=entrysplit[1];
        title=entrysplit[2];
        url=entrysplit[3];
        global_entry=entry;

        SharedPreferences sharedPref = getSharedPreferences("mySettings",MODE_PRIVATE);

        Set<String> saved_words = sharedPref.getStringSet(num, null);
        global_saved_words = saved_words;
        if (saved_words!=null) { //If there is data from previous play
            global_saved_words = saved_words;
            wordlistset = saved_words;
            for (String s : global_saved_words)
            {
                Log.i("saved_words11",s);
            }
            for (String s : wordlistset)
            {
                String[] split = s.split("\\|\\|\\|");
                wordlist.add(split[0]);
            }
        }

        final FragmentManager fm = getSupportFragmentManager();
        Button dictionary = findViewById(R.id.dictionary);
        dictionary.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordListFragment newFragment = new WordListFragment();
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("wordlist",wordlist);
                newFragment.setArguments(bundle);
                newFragment.show(fm, "Dialog Fragment");
            }
        });
        final FragmentManager fm2 = getSupportFragmentManager();
        Button guess = findViewById(R.id.guess);
        guess.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                GuessFragment guessFragment = new GuessFragment();
                Bundle bundle = new Bundle();
                Log.i("aaaaaaaaaaa",global_entry);
                bundle.putString("global_entry",global_entry);
                guessFragment.setArguments(bundle);
                guessFragment.show(fm2, "Dialog Fragment 2");
            }
        });

        //the code below should only be executed if we have no previous user data for this map, in which case this activity would have been called from Network Activity
        //We know that every time network activity starts this activity, it will always send a value for ResultLyrics
        if (intent.getStringExtra("ResultLyrics")!=null) {
            mParseTask.execute(kmlfile);
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }
    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        boolean MUSIC_ALLOWED = sharedPrefs.getBoolean("key_pref_music",true);

        if (MUSIC_ALLOWED) {
            mediaPlayer = MediaPlayer.create(getApplicationContext(), R.raw.bensoundenergy);
            mediaPlayer.setLooping(true);
            mediaPlayer.start();
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if (mediaPlayer.isPlaying()) {mediaPlayer.stop();}
//        mediaPlayer.release();
    }
    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient.isConnected())
        {
            mGoogleApiClient.disconnect();
        }
        SharedPreferences settings = getSharedPreferences("mySettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        Log.i("savedvalues",num);
        for (String s:wordlistset)
        {
            Log.i("savedvaluesm",s);
        }
        editor.putStringSet(num,wordlistset);
        editor.putString(num+"_kml_url",kml_url);
        editor.apply();

//        mediaPlayer.stop();
        mediaPlayer.release();
    }


    public void updateFromDownload(ArrayList<SongleKmlParser.Entry> entries) throws UnsupportedEncodingException, XmlPullParserException, IOException {
        Log.i("firstentry",entries.get(0).getCoordinate());
        thelist=entries;
        Log.i("entries.size()",entries.size()+"");

        for (int i=0;i<entries.size();i++) {
            String[] coordinates = entries.get(i).getCoordinate().split(",");
            String lat = coordinates[1];
            String lng = coordinates[0];
            String zero =coordinates[2];
            Log.i("latlng",lat);
            Log.i("latlng",lng);
            Log.i("zero",zero);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(new LatLng(Double.parseDouble(lat),Double.parseDouble(lng)));
            Marker marker = mMap.addMarker(markerOptions);
            marker.setTitle(entries.get(i).getName()); // (e.g. 11:3)
            String[] stringnums = entries.get(i).getName().split(":");
            int[] lineandnum = new int[stringnums.length];
            for (int k=0;k<stringnums.length;k++)
            {
                lineandnum[k]=Integer.parseInt(stringnums[k]); //we now have an array of new int[] {11,3}
            }
            marker.setTag(entries.get(i).getName());
//            Log.i("qazxs",marker.getTag().);

            if (entries.get(i).getStyleUrl().equals("#unclassified"))
            {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank));
            }
            else if (entries.get(i).getStyleUrl().equals("#boring"))
            {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ylw_blank));
            }
            else if (entries.get(i).getStyleUrl().equals("#notboring"))
            {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.ylw_circle));
            }
            else if (entries.get(i).getStyleUrl().equals("#interesting"))
            {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.orange_diamond));
            }
            else if (entries.get(i).getStyleUrl().equals("#veryinteresting"))
            {
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.mipmap.red_stars));
            }
            else {}
            mMarkers.add(marker);
            //following for loop deals with if marker has been found already
/*
            if (global_saved_words!=null) {
                Log.i("globalwordsisempty","globalwordsisempty");
                boolean has_marker_been_found_already = false;
                for (String word_and_tag : wordlistset) {
                    String[] split = word_and_tag.split("\\|\\|\\|");                    //word_and_tag is of form "Figaro|||25:3"
                    Log.i("nuuuuuuu",marker.getTitle());
                    Log.i("nuuuuuuu",split[1]);
                    if (marker.getTitle().equals(split[1])) {                                //each marker's getTitle is of form 25:3
                        has_marker_been_found_already = true;
                    }
                }
                if (!has_marker_been_found_already) {
                    mMarkers.add(marker);
                }
            }
            else{
                mMarkers.add(marker);
            }
*/
      }
        String[] lines = global_lyrics.split("\\r?\\n");
        String[][] words = new String[lines.length][];
        for (int i=0;i<lines.length;i++)
        {
            String[] separatenums = lines[i].split("\\t");
            if (separatenums.length>1) { //avoid index out of bounds
                words[i] = separatenums[1].split("[^\\w'-]+");
            }
//            if (i>=35 &&i<=40 &&words[i].length>=3) {
//                Log.i("finaltest2",words[i][0]);
//                Log.i("finaltest2",words[i][1]);
         //       Log.i("finaltest2",words[i][2]);
//            }
        }
        for (Marker marker :mMarkers)
        {
//            String[] p = marker.getTag().toString().split(":");
            Object tag = marker.getTag();
            String stringtag =String.valueOf(tag);
            String[] pair = stringtag.split(":");
            int i = Integer.parseInt(pair[0])-1;
            int j = Integer.parseInt(pair[1])-1;
            String theword = words[i][j];
//            Log.i("theword",words[i][j]);
            marker.setSnippet(theword);
//            Log.i("test5",marker.getTag().toString());
//            Log.i("test5",marker.getSnippet());
        }
        if (global_saved_words!=null) {
            for (Marker marker : mMarkers) {
                for (String word_and_tag : global_saved_words) {
                    String[] split = word_and_tag.split("\\|\\|\\|");
                    if (marker.getTitle().equals(split[1])) {
                        marker.remove();
                    }
                }
            }
        }
    }

    public class ParseTask extends AsyncTask<String, Void, ArrayList<SongleKmlParser.Entry>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<SongleKmlParser.Entry> doInBackground(String... res) { //        mParseTask.execute(result);

            String res1 = res[0];
            Log.i("res1",res1);
            InputStream stream = null;

            try {
                stream = new ByteArrayInputStream(res1.getBytes(StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ArrayList<SongleKmlParser.Entry> entries = new ArrayList<>();
            try {
                entries = songleKmlParser.parse(stream);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return entries;
        }
        @Override
        protected void onPostExecute(ArrayList<SongleKmlParser.Entry> entries) {
            //     super.onPostExecute(entries);
            try {
                updateFromDownload(entries);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
    protected void createLocationRequest() {
        Log.i(TAG,"OnLocationRequest");
        LocationRequest mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(1000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        //Can we access the user's current location?
        int permissionCheck = ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED){
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this);
        }

    }

    @Override
    public void onConnected(Bundle connectionHint) {        //protected void?
        Log.i(TAG,"OnConnected");
        try { createLocationRequest(); }
        catch (java.lang.IllegalStateException ise) {
            System.out.println("IllegalStateException thrown [onConnected]");
        }
        //Can we access the user's location?
        if (ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mLastLocation =
                    LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        } else {
            ActivityCompat.requestPermissions(this,new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        Log.i(TAG,"OnConnected");
    }

    @Override
    public void onLocationChanged(Location current)
    {
        Log.i(TAG,"OnLocationChanged");
        System.out.println("onLocationChanged] Lat/long now (" +
                String.valueOf(current.getLatitude())+","+
                String.valueOf(current.getLongitude())+")");
    }

    public void onConnectionSuspended(int flag) {
        System.out.print(">>>>onConnectionSuspended");
    }
    public void onConnectionFailed(ConnectionResult result)
    {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle
        // the failure silently
        System.out.println(">>>> onConnectionFailed");
    }

    /////////////////////////////////////////////////////////////////////////////////////
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * EXTRA STATEMENT
     */
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setMessage("Return to Main Menu?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Intent intent = new Intent(MapsActivity.this, MainActivity.class);
                        startActivity(intent);

                    }
                })
                .setNegativeButton("No", null)
                .show();
//        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please use home button!", Snackbar.LENGTH_LONG);
//        snackbar.show();

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"OnMapReady");
        mMap = googleMap;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        timer_allowed = sharedPref.getBoolean("key_pref_timer",false);
                //getString(SettingsActivity.key_pref_timer, "");
        Log.i("shareduserpreferences",String.valueOf(timer_allowed));

        if (timer_allowed)
        {
            final TextView timer=(TextView)findViewById(R.id.thetimer);
            new CountDownTimer(1800000, 1000) {

                public void onTick(long millisUntilFinished) {

                    timer.setText(""+String.format("%02d:%02d",
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }
                public void onFinish() {
                    timer.setText("Time up!");
                }
            }.start();

        }




        // move camera to centre of george square
        LatLng centre_of_GS = new LatLng(55.944425, -3.188396);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centre_of_GS, 15.2f));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                LatLng markerLatLng = marker.getPosition();
                Location markerLocation =new Location("");
                markerLocation.setLatitude(markerLatLng.latitude);
                markerLocation.setLongitude(markerLatLng.longitude);
                if (mLastLocation.distanceTo(markerLocation)<=30) {
                    wordlist.add((marker.getSnippet()));
                    wordlistset.add((marker.getSnippet())+"|||"+marker.getTitle());
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Word Collected: " + marker.getSnippet(), Snackbar.LENGTH_LONG);
                    snackbar.show();
                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                    // Vibrate for 200 milliseconds
                    v.vibrate(200);

                    marker.remove();
                }
                else {
                    Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Word "+marker.getTitle()+"         Get closer to collect!",1000);
                    snackbar.show();

//                    mMap.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng));
                }
                return true;
            }
        });

        try { //Visualise current position with small blue circle
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            mMap.setMyLocationEnabled(true);
        } catch (SecurityException se) {
            System.out.println("Security exception thrown [onMapReady]");
        }
        // Add "My location" button to user interface
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
    }
}