package com.dxxyzhourong.presenter;

import android.content.Context;
import android.util.Log;

import com.dxxyzhourong.entity.LocalMusic;
import com.dxxyzhourong.model.HomeModel;
import com.dxxyzhourong.presenter.iview.IChooseMusicView;

import java.util.List;

import rx.Subscriber;

public class ChooseMusicPresenter {

    private static final String TAG = "ChooseMusicPresenter";

    private Context mContext;
    private IChooseMusicView iChooseMusicView;
    HomeModel homeModel;

    public ChooseMusicPresenter(Context context, IChooseMusicView iChooseMusicView) {
        this.mContext = context;
        this.iChooseMusicView = iChooseMusicView;

        homeModel = new HomeModel(mContext);
    }

    /**
     * 扫描手机中本地的音频文件
     */
    public void scanMusicFile() {
        homeModel.scanMusic()
                .subscribe(new Subscriber<List<LocalMusic>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error: " + e);
                    }

                    @Override
                    public void onNext(List<LocalMusic> localMusics) {
                        Log.d(TAG, "pathList: " + localMusics.toString());
                        iChooseMusicView.scanMusic(localMusics);
                    }
                });
    }

    /**
     * 将选中的音乐存入数据库
     */
    public void insertMusic(List<LocalMusic> localMusicList) {
        homeModel.insertMusicInfo(localMusicList)
                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "error: " + e);
                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "insert success");
                        iChooseMusicView.insertSuccess();
                    }
                });
    }
}
