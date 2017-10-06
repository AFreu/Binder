package com.mobilecomputing.binder.Activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.text.TextRecognizer;
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

    private static final int RC_OCR_CAPTURE = 9003;
    private static final String TAG = "MainActivity";

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
        setScanMenu();
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
        else if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra(OcrCaptureActivity.TextBlockObject);
                    Log.d(TAG, "Text read: " + text);

                    fetchBookFromText(text);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            } else {
            }
        }
    }

    private void fetchBookFromText(String text) {
        
    }


    private void switchContent(String content){

        Fragment fragment;
        String title;

        switch (content){
            case "CardFragment":
                setScanMenu();
                fragment = cardFragment;
                title = getString(R.string.title_books);
                break;
            case "MatchesFragment":
                setMainMenu();
                fragment = matchesFragment;
                title = getString(R.string.title_matches);
                break;
            case "ProfileFragment":
                setMainMenu();
                fragment = profileFragment;
                title = getString(R.string.title_profile);
                break;
            default:
                setMainMenu();
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

            setScanMenu();
            setVisibilityOfSignIn();

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

    public void setMainMenu() {
        menu.clear();
        getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    public void setScanMenu() {
        menu.clear();
        getMenuInflater().inflate(R.menu.scan_menu, menu);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_scan:
                int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (rc == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, OcrCaptureActivity.class);
                    intent.putExtra(OcrCaptureActivity.AutoFocus, true);
                    intent.putExtra(OcrCaptureActivity.UseFlash, true);

                    startActivityForResult(intent, RC_OCR_CAPTURE);
                } else {
                    requestCameraPermission();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    /**
     * Handles the requesting of the camera permission.  This includes
     * showing a "Snackbar" message of why the permission is needed then
     * sending the request.
     */
    private static final int RC_HANDLE_CAMERA_PERM = 2;

    private void requestCameraPermission() {
        Log.w("TAG", "Camera permission is not granted. Requesting permission");

        final String[] permissions = new String[]{Manifest.permission.CAMERA};

        if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.CAMERA)) {
            ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
            return;
        }

        final Activity thisActivity = this;

        View.OnClickListener listener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ActivityCompat.requestPermissions(thisActivity, permissions,
                        RC_HANDLE_CAMERA_PERM);
            }
        };

        View view = findViewById(R.id.container);
        Snackbar.make(view, "permission_camera_rationale",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("ok", listener)
                .show();
    }
}
