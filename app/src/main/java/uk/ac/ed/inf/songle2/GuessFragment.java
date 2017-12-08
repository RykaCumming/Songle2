package uk.ac.ed.inf.songle2;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.EditText;

public class GuessFragment extends DialogFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.guessword_fragment, container, false);

        Button btnOpen = (Button) rootView.findViewById(R.id.btnOpen);
        btnOpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WinGameActivity.class);
                EditText editText = (EditText) rootView.findViewById(R.id.edit_text_guess);
                String answer = editText.getText().toString();
                if (answer.equals(FivePageActivity.glob_title) || answer.toLowerCase().equals(FivePageActivity.glob_title.toLowerCase())) {
                    startActivity(intent);
                }
                else {

                }

            }
        });

        return rootView;
    }
}

