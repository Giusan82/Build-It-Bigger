package com.example.android.jokedisplay;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokeDisplayActivity extends AppCompatActivity {
    public static String JOKE_KEY = "Joke key";
    private TextView mJokes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joke_display);

        Intent intent = getIntent();
        String joke = intent.getStringExtra(JOKE_KEY);
        mJokes = (TextView) findViewById(R.id.tv_jokes);
        if (joke != null && joke.length() != 0) {
            mJokes.setText(joke);
        }
    }
}
