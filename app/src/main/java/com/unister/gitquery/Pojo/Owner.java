package com.unister.gitquery.Pojo;


/**
 * Created by Mohamed El Amine on 28/05/2016.
 */


/**
 * Access the owner of the repo
 */
public class Owner {

    private String avatar_url;

    private Owner() {
    }

    public String getAvatar_url() {
        return avatar_url;
    }

    public void setAvatar_url(String avatar_url) {
        this.avatar_url = avatar_url;
    }
}
