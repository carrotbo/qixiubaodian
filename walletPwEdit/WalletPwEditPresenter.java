package com.gwkj.qixiubaodian.module.wallet.walletPwEdit;

import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.mvp.BasePresenterImpl;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;

import org.apache.http.HttpException;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WalletPwEditPresenter extends BasePresenterImpl<WalletPwEditContract.View> implements WalletPwEditContract.Presenter{
    @Override
    public void setMSMCode(String phone) {

        String url = Constant.PASSCODESMS;
        String json = NetInterfaceEngine.getEngine().jsonMapValue("mobile",phone);
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result) {
                if(result!=null&&mView!=null){
                    mView.MSMCode(result);
                }
            }
            @Override
            public void onFail(HttpException e, String err) {
            }
        });
    }

    @Override
    public void setPassWord(String mobile, String pay_password, String vcode, String withdraw_password) {
        String url = Constant.WALLET_PASSWORD_EDIT;
        String json = jsonPassword(mobile,pay_password,vcode,withdraw_password);
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result) {
                if(result!=null&&mView!=null){
                    mView.getPassSet(result);
                }
            }
            @Override
            public void onFail(HttpException e, String err) {
            }
        });
    }
    //充值信息
    private String jsonPassword(String mobile, String pay_password, String vcode, String withdraw_password) {
        String data = "";
        try {
            JSONObject ClientKey = new JSONObject();
            ClientKey.put("mobile", mobile);
            ClientKey.put("pay_password", pay_password);
            ClientKey.put("vcode", vcode);
            ClientKey.put("withdraw_password", withdraw_password);
            data = NetInterfaceEngine.getEngine().jsonData(ClientKey);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
