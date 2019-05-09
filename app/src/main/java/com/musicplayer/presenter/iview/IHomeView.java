package com.musicplayer.presenter.iview;


import com.musicplayer.entity.MusicInfo;

import java.util.List;

public interface IHomeView {

    void insertSuccess();

    void insertError(String errorMsg);

    void loadSongInfo(List<MusicInfo> musicInfoList);
}
