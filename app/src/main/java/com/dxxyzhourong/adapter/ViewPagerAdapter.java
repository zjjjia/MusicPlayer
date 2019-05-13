package com.dxxyzhourong.adapter;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.dxxyzhourong.entity.MusicInfo;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends PagerAdapter {

    private List<ImageView> imageViewList = new ArrayList<>();
    private Context mContext;

    public ViewPagerAdapter(Context context){
        this.mContext= context;
    }

    public void fillList(List<MusicInfo> list){
        for(MusicInfo musicInfo : list){
            ImageView view = new ImageView(mContext);
            view.setImageURI(Uri.parse(musicInfo.getImgUrl()));
            imageViewList.add(view);
        }
    }

    @Override
    public int getCount() {
        return imageViewList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position){
        ImageView imageView = imageViewList.get(position);
        container.addView(imageView);

        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object){
        container.removeView(imageViewList.get(position));
    }
}
