package com.example.pr21_capturaimatges;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.lang.reflect.Array;
import java.util.ArrayList;

public class GalleryActivity extends AppCompatActivity {

    final File photoDir = new File("/data/user/0/com.example.pr21_capturaimatges/files/photos");
    final String photoExt = ".jpeg";

    ArrayList<Photo> photosList = new ArrayList<Photo>();

    RecyclerView recyclerViewPhotos;

    RecyclerView.Adapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();

        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);

        recyclerViewPhotos.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        try {

            photosList = intent.getParcelableArrayListExtra("PHOTOS_LIST");

            System.out.println("Photos List is loaded successfully.");

            printPhotoList();


        } catch (Exception e) {

            e.printStackTrace();

        }

        RecyclerViewAdapter rvAdapter = new RecyclerViewAdapter(photosList);

        recyclerViewPhotos.setAdapter(rvAdapter);

    }

    public void returnToMain(View v) {

        Intent intent = new Intent(GalleryActivity.this, MainActivity.class);

        startActivity(intent);

    }

    public void printPhotoList() {

        System.out.println("There are " + photosList.size() + " photos available.");

        for (int i = 0; i < photosList.size(); i++) {

            System.out.println("Photo Gallery " + (i+1) + " " + photosList.get(i).getFileName());

        }

    }

}
