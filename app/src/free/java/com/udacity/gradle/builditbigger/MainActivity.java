package com.udacity.gradle.builditbigger;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.jokedisplay.JokeDisplayActivity;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.udacity.gradle.builditbigger.IdlingResources.IdlingManager;


public class MainActivity extends AppCompatActivity {
    private static final int DELAY_MILLIS = 5000;
    private ProgressBar mLoader;
    private InterstitialAd mInterstitialAd;

    @Nullable
    private IdlingManager mIdlingManager;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingManager == null) {
            mIdlingManager = new IdlingManager();
        }
        return mIdlingManager;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLoader = findViewById(R.id.loader);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId("ca-app-pub-3940256099942544/1033173712");


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void tellJoke(View view) {
        Log.d("mainActivity", "loader visible");
        mLoader.setVisibility(View.VISIBLE);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        //Toast.makeText(this, "derp", Toast.LENGTH_SHORT).show();
        final EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask(this, new EndpointsAsyncTask.ResponseCallBack() {
            @Override
            public void response(String result) {

                mLoader.setVisibility(View.INVISIBLE);
                Log.d("mainActivity", "loader gone");
                final Intent intent = new Intent(MainActivity.this, JokeDisplayActivity.class);
                intent.putExtra(JokeDisplayActivity.JOKE_KEY, result);

                mInterstitialAd.setAdListener(new AdListener(){
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();
                        if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                            startActivity(intent);
                        }
                    }
                });
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                    if (mIdlingManager != null) {
                        mIdlingManager.setIdleState(true);
                    }
                }
            }
        }, mIdlingManager);

        //simulate a delay of server response
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {

                        endpointsAsyncTask.execute(new Pair<Context, String>(getApplicationContext(), "Manfred"));
                    }
                }, DELAY_MILLIS);
    }


}
