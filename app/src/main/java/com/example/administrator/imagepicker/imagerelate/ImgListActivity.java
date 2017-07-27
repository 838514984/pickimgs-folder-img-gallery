package com.example.administrator.imagepicker.imagerelate;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.imagepicker.R;
import com.example.administrator.imagepicker.TopLayoutFragment;
import com.example.administrator.imagepicker.application.MyApplication;
import com.example.administrator.imagepicker.bean.PhotoEvent;
import com.example.administrator.imagepicker.bean.SelectStatusBean;
import com.example.administrator.imagepicker.floderRelate.FolderListActivity;
import com.example.administrator.imagepicker.gallery.GralleyPhotoActivity;
import com.example.administrator.imagepicker.photoutil.FileTraversal;
import com.example.administrator.imagepicker.photoutil.PhotoListUtil;
import com.example.administrator.imagepicker.statusutil.StatusBarUtil;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

public class ImgListActivity extends FragmentActivity implements ImgShowGridViewAdapter.CheckListener, ImgShowGridViewAdapter.ItemClickListener, View.OnClickListener {


    public enum ShowType {
        SHOW_ALL, SHOW_BYFILENAME
    }

    private ShowType showType = ShowType.SHOW_ALL;
    TopLayoutFragment topLayoutFragment;
    FileTraversal fileTraversal;
    GridView gridView;
    PhotoListUtil photoListUtil;
    TextView tvSelectPicNum;
    List<String> imgSelectPaths = new ArrayList<>();
    ImgShowGridViewAdapter adapter;

    public static void startActivity(Context context, ShowType showType, FileTraversal list) {
        Intent intent = new Intent(context, ImgListActivity.class);
        intent.putExtra("data", list);
        intent.putExtra("type", showType);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_list);
        StatusBarUtil.immersive(this);
        MyApplication.INSTANCE.addActivity(this);
        topLayoutFragment = (TopLayoutFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        StatusBarUtil.setPaddingSmart(this, topLayoutFragment.getContentView());
        //最下面的文字提示
        tvSelectPicNum = (TextView) findViewById(R.id.tv_check_num);
        gridView = (GridView) findViewById(R.id.gv);
        handleData();
        if (showType == ShowType.SHOW_ALL) {
            topLayoutFragment.setTitle("所有图片");
            photoListUtil = new PhotoListUtil(this);
            List<String> list = photoListUtil.getAllLatestImgs(photoListUtil.getLocalImgFolderList());
            fileTraversal = new FileTraversal();
            fileTraversal.filename = "所有图片";
            fileTraversal.filecontent = list;
        } else if (showType == ShowType.SHOW_BYFILENAME) {
            topLayoutFragment.setTitle(fileTraversal.filename);
        }
        adapter = new ImgShowGridViewAdapter(fileTraversal, this);
        adapter.setItemClickListener(this);
        adapter.setCheckListener(this);
        gridView.setAdapter(adapter);

        tvSelectPicNum.setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    private void handleData() {
        Intent inten = getIntent();
        showType = (ShowType) inten.getSerializableExtra("type");
        fileTraversal = inten.getParcelableExtra("data");

    }

    @Override
    public void check(CheckBox checkBox, int position, boolean isChecked, String filePtah) {
        imgSelectPaths.add(filePtah);
    }


    @Override//itemClick
    public void click(View view, int position, boolean status[]) {
        //TODO  点击image跳转到Gallery
        SelectStatusBean selectStatusBean = new SelectStatusBean();
        selectStatusBean.allSelectStatus = status;
        selectStatusBean.paths = (ArrayList<String>) fileTraversal.filecontent;
        selectStatusBean.selectIndex = position;
        selectStatusBean.selectPaths= (ArrayList<String>) imgSelectPaths;
        GralleyPhotoActivity.startActivity(this, selectStatusBean);
    }

    @Subscribe
    public void onEventReceive(PhotoEvent photoEvent) {
        if (photoEvent.fromClass == null)
            return;
        imgSelectPaths=photoEvent.photos;
        adapter.updateCheckStatus(photoEvent.isCheckStatus);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_check_num:
                if (imgSelectPaths.size() == 0) {
                    Toast.makeText(this, "至少选择一张图片！", 0).show();
                    return;
                }
                PhotoEvent photoEvent = new PhotoEvent();
                photoEvent.photos = imgSelectPaths;
                EventBus.getDefault().post(photoEvent);
                MyApplication.INSTANCE.finishRangActivity(FolderListActivity.class, ImgListActivity.class);
                break;
        }
    }
}
