package com.musicplayer.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.musicplayer.R;
import com.musicplayer.adapter.LocalMusicListAdapter;
import com.musicplayer.entity.LocalMusic;
import com.musicplayer.entity.MusicInfo;
import com.musicplayer.presenter.ChooseMusicPresenter;
import com.musicplayer.presenter.iview.IChooseMusicView;

import java.lang.invoke.CallSite;
import java.util.ArrayList;
import java.util.List;

public class ChooseMusicActivity extends AppCompatActivity implements IChooseMusicView {

    private static final String TAG = "ChooseMusicActivity";

    private Button cancelBtn;
    private Button confirmBtn;
    private RecyclerView musicRecyclerView;
    private LocalMusicListAdapter musicListAdapter;
    private ChooseMusicPresenter presenter;

    private List<LocalMusic> choosedMusicList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_music);

        presenter = new ChooseMusicPresenter(this, this);

        presenter.scanMusicFile();
        initView();
    }

    private void initView(){
        cancelBtn = findViewById(R.id.cancel_btn);
        confirmBtn = findViewById(R.id.confirm_btn);

        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                presenter.insertMusic(choosedMusicList);
                finish();
            }
        });

        initRecyclerView();
    }

    private void initRecyclerView(){
        musicRecyclerView = findViewById(R.id.music_list);

        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        musicRecyclerView.setLayoutManager(layoutManager);

        musicListAdapter = new LocalMusicListAdapter();
        musicRecyclerView.setAdapter(musicListAdapter);

        musicListAdapter.setListener(new LocalMusicListAdapter.ChooseListener() {
            @Override
            public void choosedListener(LocalMusic choosedMusic) {
                choosedMusicList.add(choosedMusic);
            }

            @Override
            public void unChooseListener(LocalMusic localMusic) {
                choosedMusicList.remove(localMusic);
            }
        });
    }

    @Override
    public void scanMusic(List<LocalMusic> musicInfoList) {
        musicListAdapter.fillList(musicInfoList);
        musicListAdapter.notifyDataSetChanged();
    }

    @Override
    public void insertSuccess() {
        Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
    }
}
