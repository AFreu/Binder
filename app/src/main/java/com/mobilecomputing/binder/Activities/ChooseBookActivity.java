package com.mobilecomputing.binder.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.ChooseBooksAdapter;

import java.util.ArrayList;
import java.util.List;

public class ChooseBookActivity extends AppCompatActivity {

    private List<Book> myLikedBooks;
    private ListView bookList;
    ChooseBooksAdapter adapter;
    List<Book> searchList = new ArrayList<>();

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_book);
        setTitle("Add book");

        Toolbar toolBar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolBar);

        bookList = (ListView) findViewById(R.id.books_list);

        myLikedBooks = ((Book) getIntent().getSerializableExtra("MyLikedBooks")).getBookList();

        populateUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_menu, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                private String oldText = "";
                @Override
                public boolean onQueryTextChange(String newText) {
                    List<Book> newList = new ArrayList<>();
                    if(oldText.length() > newText.length()){
                        for (Book b : myLikedBooks) {
                            if(b.getTitle().contains(newText) || b.getAuthor().contains(newText)){
                                newList.add(b);
                            }
                        }
                    } else {
                        for (Book b : searchList) {
                            if(b.getTitle().contains(newText) || b.getAuthor().contains(newText)){
                                newList.add(b);
                            }
                        }
                    }
                    oldText = newText;
                    searchList = newList;
                    adapter.clear();
                    adapter.addAll(newList);
                    //searchList.stream().filter(book -> {})
                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);

                    return false;
                }

            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_search:
                // Not implemented here
                return false;
            case R.id.action_signout:
                Intent intent = new Intent();
                intent.putExtra("LogOut", "Logout");
                setResult(CommonStatusCodes.CANCELED, intent);
                finish();
                return true;
            default:
                break;
        }
        searchView.setOnQueryTextListener(queryTextListener);
        return super.onOptionsItemSelected(item);
    }

    public void populateUI() {

        searchList.addAll(myLikedBooks);

        adapter = new ChooseBooksAdapter(this, R.layout.book_list_item2, searchList);

        bookList.setAdapter(adapter);
        bookList.setOnItemClickListener((parent, view, position, id) -> {

            Intent intent = new Intent();
            intent.putExtra("SelectedBook", (Book)searchList.get(position));
            setResult(CommonStatusCodes.SUCCESS, intent);
            finish();

        });
    }
}
