package com.cheyifu.business.activity;

import android.content.Intent;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cheyifu.business.R;
import com.cheyifu.business.base.BaseActivity;
import com.cheyifu.business.base.BaseBean;
import com.cheyifu.business.base.Urls;
import com.cheyifu.business.dialog.HTLoadBlock;
import com.cheyifu.business.dialog.IosDialog.CircleDialog;
import com.cheyifu.business.global.BaseApplication;
import com.cheyifu.business.global.Constans;
import com.cheyifu.business.model.CheckBean;
import com.cheyifu.business.model.LoginBean;
import com.cheyifu.business.utils.AndroidUtils;
import com.cheyifu.business.utils.AppManager;
import com.cheyifu.business.utils.JsonUtil;
import com.cheyifu.business.utils.Logger;
import com.cheyifu.business.utils.SPUtils;
import com.cheyifu.business.utils.ToastUtil;
import com.cheyifu.business.views.LicenseKeyboardUtil;

import org.angmarch.views.NiceSpinner;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class QuanActivity extends BaseActivity {
    //车牌号键盘
    @Bind(R.id.et_car_license_inputbox1)
    TextView et_car_license_inputbox1;

    @Bind(R.id.et_car_license_inputbox2)
    TextView et_car_license_inputbox2;

    @Bind(R.id.et_car_license_inputbox3)
    TextView et_car_license_inputbox3;

    @Bind(R.id.et_car_license_inputbox4)
    TextView et_car_license_inputbox4;

    @Bind(R.id.et_car_license_inputbox5)
    TextView et_car_license_inputbox5;

    @Bind(R.id.et_car_license_inputbox6)
    TextView et_car_license_inputbox6;
    @Bind(R.id.et_car_license_inputbox7)
    TextView et_car_license_inputbox7;

    @Bind(R.id.keyboard_view)
    KeyboardView keyboard_view;


    @Bind(R.id.ll_license_input_boxes_content)
    LinearLayout ll_license_input_boxes_content;

    @Bind(R.id.quan_conner)
    LinearLayout quan_content;


    @Bind(R.id.my_center)
    RelativeLayout mycenter;

    private List<LoginBean.ItemsBean> itemses;
    private LicenseKeyboardUtil keyboardUtil;
    private String carCode;//输入的车牌号码
    private int index = -1;


    @OnClick(R.id.my_center)
    public void setMycenter() {
        Intent intent = new Intent(QuanActivity.this, MyCenterActivity.class);
        startActivity(intent);
    }

    @Bind(R.id.submit_quan)
    RelativeLayout submit;

    @OnClick(R.id.submit_quan)
    public void setSubmit() {
        //生成优惠券
        String parkingPhone = this.parkingPhone.getText().toString().trim();
        String trim = parkingName.getText().toString().trim();
        if (TextUtils.isEmpty(trim)) {
            ToastUtil.showStringToast("请选择车场");
            return;
        }
        if (TextUtils.isEmpty(parkingPhone)) {
            ToastUtil.showStringToast("请输入手机号");
            return;
        }

        if (TextUtils.isEmpty(carCode)) {
            ToastUtil.showStringToast("请输入车牌号");
            return;
        }
        //检测是否是App 用户
        isAppUser();

    }

    private void submitQuan(String phone, String carCode) {
        final RequestQueue requestQueue = Volley.newRequestQueue(BaseApplication.getmContext());
        Map<String, String> params = new HashMap<>();
        if (index != -1) {
            params.put("merchantId", itemses.get(index).getMerchantId());
            params.put("mobile", phone);
            params.put("parkingId", itemses.get(index).getParkingId());
            params.put("plate", carCode);
        }
        JSONObject jsonObject = new JSONObject(params);
        HTLoadBlock.showLoadingMessage(QuanActivity.this, "加载中..", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.URL_SAVE_INFO, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HTLoadBlock.dismiss();
                BaseBean baseBean = JsonUtil.parseJsonToBean(response.toString(), BaseBean.class);
                if (baseBean.getResult() == 0) {
                    //生成成功弹框
                    new CircleDialog.Builder(QuanActivity.this)
                            .setCanceledOnTouchOutside(false)
                            .setCancelable(false)
                            .setTitle("温馨提示")
                            .setText("优惠券已生成")
//                                .setNegative("取消", null)
                            .setPositive("确定", null)
                            .show();

                } else {
                    ToastUtil.showStringToast(baseBean.getStrError());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTLoadBlock.dismiss();
                ToastUtil.showToast(R.string.error_result);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }

    @Bind(R.id.et_parking_name)
    NiceSpinner parkingName;


    @OnClick(R.id.ll_license_input_boxes_content)
    public void setParkingNum() {
        keyboardUtil.showKeyboard();
        parkingPhone.setCursorVisible(false);//失去光标
        AndroidUtils.hideSoftInput(QuanActivity.this,parkingPhone);
    }


    @Bind(R.id.et_parking_phone)
    EditText parkingPhone;

    @OnClick(R.id.et_parking_phone)
    public void setParkingPhone() {
//        parkingPhone.setCursorVisible(true);//失去光标
//        parkingPhone.setFocusable(true);
//        parkingPhone.setFocusableInTouchMode(true);
    }


    private ArrayList<String> strings = new ArrayList<>();
//    List<String> dataset = new LinkedList<>(Arrays.asList("One", "Two", "Three", "Four", "Five"));


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quan);
        AppManager.getAppManager().addActivity(this);

        ButterKnife.bind(this);

       /* Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            itemses = (List<LoginBean.ItemsBean>) bundle.getSerializable("parkingList");
            if (itemses != null && itemses.size() > 0) {
                for (int i = 0; i < itemses.size(); i++) {
                    String parkingName = itemses.get(i).getParkingName();
                    strings.add(parkingName);
                }
            }
        }*/
        //访问网络
        String user = SPUtils.getString(BaseApplication.getmContext(), Constans.userName, "");
        String pwd = SPUtils.getString(BaseApplication.getmContext(), Constans.passsWord, "");
        if (TextUtils.isEmpty(user)) {
            Intent intent=new Intent(QuanActivity.this,MainActivity.class);
            startActivity(intent);
            AppManager.getAppManager().finishActivity();
            return;
        }

        if (user != null && pwd != null) {
            login(user, pwd);
        }

        //选择
//        parkingName.setTextColor(getResources().getColor(R.color.normal_text));
//        parkingName.attachDataSource(strings);
        parkingName.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


            }
        });
        parkingName.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                index = position;
