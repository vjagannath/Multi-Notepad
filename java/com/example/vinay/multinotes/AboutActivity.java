package com.example.vinay.multinotes;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by vinay on 2/24/2017.
 */

public class AboutActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about_activity);

        // hide the left caret at the top of the activity
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
    }
}
