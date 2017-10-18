package com.mobilecomputing.binder.Views;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilecomputing.binder.Activities.ContactActivity;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.mobilecomputing.binder.Objects.User;
import com.squareup.picasso.Picasso;

/**
 * Created by Lovis on 11/10/17.
 */

public class NewMatch extends AppCompatDialogFragment {

    private Match match;
    private User user;

    private TextView matchText;
    private TextView viewProfile;
    private ImageView profilePicture;
    private ImageView matchPicture;

    public void setMatch(Match match){
        this.match = match;
    }

    public void setUser(User user){
        this.user = user;
    }


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        return super.onCreateDialog(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.new_match, container);

        matchText = view.findViewById(R.id.match_text);
        viewProfile = view.findViewById(R.id.button_view_profile);
        profilePicture = view.findViewById(R.id.profile_picture_match_view);
        matchPicture = view.findViewById(R.id.match_picture_match_view);

        populateUI();
        return view;
    }

    public void populateUI(){
        matchText.setText("You got a " + match.percent + "% match with " + match.getGivenName());
        Picasso.with(getContext()).load(match.getImageUrl()).into(matchPicture);
        Picasso.with(getContext()).load(user.getImageUrl()).into(profilePicture);

        viewProfile.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.putExtra("contact", match);
                intent.putExtra("user", user);
                getActivity().startActivity(intent);
                getDialog().dismiss();
            }
        });
    }

}


