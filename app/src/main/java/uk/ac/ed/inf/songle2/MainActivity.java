package uk.ac.ed.inf.songle2;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    @Override
    public void onBackPressed()
    {
        // exit app regardless of position in stack
        moveTaskToBack(true);
        finish();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Intent intent =getIntent();
        if (intent.getStringExtra("NetworkActivity")!=null) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), "No internet connection.", Snackbar.LENGTH_LONG);
        snackbar.show();

        }


    }
/*    ConnectivityManager cm =
            (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);

    NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
    boolean isConnected = activeNetwork != null &&
            activeNetwork.isConnectedOrConnecting(); */

    public void sendMessage(View view) {

  //      if (!isConnected){
        //    Snackbar mySnackbar = Snackbar.make(view,"There is no internet connection", 2500);
           // mySnackbar.show();
   //     }
    //    else {

            Intent intent = new Intent(this, NetworkActivity.class);
            intent.putExtra("file", "http://www.inf.ed.ac.uk/teaching/courses/selp/data/songs/songs.xml");
            startActivity(intent);
     //   }
    }
    public void HowToPlayMessage(View view) {
        Intent intent = new Intent(this, HowToPlayActivity.class);
        startActivity(intent);
    }
    public void SettingsMessage(View view) {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }


}
