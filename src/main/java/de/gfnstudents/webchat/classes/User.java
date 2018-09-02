package de.gfnstudents.webchat.classes;

import java.io.Serializable;
import java.util.Random;

public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String nickname;
    private String sid;

    public User () { 
        super();

        Random rand = new Random();
        this.sid = String.valueOf(rand.nextInt(10000) + 1);
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getSid() {
        return sid;
    }
}