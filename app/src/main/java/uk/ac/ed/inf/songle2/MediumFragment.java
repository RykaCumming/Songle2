package uk.ac.ed.inf.songle2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;



public class MediumFragment extends Fragment {

    private String num;
    private String global_entry;
    public static MediumFragment newInstance(String num,String entry){
        MediumFragment mediumFragment = new MediumFragment();
        Bundle args = new Bundle();
        args.putString("num", num);
        args.putString("entry",entry);
        mediumFragment.setArguments(args);
        return mediumFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        num = bundle.getString("num");
        global_entry=bundle.getString("entry");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medium, container, false);
        final String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+num+"/map3.kml";
        final String lyricsurl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+num+"/words.txt";
        Button btnOpen = (Button) view.findViewById(R.id.btnOpen);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NetworkActivity.class);
                intent.putExtra("file", url);
                intent.putExtra("entry",global_entry);
                startActivity(intent);
            }
        });
        return view;

    }
}