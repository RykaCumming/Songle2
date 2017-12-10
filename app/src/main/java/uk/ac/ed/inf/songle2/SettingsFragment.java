package uk.ac.ed.inf.songle2;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import static android.content.Context.MODE_PRIVATE;


public class SettingsFragment extends PreferenceFragment {
    @Override

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Load the preferences from an XML resource
        addPreferencesFromResource(R.xml.pref_notification);
        final SharedPreferences sharedPref = this.getActivity().getSharedPreferences("mySettings",MODE_PRIVATE);
        Preference button = (Preference) getPreferenceManager().findPreference("key_pref_erase");
        if (button !=null) {
            button.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
                @Override
                public boolean onPreferenceClick(Preference preference) {
                    new AlertDialog.Builder(getActivity())
                            .setMessage("Are you sure?")
                            .setCancelable(true)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    sharedPref.edit().clear().commit();
                                }
                            })
                            .setNegativeButton("No", null)
                            .show();


                    return true;
                }
            });
        }
    }
}