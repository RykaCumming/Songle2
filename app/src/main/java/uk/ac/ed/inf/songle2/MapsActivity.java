package uk.ac.ed.inf.songle2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
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
import java.util.List;import android.os.Vibrator;
import android.widget.TextView;import java.util.concurrent.TimeUnit;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    private List<Marker> mMarkers = new ArrayList<Marker>();
    private ArrayList<SongleKmlParser.Entry> thelist; //static?
    public  String[][] splits;
    private GoogleMap mMap;
    private GoogleApiClient mGoogleApiClient;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION =1;
    private boolean mLocationPermissionGranted = false;
    private Location mLastLocation;
    private static final String TAG ="MapsActivity";
    private ArrayList<String> wordlist = new ArrayList<>();

    private String global_lyrics;

    private NetworkFragment m1NetworkFragment;
  //  private DictionaryFragment mDictionaryFragment;
    // Whether there is a Wi-Fi connection.
    private static boolean wifiConnected = false;
    // Whether there is a mobile connection.
    private static boolean mobileConnected = false;
    // Whether the display should be refreshed.
    public static boolean refreshDisplay = true;
    public static String sPref = null;
    private boolean mDownloading = false;

    SongleKmlParser songleKmlParser = new SongleKmlParser();
    ParseTask mParseTask= new ParseTask();

    public class Pair {
        private int a;
        private int b;

        public Pair(int first,int second) {
            this.a=first;
            this.b=second;
        }
        public int getA(){
            return a;
        }
        public int getB(){
            return b;
        }

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String kmlfile = intent.getStringExtra("Resultkmlfromdown");
        String lyrics = intent.getStringExtra("ResultLyrics");
        global_lyrics=lyrics;

        //WordListFragment wordListFragment = new WordListFragment();
 //       wordListFragment.show(getFragmentManager(), "hello");
        final FragmentManager fm = getSupportFragmentManager();
        Button dictionary = findViewById(R.id.dictionary);
        dictionary.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                WordListFragment newFragment = new WordListFragment();
//                DialogFragment newFragment = WordListFragment.newInstance();
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
                guessFragment.show(fm2, "Dialog Fragment 2");
            }
        });

        mParseTask.execute(kmlfile);

       /* FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        WordListFragment wordListFragment = new WordListFragment();
        transaction.add(R.id.fragment_container,wordListFragment);
        transaction.commit();
        Button dictionary = (Button) findViewById(R.id.dictionary);
*/
      //  transaction.replace(R.id.dictionaryfragment2,mDictionaryFragment);

 /*       Button button4 = (Button)findViewById(R.id.dictionary2);
        button4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {


        Snackbar mySnackbar = Snackbar.make(v,"Word Collected: little", 6000);
        mySnackbar.show();


                    }
                }
        );*/

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
            Pair pair =new Pair(Integer.parseInt(stringnums[0]),Integer.parseInt(stringnums[1]));
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

//            mMap.addMarker(new LatLng())

//            split[i]=entries.get(i).getCoordinate().split(",");
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



/*
        for (int i=0;i<lines.length;i++)
        {
//            Log.i("testnumber1",lines[i]);
            String[] separatenums=lines[i].split("\\t");
            if (separatenums.length>1){
                String[] words = separatenums[1].split("[^\\w'-]+");
                int linenumber=i+1;
                for (int j=0;j<words.length;j++)
                { //possibly do "if word[j] is ==" " then disregard"
                    int wordnumber=j+1;
                    for (Marker marker:mMarkers)
                    {
                        if (marker.getTag().equals(linenumber+":"+wordnumber)) //is the equals operation causing the runtime to be so high! make it individual integers? faster
                        {
                            marker.setSnippet(words[j]);
                            Log.i("testnumber4",marker.getSnippet());
                        }
                    }
//                    Log.i("testnumber3",words[j] +" "+linenumber+":"+wordnumber);
                }
//                Log.i("testnumber2",words[0] + " "+i+1);
            }
            else {}
        }
*/
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



    /**
    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }

    }
*/

    public void sendMessage(View view) {
    //    Intent intent = new Intent(this, NetworkActivity.class);
    //    startActivity(intent);
        Snackbar mySnackbar = Snackbar.make(view,"Word Collected: little", 2500);
        mySnackbar.show();
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
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (LocationListener) this); //wtf
        }

    }

//    @Override
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

    //@Override
    public void onLocationChanged(Location current)
    {
        Log.i(TAG,"OnLocationChanged");
        System.out.println("onLocationChanged] Lat/long now (" +
                String.valueOf(current.getLatitude())+","+
                String.valueOf(current.getLongitude())+")");
        //do something with current location.
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
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Please use home button!", Snackbar.LENGTH_LONG);
        snackbar.show();

    }
    private static final String FORMAT = "%02d:%02d:%02d";
    @Override
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"OnMapReady");
        mMap = googleMap;
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
        boolean timer_allowed = sharedPref.getBoolean(SettingsActivity.key_pref_timer,false);
                //getString(SettingsActivity.key_pref_timer, "");
        Log.i("shareduserpreferences",String.valueOf(timer_allowed));

        if (timer_allowed)
        {
            final TextView timer=(TextView)findViewById(R.id.thetimer);
            new CountDownTimer(30000*60, 1000) {

                public void onTick(long millisUntilFinished) {

                    timer.setText(""+String.format(FORMAT,
                            TimeUnit.MILLISECONDS.toHours(millisUntilFinished),
                            TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                    TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                            TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                    TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
                }
                public void onFinish() {
                    timer.setText("done!");
                }
            }.start();

        }




        // Add a marker in Sydney and move the camera
        LatLng centre_of_GS = new LatLng(55.944425, -3.188396);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centre_of_GS, 15.2f));


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                wordlist.add((marker.getSnippet()));
                Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "Word Collected: "+marker.getSnippet(), Snackbar.LENGTH_LONG);
                snackbar.show();
                Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                // Vibrate for 200 milliseconds
                v.vibrate(200);
                marker.remove();
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