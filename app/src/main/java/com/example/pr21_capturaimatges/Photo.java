package com.example.pr21_capturaimatges;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

public class Photo { // Extendemos Parcelable para poder enviar una ArrayList de Photos en el intent.

    String fileName;
    Bitmap photoBitmap;

    Photo (String fileName, Bitmap photoBitmap) {

        this.fileName = fileName;
        this.photoBitmap = photoBitmap;

    }

    Photo (Parcel source) {

        this.fileName = source.readString();
        this.photoBitmap = Bitmap.CREATOR.createFromParcel(source);

    }

    public String getFileName() {
        return fileName;
    }

    public Bitmap getPhotoBitmap() {
        return photoBitmap;
    }

    public void setPhotoBitmap(Bitmap photoBitmap) {

        this.photoBitmap = photoBitmap;

    }

}
