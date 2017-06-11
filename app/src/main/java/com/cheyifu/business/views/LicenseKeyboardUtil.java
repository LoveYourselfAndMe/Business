package com.cheyifu.business.views;

import android.app.Activity;
import android.content.Context;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.inputmethodservice.KeyboardView.OnKeyboardActionListener;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.cheyifu.business.R;
import com.cheyifu.business.activity.QuanActivity;
import com.cheyifu.business.utils.ToastUtil;

import java.util.List;


/**
 * Created by xxx on 2015/9/22.
 */
public class LicenseKeyboardUtil {
    private QuanActivity ctx;
    private KeyboardView keyboardView;
    private Keyboard k1;// 省份简称键盘
    private Keyboard k2;// 数字字母键盘

    private String provinceShort[];
    private String letterAndDigit[];

    private TextView edits[];
    private int currentTextView = 0;//默认当前光标在第一个TextView
    private LinearLayout ll_input;
    private boolean isInputFinished = false;
    private boolean isPressDelete = false;
    private boolean isPutProvince = false;
    private boolean isPutSecondLetter = false;
    private boolean isFirstDelete = false;
    private OnInputFinishedListener onInputFinishedListener;
    private OnInput mOnInput;


    public LicenseKeyboardUtil(KeyboardView keyboard_view, LinearLayout boxesContainer, Context ctx, TextView edits[]) {
        this.ll_input = boxesContainer;
        this.ctx = (QuanActivity) ctx;
        this.edits = edits;
        k1 = new Keyboard(ctx, R.xml.province_short_keyboard);
        k2 = new Keyboard(ctx, R.xml.lettersanddigit_keyboard);
        List<Keyboard.Key> keys = k2.getKeys();
        for (Keyboard.Key key : keys) {
            if (key.codes[0] == 17||key.codes[0]==18) {
                key.onPressed();
            }
        }
        keyboardView = (KeyboardView) ((Activity) ctx).findViewById(R.id.keyboard_view);
        keyboardView.setKeyboard(k1);
        keyboardView.setEnabled(true);
        //设置为true时,当按下一个按键时会有一个popup来显示<key>元素设置的android:popupCharacters=""
        keyboardView.setPreviewEnabled(false);
        //设置键盘按键监听器
        provinceShort = new String[]{"京", "津", "冀", "鲁", "晋", "蒙", "辽", "吉", "黑"
                , "沪", "苏", "浙", "皖", "闽", "赣", "豫", "鄂", "湘"
                , "粤", "桂", "渝", "川", "贵", "云", "藏", "陕", "甘"
                , "青", "琼", "新", "港", "澳", "台", "宁"};

        letterAndDigit = new String[]{"0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
                , "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P"
                , "A", "S", "D", "F", "G", "H", "J", "K", "L"
                , "Z", "X", "C", "V", "B", "N", "M"};

        keyboardView.setOnKeyboardActionListener(listener);
        keyboardView.setEnabled(true);


    }

