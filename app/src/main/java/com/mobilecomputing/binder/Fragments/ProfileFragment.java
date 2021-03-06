package com.mobilecomputing.binder.Fragments;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.mobilecomputing.binder.Activities.ChooseBookActivity;
import com.mobilecomputing.binder.Activities.HomeActivity;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.BookAdapter;
import com.mobilecomputing.binder.Utils.BookTopListAdapter;
import com.mobilecomputing.binder.Utils.ImageAdapter;

import com.mobilecomputing.binder.Objects.User;
import com.mobilecomputing.binder.Utils.Review;
import com.mobilecomputing.binder.Views.AddTopList;
import com.mobilecomputing.binder.Views.BookTopList;
import com.mobilecomputing.binder.Views.BottomSheet;
import com.mobilecomputing.binder.Views.ChipButton;
import com.mobilecomputing.binder.Views.ChipView;
import com.mobilecomputing.binder.Views.ExpandableHeightGridView;
import com.squareup.picasso.Picasso;
import com.wefika.flowlayout.FlowLayout;

import org.intellij.lang.annotations.JdkConstants;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


/**
 * A simple {@link Fragment} subclass.
 */
public class ProfileFragment extends BasicFragment {


    private int CHOOSE_BOOKLIST_ACTIVITY = 1287;

    public interface ProfileFragmentListener {
        void onDislikedGenreAdded(String genre);
        void onDislikedGenreRemoved(String genre);
    }

    private ProfileFragmentListener profileFragmentListener;

    public void setProfileFragmentListener(ProfileFragmentListener profileFragmentListener) {
        this.profileFragmentListener = profileFragmentListener;
    }

    private String TAG = "ProfileFragment";

    private TextView nameText;
    private ImageView profileImage;
    private FlowLayout flowLayout;
    private BottomSheet bottomSheet = new BottomSheet();

    private List<String> availableGenres;
    private Set<String> ignoredGenres = new HashSet<>();
    private List<Book> likedBooks = new ArrayList<>();
    private List<Book> topList = new ArrayList<>();
    private AddTopList addTopListButton;
    private ChipButton chipButton;
    private Button btn;
    private ExpandableHeightGridView topListGrid;
    private ExpandableHeightGridView likedBooksGrid;
    private BookAdapter bookAdapter;
    private BookTopListAdapter bookAdapterTopList;

    private View.OnClickListener clickRemoveListener;
    private AdapterView.OnItemClickListener clickGenreListener;

    public ProfileFragment() {
        // Required empty public constructor
        availableGenres = new ArrayList<>();
        availableGenres.addAll(HomeActivity.allGenres);
    }


    // This is run when user jumps to this fragment
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        initUI(view);

