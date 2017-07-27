package com.example.administrator.imagepicker.floderRelate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.imagepicker.GlideApp;
import com.example.administrator.imagepicker.R;
import com.example.administrator.imagepicker.photoutil.FileTraversal;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class FloderAdapter extends BaseAdapter {
    private List<FileTraversal> mList;
    private Context context;

    public FloderAdapter(List<FileTraversal> list, Context context){
        this.mList=list;
        this.context=context;
    }


    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView ==null ){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_view_img_folder,parent,false);
            holder=new Holder((ImageView) getview(R.id.iv_filephoto,convertView),(TextView) getview(R.id.tv_filecount,convertView),(TextView)getview(R.id.tv_filename,convertView));
            convertView.setTag(holder);
        }else{
            holder= (Holder) convertView.getTag();
        }

        holder.tv_count.setText("("+mList.get(position).filecontent.size()+")");
        holder.tv_floderName.setText(mList.get(position).filename);
        GlideApp.with(context).load(mList.get(position).filecontent.get(0)).centerCrop().into(holder.iv);


        return convertView;
    }


    class Holder{
        ImageView iv;
        TextView tv_count;
        TextView tv_floderName;
        public Holder (ImageView iv,TextView tv_count,TextView tv_floderName){
            this.iv=iv;
            this.tv_count=tv_count;
            this.tv_floderName=tv_floderName;

        }
    }

    private <T extends View> T getview(int id,View convertView){
        return (T) convertView.findViewById(id);
    }
}
