package uk.ac.ed.inf.songle2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;


public class DictionaryFragment extends AppCompatActivity {
/*
    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_dictionary);
        Log.e("Scrolling","made it");
        Intent intent = getIntent();
        String result = intent.getStringExtra("Resultxml");
        listView = findViewById(R.id.list_view); //recyclerview
        for (int i=0;i<3;i++)
        {
            list.add("SONG");
        }

        adapter = new ArrayAdapter(ScrollingActivity.this,android.R.layout.simple_list_item_1,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Intent intent = new Intent(ScrollingActivity.this,FivePageActivity.class);
                intent.putExtra("ScrollingActivity",listView.getItemAtPosition(position).toString());
                startActivity(intent);
                //view.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
            }
        });

    }

    } */
}
