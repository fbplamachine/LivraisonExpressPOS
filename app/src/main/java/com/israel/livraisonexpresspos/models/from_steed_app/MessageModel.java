package com.israel.livraisonexpresspos.models.from_steed_app;

public class MessageModel {
    private int index;
    private String title;
    private String messageContent;

    public MessageModel(int index, String title, String messageContent) {
        this.index = index;
        this.title = title;
        this.messageContent = messageContent;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessageContent() {
        return messageContent;
    }

    public void setMessageContent(String messageContent) {
        this.messageContent = messageContent;
    }
}
