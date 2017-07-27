package com.example.administrator.imagepicker.gallery;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.administrator.imagepicker.R;
import com.example.administrator.imagepicker.bean.PhotoEvent;
import com.example.administrator.imagepicker.bean.SelectStatusBean;
import com.example.administrator.imagepicker.statusutil.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;

public class GralleyPhotoActivity extends AppCompatActivity implements View.OnClickListener {
    ViewPager vp;
    TextView tvCheckOrUncheck;
    TextView tvCompleteCheck;
    SelectStatusBean selectStatusBean;
    MyPagerAdapter adapter;
    PhotoEvent photoEvent;
    ArrayList<String> selectPath;
    boolean isChecked;

    public static void startActivity(Context context , SelectStatusBean selectStatusBean){
        Intent intent=new Intent(context,GralleyPhotoActivity.class);
        intent.putExtra("data", selectStatusBean);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gralley_photo);
        StatusBarUtil.immersive(this);
        handleData();
        vp= (ViewPager) findViewById(R.id.vp_container);
        tvCheckOrUncheck= (TextView) findViewById(R.id.tv_check_or_unckeck);
        tvCompleteCheck= (TextView) findViewById(R.id.tv_check_complete);
        tvCheckOrUncheck.setOnClickListener(this);
        tvCompleteCheck.setOnClickListener(this);
        isChecked=selectStatusBean.allSelectStatus[selectStatusBean.selectIndex];

        photoEvent=new PhotoEvent();
        photoEvent.photos=selectStatusBean.selectPaths;
        selectPath =selectStatusBean.selectPaths;
        adapter=new MyPagerAdapter(selectStatusBean.paths,this);
        vp.setAdapter(adapter);
        vp.setCurrentItem(selectStatusBean.selectIndex);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (selectStatusBean.allSelectStatus[position]){
                    isChecked=true;
                    tvCheckOrUncheck.setText("取消选择");
                }else{
                    isChecked=false;
                    tvCheckOrUncheck.setText("选中");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        changeText(isChecked);
    }

    private void handleData() {
        selectStatusBean= (SelectStatusBean) getIntent().getSerializableExtra("data");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_check_complete:
                photoEvent.isCheckStatus=selectStatusBean.allSelectStatus;
                photoEvent.fromClass="gralley";
                EventBus.getDefault().post(photoEvent);
                finish();
                break;
            case R.id.tv_check_or_unckeck:
                int position=vp.getCurrentItem();
                isChecked=!isChecked;
                if (isChecked){
                    photoEvent.photos.add(selectStatusBean.paths.get(position));
                    changeText(isChecked);
                }else{
                    photoEvent.photos.remove(selectStatusBean.paths.get(position));
                    changeText(isChecked);
                }
                selectStatusBean.allSelectStatus[position]=!selectStatusBean.allSelectStatus[position];

                break;
        }
    }

    private void changeText(boolean b){
        if (b){
            tvCheckOrUncheck.setText("取消选择");
        }else{
            tvCheckOrUncheck.setText("选中");
        }

        tvCompleteCheck.setText("完成("+selectPath.size()+")");

    }
}
