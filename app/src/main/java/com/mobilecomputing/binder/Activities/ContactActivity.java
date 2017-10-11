package com.mobilecomputing.binder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.GridView;

import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ContactPagerAdapter;
import com.mobilecomputing.binder.Utils.ImageAdapter;


public class ContactActivity extends BasicActivity {

    private String TAG = "ContactActivity";

    private GridView gridView;
    private ContactPagerAdapter mContactPagerAdapter;
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        Intent intent = getIntent();
        Match contact = (Match)intent.getSerializableExtra("contact");

        gridView = (GridView) findViewById(R.id.background_grid);
        ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.image_layout);
        imageAdapter.setLessInfo();
        imageAdapter.mockData();
        gridView.setAdapter(imageAdapter);
        gridView.setOnTouchListener((v, event) -> event.getAction() == MotionEvent.ACTION_MOVE);

        Toolbar toolBar = (Toolbar)findViewById(R.id.contact_toolbar);
        toolBar.setTitle("Match");
        setSupportActionBar(toolBar);
        mContactPagerAdapter = new ContactPagerAdapter(getSupportFragmentManager(), this, contact);

        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mContactPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);



    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact, menu);
        return true;
    }
}
