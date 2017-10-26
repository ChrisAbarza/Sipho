package com.example.chris.sipho;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.facebook.Profile;

/**
 * Created by salv8 on 26/10/2017.
 */

public class VerMiPerfil extends AppCompatActivity {
    Profile profile = Profile.getCurrentProfile();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ver_usuario);

    }
}
