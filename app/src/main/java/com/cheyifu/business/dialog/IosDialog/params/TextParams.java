package com.cheyifu.business.dialog.IosDialog.params;

import com.cheyifu.business.dialog.IosDialog.res.values.CircleColor;
import com.cheyifu.business.dialog.IosDialog.res.values.CircleDimen;

import java.io.Serializable;

/**
 * 文本内容参数
 * Created by hupei on 2017/3/30.
 */
public class TextParams implements Serializable {
    /**
     * body文本内间距
     */
    public int[] padding;
    /**
     * 文本
     */
    public String text;
    /**
     * 文本高度
     */
    public int height = CircleDimen.titleHeight;
    /**
     * 文本背景颜色
     */
    public int backgroundColor;
    /**
     * 文本字体颜色
     */
    public int textColor = CircleColor.content;
    /**
     * 文本字体大小
     */
    public int textSize = CircleDimen.contentTextSize;
}
