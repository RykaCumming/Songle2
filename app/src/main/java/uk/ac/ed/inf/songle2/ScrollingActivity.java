package uk.ac.ed.inf.songle2;

import android.content.Intent;
import android.net.Network;
import android.os.AsyncTask;
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
    public void updateFromDownload(ArrayList<SongleXmlParser.Entry> parsedresult) throws UnsupportedEncodingException,XmlPullParserException,IOException  {
        Log.e("parsed",parsedresult.get(0).getNumber());


        listView = findViewById(R.id.list_view); //recyclerview
        for (int i=0;i<parsedresult.size();i++)
        {
   /*         if (i==0||i==2||i==3) {
                list.add("SONG"+ " " + parsedresult.get(i).getNumber()+"   Completed on Easy"+"\n" +
                        "ARTIST: "+parsedresult.get(i).getArtist()+"\n"+
                        "TITLE: " +parsedresult.get(i).getTitle());
            }
            else
            {*/
                list.add("SONG"+ " " + parsedresult.get(i).getNumber()+"\n" +
                        "ARTIST: "+(parsedresult.get(i).getArtist())+"\n"+ //spaces before (parsed
                        "TITLE: " +(parsedresult.get(i).getTitle()));       //spaces before (parsed
           // }

        }

        adapter = new ArrayAdapter(ScrollingActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ScrollingActivity.this,FivePageActivity.class);
                intent.putExtra("ScrollingActivity",listView.getItemAtPosition(position).toString());
   //             intent.putExtra("thesongnum",listView.getItemAtPosition(position).toString());
                startActivity(intent);

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
