package com.example.tabswithanimatedswipe;

import android.content.Context;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ViewHolder>{
    public ArrayList<String> images = new ArrayList<>();

    @NonNull
    @Override
    public ImageAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {

        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.listview_item_image, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemView);
        System.out.println("?????????????????????????????????????????");
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ImageAdapter.ViewHolder viewHolder, int position) {

        String item = images.get(position);
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!" + item);

        Glide.with(viewHolder.itemView.getContext())
                .load(item)
                .into(viewHolder.ivMovie);

    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void setItems(ArrayList<String> items) {
        System.out.println("000000000000000000000000000000000000000000000");
        this.images = items;
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView ivMovie;

        ViewHolder(View itemView) {
            super(itemView);

            ivMovie = itemView.findViewById(R.id.iv_item_movie);
        }
    }


}