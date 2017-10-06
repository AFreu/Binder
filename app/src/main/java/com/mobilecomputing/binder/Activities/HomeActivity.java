package com.mobilecomputing.binder.Activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.mobilecomputing.binder.Fragments.CardFragment;
import com.mobilecomputing.binder.Fragments.MatchesFragment;
import com.mobilecomputing.binder.Fragments.ProfileFragment;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ImageAdapter;

import jp.wasabeef.blurry.Blurry;

public class HomeActivity extends BasicActivity implements GoogleApiClient.OnConnectionFailedListener {

    private GridView gridView;
    private LinearLayout appBody;
    private GoogleApiClient googleApiClient;
    private boolean isSignedIn = false;
    private static final int RC_SIGN_IN = 300;

    private Fragment profileFragment;
    private Fragment cardFragment;
    private Fragment matchesFragment;

    private SignInButton signInButton;
    private Button signInButtonNoGoogle;
    private RelativeLayout signInBackground;

    private Menu menu;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {

                switch (item.getItemId()) {
                    case R.id.navigation_books:
                        switchContent("CardFragment");
                        return true;
                    case R.id.navigation_matches:
                        switchContent("MatchesFragment");
                        return true;
                    case R.id.navigation_profile:
                        switchContent("ProfileFragment");
                        return true;
                }

                return false;
            };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Toolbar toolBar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolBar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        appBody = (LinearLayout) findViewById(R.id.app_body);
        ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.image_layout);
        gridView = (GridView) findViewById(R.id.background_grid);
        gridView.setAdapter(imageAdapter);
        gridView.setOnTouchListener((v, event) -> event.getAction() == MotionEvent.ACTION_MOVE);

        createFragments();
        // sets first fragment to booksfragment
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, cardFragment).commit();
        getSupportActionBar().setTitle(getString(R.string.title_books));

        initSignIn();

        setVisibilityOfSignIn();

        RelativeLayout back = (RelativeLayout) findViewById(R.id.home_gradient_background);
        // blurs the background on sign in
        back.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                back.getViewTreeObserver().removeOnPreDrawListener(this);

                Blurry.with(HomeActivity.this).radius(25)
                        .sampling(2)
                        .async()
                        .animate(300)
                        .onto((ViewGroup)back.getParent());
                return false;
            }
        });
    }

    private void initSignIn() {
        appBody.setVisibility(View.INVISIBLE);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        signInBackground = (RelativeLayout) findViewById(R.id.sign_in_background);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view -> {
            signInWithGoogle();
        });

        signInButtonNoGoogle = (Button) findViewById(R.id.sign_in_button_noGoogle);
        signInButtonNoGoogle.setOnClickListener(view -> {
            mockSignIn();
        });
    }

    private void createFragments() {
        profileFragment = new ProfileFragment();
        cardFragment = new CardFragment();
        matchesFragment = new MatchesFragment();
    }

    private void mockSignIn() {

        isSignedIn = true;
        ((ProfileFragment)profileFragment).setUserAccount(null);
        ((CardFragment)cardFragment).setUserAccount(null);
        setVisibilityOfSignIn();
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

            setVisibilityOfSignIn();
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
            isSignedIn = true;
            ((ProfileFragment)profileFragment).setUserAccount(acct);
            ((CardFragment)cardFragment).setUserAccount(acct);

            // blurs the background on sign in
            /*gridView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
                @Override
                public boolean onPreDraw() {
                    gridView.getViewTreeObserver().removeOnPreDrawListener(this);

                    Blurry.with(HomeActivity.this).radius(8)
                            .sampling(2)
                            .async()
                            .animate(300)
                            .onto((ViewGroup)gridView.getParent());
                    return false;
                }
            });*/

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
        signInBackground.setVisibility(isSignedIn ? signInButton.INVISIBLE : signInButton.VISIBLE );
        appBody.setVisibility(isSignedIn ? signInButton.VISIBLE : signInButton.INVISIBLE );
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
