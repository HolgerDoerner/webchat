package de.demoapps.webchat.classes;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Parameter;
import org.hibernate.annotations.Type;

@Entity
@Table(name="users")
public class User implements Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @Column(name="ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    private Integer id;
    
    @Column(name="NICKNAME")
    private String nickname;

    @Column(name="PASSWORD")
    private String password;

    @Column(name="SETTINGS")
    @Type(type="org.hibernate.type.SerializableToBlobType",
        parameters={@Parameter(name="classname", value="java.util.HashMap")})
    private Map<String, Integer> settings;

    public User () { 

        super();
    }

    public User(String nickname, String password) {
        this.nickname = nickname;
        this.password = password;

        this.settings = new HashMap<>();
        settings.put("enter", 1);
        settings.put("outputfontsize", 15);
        settings.put("inputfontsize", 15);
    }

    public Integer getID() {
        return id;
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

    public Integer getSingleSetting(String key) {
        return settings.get(key);
    }

    public void setSettings(Map<String, Integer> settings) {
        this.settings = settings;
    }

    public void setSingleSetting(String key, Integer value) {
        this.settings.put(key, value);
    }

    @Override
    public String toString() {
        return "ID: " + id + " nickname: " + nickname + " password: " + password;
    }
}