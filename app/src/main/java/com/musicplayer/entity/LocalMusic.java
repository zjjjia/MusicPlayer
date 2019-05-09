package com.musicplayer.entity;

public class LocalMusic {

    private String musicName;
    private String musicPath;

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getMusicPath() {
        return musicPath;
    }

    public void setMusicPath(String musicPath) {
        this.musicPath = musicPath;
    }

    @Override
    public String toString() {
        return "LocalMusic{" +
                "musicName='" + musicName + '\'' +
                ", musicPath='" + musicPath + '\'' +
                '}';
    }
}
