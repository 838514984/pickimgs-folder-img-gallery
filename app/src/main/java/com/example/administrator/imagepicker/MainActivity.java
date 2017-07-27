package com.example.administrator.imagepicker;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.administrator.imagepicker.application.MyApplication;
import com.example.administrator.imagepicker.bean.PhotoEvent;
import com.example.administrator.imagepicker.floderRelate.FolderListActivity;
import com.example.administrator.imagepicker.imagerelate.ImgShowGridViewAdapter;
import com.example.administrator.imagepicker.photoutil.FileTraversal;
import com.example.administrator.imagepicker.usecamera.TakePhotoActivity;
import com.tbruyelle.rxpermissions2.RxPermissions;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private Button button;
    private GridView gridView;
    private ImgShowGridViewAdapter adapter=new ImgShowGridViewAdapter(new FileTraversal(),this,false);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MyApplication.INSTANCE.addActivity(this);
        setContentView(R.layout.activity_main);
        button= (Button) findViewById(R.id.btn);
        gridView= (GridView) findViewById(R.id.gv);
        gridView.setAdapter(adapter);
        button.setOnClickListener(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onClick(View v) {
        RxPermissions permissions=new RxPermissions(this);
        permissions.request(Manifest.permission.READ_EXTERNAL_STORAGE).subscribe(new Observer<Boolean>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull Boolean aBoolean) {
                if (aBoolean){
                    //Toast.makeText(MainActivity.this,"授权成功",0).show();

                    new AlertDialog.Builder(MainActivity.this).setItems(new String[]{"拍摄", "从相册获取"}, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which){
                                case 0:
                                    startActivity(new Intent(MainActivity.this,TakePhotoActivity.class));
                                    break;
                                case 1:
                                    startActivity(new Intent(MainActivity.this,FolderListActivity.class));
                                    break;
                            }
                            dialog.dismiss();
                        }
                    }).create().show();



                }else{
                    Toast.makeText(MainActivity.this,"授权失败",0).show();
                }
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventReceive(PhotoEvent photoEvent){
        if (photoEvent.fromClass!=null)
            return;
        adapter.updateData(photoEvent.photos);
    }

}