//                ToastUtil.showStringToast("点击的条目aa==" + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        int selectedIndex = parkingName.getSelectedIndex();
        index = selectedIndex;

        //键盘
        keyboardUtil = new LicenseKeyboardUtil(keyboard_view, quan_content, QuanActivity.this,
                new TextView[]{et_car_license_inputbox1, et_car_license_inputbox2, et_car_license_inputbox3,
                        et_car_license_inputbox4, et_car_license_inputbox5, et_car_license_inputbox6, et_car_license_inputbox7});


        keyboardUtil.setOnInputFinishedListener(new LicenseKeyboardUtil.OnInputFinishedListener() {
            @Override
            public void onInputFinished(String code) {
                if (keyboardUtil != null) {
                    keyboardUtil.hideKeyboard();//隐藏
                }
//                keyboard_view.setVisibility(View.INVISIBLE);
                carCode = code;
                ToastUtil.showStringToast(code);

                parkingPhone.setCursorVisible(true);

            }


        });
        keyboardUtil.setOnInputListener(new LicenseKeyboardUtil.OnInput() {
            @Override
            public void onInput() {
                //监听
//                parkingPhone.setFocusable(false);
//                parkingPhone.setFocusableInTouchMode(false);

            }
        });

        //整体隐藏软键盘
        quan_content.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return AndroidUtils.hideKeyboardboolean(QuanActivity.this, getCurrentFocus());
            }
        });
        parkingPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 11) {
                    parkingPhone.setCursorVisible(false);
                    AndroidUtils.hideSoftInput(QuanActivity.this,parkingPhone);
                } else {
                    parkingPhone.setCursorVisible(true);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
//       parkingPhone.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View v, boolean hasFocus) {
//                if (hasFocus) {//得到焦点
////                    if (keyboardUtil != null) {
////                        keyboardUtil.hideKeyboard();//隐藏
////
////                    }
////                    int visibility = keyboard_view.getVisibility();
////                    if (visibility == View.VISIBLE) {
////                        keyboard_view.setVisibility(View.INVISIBLE);
////                    }
//
//                } else {
//                    parkingPhone.setCursorVisible(false);//失去光标
//                    AndroidUtils.hideSoftInput(QuanActivity.this,parkingPhone);
//                }
//            }
//        });

    }

    private void login(String user, String pass) {

        RequestQueue requestQueue = Volley.newRequestQueue(BaseApplication.getmContext());
        Map<String, String> params = new HashMap<>();
        params.put("loginName", user);
        params.put("loginPwd", pass);
        JSONObject jsonObject = new JSONObject(params);
        HTLoadBlock.showLoadingMessage(QuanActivity.this, "加载中..", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.URL_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HTLoadBlock.dismiss();
                Logger.i("返回的数据==", response.toString());
                LoginBean loginBean = JsonUtil.parseJsonToBean(response.toString(), LoginBean.class);
                if (loginBean.getResult() == 0) {
                    itemses = loginBean.getItems();
                    if (itemses != null && itemses.size() > 0) {
                        for (int i = 0; i < itemses.size(); i++) {
                            String parkingName = itemses.get(i).getParkingName();
                            strings.add(parkingName);
                        }
                    }
                    parkingName.setTextColor(getResources().getColor(R.color.normal_text));
                    parkingName.attachDataSource(strings);

                } else {
                    new CircleDialog.Builder(QuanActivity.this)
                            .setCanceledOnTouchOutside(false)
                            .setCancelable(false)
                            .setTitle("温馨提示")
                            .setText(loginBean.getStrError())
//                                .setNegative("取消", null)
                            .setPositive("确定", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    SPUtils.clearAll(BaseApplication.getmContext());
                                    Intent intent = new Intent(QuanActivity.this, MainActivity.class);
                                    startActivity(intent);
                                    AppManager.getAppManager().finishActivity();

                                }
                            })
                            .show();


                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTLoadBlock.dismiss();
                ToastUtil.showToast(R.string.error_result);
            }
        });
        requestQueue.add(jsonObjectRequest);

    }

    public void isAppUser() {
        final RequestQueue requestQueue = Volley.newRequestQueue(BaseApplication.getmContext());
        Map<String, String> params = new HashMap<>();
        if (index != -1) {
            params.put("parkingId", itemses.get(index).getParkingId());
        }
        params.put("mobile", parkingPhone.getText().toString().trim());
        JSONObject jsonObject = new JSONObject(params);
        HTLoadBlock.showLoadingMessage(QuanActivity.this, "加载中..", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.URL_CHECK, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HTLoadBlock.dismiss();
                CheckBean checkBean = JsonUtil.parseJsonToBean(response.toString(), CheckBean.class);

                if (checkBean.getResult() == 0) {
                    //0可以通过
                    if (checkBean.getSubResult().equals("1")) {
                        //访问下个接口
                        submitQuan(parkingPhone.getText().toString(), carCode);
                    } else {
                        //不可通过弹窗
                        //取消预约
                        new CircleDialog.Builder(QuanActivity.this)
                                .setCanceledOnTouchOutside(false)
                                .setCancelable(false)
                                .setTitle("温馨提示")
                                .setText(checkBean.getStrError())
//                                .setNegative("取消", null)
                                .setPositive("确定", null)
                                .show();
                    }

                } else {
                    ToastUtil.showStringToast(checkBean.getStrError());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                HTLoadBlock.dismiss();
                ToastUtil.showToast(R.string.error_result);
            }
        });
        requestQueue.add(jsonObjectRequest);
    }
}
