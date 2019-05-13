package com.dxxyzhourong.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.dxxyzhourong.R;
import com.dxxyzhourong.entity.LocalMusic;

import java.util.ArrayList;
import java.util.List;

public class LocalMusicListAdapter extends RecyclerView.Adapter<LocalMusicListAdapter.MyViewHolder> {

    private List<LocalMusic> localMusicList = new ArrayList<>();
    private ChooseListener listener;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_music_list, parent, false);

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, final int position) {
        myViewHolder.musicName.setText(localMusicList.get(position).getMusicName());

        myViewHolder.isChooseBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    listener.choosedListener(localMusicList.get(position));
                } else {
                    listener.unChooseListener(localMusicList.get(position));
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return localMusicList.size();
    }

    public void fillList(List<LocalMusic> localMusicList) {
        this.localMusicList = localMusicList;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView musicName;
        CheckBox isChooseBox;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            musicName = itemView.findViewById(R.id.music_name);
            isChooseBox = itemView.findViewById(R.id.choose_music_box);
        }
    }

    public void setListener(ChooseListener listener) {
        this.listener = listener;
    }

    public interface ChooseListener {
        /**
         * 被选中的的music
         */
        void choosedListener(LocalMusic localMusic);

        /**
         * 选中的被取消
         */
        void unChooseListener(LocalMusic localMusic);
    }
}
