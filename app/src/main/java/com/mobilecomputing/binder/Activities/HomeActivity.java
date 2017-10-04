package com.mobilecomputing.binder.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.MenuItem;
import android.widget.TextView;

import com.mobilecomputing.binder.Fragments.CardFragment;
import com.mobilecomputing.binder.Fragments.MatchesFragment;
import com.mobilecomputing.binder.Fragments.ProfileFragment;
import com.mobilecomputing.binder.R;

public class HomeActivity extends BasicActivity {

    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_books:
                    mTextMessage.setText(R.string.title_books);
                    switchToFragment("CardFragment");
                    return true;
                case R.id.navigation_matches:
                    mTextMessage.setText(R.string.title_matches);
                    switchToFragment("MatchesFragment");
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    switchToFragment("ProfileFragment");
                    return true;
            }

            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mTextMessage = (TextView) findViewById(R.id.message);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }


    private void switchToFragment(String fragment){

        Fragment frag;

        switch (fragment){
            case "CardFragment":
                frag = new CardFragment();
                break;
            case "MatchesFragment":
                frag = new MatchesFragment();
                break;
            case "ProfileFragment":
                frag = new ProfileFragment();
                break;
            default:
                frag = new CardFragment();
                break;
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.container, frag).commit();
    }

}
