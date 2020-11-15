package com.example.pr21_capturaimatges;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import javax.xml.transform.Result;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    static ArrayList<Photo> photosList;

    RecyclerViewAdapter(ArrayList<Photo> photosList) {

        this.photosList = photosList;

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView textViewPhotoName;
        public ImageView imageViewPhotoThumbnail;

        public ViewHolder(View itemView) {

            super(itemView);

            this.textViewPhotoName = itemView.findViewById(R.id.textViewPhotoName);
            this.imageViewPhotoThumbnail = itemView.findViewById(R.id.imageViewPhotoThumbnail);

        }

        public void setData(Photo photosData) {

            this.textViewPhotoName.setText(photosData.getFileName());
            this.imageViewPhotoThumbnail.setImageBitmap(photosData.getPhotoBitmap());

        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_photos_list, null, false);

        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {

        holder.setData(photosList.get(position));

    }

    @Override
    public int getItemCount() {

        return photosList.size();

    }
}
