package com.musicplayer.presenter;

import android.app.Activity;
import android.content.Context;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.UiThread;
import android.util.Log;

import com.musicplayer.entity.MusicInfo;
import com.musicplayer.model.HomeModel;
import com.musicplayer.presenter.iview.IHomeView;
import com.musicplayer.view.HomeActivity;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import rx.Subscriber;

public class HomePresenter {

    private static final String TAG = "HomePresenter";

    private Context mContext;
    private HomeModel homeModel;
    private IHomeView iHomeView;

    private MediaPlayer mediaPlayer;
    private List<MusicInfo> mMusicInfoList = new ArrayList<>();
    private Thread thread;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            iHomeView.updateCurrentTime(formatTime(msg.what));
            iHomeView.updateProgress(msg.what, mediaPlayer.getDuration());
        }
    };

    public HomePresenter(Context context, IHomeView iHomeView) {
        this.mContext = context;
        homeModel = new HomeModel(mContext);
        this.iHomeView = iHomeView;

        loadInfo();
    }

    /**
     * 存储图片信息
     */
    public void saveImagePath(List<String> imgPathList) {
        homeModel.insertImgPath(imgPathList)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error: " + e);
                        iHomeView.insertError(e.getMessage());
                    }

                    @Override
                    public void onNext(String s) {
                        iHomeView.insertSuccess();
                        loadInfo();
                    }
                });
    }

    /**
     * 加载图片和音乐信息
     */
    public void loadInfo() {
        homeModel.loadSongInfo()
                .subscribe(new Subscriber<List<MusicInfo>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error: " + e);
                        iHomeView.insertError(e.getMessage());
                    }

                    @Override
                    public void onNext(List<MusicInfo> musicInfoLList) {
                        mMusicInfoList = musicInfoLList;
                        initMediaPlayer();
                        iHomeView.loadMusicInfo(musicInfoLList);
                    }
                });
    }

    /**
     * 初始化MediaPlayer
     */
    private void initMediaPlayer() {
        if (mediaPlayer == null) {
            mediaPlayer = new MediaPlayer();
            if (!mMusicInfoList.isEmpty()) {
                try {
                    mediaPlayer.reset();
                    mediaPlayer.setDataSource(mMusicInfoList.get(0).getMusicUrl());                  //每次启动默认从第一首开始播放
                    mediaPlayer.prepare();
                    iHomeView.showMusicTotalTime(formatTime(mediaPlayer.getDuration()));
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.e(TAG, "error: " + e);
                }
            } else {
                Log.d(TAG, "music List is empty");
            }
        }
    }

    /**
     * 播放或者暂停
     */
    public void play() {
        if (mediaPlayer != null) {
            if (mediaPlayer.isPlaying()) {

                mediaPlayer.pause();
                iHomeView.pause();
            } else {
                mediaPlayer.start();
                iHomeView.play();
            }
            thread = new Thread(new MusicThread());
            thread.start();
        }
    }

    /**
     * 切歌
     */
    public void switchMusic(int position) {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            try {
                Log.d(TAG, "position: " + position);
                if (position >= 0 && position < mMusicInfoList.size()) {
                    mediaPlayer.reset();
                    if (mMusicInfoList.get(position).getMusicUrl() != null) {
                        mediaPlayer.setDataSource(mMusicInfoList.get(position).getMusicUrl());
                        mediaPlayer.prepare();
                        iHomeView.showMusicTotalTime(formatTime(mediaPlayer.getDuration()));
                        mediaPlayer.start();
                        iHomeView.play();
                    } else {
                        iHomeView.noMore("音乐");
                        iHomeView.pause();
                    }
                } else {
                    iHomeView.noMore("图片");
                    iHomeView.pause();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            thread = new Thread(new MusicThread());
            thread.start();
        } else {
            Log.e(TAG, "mediaPlayer is null");
        }
    }

    /**
     * 添加收藏
     */
    public void addCollection(int id) {
        homeModel.addCollection(id)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        iHomeView.collectionSuccess();
                    }
                });
    }

    /**
     * 取消收藏
     */
    public void cancelCollection(int id) {
        homeModel.cancelCollection(id)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        iHomeView.cancelSuccess();
                    }
                });
    }

    /**
     * 将毫秒转为秒
     */
    private String formatTime(int length) {
        Date date = new Date(length);
        SimpleDateFormat sdf = new SimpleDateFormat("mm:ss");
        String totalTime = sdf.format(date);
        return totalTime;
    }


    class MusicThread implements Runnable {

        @Override
        public void run() {
            while (mediaPlayer != null && mediaPlayer.isPlaying()) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.sendEmptyMessage(mediaPlayer.getCurrentPosition());
            }

            iHomeView.playEnd();
        }
    }

}
