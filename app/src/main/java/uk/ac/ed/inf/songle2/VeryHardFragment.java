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
 * {@link VeryHardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VeryHardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VeryHardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.fragment_very_hard, container, false);

        return rootView;
    }
}