package com.musicplayer.adapter;

import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.musicplayer.R;
import com.musicplayer.entity.MusicInfo;

import java.util.ArrayList;
import java.util.List;

public class ImageViewAdapter extends RecyclerView.Adapter<ImageViewAdapter.ImageViewHolder> {

    private List<MusicInfo> musicInfoList = new ArrayList<>();

    @NonNull
    @Override
    public ImageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_view, parent, false);

        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ImageViewHolder imageViewHolder, int position) {
        imageViewHolder.imageView.setImageURI(Uri.parse(musicInfoList.get(position).getImgUrl()));
    }

    @Override
    public int getItemCount() {
        return musicInfoList.size();
    }

    public void fillList(List<MusicInfo> musicInfoList){
        this.musicInfoList = musicInfoList;
    }

    static class ImageViewHolder extends RecyclerView.ViewHolder{

        private ImageView imageView;

        public ImageViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.image_recycler_view);
        }
    }
}
