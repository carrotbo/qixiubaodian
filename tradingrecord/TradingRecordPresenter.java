package com.gwkj.qixiubaodian.module.wallet.tradingrecord;

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

public class TradingRecordPresenter extends BasePresenterImpl<TradingRecordContract.View> implements TradingRecordContract.Presenter{

    @Override
    public void postRecord(String type,String first_time,int this_page) {
        String url = Constant.RECHARGE_RECORD;
        String json = postrequest(type,first_time,this_page);
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result) {
                if(result!=null&&mView!=null){
                    mView.getRecordData(result);
                }
            }
            @Override
            public void onFail(HttpException e, String err) {
                // Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
            }
        });
    }
    private String postrequest(String type,String first_time,int this_page) {
        String data = "";
        try {
            JSONObject ClientKey = new JSONObject();
            if (!first_time.isEmpty()) {
                ClientKey.put("first_time", first_time);
            }
            ClientKey.putOpt("this_page", this_page);
            ClientKey.putOpt("type", type);
            data = NetInterfaceEngine.getEngine().jsonData(ClientKey);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }
}
