package com.example.chris.sipho;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import de.hdodenhof.circleimageview.CircleImageView;

public class Spash extends AppCompatActivity {

    private ImageView photoImageViewLogo;
    private ImageView photoImageViewTexto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spash);

        photoImageViewLogo = (ImageView) findViewById(R.id.imageView3);
        photoImageViewTexto = (ImageView) findViewById(R.id.imageView4);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(Spash.this, MainActivity.class);
                startActivity(intent);
                finish();

            }
        }, 3000);
    }
}
