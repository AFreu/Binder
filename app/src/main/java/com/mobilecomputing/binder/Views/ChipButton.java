package com.mobilecomputing.binder.Views;

import android.content.Context;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mobilecomputing.binder.R;

/**
 * Created by Lovis on 06/10/17.
 */

public class ChipButton extends LinearLayout {


    public ChipButton(Context context) {
        super(context);
        init();
    }

    private void init(){
        inflate(getContext(), R.layout.add_genre, this);

    }

}
