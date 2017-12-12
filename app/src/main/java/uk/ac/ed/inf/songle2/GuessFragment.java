package uk.ac.ed.inf.songle2;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
        final String entry = getArguments().getString("global_entry");
        final String difficulty = getArguments().getString("difficulty");
//        this.getView().setBackgroundColor(Color.WHITE);
        final String[] entrysplit = entry.split("\\|\\|\\|");
        btnOpen.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), WinGameActivity.class);
                EditText editText = (EditText) rootView.findViewById(R.id.edit_text_guess);
                String answer = editText.getText().toString();
                if (answer.equals(entrysplit[2]) || answer.toLowerCase().equals(entrysplit[2].toLowerCase())) {
                    intent.putExtra("num",entrysplit[0]);
                    intent.putExtra("artist",entrysplit[1]);
                    intent.putExtra("title",entrysplit[2]);
                    intent.putExtra("url",entrysplit[3]);
                    intent.putExtra("difficulty",difficulty);
                    startActivity(intent);
                }
                else {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Incorrect! Try again")
                            .setCancelable(true)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {

                                }
                            })
                            .show();
                }
            }
        });

        return rootView;
    }
}

