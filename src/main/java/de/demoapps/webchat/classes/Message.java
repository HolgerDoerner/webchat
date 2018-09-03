package de.demoapps.webchat.classes;

public class Message {

    private String nickname;
    private String to;
    private String content;

    public Message() {}

    public void setNickname(String from) {
        this.nickname = from;
    }

    public String getNickname() {
        return nickname;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getTo() {
        return to;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}