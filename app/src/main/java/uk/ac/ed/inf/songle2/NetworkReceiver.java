package uk.ac.ed.inf.songle2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Network;

import com.google.android.gms.common.ConnectionResult;

/**
 * Created by s1540547 on 15/10/17.
 */

public class NetworkReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        ConnectivityManager connMgr = (ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        /**
        if (networkPref.equals(WIFI) && networkInfo != null && networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {

        } else if (networkPref.equals(ANY) && networkInfo != null) {

        } else {
        }
         */


    }
}
