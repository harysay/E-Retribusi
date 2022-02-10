package id.go.kebumenkab.retribusipasar;

import androidx.appcompat.app.AppCompatActivity;
import id.go.kebumenkab.retribusipasar.handler.SessionManager;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;

public class SplashActivity extends AppCompatActivity {
    public boolean isFirstStart;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        session = new SessionManager(getApplication());
        if (CheckNetwork.isInternetAvailable(SplashActivity.this))
        {
            if (!session.isLoggedIn()) {
                Thread t = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //  Intro App Initialize SharedPreferences
                        SharedPreferences getSharedPreferences = PreferenceManager
                                .getDefaultSharedPreferences(getBaseContext());

                        //  Create a new boolean and preference and set it to true
                        isFirstStart = getSharedPreferences.getBoolean("firstStart", true);

                        //  Check either activity or app is open very first time or not and do action
                        if (isFirstStart) {

                            //  Launch application introduction screen
                            Intent i = new Intent(SplashActivity.this, Prasyarat.class);
                            startActivity(i);
                            SharedPreferences.Editor e = getSharedPreferences.edit();
                            e.putBoolean("firstStart", false);
                            e.apply();
                        }
                    }
                });
                t.start();
                // user is not logged in redirect him to Login Activity
                Intent i = new Intent(SplashActivity.this, LoginActivity.class);

                // Closing all the Activities
                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                // Add new Flag to start new Activity
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                // Staring Login Activity
                startActivity(i);
                finish();

            } else {
                Intent i = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(i);
            }

        }else {
            try {
                AlertDialog alertDialog = new AlertDialog.Builder(SplashActivity.this).create();

                alertDialog.setTitle("Tidak ada koneksi internet!");
                alertDialog.setMessage("Cek koneksi internet Anda dan ulangi lagi");
                alertDialog.setIcon(android.R.drawable.stat_sys_warning);
                alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int n) {
                        dialog.cancel();
                    }
                });
                alertDialog.show();
            } catch (Exception e) {
                //Log.d(Constants.TAG, "Show Dialog: "+e.getMessage());
            }
        }
    }
}