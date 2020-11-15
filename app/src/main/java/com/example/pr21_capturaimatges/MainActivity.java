package com.example.pr21_capturaimatges;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;

public class MainActivity extends AppCompatActivity {

    final File filesDir = new File ("/data/user/0/com.example.pr21_capturaimatges/files");
    final File photoDir = new File("/data/user/0/com.example.pr21_capturaimatges/files/photos");
    final File mainPhotoFile = new File("/data/user/0/com.example.pr21_capturaimatges/files/main.jpeg");
    final String photoExt = ".jpeg";

    ArrayList<Photo> photosList = new ArrayList<Photo>();

    File photoFile;
    String nickname;
    Bitmap photoBitmap;

    ImageView imageViewPhoto;
    EditText editTextName;

    AlertDialog.Builder adb;
    AlertDialog adFoundPhoto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageViewPhoto = findViewById(R.id.imageViewPhoto);

        editTextName = findViewById(R.id.editTextName);

        try {

            checkDir();
            checkMainPhoto();
            getPhotoList();

        } catch (FileNotFoundException e) {

            e.printStackTrace();

        }

        setFoundPhotoDialog();

    }

    public void checkDir() { // Comprobamos si existen los directorio de almacenamiento, en caso negativo los creamos.

        if (!filesDir.exists()) {

            filesDir.mkdir();

            photoDir.mkdir();

            return;

        }

        if (!photoDir.exists()) {

            photoDir.mkdir();

        }

    }

    public void checkMainPhoto() throws FileNotFoundException { // Comprobamos si el usuario ha escogido foto predeterminada para mostrar en la aplicacion, en ese caso la mostramos.

        if (mainPhotoFile.exists()) {

            imageViewPhoto.setImageBitmap(BitmapFactory.decodeStream(new FileInputStream(mainPhotoFile)));

        }

    }

    public void checkingName(View v) { // Comprobamos si existe ya una foto almacenada con ese nombre y en ese caso lanzamos un dialogo. Si aun no existe la foto, llamamos a la funcion para capturarla.

        if (editTextName.getText().toString().equals("main")) {

            editTextName.setText("");

            Toast.makeText(getApplicationContext(), "Palabra reservada, introduce otro nombre.", Toast.LENGTH_SHORT).show();

            return;

        } else if(editTextName.getText().toString().equals("")) {

            Toast.makeText(getApplicationContext(), "Introduce un nombre...", Toast.LENGTH_SHORT).show();

            return;

        } else {

            nickname = editTextName.getText().toString();

            for (int i = 0; i < photosList.size(); i++) {

                if ((nickname + photoExt).equalsIgnoreCase(photosList.get(i).getFileName())) { // Comparamos el nombre del hipotetico fichero, con los nombres de las fotos almacenadas.

                    showFoundPhotoDialog(); // Si ya hay una foto guardada con ese nombre, mostramos el dialogo para preguntar al usuario si quiere reemplazarla.

                    return;

                }

            }

            takePhoto(); // Si no hay una foto guardada con ese nombre, llamamos al intent de la camara.

        }

    }

    public void takePhoto() { // Hacemos un intent para sacar la foto.

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(takePictureIntent, 1);

    }

    public void onActivityResult(int requestCode, int resultCode, Intent takePictureIntent) { // Si el resultado del intent es positivo, llamamos a la funcion para almacenar la foto.

        super.onActivityResult(requestCode, resultCode, takePictureIntent);

        if (requestCode == 1 && resultCode == RESULT_OK) {

            Bundle extras = takePictureIntent.getExtras();
            photoBitmap = (Bitmap) extras.get("data");

            editTextName.setText("");

            savePhoto();

        }

    }

    public void savePhoto() { // Guardamos la foto en el archivo correspondiente, segun el nombre del usuario.

        photoFile = new File(photoDir + "/" + nickname + photoExt);

        try {

            if (!photoFile.exists()) { // Si el archivo no existe, lo creamos.

                photoFile.createNewFile();

            }

            FileOutputStream fos = new FileOutputStream(photoFile);

            photoBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fos);

            getPhotoList(); // Despues de guardarla, actualizamos la lista.

        } catch (Exception e) {

            e.printStackTrace();

        }

    }

    public void setFoundPhotoDialog() {

        adb = new AlertDialog.Builder(this);

        adb.setTitle("Ya existe una foto con ese nombre.");
        adb.setMessage("¿Desea reemplazarla?");

        adb.setPositiveButton("Sí", null);
        adb.setNegativeButton("No", null);

        adFoundPhoto = adb.create();

        adFoundPhoto.setCancelable(false);
        adFoundPhoto.setCanceledOnTouchOutside(false);

    }

    public void showFoundPhotoDialog() { // Mostramos el dialogo correspondiente, al haber encontrado una foto guardada con el mismo nombre. Si quiere reemplazarla hacemos la foto.

        adFoundPhoto.show();

        Button positiveButton = adFoundPhoto.getButton(AlertDialog.BUTTON_POSITIVE);
        positiveButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                adFoundPhoto.dismiss();

                takePhoto();

            }
        });

        Button negativeButton = adFoundPhoto.getButton(AlertDialog.BUTTON_NEGATIVE);
        negativeButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                adFoundPhoto.dismiss();

            }
        });

    }

    public void getPhotoList() throws FileNotFoundException { // Actualizamos la lista con los nombres de las fotos, con el objetivo de comprobar si ya hay una guardada con el nombre introducido por el usuario.

        File[] photoFiles = photoDir.listFiles();

        if (photoFiles.length > 1) {

            Arrays.sort(photoFiles, (a, b) -> Long.compare(a.lastModified(), b.lastModified())); // Ordenamos la lista de ficheros por fecha de modificacion, de forma que las primeras posiciones de la lista correspondan siempre a las fotos ya almacenadas, para que al guardar una nueva foto solo tengamos que agregar esa a la lista en lugar de reconstruirla entera.

        }

        if (photosList.size() > 0 && photosList.size() == photoFiles.length) { // Si la foto ha sido reemplazada, significa que el numero total de fotos no ha variado.

            for (int i = 0; i < photosList.size(); i++) {

                if (photosList.get(i).getFileName().equalsIgnoreCase(nickname + photoExt)) { // Buscamos que foto ha sido la reemplazada.

                    photosList.get(i).setPhotoBitmap(photoBitmap); // Reemplazamos el bitmap al actual.

                    System.out.println("Replacing Photo " + (i+1) + " " + photosList.get(i).getFileName() + " ...");

                    return;

                }

            }

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

    public void printPhotoList() { // Esta funcion es unicamente para que nos muestre por consola las fotos disponibles, para facilitarnos saber que esta pasando en el programa.

        for (int i = 0; i < photosList.size(); i++) {

            System.out.println("Photo " + (i+1) + " " + photosList.get(i).getFileName());

        }

    }

    public void openGallery(View v) { // Comprobamos si hay alguna foto guardada y en ese caso abrimos la galeria. Si aun no hay ninguna almacenada, notificamos al usuario.

        if (photosList.size() > 0) {

            Intent intent = new Intent(MainActivity.this, GalleryActivity.class);

            startActivity(intent);

        } else {

            Toast.makeText(getApplicationContext(), "No hay fotos guardadas... Vuelve a intentarlo cuando guardes alguna.", Toast.LENGTH_LONG).show();

        }

    }

}