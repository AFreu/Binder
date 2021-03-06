package com.mobilecomputing.binder.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.mobilecomputing.binder.Objects.Author;
import com.mobilecomputing.binder.Objects.ChatImage;
import com.mobilecomputing.binder.Objects.Message;
import com.mobilecomputing.binder.Objects.Match;
import com.mobilecomputing.binder.R;
import com.stfalcon.chatkit.commons.ImageLoader;
import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.messages.MessageInput;
import com.stfalcon.chatkit.messages.MessagesList;
import com.stfalcon.chatkit.messages.MessagesListAdapter;

import java.util.Calendar;
import java.util.Date;

import com.squareup.picasso.Picasso;


/**
 * A simple {@link Fragment} subclass.
 */
public class ContactChatFragment extends BasicFragment
        implements MessagesListAdapter.OnLoadMoreListener {

    private Match mContact;

    private String senderId = "Me";
    private ImageLoader imageLoader;


    MessagesListAdapter adapter;
    MessagesList messagesList;
    private int total;
    private Date lastLoadedDate;

    public ContactChatFragment() {
        // Required empty public constructor
    }

    public static ContactChatFragment newInstance(Match contact) {
        ContactChatFragment fragment = new ContactChatFragment();
        Bundle args = new Bundle();
        args.putSerializable("contact", contact);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mContact = (Match)getArguments().getSerializable("contact");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contact_chat, container, false);
        imageLoader = new ImageLoader() {
            @Override
            public void loadImage(ImageView imageView, String url) {
                Picasso.with(view.getContext()).load(url).into(imageView);
            }
        };
        messagesList = view.findViewById(R.id.messagesList);
        adapter = new MessagesListAdapter<Message>(senderId, imageLoader);
        messagesList.setAdapter(adapter);
        MessageInput inputView = view.findViewById(R.id.input);
        inputView.setInputListener(new MessageInput.InputListener() {
            @Override
            public boolean onSubmit(CharSequence input) {
                Author author = new Author();
                author.setId(total % 2 == 0 ? "Me" : "Match");
                author.setName(total % 2 == 0 ? "Me" : mContact.getGivenName());
                author.setAvatar("https://blog.prepscholar.com/hubfs/body_testinprogress.gif?t=1506619170859");
                Message msg = new Message();
                msg.setId(String.valueOf(total));
                msg.setText(String.valueOf(input));
                msg.setAuthor(author);
                msg.setCreatedAt(Calendar.getInstance().getTime());
                adapter.addToStart(msg, true);
                total++;
                return true;
            }
        });
        inputView.setAttachmentsListener(new MessageInput.AttachmentsListener() {
            @Override
            public void onAddAttachments() {
                Author author = new Author();
                author.setId(total % 2 == 0 ? "Me" : "Match");
                author.setName(total % 2 == 0 ? "Me" : mContact.getGivenName());
                author.setAvatar("https://blog.prepscholar.com/hubfs/body_testinprogress.gif?t=1506619170859");
                ChatImage image = new ChatImage();
                image.setUrl("https://blog.prepscholar.com/hubfs/body_testinprogress.gif?t=1506619170859");
                Message msg = new Message();
                msg.setId(String.valueOf(total));
//                msg.setText(String.valueOf(input));
                msg.setImage(image);
                msg.setAuthor(author);
                msg.setCreatedAt(Calendar.getInstance().getTime());
                adapter.addToStart(msg, true);//select attachments
                total++;
            }
        });
        return view;
    }



    @Override
    public void onLoadMore(int page, int totalItemsCount) {
        if (totalItemsCount < this.total) {
            //loadMessages(...);
        }
    }


    public interface OnMessageClickListener<MESSAGE extends IMessage> {
        void onMessageClick(MESSAGE message);
    }
    public interface OnMessageLongClickListener<MESSAGE extends IMessage> {
        void onMessageLongClick(MESSAGE message);
    }
}
