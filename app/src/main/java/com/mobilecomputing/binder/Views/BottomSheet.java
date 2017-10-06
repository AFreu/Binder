package com.mobilecomputing.binder.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.GenreAdapter;

import java.util.List;


/**
 * Created by Libra on 06/10/17.
 */

public class BottomSheet extends BottomSheetDialogFragment {


    GenreAdapter adapter;
    ListView listView;
    List<String> mGenres;
    OnItemClickListener mOnClickListener;


    public BottomSheet() {

    }

    public void setGenres(List<String> genres) {
        this.mGenres = genres;
    }

    public void setOnItemClickListener(OnItemClickListener onClickListener){
        this.mOnClickListener = onClickListener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.bottom_sheet, container);
        listView = view.findViewById(R.id.bottom_sheet_list);


        adapter = new GenreAdapter(getContext(), R.layout.genre_item, mGenres);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(mOnClickListener);
        return view;
    }
}
