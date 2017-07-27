package com.example.administrator.imagepicker.gallery;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.imagepicker.GlideApp;
import com.example.administrator.imagepicker.R;
import com.example.administrator.imagepicker.bean.SelectStatusBean;
import com.github.chrisbanes.photoview.PhotoView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class MyPagerAdapter extends PagerAdapter {
    private ArrayList<String> mdatas;

    private Context context;
    public MyPagerAdapter(ArrayList<String> mdatas,Context context){
        this.mdatas=mdatas;
        this.context=context;
    }


    @Override
    public int getCount() {
        return mdatas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view==object;
    }


    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        PhotoView photoView=new PhotoView(context);
        GlideApp.with(context).load(mdatas.get(position)).placeholder(R.drawable.loading_01).fitCenter().into(photoView);
        container.addView(photoView);
        return photoView ;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (((View)object).getParent()!=null){
            container.removeView((View) object);
        }

    }



}
