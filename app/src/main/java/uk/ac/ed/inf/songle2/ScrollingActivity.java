package uk.ac.ed.inf.songle2;
import android.content.Context;
import android.graphics.Color;
import android.support.annotation.VisibleForTesting;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Network;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;



import android.util.Log;
import android.webkit.WebView;
import android.widget.EditText;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

@VisibleForTesting
public class ScrollingActivity extends AppCompatActivity {

    SongleXmlParser songleXmlParser = new SongleXmlParser();
    ParseTask mParseTask= new ParseTask();
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;

    @Override
    public void onBackPressed() {//back pressed must take user to main activity
        Intent intent = new Intent(ScrollingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        Intent intent = getIntent();
        String result = intent.getStringExtra("Resultxml");
        //^ the downloaded xml string
        mParseTask.execute(result);
        //^ perform async parsing task the xml file to turn it into a useable form
    }

    public static String spaces(String s){ //this method turns each letter into an asterisk. Used when populating listview
        String result ="";
        for (char c : s.toCharArray())
        {
            if (c == ' ')
            {
                result=result+" ";
            }
            else
            {
                result=result+"*";
            }
        }
        return result;
    }

    public void updateFromDownload(final ArrayList<SongleXmlParser.Entry> parsedresult) throws UnsupportedEncodingException,XmlPullParserException,IOException  {
        Log.i("parsed",parsedresult.get(0).getNumber());
        final SharedPreferences settings = getSharedPreferences("mySettings",MODE_PRIVATE);
//        final String kml_url_of_saved_data= sharedPref.getString(parsedresult.get(position).getNumber()+"_kml_url",null);
        listView = findViewById(R.id.list_view);
        int num_of_songs_completed_so_far=0;
        for (int i=0;i<parsedresult.size();i++) //iterate over every entry
        {
            if ((settings.getString(parsedresult.get(i).getNumber()+"NumberWin",null)!=null)) //If data with key"03NumberWin" is null, it means the user has NOT guessed the third song so we go to the else
           {
               num_of_songs_completed_so_far++;
               //this is the case where the user has correct guessed song i+1. Show them the details of this song
                list.add("SONG"+ " " + parsedresult.get(i).getNumber()+"\n" +
                        "ARTIST: "+parsedresult.get(i).getArtist()+"\n"+
                        "TITLE: " +parsedresult.get(i).getTitle()+"\n"+
                        "RECENTLY COMPLETED ON: "+settings.getString(parsedresult.get(i).getNumber()+"DifficultyWin",null));
                        //^ Shows the user the difficulty they completed the song on most recently
            }
            else //case where user has NOT completed song i+1
            {
                //simply replaces the title and artist with asterisks
                list.add("SONG"+ " " + parsedresult.get(i).getNumber()+"\n" +
                        "ARTIST: "+spaces(parsedresult.get(i).getArtist())+"\n"+
                        "TITLE: " +spaces(parsedresult.get(i).getTitle()));
            }

        }
        //keeps track of how many songs have been guessed correctly so far
        SharedPreferences.Editor editor = settings.edit();
        if (num_of_songs_completed_so_far>=15) {
            editor.putString("15_or_more_songs","15_or_more_songs").apply();
        }
        else if(num_of_songs_completed_so_far>=3 &&num_of_songs_completed_so_far<15)
        {
            editor.putString("3_or_more_songs","3_or_more_songs").apply();
        }
        else {

        }
        // an adapter linking the arraylist to the listview.
        adapter = new ArrayAdapter(ScrollingActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() { //Responding to clicking a position
            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
                final SharedPreferences sharedPref = getSharedPreferences("mySettings",MODE_PRIVATE);
                final String kml_url_of_saved_data= sharedPref.getString(parsedresult.get(position).getNumber()+"_kml_url",null);
                final String parsedresultstring = parsedresult.get(position).getNumber() + "|||" +
                        parsedresult.get(position).getArtist() + "|||" +
                        parsedresult.get(position).getTitle() + "|||" +
                        parsedresult.get(position).getLink();
                final SharedPreferences sharedPref2 = PreferenceManager.getDefaultSharedPreferences(ScrollingActivity.this);
                boolean timer_allowed = sharedPref2.getBoolean("key_pref_timer",false);

                if (sharedPref.getStringSet(parsedresult.get(position).getNumber(), null)!=null &&!timer_allowed) {

                    new AlertDialog.Builder(ScrollingActivity.this)
                            .setMessage("You have previously stored data on this song")
                            .setCancelable(true)
                            .setPositiveButton("Resume Game", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    Intent intent = new Intent(ScrollingActivity.this, NetworkActivity.class);
                                    intent.putExtra("file",kml_url_of_saved_data);
                                    intent.putExtra("entry",parsedresultstring);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("New Game", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sharedPref.edit().remove(parsedresult.get(position).getNumber()).commit();; // The song number corresponding to the position on listview e.g. "04"
                                    sharedPref.edit().remove(parsedresult.get(position).getNumber()+"_kml_url").commit();
                                    Intent intent = new Intent(ScrollingActivity.this, FivePageActivity.class);
                                    intent.putExtra("ScrollingActivity", parsedresultstring);
                                    startActivity(intent);
                                }
                            })
                            .show();
                }
                else if((sharedPref.getStringSet(parsedresult.get(position).getNumber(), null)!=null)&&timer_allowed)
                {
                    new AlertDialog.Builder(ScrollingActivity.this)
                            .setMessage("Timed Attack mode can only be played if you delete your previous data for this map. If you have already completed this song, however, this will still be kept track of. Is this ok?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sharedPref.edit().remove(parsedresult.get(position).getNumber()).commit();; // The song number corresponding to the position on listview e.g. "04"
                                    sharedPref.edit().remove(parsedresult.get(position).getNumber()+"_kml_url").commit();
                                    Intent intent = new Intent(ScrollingActivity.this, FivePageActivity.class);
                                    intent.putExtra("ScrollingActivity", parsedresultstring);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();
                }
                else {
                    Intent intent = new Intent(ScrollingActivity.this, FivePageActivity.class);
                    intent.putExtra("ScrollingActivity", parsedresultstring);
                    startActivity(intent);
                }
            }
        });
    }
    public class ParseTask extends AsyncTask<String, Void, ArrayList<SongleXmlParser.Entry>> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected ArrayList<SongleXmlParser.Entry> doInBackground(String... res) { //        mParseTask.execute(result);

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
    public class CustomListAdapter extends ArrayAdapter <String> {

        private Context mContext;
        private int id;
        private List <String>items ;

        public CustomListAdapter(Context context, int textViewResourceId , List<String> list )
        {
            super(context, textViewResourceId, list);
            mContext = context;
            id = textViewResourceId;
            items = list ;
        }

        @Override
        public View getView(int position, View v, ViewGroup parent)
        {
            View mView = v ;
            if(mView == null){
                LayoutInflater vi = (LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                mView = vi.inflate(id, null);
            }

            TextView text = (TextView) mView.findViewById(R.id.textView);

            if(items.get(position) != null )
            {
                text.setTextColor(Color.WHITE);
                text.setText(items.get(position));
                text.setBackgroundColor(Color.RED);
                int color = Color.argb( 200, 255, 64, 64 );
                text.setBackgroundColor( color );

            }

            return mView;
        }

    }
}
