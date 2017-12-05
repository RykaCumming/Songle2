package uk.ac.ed.inf.songle2;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
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
import java.util.List;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {
    public List<Marker> mMarkers = new ArrayList<Marker>();
    public ArrayList<SongleKmlParser.Entry> thelist; //static?
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
    //LyricsDownloadTask mlyricsdownloadtask = new LyricsDownloadTask();


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
//        Log.i("Resultkmlfromdown",kmlfile);
        Log.i("lyrics11",lyrics);

     //   WordListFragment wordListFragment = WordListFragment.newInstance();
//        wordListFragment.show(getFragmentManager(), "hello");
    //    Button dictionary = findViewById(R.id.dictionary);
    //    dictionary.setOnClickListener(new Button.OnClickListener() {
    //        @Override
     //       public void onClick(View v) {
      //          Snackbar mySnackbar = Snackbar.make(v,"Word Collected: little", 6000);
       //         mySnackbar.show();
               // WordListFragment wordListFragment = WordListFragment.newInstance();
              //  wordListFragment.show(getFragmentManager(), "hello");
      //      }
   //     });


       /* FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        WordListFragment wordListFragment = new WordListFragment();
        transaction.add(R.id.fragment_container,wordListFragment);
        transaction.commit();
        Button dictionary = (Button) findViewById(R.id.dictionary);
*/
      //  transaction.replace(R.id.dictionaryfragment2,mDictionaryFragment);

        Button button4 = (Button)findViewById(R.id.dictionary2);
        button4.setOnClickListener(
                new Button.OnClickListener() {
                    public void onClick(View v) {


        Snackbar mySnackbar = Snackbar.make(v,"Word Collected: little", 6000);
        mySnackbar.show();


                    }
                }
        );
        mParseTask.execute(kmlfile);
    }

    public void updateFromDownload(ArrayList<SongleKmlParser.Entry> entries) throws UnsupportedEncodingException, XmlPullParserException, IOException {
        Log.i("firstentry",entries.get(0).getCoordinate());
        thelist=entries;
        Log.i("entries.size()",entries.size()+"");

//        String[][] split =new String[3][entries.size()];
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
            marker.setTitle(entries.get(i).getName());
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
 //       String[] lines = global_lyrics


    }
    /*public class LyricsDownloadTask extends AsyncTask<String, Void, LyricsDownloadTask.Result {

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
*/




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
    public void onMapReady(GoogleMap googleMap) {
        Log.i(TAG,"OnMapReady");
        mMap = googleMap;


        // Add a marker in Sydney and move the camera
        LatLng centre_of_GS = new LatLng(55.944425, -3.188396);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(centre_of_GS, 15.2f));

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
