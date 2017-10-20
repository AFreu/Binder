package com.mobilecomputing.binder.Activities;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
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
import com.google.android.gms.vision.barcode.Barcode;
import com.google.gson.Gson;
import com.mobilecomputing.binder.Fragments.CardFragment;
import com.mobilecomputing.binder.Fragments.MatchesFragment;
import com.mobilecomputing.binder.Fragments.ProfileFragment;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ImageAdapter;
import com.mobilecomputing.binder.Objects.User;
import com.mobilecomputing.binder.Views.NewMatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.util.stream.Collectors.toList;

public class HomeActivity extends BasicActivity
        implements GoogleApiClient.OnConnectionFailedListener,
        ProfileFragment.ProfileFragmentListener,
        CardFragment.CardFragmentListener {

    private User user;
    private GridView gridView;
    private LinearLayout appBody;
    private GoogleApiClient googleApiClient;
    private boolean isSignedIn = false;
    private static final int RC_SIGN_IN = 300;
    public static List<String> allGenres = new ArrayList<>();

    private List<Book> likedBooks = new ArrayList<>();
    private List<Book> dislikedBooks = new ArrayList<>();
    private List<Book> topList = new ArrayList<>();

    private List<Match> matches = new ArrayList<>();

    private Fragment profileFragment;
    private Fragment cardFragment;
    private Fragment matchesFragment;

    private SignInButton signInButton;
    private Button signInButtonNoGoogle;
    private RelativeLayout signInBackground;

    private static final int RC_OCR_CAPTURE = 9003;
    private static final int RC_BARCODE_CAPTURE = 9001;

    private static final String TAG = "MainActivity";

    private static final int CHOOSE_BOOK_ACTIVITY = 1435;

    private int matchCounter = 4;


    private Menu menu;
    private SharedPreferences sharedPreferences;

    private String idFragment = "CardFragment";
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
                switch (item.getItemId()) {
                    case R.id.navigation_books:
                        idFragment = "CardFragment";
                        switchContent("CardFragment");
                        return true;
                    case R.id.navigation_matches:
                        idFragment = "MatchesFragment";
                        switchContent("MatchesFragment");
                        return true;
                    case R.id.navigation_profile:
                        idFragment = "ProfileFragment";
                        switchContent("ProfileFragment");
                        return true;
                }

                return false;
            };

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        addAllGenres();
        addAllMatches();
        initGoogleApiClient();

        initUI();

        createFragments();
        loadLikesAndDisliked();
        loadIgnoreGenres();
        loadMatches();

        // sets first fragment to booksfragment
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.content, cardFragment).commit();
        // recovering the instance state
        if (savedInstanceState != null) {
            idFragment = savedInstanceState.getString("SelectedFragment");
            Book book = (Book)savedInstanceState.getSerializable("likedBooks");
            likedBooks = book.getBookList();
            Book book2 = (Book)savedInstanceState.getSerializable("dislikedBooks");
            dislikedBooks = book2.getBookList();
            Book book3 = (Book)savedInstanceState.getSerializable("topList");
            setTopList(book3.getBookList());
            ((MatchesFragment) matchesFragment).setLikedBooks(likedBooks);
            ((ProfileFragment) profileFragment).setLikedBooks(likedBooks);
            ((CardFragment) cardFragment).setLikedBooks(likedBooks);
            ((CardFragment) cardFragment).setDislikedBooks(dislikedBooks);

            switchContent(idFragment);
        }

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString("SelectedFragment", idFragment);
        Book book = new Book();
        book.setBookList(likedBooks);
        outState.putSerializable("likedBooks", book);
        Book book2 = new Book();
        book2.setBookList(dislikedBooks);
        outState.putSerializable("dislikedBooks", book2);
        Book book3 = new Book();
        book3.setBookList(topList);
        outState.putSerializable("topList", book3);
        // call superclass to save any view hierarchy
        super.onSaveInstanceState(outState);
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }

    public void initUI() {
        Toolbar toolBar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolBar);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        appBody = (LinearLayout) findViewById(R.id.app_body);
        ImageAdapter imageAdapter = new ImageAdapter(this, R.layout.image_layout);
        gridView = (GridView) findViewById(R.id.background_grid);
        imageAdapter.hideActionArea();
        imageAdapter.mockData();
        gridView.setAdapter(imageAdapter);
        gridView.setOnTouchListener((v, event) -> event.getAction() == MotionEvent.ACTION_MOVE);

        signInBackground = (RelativeLayout) findViewById(R.id.sign_in_background);

        signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(view -> signInWithGoogle());

        signInButtonNoGoogle = (Button) findViewById(R.id.sign_in_button_noGoogle);
        signInButtonNoGoogle.setOnClickListener(view -> mockSignIn());
    }

    /**
     * Called after onCreate
     * Checks if user is signed in..
     */
    @Override
    protected void onStart () {
        super.onStart();

        if(sharedPreferences == null)
            sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS_USER_DATA_TAG), MODE_PRIVATE);

        isSignedIn = sharedPreferences.getBoolean(getString(R.string.SHARED_PREFS_USER_DATA_TAG_SIGNED_IN), false);

        if(isSignedIn) {
            user = new User(
                sharedPreferences.getString(getString(R.string.SHARED_PREFS_USER_DATA_TAG_DISPLAY_NAME), ""),
                sharedPreferences.getString(getString(R.string.SHARED_PREFS_USER_DATA_TAG_PHOTO_URL), ""));

            ((CardFragment) cardFragment).setUserAccount(user);
            ((ProfileFragment) profileFragment).setUserAccount(user);
        }

        setVisibilityOfSignIn();
    }

    private void addAllGenres() {
        allGenres.add("science");
        allGenres.add("biography");
        allGenres.add("drama");
        allGenres.add("sci-fi");
        allGenres.add("romance");
        allGenres.add("fantasy");
        allGenres.add("action");
        allGenres.add("horror");
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void addAllMatches(){
        Match match1 = new Match("Lovisa", 26, null, 0, "http://cdn-fashionisers.fcpv4ak.maxcdn-edge.com/wp-content/uploads/2014/03/top_80_updo_hairstyles_2014_for_women_Emma_Stone_updos2.jpg", 0);
        //match1.percent = calculateMatchProcent(match1);
        match1.percent = 76;
        matches.add(match1);
        Match match2 = new Match("Mikael", 24, null, 1, "https://www.aceshowbiz.com/images/photo/ryan_gosling.jpg", 0);
        //match2.percent = calculateMatchProcent(match2);
        match2.percent = 89;
        matches.add(match2);
        Match match3 = new Match("Anton", 73, null, 2, "http://akns-images.eonline.com/eol_images/Entire_Site/20161129/rs_300x300-161229151358-ap.jpg?downsize=300:*&crop=300:300;left,top", 0);
        //match3.percent = calculateMatchProcent(match3);
        match3.percent = 75;
        matches.add(match3);
        Match match4 = new Match("Jimmy", 45, null, 3, "http://3.bp.blogspot.com/-a71LPYXKmYs/T5KQLsCOQNI/AAAAAAAAErw/vyC3o5j7JoA/s1600/Orlando+Bloom+(1).jpg", 100);
        //match4.percent = calculateMatchProcent(match4);
        match4.percent = 91;
        matches.add(match4);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private int calculateMatchProcent(Match match) {
        List<List<String>> likedBooks = new ArrayList<List<String>>();
        String list1[] = {"/works/OL13101191W", "/works/OL13101191W","/works/OL20600W","/works/OL362703W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W", "/works/OL10279W", "/works/OL262758W", "/works/OL362703W", "/works/OL20600W"};
        String list2[] = {"/works/OL13101191W","/works/OL20600W","/works/OL362703W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W"};
        String list3[] = {"/works/OL13101191W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W"};
        String list4[] = {"/works/OL13101191W","/works/OL362703W","/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W"};

        likedBooks.add(Arrays.asList(list1));
        likedBooks.add(Arrays.asList(list2));
        likedBooks.add(Arrays.asList(list3));
        likedBooks.add(Arrays.asList(list4));
        List<String> myLikedBooks = myLikedBooks();
        List<String> matchBooks = getLikedBooks(match.id, likedBooks, myLikedBooks);

        return (int) (((float)matchBooks.size()/ (float)myLikedBooks.size()) * 100);
    }

    @TargetApi(Build.VERSION_CODES.N)
    private List<String> getLikedBooks(int id, List<List<String>> likedBooks, List<String> myLikedBooks) {
        return myLikedBooks.stream().filter(likedBooks.get(id)::contains).collect(toList());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private List<String> myLikedBooks() {
        //String list1[] = {"/works/OL262758W","/works/OL10279W","/works/OL676009W","/works/OL82592W","/works/OL71175W","/works/OL45891W","/works/OL71174W", "/works/OL71172W", "/works/OL15638539W", "/works/OL10279W", "/works/OL262758W", "/works/OL362703W", "/works/OL20600W"};
        List<String> list2 = likedBooks.stream().map(book -> {return book.getKey();}).collect(toList());
        return list2;
    }

    private void initGoogleApiClient() {

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void createFragments() {
        profileFragment = new ProfileFragment();
        ((ProfileFragment) profileFragment).setProfileFragmentListener(this);
        cardFragment = new CardFragment();
        ((CardFragment) cardFragment).setCardFragmentListener(this);
        matchesFragment = new MatchesFragment();
    }

    private void mockSignIn() {

        user = new User("TestUser",
                "https://cdn3.iconfinder.com/data/icons/black-easy/512/538642-user_512x512.png");
        isSignedIn = true;
        ((ProfileFragment)profileFragment).setUserAccount(user);
        ((CardFragment)cardFragment).setUserAccount(user);


        if(sharedPreferences == null)
            sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS_USER_DATA_TAG), MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(getString(R.string.SHARED_PREFS_USER_DATA_TAG_SIGNED_IN), isSignedIn);
        editor.putString(getString(R.string.SHARED_PREFS_USER_DATA_TAG_DISPLAY_NAME), user.getGivenName());
        editor.putString(getString(R.string.SHARED_PREFS_USER_DATA_TAG_PHOTO_URL),
                user.getImageUrl());
        editor.apply();

        setVisibilityOfSignIn();
        setScanMenu();
        loadIgnoreGenres();

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

        else if(requestCode == RC_OCR_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    String text = data.getStringExtra("bookResult");
                    Log.d(TAG, "Text read: " + text);

                    ((CardFragment)cardFragment).addBookToTop(text);
                    switchContent("CardFragment");

                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
        }
        else if(requestCode == RC_BARCODE_CAPTURE) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Barcode barcode = data.getParcelableExtra(BarcodeCaptureActivity.BarcodeObject);
                    //statusMessage.setText(R.string.barcode_success);
                    //barcodeValue.setText(barcode.displayValue);

                    Book book = new Book(barcode.displayValue, "By Barcode scanner",
                            "", "http://book5s.com/wp-content/uploads/2016/07/Linguaforum-Toefl-Ibt-Test-Book-I-4-Audio-CDs-362x480.jpg", "codeScanner");

                    String strBook = new Gson().toJson(book);
                    ((CardFragment)cardFragment).addBookToTop2(strBook);
                    switchContent("CardFragment");

                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
        }
        else {
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
            user = new User(acct.getGivenName(),
                    acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() :
                            "https://cdn3.iconfinder.com/data/icons/black-easy/512/538642-user_512x512.png");
            isSignedIn = true;
            ((ProfileFragment)profileFragment).setUserAccount(user);
            ((CardFragment)cardFragment).setUserAccount(user);
            setScanMenu();
            setVisibilityOfSignIn();

            if(sharedPreferences == null)
                sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS_USER_DATA_TAG), MODE_PRIVATE);

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.SHARED_PREFS_USER_DATA_TAG_SIGNED_IN), isSignedIn);
            editor.putString(getString(R.string.SHARED_PREFS_USER_DATA_TAG_DISPLAY_NAME), acct.getGivenName());
            editor.putString(getString(R.string.SHARED_PREFS_USER_DATA_TAG_PHOTO_URL),
                    acct.getPhotoUrl() != null ? acct.getPhotoUrl().toString() : "https://cdn3.iconfinder.com/data/icons/black-easy/512/538642-user_512x512.png");
            editor.apply();

        } else {
            isSignedIn = false;
            ((ProfileFragment)profileFragment).setUserAccount(null);
            ((CardFragment)cardFragment).setUserAccount(null);
        }

        loadIgnoreGenres();
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

    public void setEmptyMenu() {
        menu.clear();
    }

    public void setScanMenu() {
        menu.clear();
        getMenuInflater().inflate(R.menu.scan_menu, menu);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.menu = menu;

        // defaults to scanMenu
        if(isSignedIn && this.menu != null)
            setScanMenu();

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_signout:
                logOut();
                return true;
            case R.id.action_scan_dropdown1:
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
            case R.id.action_scan_dropdown2:
                int rc2 = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
                if (rc2 == PackageManager.PERMISSION_GRANTED) {
                    Intent intent = new Intent(this, BarcodeCaptureActivity.class);
                    intent.putExtra(BarcodeCaptureActivity.AutoFocus, true);
                    intent.putExtra(BarcodeCaptureActivity.UseFlash, false);

                    startActivityForResult(intent, RC_BARCODE_CAPTURE);
                }
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void logOut() {

        isSignedIn = sharedPreferences.getBoolean(getString(R.string.SHARED_PREFS_USER_DATA_TAG_SIGNED_IN), false);

        if(isSignedIn) {

            setEmptyMenu();

            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(getString(R.string.SHARED_PREFS_USER_DATA_TAG_SIGNED_IN), false);
            editor.remove(getString(R.string.SHARED_PREFS_USER_DATA_TAG_IGNORE_GENRES));
            editor.apply();
            isSignedIn = false;
            idFragment = "CardFragment";
            if(googleApiClient != null && googleApiClient.isConnected())
                googleApiClient.disconnect();

            //setVisibilityOfSignIn();
            //switchContent(idFragment);
            Intent intent = new Intent(this, HomeActivity.class);

            finish();

            startActivity(intent);
        }

        if(sharedPreferences == null)
            sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS_USER_DATA_TAG), MODE_PRIVATE);

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

        View.OnClickListener listener = view -> ActivityCompat.requestPermissions(thisActivity, permissions,
                RC_HANDLE_CAMERA_PERM);

        View view = findViewById(R.id.container);
        Snackbar.make(view, "permission_camera_rationale",
                Snackbar.LENGTH_INDEFINITE)
                .setAction("ok", listener)
                .show();
    }

    // Loads liked and disliked books to be
    public void loadLikesAndDisliked() {
        // TODO implement this..
    }

    // Loads disliked genres from local storage
    public void loadIgnoreGenres() {
        if(sharedPreferences == null)
            sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS_USER_DATA_TAG), MODE_PRIVATE);

        Set<String> ignoreGenres = sharedPreferences.getStringSet(getString(R.string.SHARED_PREFS_USER_DATA_TAG_IGNORE_GENRES), new HashSet<>());

        ((ProfileFragment)profileFragment).refreshIgnoreGenres(ignoreGenres);
        ((CardFragment)cardFragment).refreshIgnoreGenres(ignoreGenres);
    }

    public void loadMatches(){
        ((MatchesFragment)matchesFragment).setMatches(matches);
    }

    @Override
    public void onDislikedGenreAdded(String genre) {

        if(sharedPreferences == null)
            sharedPreferences = getSharedPreferences(getString(
                    R.string.SHARED_PREFS_USER_DATA_TAG), MODE_PRIVATE);

        Set<String> ignoreGenresOld = sharedPreferences.getStringSet(
                getString(R.string.SHARED_PREFS_USER_DATA_TAG_IGNORE_GENRES), new HashSet<>());

        Set<String> ignoreGenresNew = new HashSet<>();
        ignoreGenresNew.addAll(ignoreGenresOld);
        ignoreGenresNew.add(genre);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(
                getString(R.string.SHARED_PREFS_USER_DATA_TAG_IGNORE_GENRES),
                ignoreGenresNew);
        editor.apply();

        ((CardFragment)cardFragment).getIgnoreGenres().add(genre);
    }

    @Override
    public void onDislikedGenreRemoved(String genre) {

        if(sharedPreferences == null)
            sharedPreferences = getSharedPreferences(getString(R.string.SHARED_PREFS_USER_DATA_TAG), MODE_PRIVATE);

        Set<String> ignoreGenresOld = sharedPreferences.getStringSet(getString(R.string.SHARED_PREFS_USER_DATA_TAG_IGNORE_GENRES), new HashSet<>());

        Set<String> ignoreGenresNew = new HashSet<>();
        ignoreGenresNew.addAll(ignoreGenresOld);
        ignoreGenresNew.remove(genre);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putStringSet(
                getString(R.string.SHARED_PREFS_USER_DATA_TAG_IGNORE_GENRES),
                ignoreGenresNew);
        editor.apply();

        ((CardFragment)cardFragment).getIgnoreGenres().remove(genre);
    }

    @Override
    public void bookLiked(Book book) {
        likedBooks.add(book);
        dislikedBooks.remove(book);
        ((ProfileFragment)profileFragment).setLikedBooks(likedBooks);
        ((MatchesFragment)matchesFragment).setLikedBooks(likedBooks);

        user.setLikedBooks(likedBooks);
        ((ProfileFragment)profileFragment).setUserAccount(user);
        ((MatchesFragment)matchesFragment).setUserAccount(user);



        runMatchMakingSystem();

    }

    @Override
    public void bookDisliked(Book book) {
        dislikedBooks.add(book);
        likedBooks.remove(book);
        ((ProfileFragment)profileFragment).setLikedBooks(likedBooks);
        ((MatchesFragment)matchesFragment).setLikedBooks(likedBooks);

        user.setLikedBooks(likedBooks);
        ((ProfileFragment)profileFragment).setUserAccount(user);
        ((MatchesFragment)matchesFragment).setUserAccount(user);
        ((CardFragment)cardFragment).setUserAccount(user);

        runMatchMakingSystem();

    }

    private void runMatchMakingSystem(){

        matchCounter--;
        if(matchCounter == 0){
            NewMatch newMatch = new NewMatch();
            Match match = new Match("Erik", 27, null, 1, "http://fanexpocanada.com/wp-content/uploads/Eric-McCormack.png", 64);
            newMatch.setMatch(match);
            newMatch.setUser(user);
            newMatch.show(getSupportFragmentManager(), newMatch.getTag());

            matches.add(match);
            loadMatches();

            matchCounter = 4;
        }
    }

    public List<Book> getTopList() {
        return topList;
    }

    public void setTopList(List<Book> topList) {
        this.topList = topList;
    }
}
