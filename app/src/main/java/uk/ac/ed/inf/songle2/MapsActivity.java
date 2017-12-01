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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        String kmlfile = intent.getStringExtra("Resultkml");
        Log.e("thekmlfile",kmlfile);
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
/*        splits=split;
        Log.i("Shouldbezero",split[2][0]);
        Log.i("Shouldbezero",split[2][1]);
        Log.i("Shouldbezero",split[2][2]);
        Log.i("Shouldbezero",split[2][3]);
        Log.i("-3something",split[0][0]);
        Log.i("-3something",split[0][1]);
        Log.i("-3something",split[0][2]);
        Log.i("-3something",split[0][3]);
        Log.i("55something",split[1][0]);
        Log.i("55something",split[1][1]);
        Log.i("55something",split[1][2]);
        Log.i("55something",split[1][3]);
*/



    }

    public static void largeLog(String tag, String content) {
        if (content.length() > 4000) {
            Log.d(tag, content.substring(0, 4000));
            largeLog(tag, content.substring(4000));
        } else {
            Log.d(tag, content);
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
        LatLng sydney = new LatLng(55.944425, -3.188396);

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15.2f));

//        LatLng[] points =new LatLng[splits.length];
  //      for


 /*       mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94411695607683, -3.1867307741115662))); //additional point I added
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94481695607683, -3.1869307741115662)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94482456601353, -3.1885226965749776)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94444913310533, -3.186669178092598)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94571668647767, -3.1874999403193094)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94391668532715, -3.189825070523909)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94573429161991, -3.187187260225937)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94417006114126, -3.188807149746576)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94613465373285, -3.185103876507775)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94396512441551, -3.1874897837202845)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94270279892545, -3.1856599224745814)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94401210140577, -3.1908871748006895)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.9442291690531, -3.191116855341561)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94475708966015, -3.1893688197156913)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944562205738706, -3.1917985065734995)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944755855610026, -3.18905518613561)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94504109525317, -3.18877456837294)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94525245192859, -3.1858537017642288)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94361049644465, -3.189740311635996)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.9437487913566, -3.186230285249606)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94591750325367, -3.188769840090384)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94286354848715, -3.191891442993622)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94525201566233, -3.1878844625314344)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943490577571204, -3.1850344000135116)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94375942190045, -3.187515862502797)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94607416560725, -3.1891372827587956)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.9446636265689, -3.186816117267603)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943850611691154, -3.192398084063591)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.942893410688725, -3.1854827168124915)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94283547969892, -3.187408013737283)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94267356941682, -3.188294207776208)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.9456170055639, -3.18782721494843)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94480653530339, -3.1886110940472916)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.9436069560896, -3.190311910619552)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943409127168955, -3.1912563504711855)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94472682396092, -3.184847250480333)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.942861826759156, -3.185871628317892)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944445437911206, -3.1864560396550985)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.9429533684282, -3.1897034445044308)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943754670538574, -3.185114425134018)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94612447282848, -3.18948608333086)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944987623438664, -3.1887004116575923)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94283966838679, -3.187408711242392)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94448688151412, -3.1919283891876478)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944608010121115, -3.18843335209067)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94408489549409, -3.191201211134933)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94331734446388, -3.1897677588328905)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943781318466385, -3.1892606557793326)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94472465805409, -3.185907786743285)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94265126494643, -3.1878100940357865)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943805418754216, -3.1847048014644153)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94569463034291, -3.1893760512724585)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.9446416547813, -3.1911118561344534)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94612728568721, -3.189984277497575)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94306240934491, -3.1888005476795156)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.946113027146154, -3.185131569638941)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.945674032934704, -3.186912943067385)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94421472261792, -3.1906940927083247)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94427602258377, -3.1910545560209065)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94418169961343, -3.189138348525384)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94352647608667, -3.189609793688234)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94564428626619, -3.189482741779467)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94336398581892, -3.1911436055648146)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944333790228214, -3.185208121773638)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943533154659804, -3.18893025205708)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943531255269754, -3.1855702795123224)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944847836448474, -3.188482674721149)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94512789058467, -3.186764926659653)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944543450647714, -3.1847355509775004)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94374439593647, -3.1844015439031796)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94322110413157, -3.1884767332798356)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.944396400177304, -3.1913869343440475)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94268211206872, -3.1855422707538197)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94470171298357, -3.189481857004818)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94414514066531, -3.1875172628798887)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94568194727527, -3.1888379190971925)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94267397384104, -3.1914606474883374)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.945000889430816, -3.190065533652383)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94290382022087, -3.1871490441895878)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.943631892989316, -3.1915574202944623)));
        mMap.addMarker(new MarkerOptions().icon(BitmapDescriptorFactory.fromResource(R.mipmap.wht_blank)).position(new LatLng(55.94486283928077, -3.186439542156783)));
*/

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