    private OnKeyboardActionListener listener = new OnKeyboardActionListener() {
        @Override
        public void swipeUp() {
        }

        @Override
        public void swipeRight() {
        }

        @Override
        public void swipeLeft() {
        }

        @Override
        public void swipeDown() {
        }

        @Override
        public void onText(CharSequence text) {
        }

        @Override
        public void onRelease(int primaryCode) {

        }

        @Override
        public void onPress(int primaryCode) {
        }

        @Override
        public void onKey(int primaryCode, int[] keyCodes) {

            if (primaryCode == 112) {
//                //按钮不可点击
//                mOnInput.onInput();
                isPressDelete = true;
                //xml中定义的删除键值为112
                if (isFirstDelete) {
                    edits[currentTextView - 1].setText("");//将当前TextView置为""并currentTextView-1
                    currentTextView -= 2;
                    isFirstDelete = false;
                } else {
                    edits[currentTextView].setText("");//将当前TextView置为""并currentTextView-1

                    currentTextView--;
                }

                Log.i("currentEt", currentTextView + "");


                if (currentTextView < 0) {
                    edits[0].setText("");
                    isPutProvince = true;
                    currentTextView = 0;
                    keyboardView.setKeyboard(k1);
                }

            } else if (primaryCode == 66) { //xml中定义的完成键值为66
                getCarLiscense();

            } else if (primaryCode==17) {//

                return;

            } else if (primaryCode==18) {//O
                return;

            } else {

                Log.i("currentEt", currentTextView + "other");
                //其它字符按键
                if (currentTextView == 0) {
                    //如果currentTextView==0代表当前为省份键盘,
                    // 按下一个按键后,设置相应的TextView的值
                    // 然后切换为字母数字键盘
                    //currentTextView+1
                    if (isPressDelete && !isPutProvince) {
                        //第二位必须大写字母
                        if (letterAndDigit[primaryCode].matches("[A-Z]{1}")) {
                            edits[currentTextView + 1].setText(letterAndDigit[primaryCode]);
                            isPutSecondLetter = true;
                        }


                    } else {
                        edits[0].setText(provinceShort[primaryCode]);

                    }
                    currentTextView = 1;
                    //切换为字母数字键盘
                    keyboardView.setKeyboard(k2);

                    isPutProvince = false;
                    isPressDelete = false;

                } else {


                    //第二位必须大写字母
                    if (currentTextView == 1 && !letterAndDigit[primaryCode].matches("[A-Z]{1}")) {
                        return;
                    }

                    if (isPressDelete) {
                        edits[currentTextView + 1].setText(letterAndDigit[primaryCode]);

                        isPressDelete = false;


                        currentTextView++;
                    } else {
                        if (!isPutSecondLetter) {
                            edits[currentTextView].setText(letterAndDigit[primaryCode]);
                        } else {
                            isPutSecondLetter = false;
                        }
                    }
//                    isFirstDelete = true;
                    currentTextView++;

                    if (currentTextView > 6) {
                        getCarLiscense();
                        currentTextView = 6;
                        isFirstDelete = false;
                        return;
                    }
                }
                isFirstDelete = true;
            }
        }
    };

    /**
     * 显示键盘
     */
    public void showKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.GONE || visibility == View.INVISIBLE) {
            keyboardView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏键盘
     */
    public void hideKeyboard() {
        int visibility = keyboardView.getVisibility();
        if (visibility == View.VISIBLE) {
            keyboardView.setVisibility(View.INVISIBLE);
        }
    }


    public void getCarLiscense() {

        String license = "";
        for (int i = 0; i < 7; i++) {
            String nowEdit = edits[i].getText().toString();
            if (TextUtils.isEmpty(nowEdit)) {
                ToastUtil.showStringToast("请输入完整车牌号");
//                isInputFinished = false;

            } else {
                license += edits[i].getText().toString();
                if (i == 6) {
//                    isInputFinished = true;
                    onInputFinishedListener.onInputFinished(license);
                }
            }

        }


//        if (isInputFinished) {
//
////            currentTextView = 0;
////            for (int i = 0; i < 7; i++) {
////                edits[i].setText("");
////            }
////            keyboardView.setKeyboard(k1);
//
//            if (license != null) {
//                onInputFinishedListener.onInputFinished(license);
//            }
//
//
//
//
//        } else {
//            ToastUtil.showStringToast("请输入完整车牌号");
//        }
    }

    /**
     * 输入完成时的回调接口
     */
    public interface OnInputFinishedListener {
        void onInputFinished(String str);
    }

    /**
     * 对外开放的方法
     *
     * @param onInputFinishedListener
     */
    public void setOnInputFinishedListener(OnInputFinishedListener onInputFinishedListener) {
        this.onInputFinishedListener = onInputFinishedListener;
    }

    /**
     * 输入时的回调接口
     */
    public interface OnInput {
        void onInput();
    }

    /**
     * 对外开放的方法
     *
     * @param
     */
    public void setOnInputListener(OnInput onInput) {
        this.mOnInput = onInput;
    }

}