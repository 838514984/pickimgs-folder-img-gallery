package com.example.administrator.imagepicker.floderRelate;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.administrator.imagepicker.TopLayoutFragment;
import com.example.administrator.imagepicker.application.MyApplication;
import com.example.administrator.imagepicker.imagerelate.ImgListActivity;
import com.example.administrator.imagepicker.R;
import com.example.administrator.imagepicker.photoutil.FileTraversal;
import com.example.administrator.imagepicker.photoutil.PhotoListUtil;
import com.example.administrator.imagepicker.statusutil.StatusBarUtil;

import java.util.List;

public class FolderListActivity extends FragmentActivity implements AdapterView.OnItemClickListener {
    private PhotoListUtil photoListUtil;
    private List<FileTraversal> fileTraversals;
    private ListView listView;
    private TopLayoutFragment topLayoutFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.INSTANCE.addActivity(this);
        setContentView(R.layout.activity_folder_list);
        topLayoutFragment= (TopLayoutFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);
        StatusBarUtil.immersive(this);
        StatusBarUtil.setPaddingSmart(this,topLayoutFragment.getContentView());
        photoListUtil=new PhotoListUtil(this);
        fileTraversals=photoListUtil.getLocalImgFolderList();
        listView= (ListView) findViewById(R.id.lv);
        listView.setAdapter(new FloderAdapter(fileTraversals,this));
        listView.setOnItemClickListener(this);
        ImgListActivity.startActivity(this, ImgListActivity.ShowType.SHOW_ALL,null);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        FileTraversal fileTraversal=fileTraversals.get(position);

        ImgListActivity.startActivity(this, ImgListActivity.ShowType.SHOW_BYFILENAME,fileTraversal);
    }
}
