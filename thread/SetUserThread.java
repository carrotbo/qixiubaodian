package com.gwkj.qixiubaodian.thread;

import android.content.Context;

import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.qxbd.MyApplication;
import com.gwkj.qixiubaodian.response.MsgEvent;
import com.gwkj.qixiubaodian.response.User;
import com.gwkj.qixiubaodian.utils.BaseCacheUtil;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;
import com.gwkj.qixiubaodian.utils.RxBus;

public class SetUserThread extends Thread
{
	private static SetUserThread instanceInitThread = null;
	private  Context mcontext;
	private boolean isFrist=true;

	public static SetUserThread getInstnceThread(Context context)
	{

		if (instanceInitThread == null)
			instanceInitThread = new SetUserThread(context);
		return instanceInitThread;
	}

	private SetUserThread(Context context)
	{
		mcontext=context;
	}

	@Override
	public void run()
	{
		String openid = BaseCacheUtil.getString(mcontext, "login_openid");
		if(openid!=null&&!openid.isEmpty()) {
			getUserData(openid);
		}
	}
	public void getUserData(String openid) {
		if(openid==null||openid.isEmpty()){
			return;
		}
		String json =NetInterfaceEngine.getEngine().jsonMapValue("openid",openid);
		String url = Constant.GETUSER;
		NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
			@Override
			public void onSuccess(String result) {
				User user = JSONUtil.parse(result, User.class);
				if (user != null && user.getStatus().equals("ok") && user.getData() != null) {
					MyApplication.getInstances().setUser(user);
					if(isFrist) {
						isFrist=false;
						RxBus.getInstance().post(new MsgEvent(1, ""));//通知有推送
					}
				}
			}

			@Override
			public void onFail(org.apache.http.HttpException e, String err) {
			}
		});
	}

}
