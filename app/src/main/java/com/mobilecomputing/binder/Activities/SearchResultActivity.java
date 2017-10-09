package com.mobilecomputing.binder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.BooksAdapter;
import com.mobilecomputing.binder.Utils.MatchesAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    ListView list;
    BooksAdapter books;
    private ArrayList<Book> bookList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolBar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolBar);

        list = (ListView) findViewById(R.id.searchResults);

        bookList = new ArrayList<Book>();

        JsonArray bookArr = new Gson().fromJson(getIntent().getStringExtra("books"), JsonArray.class);

        for (int i = 0; i < bookArr.size();  i++)
        {
            Book book = new Gson().fromJson(bookArr.get(i), Book.class);
            bookList.add(book);
        }
        books = new BooksAdapter(this, R.layout.book_list_item, bookList);

        list.setAdapter(books);

        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Book book = (Book)list.getItemAtPosition(position);
                String strBook = new Gson().toJson(book);
                Intent data = new Intent();
                data.putExtra("bookResult", strBook);
                setResult(CommonStatusCodes.SUCCESS, data);
                finish();
            }
        });

    }
}
