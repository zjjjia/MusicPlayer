package com.dxxyzhourong.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.stetho.Stetho;
import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.dxxyzhourong.R;
import com.dxxyzhourong.adapter.ViewPagerAdapter;
import com.dxxyzhourong.entity.MusicInfo;
import com.dxxyzhourong.presenter.HomePresenter;
import com.dxxyzhourong.presenter.iview.IHomeView;
import com.scrat.app.selectorlibrary.ImageSelector;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, IHomeView{

    private static final String TAG = "HomeActivity";

    private static final int REQUEST_CODE_SELECT_IMG = 1;
    private static final int MAX_SELECT_COUNT = 9;

    private TextView musicName;
    private ViewPager imgViewPager;
    private TextView TotalTimeTextView;
    private TextView currentTimeTextView;
    private ImageButton playerBtn;
    private ImageButton previousBtn;
    private ImageButton nextBtn;
    private ImageButton collectionBtn;
    private Button chooseImgBtn;
    private Button chooseSongBtn;
    private SeekBar playProgress;

    private ViewPagerAdapter adapter;
    private HomePresenter homePresenter;

    private List<MusicInfo> musicInfoList = new ArrayList<>();
    private int currentPosition = 0;

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
        musicName = findViewById(R.id.music_name);
        imgViewPager = findViewById(R.id.image_view_pager);
        TotalTimeTextView = findViewById(R.id.end_time);
        currentTimeTextView = findViewById(R.id.first_time);
        playerBtn = findViewById(R.id.player_btn);
        previousBtn = findViewById(R.id.previous_btn);
        nextBtn = findViewById(R.id.next_btn);
        collectionBtn = findViewById(R.id.collection_btn);
        chooseImgBtn = findViewById(R.id.choose_img);
        chooseSongBtn = findViewById(R.id.choose_song);
        playProgress = findViewById(R.id.play_progress);

        playerBtn.setOnClickListener(this);
        nextBtn.setOnClickListener(this);
        previousBtn.setOnClickListener(this);
        collectionBtn.setOnClickListener(this);
        chooseImgBtn.setOnClickListener(this);
        chooseSongBtn.setOnClickListener(this);

        initViewPager();
    }

    /**
     * 初始化ViewPager，左右滑动的图片的展示
     */
    private void initViewPager() {
        adapter = new ViewPagerAdapter(this);

        imgViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                homePresenter.switchMusic(position);
                musicName.setText(musicInfoList.get(position).getMusicName());
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.player_btn:
                homePresenter.play();
                break;
            case R.id.next_btn:
                currentPosition++;
                homePresenter.switchMusic(currentPosition);
                if (currentPosition < musicInfoList.size()
                        && musicInfoList.get(currentPosition).getMusicName() != null) {
                    musicName.setText(musicInfoList.get(currentPosition).getMusicName());
                    imgViewPager.setCurrentItem(currentPosition);
                }
                break;
            case R.id.previous_btn:
                currentPosition--;
                homePresenter.switchMusic(currentPosition);
                if (currentPosition >= 0) {
                    musicName.setText(musicInfoList.get(currentPosition).getMusicName());
                    imgViewPager.setCurrentItem(currentPosition);
                }
                break;
            case R.id.collection_btn:
                if (musicInfoList.get(currentPosition).getIsCollection() == 0) {
                    homePresenter.addCollection(musicInfoList.get(currentPosition).getId());
                    musicInfoList.get(currentPosition).setIsCollection(1);
                    collectionBtn.setImageResource(R.drawable.collectioned);
                } else {
                    homePresenter.cancelCollection(musicInfoList.get(currentPosition).getId());
                    musicInfoList.get(currentPosition).setIsCollection(0);
                    collectionBtn.setImageResource(R.drawable.un_collection);
                }
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE_SELECT_IMG) {
            List<String> imgPathList = ImageSelector.getImagePaths(data);
            if (imgPathList.isEmpty()) {
                return;
            }
            Log.d(TAG, "onActivityResult: " + imgPathList.toString());
            homePresenter.saveImagePath(imgPathList);
        }
    }

    @Override
    public void loadMusicInfo(List<MusicInfo> musicInfoList) {
        this.musicInfoList = musicInfoList;
        musicName.setText(musicInfoList.get(currentPosition).getMusicName());
        if (musicInfoList.get(currentPosition).getIsCollection() == 0) {
            collectionBtn.setImageResource(R.drawable.un_collection);
        } else {
            collectionBtn.setImageResource(R.drawable.collectioned);
        }

        adapter.fillList(musicInfoList);
        imgViewPager.setAdapter(adapter);
    }

    @Override
    public void insertSuccess() {
        Toast.makeText(this, "添加成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void insertError(String errorMsg) {
        Toast.makeText(this, "添加失败：" + errorMsg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void collectionSuccess() {
        Toast.makeText(this, "收藏成功", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void cancelSuccess() {
        Toast.makeText(this, "成功取消收藏", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void play() {
        playerBtn.setImageResource(R.drawable.play);
    }

    @Override
    public void pause() {
        playerBtn.setImageResource(R.drawable.pause);
    }

    @Override
    public void noMore(String message) {
        Toast.makeText(this, "没有更多" + message + "了", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showMusicTotalTime(String totalTime) {
        TotalTimeTextView.setText(totalTime);
    }

    @Override
    public void updateCurrentTime(String currentTime) {
        currentTimeTextView.setText(currentTime);
    }

    @Override
    public void updateProgress(int progress, int totalTime) {
        playProgress.setMax(totalTime);
        playProgress.setProgress(progress);

    }

    @Override
    public void playEnd() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                playerBtn.setImageResource(R.drawable.pause);
            }
        });
    }
}
