package com.dxxyzhourong.presenter.iview;

import com.dxxyzhourong.entity.LocalMusic;

import java.util.List;

public interface IChooseMusicView {

    void scanMusic(List<LocalMusic> musicInfoList);

    void insertSuccess();
}