        return view;
    }


    // Loads disliked genres from local storage
    public void refreshIgnoreGenres(Set<String> ignores) {
        ignoredGenres.addAll(ignores);
        availableGenres.removeAll(ignores);
    }

    // called from HomeActivity when user swipes on a book
    public void setLikedBooks(List<Book> likedBooks) {
        this.likedBooks = likedBooks;
    }

    public void initUI(View view) {

        topListGrid = view.findViewById(R.id.profile_top_books);
        topListGrid.setExpanded(true);
        bookAdapterTopList = new BookTopListAdapter(getContext(), R.layout.book_item, null);
        bookAdapterTopList.addSeparatorItem(null);
        topListGrid.setAdapter(bookAdapterTopList);
        topList = ((HomeActivity)getActivity()).getTopList();
        bookAdapterTopList.addManyItems(topList);



        topListGrid.setOnItemClickListener((parent, view1, position, id) -> {
            if(position != 0) {
                showBook(bookAdapterTopList.getItem(position), true);
            }
            else {
                if(topList.size() < 6) {
                    Intent intent = new Intent(getContext(), ChooseBookActivity.class);
                    Book book = new Book();
                    List<Book> listBook = new ArrayList<Book>();
                    listBook.addAll(likedBooks);
                    book.setBookList(listBook);
                    intent.putExtra("MyLikedBooks", book);
                    startActivityForResult(intent, CHOOSE_BOOKLIST_ACTIVITY);
                }
            }
        });

        likedBooksGrid = view.findViewById(R.id.profile_liked_books);
        likedBooksGrid.setExpanded(true);

        bookAdapter = new BookAdapter(getContext(), R.layout.book_item, likedBooks);

        likedBooksGrid.setAdapter(bookAdapter);

        likedBooksGrid.setOnItemClickListener((parent, view1, position, id) -> {
            showBook(bookAdapter.getItem(position), true);
        });


        // removes an ignored genre
        clickRemoveListener = view13 -> {

            ChipView c = (ChipView) view13;

            ((FlowLayout)c.getParent()).removeView(c);

            chipButton.setVisibility(View.VISIBLE);

            ignoredGenres.remove(c.getText());
            availableGenres.add(c.getText());

            // notifies listener about the genre to ignore
            if(profileFragmentListener != null)
                profileFragmentListener.onDislikedGenreRemoved(c.getText());

        };

        View.OnClickListener clickAddListener = view12 -> {
            FragmentTransaction ft = getFragmentManager().beginTransaction();
            ft.addToBackStack(null);

            bottomSheet.setGenres(availableGenres);
            bottomSheet.setOnItemClickListener(clickGenreListener);
            bottomSheet.show(ft, "dialog");
        };

        // when a genre to ignore is clicked
        clickGenreListener = (adapterView, view1, i, l) -> {

            addDislikedGenre((String)adapterView.getAdapter().getItem(i));
            ignoredGenres.add((String)adapterView.getAdapter().getItem(i));

            // notifies listener about the genre to ignore
            if(profileFragmentListener != null)
                profileFragmentListener.onDislikedGenreAdded((String)adapterView.getAdapter().getItem(i));

            availableGenres.remove(adapterView.getAdapter().getItem(i));

            bottomSheet.dismiss();

            if(availableGenres.isEmpty())
                chipButton.setVisibility(View.INVISIBLE);
        };

        nameText = view.findViewById(R.id.profile_name);
        profileImage = view.findViewById(R.id.profile_picture);

        flowLayout = view.findViewById(R.id.profile_flowlayout);


        chipButton = new ChipButton(getContext());
        chipButton.setOnClickListener(clickAddListener);
        chipButton.setLayoutParams(new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT));
        flowLayout.addView(chipButton);

        populateUI();
    }

    /**
     * Populates the user interface based on Google user data
     */
    public void populateUI() {

        if(userAccount != null && nameText != null) {
            nameText.setText(userAccount.getGivenName());
            Picasso.with(getContext()).load(userAccount.getImageUrl()).into(profileImage);
        }

        // adds all disliked genres to UI
        for(String genre : ignoredGenres)
            addDislikedGenre(genre);
    }

    public void setUserAccount(User userAccount) {
        this.userAccount = userAccount;
        try {
            Picasso.with(getContext()).load(userAccount.getImageUrl()).into(profileImage);
        } catch (Exception e){}    }

    private void addDislikedGenre(String name){

        ChipView c = new ChipView(getContext());
        c.setText(name);
        c.setOnClickListener(clickRemoveListener);
        c.setLayoutParams(new FlowLayout.LayoutParams(FlowLayout.LayoutParams.WRAP_CONTENT,FlowLayout.LayoutParams.WRAP_CONTENT));

        flowLayout.addView(c, flowLayout.getChildCount() - 1);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CHOOSE_BOOKLIST_ACTIVITY) {
            if (resultCode == CommonStatusCodes.SUCCESS) {
                if (data != null) {
                    Book book = (Book) data.getSerializableExtra("SelectedBook");
                    bookAddToIntresstList(book);
                } else {
                    Log.d(TAG, "No Text captured, intent data is null");
                }
            }
            else if (resultCode == CommonStatusCodes.CANCELED){
                ((HomeActivity)getActivity()).logOut();
            }
        }
        else {
        }
    }

    public void bookAddToIntresstList(Book book) {
        topList.add(book);
        bookAdapterTopList.addItem(book);
        ((HomeActivity)getActivity()).setTopList(topList);

    }

}
