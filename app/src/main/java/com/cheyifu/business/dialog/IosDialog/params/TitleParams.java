package com.cheyifu.business.dialog.IosDialog.params;

import com.cheyifu.business.dialog.IosDialog.res.values.CircleColor;
import com.cheyifu.business.dialog.IosDialog.res.values.CircleDimen;

import java.io.Serializable;

/**
 * 标题参数
 * Created by hupei on 2017/3/30.
 */
public class TitleParams implements Serializable {
    /**
     * 标题
     */
    public String text;
    /**
     * 标题高度
     */
    public int height = CircleDimen.titleHeight;
    /**
     * 标题字体大小
     */
    public int textSize = CircleDimen.titleTextSize;
    /**
     * 标题字体颜色
     */
    public int textColor = CircleColor.title;
    /**
     * 标题背景颜色
     */
    public int backgroundColor;
}
