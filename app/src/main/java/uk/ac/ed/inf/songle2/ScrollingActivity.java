package uk.ac.ed.inf.songle2;

import android.content.Intent;
import android.net.Network;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.io.ByteArrayInputStream;import android.Manifest;
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


public class ScrollingActivity extends AppCompatActivity {

    SongleXmlParser songleXmlParser = new SongleXmlParser();
    ParseTask mParseTask= new ParseTask();
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling2);
        Log.e("Scrolling","made it");
        Intent intent = getIntent();
        String result = intent.getStringExtra("Resultxml");


        mParseTask.execute(result);




    }
    public void updateFromDownload(ArrayList<SongleXmlParser.Entry> parsedresult) throws UnsupportedEncodingException,XmlPullParserException,IOException  {
        Log.e("parsed",parsedresult.get(0).getNumber());
        listView = findViewById(R.id.list_view); //recyclerview
        for (int i=0;i<parsedresult.size();i++)
        {
            list.add("SONG"+ " " + parsedresult.get(i).getNumber());
        }

        adapter = new ArrayAdapter(ScrollingActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                //view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });
    }
    public class ParseTask extends AsyncTask<String, Void, ArrayList<SongleXmlParser.Entry>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<SongleXmlParser.Entry> doInBackground(String... res) {

            String res1 = res[0];
            InputStream stream = null;

            try {
                stream = new ByteArrayInputStream(res1.getBytes(StandardCharsets.UTF_8.name()));
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            ArrayList<SongleXmlParser.Entry> entries = new ArrayList<>();
            try {
                entries = songleXmlParser.parse(stream);
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return entries;
        }
        @Override
        protected void onPostExecute(ArrayList<SongleXmlParser.Entry> entries) {
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
}
