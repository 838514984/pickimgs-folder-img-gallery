package com.example.administrator.imagepicker.usecamera;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.administrator.imagepicker.R;
import com.example.administrator.imagepicker.bean.PhotoEvent;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class TakePhotoActivity extends AppCompatActivity {

    public static final int PHOTO_REQUEST_TAKEPHOTO = 1;
    public static final int PHOTO_REQUEST_CUT = 2;
    private String tempFileFolderpath = Environment.getExternalStorageDirectory() + "/photo";
    private File tempFile = new File(Environment.getExternalStorageDirectory() + "/photo", getPhotoFileName());
    private File tempFileAfterResize = new File(Environment.getExternalStorageDirectory() + "/photo", "resize" + getPhotoFileName());

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_photo);
        resloveIntent(getIntent());
        File file = new File(Environment.getExternalStorageDirectory() + "/photo");
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
            }
        }

        init();
    }

    /**
     * 处理参数
     */

    public void resloveIntent(Intent intent) {

    }

    private void init() {

        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFile));
        startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // TODO Auto-generated method stub

        switch (requestCode) {
            case PHOTO_REQUEST_TAKEPHOTO:
                if (tempFile.length() == 0) {
                    finish();
                    return;
                }

                startPhotoZoom(Uri.fromFile(tempFile), dp2px(150));
                break;


            case PHOTO_REQUEST_CUT:
                if (tempFileAfterResize.length() != 0)
                    returnResult();
                else
                    finish();
                break;

            default:
                finish();
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);

    }

    private void startPhotoZoom(Uri uri, int size) {
        //删除之前的resize文件
        /*File folder = new File(tempFileFolderpath);
        File[] files = folder.listFiles();
        for(File file:files){
            if(file.getName().startsWith("resize")) {
                file.delete();
            }
        }*/

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");

        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        intent.putExtra("outputX", size);
        intent.putExtra("outputY", size);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(tempFileAfterResize));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());

        startActivityForResult(intent, PHOTO_REQUEST_CUT);
    }
    //自己需要的操作
    private void returnResult() {
        PhotoEvent photoEvent=new PhotoEvent();
        photoEvent.photos=new ArrayList<String>(){{add(tempFileAfterResize.getAbsolutePath());}};
        EventBus.getDefault().post(photoEvent);
        if (tempFile.exists())
            tempFile.delete();
        finish();
    }

    private String getPhotoFileName() {
        Date date = new Date(System.currentTimeMillis());
        SimpleDateFormat dateFormat = new SimpleDateFormat("'IMG'_yyyyMMdd_HHmmss");
        return dateFormat.format(date) + ".jpg";
    }

    public int dp2px(int dp) {
        return (int) (getResources().getDisplayMetrics().scaledDensity * dp);
    }
}
