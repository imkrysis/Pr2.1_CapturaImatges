package com.example.pr21_capturaimatges;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class GalleryActivity extends AppCompatActivity {

    final File photoDir = new File("/data/user/0/com.example.pr21_capturaimatges/files/photos");
    final File mainPhotoFile = new File("/data/user/0/com.example.pr21_capturaimatges/files/main.jpeg");
    final String photoExt = ".jpeg";

    ArrayList<Photo> photosList = new ArrayList<Photo>();

    File photoFile;
    String nickname;
    Bitmap photoBitmap;

    RecyclerView recyclerViewPhotos;
    RecyclerViewAdapter rvAdapter;

    AlertDialog.Builder adb;
    AlertDialog adMainPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);

        Intent intent = getIntent();

        try {

            getPhotoList();

            printPhotoList();

            setMainPhotoDialog();

        } catch (Exception e) {

            e.printStackTrace();

        }

        buildRecyclerView();

    }

    public void buildRecyclerView() {

        recyclerViewPhotos = findViewById(R.id.recyclerViewPhotos);

        recyclerViewPhotos.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));

        rvAdapter = new RecyclerViewAdapter(photosList);

        rvAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                photoBitmap = photosList.get(recyclerViewPhotos.getChildAdapterPosition(v)).getPhotoBitmap(); // Obtenemos el bitmap de la foto correspondiente a la posicion clickada en el RecyclerView.

                showMainPhotoDialog(photoBitmap); // Lanzamos el dialogo enviando el Bitmap.

            }
        });

        recyclerViewPhotos.setAdapter(rvAdapter);

    }

    public void setMainPhotoDialog() {

        adb = new AlertDialog.Builder(this);

        adb.setTitle("Configuración foto predeterminada.");
        adb.setMessage("¿Desea definir esta foto como predeterminada?");

        adb.setPositiveButton("Sí", null);
        adb.setNegativeButton("No", null);

        adMainPhoto = adb.create();

        adMainPhoto.setCancelable(false);
        adMainPhoto.setCanceledOnTouchOutside(false);

    }

    public void showMainPhotoDialog(Bitmap photoBitmap) { // Mostramos un dialogo para que el usuario confirme si quiere establecer la foto seleccionada como predeterminada.

        adMainPhoto.show();

        Button positiveButton = adMainPhoto.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                adMainPhoto.dismiss();

                try {

                    setMainPhoto(photoBitmap);

                } catch (IOException e) {

                    e.printStackTrace();
                }

            }
        });

        Button negativeButton = adMainPhoto.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                adMainPhoto.dismiss();

            }
        });

    }

    public void setMainPhoto(Bitmap photoBitmap) throws IOException { // Si aun no hay ninguna foto predeterminada, creamos el archivo y posteriormente almacenamos el bitmap.

        if (!mainPhotoFile.exists()) {

            mainPhotoFile.createNewFile();

        }

        FileOutputStream fos = new FileOutputStream(mainPhotoFile);

        photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);


    }

    public void getPhotoList() throws FileNotFoundException { // Actualizamos la lista con los nombres de las fotos, con el objetivo de comprobar si ya hay una guardada con el nombre introducido por el usuario.

        File[] photoFiles = photoDir.listFiles();

        if (photoFiles.length > 1) {

            Arrays.sort(photoFiles, (a, b) -> Long.compare(a.lastModified(), b.lastModified())); // Ordenamos la lista de ficheros por fecha de modificacion, de forma que las primeras posiciones de la lista correspondan siempre a las fotos ya almacenadas, para que al guardar una nueva foto solo tengamos que agregar esa a la lista en lugar de reconstruirla entera.

        }

        for (int i = photosList.size(); i < photoFiles.length; i++) {

            photoFile = new File(photoDir + "/" + photoFiles[i].getName());

            photoBitmap = BitmapFactory.decodeStream(new FileInputStream(photoFile));

            System.out.println("Adding Photo " + (i+1) + " " + photoFiles[i].getName() + " ...");

            photosList.add(new Photo (photoFiles[i].getName(), photoBitmap));

        }

        System.out.println("Loading Photo List ...");

        printPhotoList();

    }

    public void returnToMain(View v) {

        Intent intent = new Intent(GalleryActivity.this, MainActivity.class);

        startActivity(intent);

    }

    public void printPhotoList() { // Esta funcion es unicamente para que nos muestre por consola las fotos disponibles, para facilitarnos saber que esta pasando en el programa.

        System.out.println("There are " + photosList.size() + " photos available.");

        for (int i = 0; i < photosList.size(); i++) {

            System.out.println("Photo Gallery " + (i+1) + " " + photosList.get(i).getFileName());

        }

    }

}
