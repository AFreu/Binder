package com.mobilecomputing.binder.Activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.mobilecomputing.binder.R;

public abstract class BasicActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic);
    }
}
