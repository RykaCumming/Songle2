package uk.ac.ed.inf.songle2;

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
import android.view.View;
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

import org.xmlpull.v1.XmlPullParserException;


import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;



public class ScrollingActivity extends AppCompatActivity {

    SongleXmlParser songleXmlParser = new SongleXmlParser();
    ParseTask mParseTask= new ParseTask();
    ListView listView;
    List list = new ArrayList();
    ArrayAdapter adapter;

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ScrollingActivity.this, MainActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("             CHOOSE YOUR SONG");
        Intent intent = getIntent();
        String result = intent.getStringExtra("Resultxml");
        mParseTask.execute(result);
    }

    public static String spaces(String s){
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
        listView = findViewById(R.id.list_view); //recyclerview
        for (int i=0;i<parsedresult.size();i++)
        {
            if ((settings.getString(parsedresult.get(i).getNumber()+"NumberWin",null)!=null) && settings.getString(parsedresult.get(i).getNumber()+"NumberWin",null).equals(parsedresult.get(i).getNumber())) {
                list.add("SONG"+ " " + parsedresult.get(i).getNumber()+"\n" +
                        "ARTIST: "+parsedresult.get(i).getArtist()+"\n"+
                        "TITLE: " +parsedresult.get(i).getTitle()+"\n"+
                        "RECENTLY COMPLETED ON: "+settings.getString(parsedresult.get(i).getNumber()+"DifficultyWin",null));
            }
            else
            {
                list.add("SONG"+ " " + parsedresult.get(i).getNumber()+"\n" +
                        "ARTIST: "+spaces(parsedresult.get(i).getArtist())+"\n"+ //spaces before (parsed
                        "TITLE: " +spaces(parsedresult.get(i).getTitle()));       //spaces before (parsed
            }

        }

        adapter = new ArrayAdapter(ScrollingActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
                                    Log.i("aaaaaaaeeeee",kml_url_of_saved_data);
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
                            .setMessage("Timed Attack mode can only be played if you delete your previous data for this map. Is this ok?")
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
}
