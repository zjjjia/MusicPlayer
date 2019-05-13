package com.dxxyzhourong.presenter.iview;


import com.dxxyzhourong.entity.MusicInfo;

import java.util.List;

public interface IHomeView {

    /**
     * 存入数据库成功
     */
    void insertSuccess();

    /**
     * 存入数据库失败
     */
    void insertError(String errorMsg);

    /**
     * 从数据库加载音乐信息
     */
    void loadMusicInfo(List<MusicInfo> musicInfoList);

    /**
     * 显示音乐总时长
     */
    void showMusicTotalTime(String totalTime);

    /**
     * 更新已播放时长
     */
    void updateCurrentTime(String currentTime);

    /**
     * 更新播放进度条
     */
    void updateProgress(int progress, int totalTime);

    /**
     * 成功添加收藏
     */
    void collectionSuccess();

    /**
     * 成功取消收藏
     */
    void cancelSuccess();

    /**
     * 开始播放
     */
    void play();

    /**
     * 暂停
     */
    void pause();

    /**
     * 没有更多的提示
     */
    void noMore(String message);

    void playEnd();
}
