package uk.ac.ed.inf.songle2;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


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
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_medium, container, false);

        return rootView;
    }
}