package com.example.administrator.imagepicker.bean;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Administrator on 2017/7/26 0026.
 */

public class SelectStatusBean implements Serializable {
    public ArrayList<String> paths;
    public int selectIndex;
    public boolean[] allSelectStatus;
    public ArrayList<String> selectPaths;
}
