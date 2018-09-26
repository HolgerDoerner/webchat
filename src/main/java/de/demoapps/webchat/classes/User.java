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
    private Map<String, Integer> settings;

    public User () { 

        super();
    }

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;

        settings.put("sendWithEnter", 1);
        settings.put("outputFontSize", 15);
        settings.put("inputFontSize", 15);
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

    public Map<String, Integer> getSettings() {
        return settings;
    }

    public void setSettings(Integer sendWithEnter, Integer outputFontSize, Integer inputFontSize) {
        settings.replace("sendWithEnter", sendWithEnter);
        settings.replace("outputFontSize", outputFontSize);
        settings.replace("inputFontSize", inputFontSize);
    }

    public Integer getSetting_sendWithEnter() {
        return settings.get("sendWithEnter");
    }

    public void setSetting_sendWithEnter(Integer value) {
        settings.replace("sendWithEnter", value);
    }

    public Integer getSetting_outputFontSize() {
        return settings.get("outputFontSize");
    }

    public void setSetting_outputFontSize(Integer value) {
        settings.replace("outputFontSize", value);
    }

    public Integer getSetting_inputFontSize() {
        return settings.get("inputFontSize");
    }

    public void setSetting_inputFontSize(Integer value) {
        settings.replace("inputFontSize", value);
    }

    @Override
    public String toString() {
        return "nickname: " + getNickname() + " password: " + getPassword() + " settings: " + getSettings();
    }
}