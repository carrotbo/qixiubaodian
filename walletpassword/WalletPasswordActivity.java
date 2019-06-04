package com.gwkj.qixiubaodian.module.wallet.walletpassword;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;

import com.gwkj.qixiubaodian.R;
import com.gwkj.qixiubaodian.databinding.WalletPasswordActivityBinding;
import com.gwkj.qixiubaodian.module.wallet.WalletPasswordSuccessActivity;
import com.gwkj.qixiubaodian.mvp.MVPBaseActivity;
import com.gwkj.qixiubaodian.response.BaseEmty;
import com.gwkj.qixiubaodian.utils.BaseCacheUtil;
import com.gwkj.qixiubaodian.utils.Code;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.Utils;


/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WalletPasswordActivity extends MVPBaseActivity<WalletPasswordContract.View, WalletPasswordPresenter> implements WalletPasswordContract.View,View.OnClickListener{

    private WalletPasswordActivityBinding binding;
    private String getCode;
    private String phone;
    private Handler limitHandler = null;
    private int time = 60;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.wallet_password_activity);
        initView();


    }
    private void initView(){
        binding.vcImage.setImageBitmap(Code.getInstance().getBitmap());
        getCode = Code.getInstance().getCode(); // 获取显示的验证码

        binding.vcImage.setOnClickListener(this);
        binding.getAuthCode.setOnClickListener(this);
        binding.tvCommit.setOnClickListener(this);
        String loginMobile = BaseCacheUtil.getString(this, "login_mobile");
        binding.edPhone.setText(loginMobile);
    }
    @Override
    public void MSMCode(String result) {
        BaseEmty response = JSONUtil.parse(result, BaseEmty.class);
        if (response != null && response.getStatus().equals("ok")) {
            sendCodeTimeOut();
        } else {
            toast(response.getMessage());
        }
    }

    @Override
    public void getPassSet(String result) {
        BaseEmty emty=JSONUtil.parse(result,BaseEmty.class);
        if(emty!=null&&emty.getStatus().equals("ok")){
            Intent intent=new Intent(this, WalletPasswordSuccessActivity.class);
            startActivity(intent);
            finish();
        }else{
            if(emty!=null&&emty.getMessage()!=null){
                Utils.toastShow(this,emty.getMessage());
            }else{
                Utils.toastShow(this,"服务器异常");
            }
        }

    }

    @SuppressLint({"HandlerLeak", "SetTextI18n"})
    private void sendCodeTimeOut() {
        time = 120;
        binding.getAuthCode.setEnabled(false);
        binding.getAuthCode.setText("120秒后重新发送");
        limitHandler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                time--;
                if (msg.what == 100) {
                    binding.getAuthCode.setText(time + "秒后重新获取");
                    if (time <= 0) {
                        limitHandler = null;
                        binding.getAuthCode.setEnabled(true);
                        binding.getAuthCode.setText("重新获取验证码");
                        return;
                    }
                    limitHandler.sendEmptyMessageDelayed(100, 1000);
                }
            }
        };
        limitHandler.sendEmptyMessageDelayed(100, 1000);
    }
    @Override
    public void onClick(View view) {
        if(view==binding.vcImage){
            binding.vcImage.setImageBitmap(Code.getInstance().getBitmap());
            getCode = Code.getInstance().getCode();
        }else if(view==binding.getAuthCode){
            phone = binding.edPhone.getText().toString().trim();
            String pay_password=binding.edPay.getText().toString().trim();
            if(pay_password.isEmpty()){
                Utils.toastShow(this,"请输入支付密码");
                return;
            }
            if(pay_password.length()!=6){
                Utils.toastShow(this,"请输入6位支付密码");
                return;
            }
            String withdraw_password=binding.edGet.getText().toString().trim();
            if(withdraw_password.isEmpty()){
                Utils.toastShow(this,"请输入提现密码");
                return;
            }
            if(withdraw_password.length()!=6){
                Utils.toastShow(this,"请输入6位提现密码");
                return;
            }
            String vc_code = binding.editVerifyCode.getText().toString().trim().toUpperCase();
            getCode = getCode.toUpperCase();
            if (vc_code == null || vc_code.isEmpty()) {
                Utils.toastShow(this, "请输入图形验证码！");
                return;
            }
            if (!getCode.equals(vc_code)) {
                binding.editVerifyCode.setText("");
                binding.vcImage.setImageBitmap(Code.getInstance().getBitmap());
                getCode = Code.getInstance().getCode(); // 获取显示的验证码
                Utils.toastShow(this,  "图形验证码错误！");
                return;
            }
            if (TextUtils.isEmpty(phone)) {
                Utils.toastShow(this,"请输入手机号");
                return;
            }
            String loginMobile = BaseCacheUtil.getString(this, "login_mobile");
            if(!phone.equals(loginMobile)){
                Utils.toastShow(this,  "请输入登录的手机号！");
                return;
            }
            mPresenter.setMSMCode(phone);

        }else if(view==binding.tvCommit){
            phone = binding.edPhone.getText().toString().trim();
            if (TextUtils.isEmpty(phone)) {
                Utils.toastShow(this,"请输入手机号");
                return;
            }
            String pay_password=binding.edPay.getText().toString().trim();
            if(pay_password.isEmpty()){
                Utils.toastShow(this,"请输入支付密码");
                return;
            }
            if(pay_password.length()!=6){
                Utils.toastShow(this,"请输入6位支付密码");
                return;
            }
            String withdraw_password=binding.edGet.getText().toString().trim();
            if(withdraw_password.isEmpty()){
                Utils.toastShow(this,"请输入提现密码");
                return;
            }
            if(withdraw_password.length()!=6){
                Utils.toastShow(this,"请输入6位提现密码");
                return;
            }
            String vcode=binding.editCode.getText().toString().trim();
            if(vcode.isEmpty()){
                Utils.toastShow(this,"请输入短信验证码");
                return;
            }
            mPresenter.setPassWord(phone,pay_password,vcode,withdraw_password);

        }
    }


}
