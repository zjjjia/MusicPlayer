package com.musicplayer.presenter;

import android.content.Context;
import android.util.Log;

import com.musicplayer.entity.LocalMusic;
import com.musicplayer.entity.MusicInfo;
import com.musicplayer.model.HomeModel;
import com.musicplayer.presenter.iview.IHomeView;

import java.util.List;

import rx.Subscriber;

public class HomePresenter {

    private static final String TAG = "HomePresenter";

    private Context mContext;
    private HomeModel homeModel;
    private IHomeView iHomeView;

    public HomePresenter(Context context, IHomeView iHomeView) {
        this.mContext = context;
        homeModel = new HomeModel(mContext);
        this.iHomeView = iHomeView;
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
                        iHomeView.loadSongInfo(musicInfoLList);
                        iHomeView.insertSuccess();
                    }
                });
    }
}
