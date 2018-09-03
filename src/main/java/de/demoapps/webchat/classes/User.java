package de.demoapps.webchat.classes;

import java.io.Serializable;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nickname;

    public User () { 

        super();
    }

    public String getNickname() {

        return nickname;
    }

    public void setNickname(String nickname) {

        this.nickname = nickname;
    }
}