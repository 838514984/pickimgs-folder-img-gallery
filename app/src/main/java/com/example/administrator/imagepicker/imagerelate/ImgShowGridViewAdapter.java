package com.example.administrator.imagepicker.imagerelate;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.example.administrator.imagepicker.GlideApp;
import com.example.administrator.imagepicker.R;
import com.example.administrator.imagepicker.photoutil.FileTraversal;

import java.util.List;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class ImgShowGridViewAdapter extends BaseAdapter {
    private FileTraversal fileTraversal;
    private Context context;
    private boolean[] checks;
    private CheckListener checkListener;
    private ItemClickListener itemClickListener;
    private boolean isShowCheckBox=true;
    public ImgShowGridViewAdapter(FileTraversal mLists, Context context){
        this.fileTraversal=mLists;
        this.context=context;
        checks=new boolean[mLists.filecontent.size()];
    }

    public ImgShowGridViewAdapter(FileTraversal mLists, Context context,boolean isShowCheckBox){
        this.fileTraversal=mLists;
        this.context=context;
        this.isShowCheckBox=isShowCheckBox;
    }


    public void setCheckListener(CheckListener checkListener){
        this.checkListener=checkListener;
    }

    public void setItemClickListener(ItemClickListener itemClickListener){
        this.itemClickListener=itemClickListener;
    }

    @Override
    public int getCount() {
        return fileTraversal.filecontent.size();
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
    public View getView(final int position, View convertView, ViewGroup parent) {
        Holder holder;
        if (convertView==null){
            convertView= LayoutInflater.from(context).inflate(R.layout.item_view_photo_image_item,parent,false);
            holder=new Holder();
            holder.cb_checkbox= (CheckBox) convertView.findViewById(R.id.checkBox1);
            holder.iv_img= (ImageView) convertView.findViewById(R.id.imageView1);
            convertView.setTag(holder);
            ViewGroup.LayoutParams layoutParams=holder.iv_img.getLayoutParams();
            int length=context.getResources().getDisplayMetrics().widthPixels/4;
            layoutParams.width=length;
            layoutParams.height=length;
            if (!isShowCheckBox)
                holder.cb_checkbox.setVisibility(View.GONE);
        }else{
            holder= (Holder) convertView.getTag();
        }

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (itemClickListener!=null)
                    itemClickListener.click(v,position,checks);
            }
        });

        GlideApp.with(context).load(fileTraversal.filecontent.get(position)).placeholder(R.drawable.loading_01).centerCrop().into(holder.iv_img);
        holder.cb_checkbox.setChecked(checks[position]);
        holder.cb_checkbox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checks[position]=!checks[position];
                if (checkListener!=null)
                    checkListener.check((CheckBox) v,position,checks[position],fileTraversal.filecontent.get(position));
            }
        });


        return convertView;
    }

    class Holder{
        ImageView iv_img;
        CheckBox cb_checkbox;
    }

    public interface CheckListener{
        void check(CheckBox checkBox,int position,boolean isChecked,String filePtah);
    }
    public interface ItemClickListener{
        void click(View view,int position,boolean[] checkStatus);
    }

    public void updateData(List<String> strings){
        fileTraversal.filecontent.addAll(strings);
        checks=new boolean[fileTraversal.filecontent.size()];
        notifyDataSetChanged();
    }

    public void updateCheckStatus(boolean checkstatus[]){
        this.checks=checkstatus;
        notifyDataSetChanged();
    }

}
