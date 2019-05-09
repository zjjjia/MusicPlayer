package com.musicplayer.entity;


public class MusicInfo {

    private int id;
    private String imgUrl;
    private String musicName;
    private String musicUrl;
    private boolean isCollection;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMusicName() {
        return musicName;
    }

    public void setMusicName(String musicName) {
        this.musicName = musicName;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getMusicUrl() {
        return musicUrl;
    }

    public void setMusicUrl(String songUrl) {
        this.musicUrl = songUrl;
    }

    public boolean isCollection() {
        return isCollection;
    }

    public void setCollection(boolean collection) {
        isCollection = collection;
    }

    @Override
    public String toString() {
        return "MusicInfo{" +
                "id=" + id +
                ", imgUrl='" + imgUrl + '\'' +
                ", musicName='" + musicName + '\'' +
                ", musicUrl='" + musicUrl + '\'' +
                ", isCollection=" + isCollection +
                '}';
    }
}
