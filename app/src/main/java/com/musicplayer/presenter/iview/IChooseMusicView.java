package com.musicplayer.presenter.iview;

import com.musicplayer.entity.LocalMusic;
import com.musicplayer.entity.MusicInfo;

import java.util.List;

public interface IChooseMusicView {

    void scanMusic(List<LocalMusic> musicInfoList);

    void insertSuccess();
}
