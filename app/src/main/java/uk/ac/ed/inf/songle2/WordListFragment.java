package uk.ac.ed.inf.songle2;

import android.app.DialogFragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by s1540547 on 07/11/17.
 */

public class WordListFragment extends DialogFragment {
    public static WordListFragment newInstance(){
        WordListFragment wordListFragment = new WordListFragment();
        return wordListFragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_word_list, container, false);
        return view;
    }
}

