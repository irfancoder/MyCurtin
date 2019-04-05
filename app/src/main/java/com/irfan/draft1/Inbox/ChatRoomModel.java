package com.irfan.draft1.Inbox;

/**
 * Created by irfan on 27/01/2018.
 */

public class ChatRoomModel {
    private String name;
    private String image;
    private ChatModel lastMessageSent;

    public ChatRoomModel(String name, String image, ChatModel lastMessageSent) {
        this.name = name;
        this.image = image;
        this.lastMessageSent = lastMessageSent;
    }

    public ChatRoomModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public ChatModel getLastMessageSent() {
        return lastMessageSent;
    }

    public void setLastMessageSent(ChatModel lastMessageSent) {
        this.lastMessageSent = lastMessageSent;
    }
}
