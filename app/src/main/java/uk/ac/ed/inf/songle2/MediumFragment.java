package uk.ac.ed.inf.songle2;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MediumFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MediumFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MediumFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_medium, container, false);
        final String url = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+FivePageActivity.songno+"/map3.kml";
        final String lyricsurl = "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/"+FivePageActivity.songno+"/words.txt";
        Button btnOpen = (Button) view.findViewById(R.id.btnOpen);

        btnOpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NetworkActivity.class);
                intent.putExtra("file", url);
                startActivity(intent);
            }
        });
        return view;

    }
}