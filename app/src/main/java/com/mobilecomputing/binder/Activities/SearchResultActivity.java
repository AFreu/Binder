package com.mobilecomputing.binder.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.mobilecomputing.binder.Objects.Book;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.MatchesAdapter;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchResultActivity extends AppCompatActivity {

    ListView list;
    MatchesAdapter matches;
    private ArrayList<Match> matchList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Toolbar toolBar = (Toolbar)findViewById(R.id.home_toolbar);
        setSupportActionBar(toolBar);

        list = (ListView) findViewById(R.id.searchResults);

        matchList = new ArrayList<Match>();

        JsonArray bookArr = new Gson().fromJson(getIntent().getStringExtra("books"), JsonArray.class);
        matchList.add(new Match("Lovisa", 26, null, null, null, 55));
        matchList.add(new Match("Mikael", 24, null, null, null, 67));
        for (int i = 0; i < bookArr.size();  i++)
        {
            Book book = new Gson().fromJson(bookArr.get(i), Book.class);
            Match match = new Match(book.getTitle(), 0, book.getAuthor(),null,null,0);
            matchList.add(match);
        }
        matches = new MatchesAdapter(this, R.layout.match_item, matchList);

        list.setAdapter(matches);

        list.setClickable(true);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

    }
}
