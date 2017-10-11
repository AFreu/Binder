package com.mobilecomputing.binder.Objects;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.MessageContentType;

import java.util.Date;

/**
 * Created by mpq on 2017-10-04.
 */

public class Message implements IMessage,
        MessageContentType.Image {

    private String id;
    private String text;
    private Author author;
    private Date createdAt;

    private ChatImage image;

    /*...*/
   public void setId(String id) {
       this.id = id;
   }

   public void setText(String text) {
       this.text = text;
   }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
    public void setImage(ChatImage image) {
        this.image = image;
    }
    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getText() {
        return text;
    }

    @Override
    public Author getUser() {
        return author;
    }

    @Override
    public Date getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getImageUrl() {
        return image == null ? null : image.getUrl();
    }
}
