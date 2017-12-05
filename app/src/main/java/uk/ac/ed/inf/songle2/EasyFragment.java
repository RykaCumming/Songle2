package uk.ac.ed.inf.songle2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.support.v4.app.Fragment;
import android.widget.Button;import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;



public class EasyFragment extends Fragment {
    String num;

    public static EasyFragment newInstance(String num){
        EasyFragment easyFragment = new EasyFragment();
        Bundle args = new Bundle();
        args.putString("num", num);
        easyFragment.setArguments(args);
        return easyFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        num = bundle.getString("num");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_easy, container, false);

        final String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+ num +"/map5.kml";
        final String lyricsurl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+ num + "/words.txt";
        Button btnOpen = (Button) view.findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NetworkActivity.class);
                intent.putExtra("file", url);
                intent.putExtra("lyrics", lyricsurl);
                startActivity(intent);
            }
        });

        return view;

    }

}

