package com.mobilecomputing.binder.Views;

import android.content.Context;
import android.widget.LinearLayout;

import com.mobilecomputing.binder.R;

/**
 * Created by Lovis on 13/10/17.
 */

public class AddTopList extends LinearLayout {

    public AddTopList(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.add_toplist, this);

    }
}
