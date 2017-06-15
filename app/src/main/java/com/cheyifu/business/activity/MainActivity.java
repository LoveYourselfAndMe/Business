package com.cheyifu.business.activity;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.cheyifu.business.R;
import com.cheyifu.business.base.BaseActivity;
import com.cheyifu.business.base.Urls;
import com.cheyifu.business.dialog.HTLoadBlock;
import com.cheyifu.business.global.BaseApplication;
import com.cheyifu.business.global.Constans;
import com.cheyifu.business.model.LoginBean;
import com.cheyifu.business.utils.AndroidUtils;
import com.cheyifu.business.utils.AppManager;
import com.cheyifu.business.utils.JsonUtil;
import com.cheyifu.business.utils.Logger;
import com.cheyifu.business.utils.MD5;
import com.cheyifu.business.utils.SPUtils;
import com.cheyifu.business.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    private boolean ischeck = true;
    @Bind(R.id.iv_hide)
    ImageView iv_hide;

    @OnClick(R.id.iv_hide)
    public void setIv_hide() {
        if (ischeck) {
            iv_hide.setBackgroundResource(R.drawable.mima_icon_active);
            ischeck = !ischeck;
            pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            iv_hide.setBackgroundResource(R.drawable.mima_icon);
            ischeck = !ischeck;
            pass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    @Bind(R.id.ll_main)
    LinearLayout ll_main;

    @Bind(R.id.submit)
    RelativeLayout submit;

    @Bind(R.id.et_pass)
    EditText pass;

    @Bind(R.id.et_user)
    EditText user;


    @OnClick(R.id.submit)
    public void setSubmit() {
        String pass = this.pass.getText().toString().trim();
        String user = this.user.getText().toString().trim();


        if (TextUtils.isEmpty(user)) {
            ToastUtil.showStringToast("用户名不能为空");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            ToastUtil.showStringToast("请输入密码");
            return;
        }
        if (pass.length() < 6) {
            ToastUtil.showStringToast("密码不能低于6位");
            return;
        }
        login(user, MD5.getEncode(pass));

    }

    public void login(final String user, final String pass) {
        RequestQueue requestQueue = Volley.newRequestQueue(BaseApplication.getmContext());
        Map<String, String> params = new HashMap<>();
        params.put("loginName", user);
        params.put("loginPwd", pass);
        JSONObject jsonObject = new JSONObject(params);
        HTLoadBlock.showLoadingMessage(MainActivity.this, "加载中..", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.URL_LOGIN, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HTLoadBlock.dismiss();
                Logger.i("返回的数据==", response.toString());
                LoginBean loginBean = JsonUtil.parseJsonToBean(response.toString(), LoginBean.class);
                if (loginBean.getResult() == 0) {
                    SPUtils.putString(MainActivity.this, Constans.userName, user);
                    SPUtils.putString(MainActivity.this, Constans.passsWord, pass);

//                    List<LoginBean.ItemsBean> itemses = loginBean.getItems();
                    Intent intent = new Intent(MainActivity.this, QuanActivity.class);
//                    Bundle bundle=new Bundle();
//                    if (itemses != null) {
//                        bundle.putSerializable("parkingList",(Serializable) itemses);
//                        intent.putExtras(bundle);
//                    }
                    startActivity(intent);
//                    finish();
                } else {
                    ToastUtil.showStringToast(loginBean.getStrError());
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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AppManager.getAppManager().addActivity(this);


//        String userName = SPUtils.getString(BaseApplication.getmContext(), Constans.userName, "");
//        if (!TextUtils.isEmpty(userName)) {
//            AppManager.getAppManager().finishActivity();
//            Intent intent = new Intent(MainActivity.this, QuanActivity.class);
//            startActivity(intent);
//        }

        ButterKnife.bind(this);


        //隐藏软键盘
        ll_main.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                return AndroidUtils.hideKeyboardboolean(MainActivity.this, getCurrentFocus());
            }
        });


    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Activity销毁时，取消网络请求
        unSubscribe();
    }
}
