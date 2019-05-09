package com.musicplayer.model;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.MediaStore;

import com.musicplayer.db.MyDatabaseHelper;
import com.musicplayer.entity.LocalMusic;
import com.musicplayer.entity.MusicInfo;

import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class HomeModel {
    private static final String TAG = "HomeModel";

    private SQLiteDatabase db;
    private Context mContext;

    public HomeModel(Context context) {
        mContext = context;
        MyDatabaseHelper databaseHelper = new MyDatabaseHelper(context, "SongStore.db", null, 1);
        db = databaseHelper.getWritableDatabase();
    }

    /**
     * 将选中的图片的路径存入数据库
     */
    public Observable<String> insertImgPath(final List<String> imgPathList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                for (String imagePath : imgPathList) {
                    ContentValues values = new ContentValues();
                    values.put("imgPath", imagePath);
                    db.insert("song", null, values);
                    subscriber.onNext("success");
                    subscriber.onCompleted();
                }
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 扫描本地的音频文件
     */
    public Observable<List<LocalMusic>> scanMusic() {
        return Observable.create(new Observable.OnSubscribe<List<LocalMusic>>() {
            @Override
            public void call(Subscriber<? super List<LocalMusic>> subscriber) {
                Cursor cursor = mContext.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                        null, null, null, MediaStore.Audio.AudioColumns.IS_MUSIC);

                List<LocalMusic> localMusicList = new ArrayList<>();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        LocalMusic localMusic = new LocalMusic();
                        localMusic.setMusicName(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME)));
                        localMusic.setMusicPath(cursor.getString(cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA)));
                        localMusicList.add(localMusic);
                    }
                }
                subscriber.onNext(localMusicList);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 将选中的音乐存入数据库
     */
    public Observable<String> insertMusicInfo(final List<LocalMusic> localMusicList) {
        return Observable.create(new Observable.OnSubscribe<String>() {
            @Override
            public void call(Subscriber<? super String> subscriber) {
                Cursor cursor = db.query("song", null, null, null,
                        null, null, "id");
                int id = 1;
                if (localMusicList.size() < cursor.getCount()) {          //选择的音乐比图片数量少
                    for (LocalMusic musicInfo : localMusicList) {
                        ContentValues values = new ContentValues();
                        values.put("songName", musicInfo.getMusicName());
                        values.put("songPath", musicInfo.getMusicPath());
                        String[] whereArgs = {String.valueOf(id++)};
                        db.update("song", values, "id=?", whereArgs);
                    }
                } else if (localMusicList.size() > cursor.getCount()) {          //选择的音乐比图片数量多
                    for (int i = 0; i < cursor.getCount(); i++) {                    //先更新数据库
                        ContentValues values = new ContentValues();
                        values.put("songName", localMusicList.get(i).getMusicName());
                        values.put("songPath", localMusicList.get(i).getMusicPath());
                        String[] whereArgs = {String.valueOf(id++)};
                        db.update("song", values, "id=?", whereArgs);
                    }
                    for (int i = cursor.getCount(); i < localMusicList.size(); i++) {              //在插入数据库
                        ContentValues values = new ContentValues();
                        values.put("songName", localMusicList.get(i).getMusicName());
                        values.put("songPath", localMusicList.get(i).getMusicPath());
                        db.insert("song", null, values);
                    }
                }
                subscriber.onNext(" ");
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 从数据库加载图片的路径
     */
    public Observable<List<MusicInfo>> loadSongInfo() {
        return Observable.create(new Observable.OnSubscribe<List<MusicInfo>>() {
            @Override
            public void call(Subscriber<? super List<MusicInfo>> subscriber) {
                Cursor cursor = db.query("song", null, null,
                        null, null, null, "id");

                List<MusicInfo> list = new ArrayList<>();
                if (cursor != null) {
                    while (cursor.moveToNext()) {
                        MusicInfo musicInfo = new MusicInfo();
                        musicInfo.setId(cursor.getInt(cursor.getColumnIndex("id")));
                        musicInfo.setImgUrl(cursor.getString(cursor.getColumnIndex("imgPath")));
                        musicInfo.setMusicName(cursor.getString(cursor.getColumnIndex("songName")));
                        musicInfo.setMusicUrl(cursor.getString(cursor.getColumnIndex("songPath")));
                        list.add(musicInfo);
                    }
                }
                subscriber.onNext(list);
                subscriber.onCompleted();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
