package com.cheyifu.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.cheyifu.business.R;
import com.cheyifu.business.base.BaseActivity;
import com.cheyifu.business.base.Urls;
import com.cheyifu.business.dialog.HTLoadBlock;
import com.cheyifu.business.global.BaseApplication;
import com.cheyifu.business.model.UpDataBean;
import com.cheyifu.business.utils.AndroidUtils;
import com.cheyifu.business.utils.AppManager;
import com.cheyifu.business.utils.JsonUtil;
import com.cheyifu.business.utils.SPUtils;
import com.cheyifu.business.utils.ToastUtil;
import com.cheyifu.business.utils.UpdateManager;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyCenterActivity extends BaseActivity {
    @Bind(R.id.tv_vername)
    TextView tv_vername;


    @Bind(R.id.iv_back)
    RelativeLayout back;
    private String verName;

    @OnClick(R.id.iv_back)
    public void setBack() {
        this.finish();
    }

    @Bind(R.id.check_version)
    RelativeLayout check_version;

    @OnClick(R.id.check_version)
    public void setCheck_version() {
//        ToastUtil.showStringToast("以是最新版本!");
        //检测更新

        checkViersion(verName);


    }

    private void checkViersion(final String verName) {
        HTLoadBlock.showLoadingMessage(MyCenterActivity.this, "加载中..", true);
        RequestQueue requestQueue = Volley.newRequestQueue(BaseApplication.getmContext());

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.UPDATE_APK, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                HTLoadBlock.dismiss();
//             Logger.i("返回的数据==", response.toString());
                UpDataBean loginBean = JsonUtil.parseJsonToBean(s, UpDataBean.class);
                if (loginBean.getResult() == 0) {
                    if (loginBean.getCheckForUpdate() == 1) {
                        //版本号不一致更新
                        UpdateManager updateManager = new UpdateManager(MyCenterActivity.this);
                        //下载地址
                        String URL = Urls.DownloadAPK;
                        updateManager.checkUpdateInfo(URL, "为了您更好的体验！请您及时更新...");

                    } else {
                        ToastUtil.showStringToast("已是最新版本!");
                    }


                } else {
                    ToastUtil.showStringToast(loginBean.getStrError());
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                HTLoadBlock.dismiss();
                ToastUtil.showToast(R.string.error_result);
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("version", verName);
                params.put("appId", "2");
                params.put("token", "");
                return params;
            }
        };


        requestQueue.add(stringRequest);

    }

    @Bind(R.id.rl_clear)
    RelativeLayout clear;

    @OnClick(R.id.rl_clear)
    public void setClear() {
        ToastUtil.showStringToast("没有检测到垃圾,无须清理!");
    }

    @Bind(R.id.update_pass)
    RelativeLayout updaPass;

    @OnClick(R.id.update_pass)
    public void setUpdaPass() {
        Intent intent = new Intent(MyCenterActivity.this, modificationActivity.class);
        startActivity(intent);
    }

    @Bind(R.id.exit_submit)
    RelativeLayout exit;

    @OnClick(R.id.exit_submit)
    public void setExit() {
        SPUtils.clearAll(BaseApplication.getmContext());
//        android.os.Process.killProcess(android.os.Process.myPid()) ;
//        Intent intent = new Intent();
//        intent.setClass(MyCenterActivity.this, MainActivity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        startActivity(intent);
////        this.finish();
        AppManager.getAppManager().finishAllActivity();

//        AppManager.getAppManager().AppExit(BaseApplication.getmContext());
        Intent intent = new Intent();
        intent.setClass(MyCenterActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_center);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        verName = AndroidUtils.getVerName(BaseApplication.getmContext());
        tv_vername.setText(verName);
    }
}
