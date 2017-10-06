package com.mobilecomputing.binder.Views;

import android.app.Dialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Utils.GenreAdapter;

import java.util.List;


/**
 * Created by Libra on 06/10/17.
 */

public class BottomSheet extends BottomSheetDialogFragment {


    ArrayAdapter adapter;
    ListView listView;
    List<String> mGenres;

    public BottomSheet() {

    }

    public void setGenres(List<String> genres) {
        this.mGenres = mGenres;
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



        adapter = new GenreAdapter(getContext(), R.layout.add_genre, mGenres);
        listView.setAdapter(adapter);
        return view;
    }
}
