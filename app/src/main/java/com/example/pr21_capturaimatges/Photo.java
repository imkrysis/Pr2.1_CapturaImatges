package com.example.pr21_capturaimatges;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Photo implements Parcelable { // Extendemos Parcelable para poder enviar una ArrayList de Photos en el intent.

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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(fileName);
        photoBitmap.writeToParcel(dest, flags);

    }

    public static final Creator<Photo> CREATOR = new Creator<Photo>() {
        @Override
        public Photo createFromParcel(Parcel source) {
            return new Photo(source);
        }

        @Override
        public Photo[] newArray(int size) {
            return new Photo[size];
        }

    };

}
