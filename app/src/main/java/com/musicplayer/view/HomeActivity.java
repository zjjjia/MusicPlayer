package com.musicplayer.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.musicplayer.R;
import com.musicplayer.adapter.ImageViewAdapter;
import com.musicplayer.entity.MusicInfo;
import com.musicplayer.presenter.HomePresenter;
import com.musicplayer.presenter.iview.IHomeView;
import com.scrat.app.selectorlibrary.ImageSelector;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener , IHomeView {

    private static final String TAG = "HomeActivity";

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;

    private TextView songName;
    private RecyclerView imageRecyclerView;
    private TextView durationText;
    private ImageButton playerBtn;
    private ImageButton previouBtn;
    private ImageButton nextBtn;
    private ImageButton collectionBtn;
    private ImageButton voiceBtn;
    private Button chooseImgBtn;
    private Button chooseSongBtn;

    private ImageViewAdapter imageViewAdapter;
    private HomePresenter homePresenter;

    private List<MusicInfo> musicInfoList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();
        homePresenter = new HomePresenter(this, this);
        initView();
    }

    /**
     * sqlite可视化工具初始化
     */
    private void init() {
        Stetho.initializeWithDefaults(this);
        new OkHttpClient.Builder()
                .addNetworkInterceptor(new StethoInterceptor())
                .build();
    }

    /**
     * 初始化Home界面
     */
    private void initView() {
        songName = findViewById(R.id.song_name);
        imageRecyclerView = findViewById(R.id.image_recycler_view);
        durationText = findViewById(R.id.duration_text);
        playerBtn = findViewById(R.id.player_btn);
        previouBtn = findViewById(R.id.previous_btn);
        nextBtn = findViewById(R.id.next_btn);
        collectionBtn = findViewById(R.id.collection_btn);
        voiceBtn = findViewById(R.id.voice_controller_btn);
        chooseImgBtn = findViewById(R.id.choose_img);
        chooseSongBtn = findViewById(R.id.choose_song);

        playerBtn.setOnClickListener(this);
        chooseImgBtn.setOnClickListener(this);
        chooseSongBtn.setOnClickListener(this);

        initRecyclerView();
    }

    /**
     * 初始化Home界面的recyclerView，用来展示左右滑动的图片
     */
    private void initRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        imageRecyclerView.setLayoutManager(layoutManager);

        PagerSnapHelper pagerSnapHelper = new PagerSnapHelper();                       //PagerSnapHelper用来限制recyclerView一次只能滑动一页
        pagerSnapHelper.attachToRecyclerView(imageRecyclerView);

        imageViewAdapter = new ImageViewAdapter();
        imageRecyclerView.setAdapter(imageViewAdapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.player_btn:
                homePresenter.loadInfo();
                break;
            case R.id.choose_img:
                Log.d(TAG, "choose img");
                ImageSelector.show(this, REQUEST_CODE_SELECT_IMG, MAX_SELECT_COUNT);
                break;
            case R.id.choose_song:
                Intent intent = new Intent(this, ChooseMusicActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        if(requestCode == REQUEST_CODE_SELECT_IMG){
            List<String> imgPathList = ImageSelector.getImagePaths(data);
            if(imgPathList.isEmpty()){
                return;
            }
            Log.d(TAG, "onActivityResult: " + imgPathList.toString());
            homePresenter.saveImagePath(imgPathList);
        }
    }

    @Override
    public void loadSongInfo(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
        imageViewAdapter.fillList(musicInfoList);
        imageViewAdapter.notifyDataSetChanged();
    }

    @Override
    public void insertSuccess() {
        Toast.makeText(this, "添加成功", Toast.LENGTH_LONG).show();
    }

    @Override
    public void insertError(String errorMsg) {
        Toast.makeText(this, "添加失败：" + errorMsg, Toast.LENGTH_LONG).show();
    }
}
