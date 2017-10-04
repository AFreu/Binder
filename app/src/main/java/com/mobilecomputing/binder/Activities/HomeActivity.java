package com.mobilecomputing.binder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.mobilecomputing.binder.Fragments.CardFragment;
import com.mobilecomputing.binder.Fragments.MatchesFragment;
import com.mobilecomputing.binder.Fragments.ProfileFragment;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import com.mobilecomputing.binder.R;

public class HomeActivity extends BasicActivity implements GoogleApiClient.OnConnectionFailedListener {

    private TextView mTextMessage;
    private GoogleApiClient googleApiClient;
    private boolean isSignedIn = false;
    private static final int RC_SIGN_IN = 300;

    private Fragment profileFragment;
    private Fragment cardFragment;
    private Fragment matchesFragment;

    private SignInButton signInButton;

    private Menu menu;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {

            switch (item.getItemId()) {
                case R.id.navigation_books:
                    mTextMessage.setText(R.string.title_books);
                    switchContent("CardFragment");
                    return true;
                case R.id.navigation_matches:
                    mTextMessage.setText(R.string.title_matches);
                    switchContent("MatchesFragment");
                    return true;
                case R.id.navigation_profile:
                    mTextMessage.setText(R.string.title_profile);
                    switchContent("ProfileFragment");
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


        createFragments();

        // sets first fragment to booksfragment
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, cardFragment).commit();
        getSupportActionBar().setTitle(getString(R.string.title_books));

        initSignIn();

        setVisibilityOfSignIn();
    }

    private void initSignIn() {
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view -> {
            signInWithGoogle();
        });
    }

    private void createFragments() {
        profileFragment = new ProfileFragment();
        cardFragment = new CardFragment();
        matchesFragment = new MatchesFragment();
    }

    /**
     * Uses Google's Api to sign in, response in onActivityResult
     */
    private void signInWithGoogle() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }


    private void switchContent(String content){

        Fragment fragment;
        String title;

        switch (content){
            case "CardFragment":
                fragment = cardFragment;
                title = getString(R.string.title_books);
                break;
            case "MatchesFragment":
                fragment = matchesFragment;
                title = getString(R.string.title_matches);
                break;
            case "ProfileFragment":
                fragment = profileFragment;
                title = getString(R.string.title_profile);
                break;
            default:
                fragment = cardFragment;
                title = getString(R.string.title_books);
                break;
        }

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, fragment).commit();
        getSupportActionBar().setTitle(title);
    }

    private void handleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {
            GoogleSignInAccount acct = result.getSignInAccount();
            mTextMessage.setText("Signed in as " + acct.getDisplayName());
            isSignedIn = true;
            ((ProfileFragment)profileFragment).setUserAccount(acct);
            ((CardFragment)cardFragment).setUserAccount(acct);
        } else {
            isSignedIn = false;
            ((ProfileFragment)profileFragment).setUserAccount(null);
            ((CardFragment)cardFragment).setUserAccount(null);
        }

        setVisibilityOfSignIn();
    }


    /**
     * Hides sign-in button when user is signed in and shows it otherwise
     */
    public void setVisibilityOfSignIn() {
        signInButton.setVisibility(isSignedIn ? signInButton.INVISIBLE : signInButton.VISIBLE );
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }
}
