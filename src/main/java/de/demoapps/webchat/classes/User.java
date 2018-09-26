package de.demoapps.webchat.classes;

import java.io.Serializable;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="USERS")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    
    @Column(name="NICKNAME")
    private String nickname;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="SETTINGS")
    private String settings;

    public User () { 

        super();
    }

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;

        this.settings = "enter=1;outFont=15;inFont=15;";
    }

    public String getNickname() {

        return nickname;
    }

    public void setNickname(String nickname) {

        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSettings() {
        return settings;
    }

    public void setSettings(String setting) {
        this.settings = setting;
    }

    @Override
    public String toString() {
        return "nickname: " + getNickname() + " password: " + getPassword() + " settings: " + getSettings();
    }
}