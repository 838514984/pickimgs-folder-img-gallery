package com.example.administrator.imagepicker;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.administrator.imagepicker.statusutil.StatusBarUtil;

/**
 * Created by Administrator on 2017/7/25 0025.
 */

public class TopLayoutFragment extends Fragment {
    private TextView tv_title;
    private ImageView iv_back;
    private TextView tv_right;
    private View contentView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (contentView==null){
            contentView=inflater.inflate(R.layout.layout_my_topbar,container,false);
            tv_right= (TextView) contentView.findViewById(R.id.tv_right);
            tv_title= (TextView) contentView.findViewById(R.id.tv_title);
            iv_back= (ImageView) contentView.findViewById(R.id.iv_back);
            iv_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getActivity().finish();
                }
            });
        }
        return contentView;
    }

    public void setTitle(String title){
        tv_title.setText(title);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //StatusBarUtil.setPaddingSmart(this.getActivity(),contentView);
    }

    public void setRightView(String s, View.OnClickListener listener){
        tv_right.setVisibility(View.VISIBLE);
        tv_right.setText(s);
        tv_right.setOnClickListener(listener);
    }




}
