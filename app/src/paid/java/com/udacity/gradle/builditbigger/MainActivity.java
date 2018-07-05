package com.udacity.gradle.builditbigger;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.jokedisplay.JokeDisplayActivity;
import com.udacity.gradle.builditbigger.IdlingResources.IdlingManager;

public class MainActivity extends AppCompatActivity {
    private static final int DELAY_MILLIS = 5000;
    private ProgressBar mLoader;

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
        mLoader.setVisibility(View.VISIBLE);

        //Toast.makeText(this, "derp", Toast.LENGTH_SHORT).show();
        final EndpointsAsyncTask endpointsAsyncTask = new EndpointsAsyncTask(this, new EndpointsAsyncTask.ResponseCallBack() {
            @Override
            public void response(String result) {

                mLoader.setVisibility(View.INVISIBLE);
                final Intent intent = new Intent(MainActivity.this, JokeDisplayActivity.class);
                intent.putExtra(JokeDisplayActivity.JOKE_KEY, result);
                if (mIdlingManager != null) {
                    mIdlingManager.setIdleState(true);
                }
                if (intent.resolveActivity(getApplicationContext().getPackageManager()) != null) {
                    startActivity(intent);
                }
            }
        }, mIdlingManager);

        //simulate a delay of server response
        new Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        endpointsAsyncTask.execute();
                    }
                }, DELAY_MILLIS);
    }
}
