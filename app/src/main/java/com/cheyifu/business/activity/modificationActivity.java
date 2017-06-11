package com.cheyifu.business.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.EditText;
import android.widget.ImageView;
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
import com.cheyifu.business.utils.AppManager;
import com.cheyifu.business.utils.JsonUtil;
import com.cheyifu.business.utils.MD5;
import com.cheyifu.business.utils.SPUtils;
import com.cheyifu.business.utils.ToastUtil;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class modificationActivity extends BaseActivity {
    private boolean olderChick = true;
    private boolean newPassChick = true;
    private boolean surePassChick = true;

    @Bind(R.id.iv_sure)
    ImageView iv_sure;

    @OnClick(R.id.iv_sure)
    public void setIv_sure() {
        if (surePassChick) {
            iv_sure.setBackgroundResource(R.drawable.mima_icon_active);
            surePassChick = !surePassChick;
            surePass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            iv_sure.setBackgroundResource(R.drawable.mima_icon);
            surePassChick = !surePassChick;
            surePass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    @Bind(R.id.iv_older)
    ImageView iv_older;

    @OnClick(R.id.iv_older)
    public void setIv_older() {
        if (olderChick) {
            iv_older.setBackgroundResource(R.drawable.mima_icon_active);
            olderChick = !olderChick;
            olderPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            iv_older.setBackgroundResource(R.drawable.mima_icon);
            olderChick = !olderChick;
            olderPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }

    @Bind(R.id.iv_setting)
    ImageView iv_setting;

    @OnClick(R.id.iv_setting)
    public void setIv_setting() {
        if (newPassChick) {
            iv_setting.setBackgroundResource(R.drawable.mima_icon_active);
            newPassChick = !newPassChick;
            newPass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            iv_setting.setBackgroundResource(R.drawable.mima_icon);
            newPassChick = !newPassChick;
            newPass.setTransformationMethod(PasswordTransformationMethod.getInstance());

        }
    }


    @Bind(R.id.iv_back_update)
    RelativeLayout back;

    @OnClick(R.id.iv_back_update)
    public void setBack() {
        this.finish();
    }

    @Bind(R.id.submit_modification)
    RelativeLayout submit;

    @OnClick(R.id.submit_modification)
    public void setSubmit() {
        String name = user.getText().toString().trim();
        String pass = this.olderPass.getText().toString().toString().trim();
        String newpass = newPass.getText().toString().trim();
        String sure = surePass.getText().toString().trim();
        if (TextUtils.isEmpty(name)) {
            ToastUtil.showStringToast("请输入账号");
            return;
        }
        if (TextUtils.isEmpty(pass)) {
            ToastUtil.showStringToast("请输入旧密码");
            return;
        }
        if (TextUtils.isEmpty(newpass)) {
            ToastUtil.showStringToast("请输入新密码");
            return;
        }
        if (TextUtils.isEmpty(sure)) {
            ToastUtil.showStringToast("确认密码不能为空");
            return;
        }

        if (!newpass.equals(sure)) {
            new CircleDialog.Builder(modificationActivity.this)
                    .setCanceledOnTouchOutside(false)
                    .setCancelable(false)
                    .setTitle("温馨提示")
                    .setText("请确认两次密码是否一致")
//                                .setNegative("取消", null)
                    .setPositive("确定", null)
                    .show();
            return;
        }
        if (pass.length() < 6) {

            ToastUtil.showStringToast("旧密码不能少于6位");
            return;
        }
        if (newpass.length() < 6) {
            ToastUtil.showStringToast("密码不能少于6位");
            return;
        }

        updatePass(name, MD5.getEncode(pass), newpass);

    }

    private void updatePass(String name, String pass, String newPass) {
        final RequestQueue requestQueue = Volley.newRequestQueue(BaseApplication.getmContext());
        Map<String, String> params = new HashMap<>();

        params.put("loginName", name);
        params.put("oldPassWord", pass);
        params.put("newPassWord", newPass);
//        params.put("parkingId",itemses.get(index).getParkingId());

        JSONObject jsonObject = new JSONObject(params);
        HTLoadBlock.showLoadingMessage(modificationActivity.this, "加载中..", true);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, Urls.URL_UPDATEPWD, jsonObject, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                HTLoadBlock.dismiss();
                BaseBean baseBean = JsonUtil.parseJsonToBean(response.toString(), BaseBean.class);
                if (baseBean.getResult() == 0) {
                    SPUtils.clearAll(BaseApplication.getmContext());
                    AppManager.getAppManager().finishAllActivity();
                    Intent intent = new Intent();
                    intent.setClass(modificationActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    ToastUtil.showStringToast("修改成功,请登录");
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

    @Bind(R.id.et_update_user)
    TextView user;

    @Bind(R.id.older_pass)
    EditText olderPass;

    @Bind(R.id.new_pass)
    EditText newPass;

    @Bind(R.id.new_sure_pass)
    EditText surePass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modification);
        ButterKnife.bind(this);
        AppManager.getAppManager().addActivity(this);
        String string = SPUtils.getString(BaseApplication.getmContext(), Constans.userName, "");
        if (!TextUtils.isEmpty(string)) {
            user.setText(string);
        }
    }
}
