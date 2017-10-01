package com.example.chris.sipho;

import com.facebook.Profile;

/**
 * Created by salv8 on 02/09/2017.
 */

public class Metodos {

    private String id;
    private String name;
    private String photoUrl;
    private String bdUrl ="http://192.168.1.6/sipho/";

    public String getBdUrl() {
        return bdUrl;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }

    public void guardarDatosFacebook(Profile profile) {
        setId(profile.getId());
        setName(profile.getName());
        setPhotoUrl(profile.getProfilePictureUri(100, 100).toString());

    }



}
