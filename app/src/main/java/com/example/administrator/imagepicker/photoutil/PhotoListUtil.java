package com.example.administrator.imagepicker.photoutil;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.MediaStore;
import android.widget.ImageView;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class PhotoListUtil {
	public final static String FolderName = "folder_name";
	public final static String FolderPath = "folder_path";
	public final static String FolderFileCount = "folder_file_count";

	private Context mContext;

	public PhotoListUtil(Context mContext)
	{
		this.mContext=mContext;
	}

	public ArrayList<String> listAlldir()
	{
		Intent intent = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
		Uri uri = intent.getData();
		ArrayList<String> list = new ArrayList<String>();
		String[] proj = {MediaStore.Images.Media.DATA};
		Cursor cursor = mContext.getContentResolver().query(uri, proj, null, null, null);//managedQuery(uri, proj, null, null, null);
		while(cursor.moveToNext()){
			String path = cursor.getString(0);
			list.add(new File(path).getAbsolutePath());
		}
		cursor.close();
		return list;
	}
	/**
	 * 获取folder的列表
	 * @return
	 */
	public List<FileTraversal> getLocalImgFolderList(){
		List<FileTraversal> data = new ArrayList<FileTraversal>();
		String filename = "";
		List<String> mAllImgList = listAlldir();
		List<String> mResultList = new ArrayList<String>();

		if (mAllImgList!=null) {
			Set set = new TreeSet();
			String[]str;
			for (int i = 0; i < mAllImgList.size(); i++) {
				mResultList.add(getfileinfo(mAllImgList.get(i)));
			}
			for (int i = 0; i < mResultList.size(); i++) {
				set.add(mResultList.get(i));
			}
			str = (String[]) set.toArray(new String[0]);
			for (int i = 0; i < str.length; i++) {
				filename = str[i];
				FileTraversal ftl = new FileTraversal();
				ftl.filename = filename;
				data.add(ftl);
			}

			for (int i = 0; i < data.size(); i++) {
				for (int j = 0; j < mAllImgList.size(); j++) {
					if (data.get(i).filename.equals(getfileinfo(mAllImgList.get(j)))) {
						data.get(i).filecontent.add(mAllImgList.get(j));
					}
				}
			}
		}
		return data;
	}
	/**
	 * 获取图片
	 * @param imageFilePath
	 * @param dw
	 * @param dh
	 * @return
	 * @throws FileNotFoundException
	 */
	public Bitmap getPathBitmap(Uri imageFilePath, int dw, int dh)
			throws FileNotFoundException
	{

		BitmapFactory.Options op = new BitmapFactory.Options();
		op.inJustDecodeBounds = true;

		Bitmap pic = BitmapFactory.decodeStream(mContext.getContentResolver().openInputStream(imageFilePath),
				null, op);

		int wRatio = (int) Math.ceil(op.outWidth / (float) dw);
		int hRatio = (int) Math.ceil(op.outHeight / (float) dh);

		if (wRatio > 1 && hRatio > 1) {
			if (wRatio > hRatio) {
				op.inSampleSize = wRatio;
			} else {
				op.inSampleSize = hRatio;
			}
		}
		op.inJustDecodeBounds = false;
		pic = BitmapFactory.decodeStream(mContext.getContentResolver()
				.openInputStream(imageFilePath), null, op);

		return pic;
	}

	public String getfileinfo(String data){
		String filename[]= data.split("/");
		if (filename!=null) {
			return filename[filename.length-2];
		}
		return null;
	}
	/**
	 * 获取图片的任务
	 * @param mImageView
	 * @param mCallback
	 * @param params
	 */
	public void startGetBitmapTask(ImageView mImageView, ImgCallBack mCallback, String... params){
		LoadBitAsynk loadBitAsynk=new LoadBitAsynk(mImageView,mCallback);
		loadBitAsynk.execute(params);
	}

	public class LoadBitAsynk extends AsyncTask<String, Integer, Bitmap> {

		ImageView mImageView;
		ImgCallBack mCallback;

		LoadBitAsynk(ImageView mImageView, ImgCallBack mCallback){
			this.mImageView = mImageView;
			this.mCallback = mCallback;
		}

		@Override
		protected Bitmap doInBackground(String... params) {
			Bitmap bitmap=null;
			try {
				if (params!=null) {
					for (int i = 0; i < params.length; i++) {
						bitmap = getPathBitmap(Uri.fromFile(new File(params[i])), 200, 200);
					}
				}
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}

			return bitmap;
		}

		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if (result!=null) {
				mCallback.resultImgCall(mImageView, result);
			}
		}


	}

	/**
	 * 获取最近修改的所有图片地址
	 */
	public List<String> getAllLatestImgs(List<FileTraversal> mFolderList) {
		List<String> allImgs = new ArrayList<String>();

		for(int i = 0; i < mFolderList.size(); i++) {
			allImgs.addAll(mFolderList.get(i).filecontent);
		}
		Collections.sort(allImgs, new Comparator<String>() {
			@Override
			public int compare(String lhs, String rhs) {
				if(new File(rhs).lastModified() == new File(lhs).lastModified()) {
					return 0;
				}else if(new File(rhs).lastModified() > new File(lhs).lastModified()) {
					return 1;
				}else {
					return -1;
				}
			}
		});
		return allImgs;
	}

}
