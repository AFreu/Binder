package com.mobilecomputing.binder.Views;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilecomputing.binder.R;

/**
 * Created by Lovis on 06/10/17.
 */

public class ChipView extends LinearLayout {

    private TextView genreName;
    private TextView delete;

    public ChipView(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.added_genre, this);
        genreName = findViewById(R.id.genre_name);
        delete = findViewById(R.id.genre_delete);
    }

    public void setText(String name){
        genreName.setText(name);
    }


}
