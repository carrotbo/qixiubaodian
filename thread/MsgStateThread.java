package com.gwkj.qixiubaodian.thread;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gwkj.qixiubaodian.R;
import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.module.message.MsgManageActivity;
import com.gwkj.qixiubaodian.module.message.item.MngState;
import com.gwkj.qixiubaodian.utils.BaseCacheUtil;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;
import com.gwkj.qixiubaodian.view.CommonDialogManager;
import com.gwkj.qixiubaodian.view.CustomDialog;

import org.apache.http.HttpException;

public class MsgStateThread extends Thread
{
	private static MsgStateThread instanceInitThread = null;
	private static Context mcontext;

	public static MsgStateThread getInstnceInitThread(Context context)
	{

		if (instanceInitThread == null)
			instanceInitThread = new MsgStateThread(context);
		return instanceInitThread;
	}

	private MsgStateThread(Context context)
	{
		mcontext=context;
	}

	@Override
	public void run()
	{
		initMsgRedState("-1");
	}
	public void initMsgRedState(final String type) {
		String url = Constant.MSGMNGSTATE;
		String json = NetInterfaceEngine.getEngine().jsonMapValue("type", type);
		NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
			@Override
			public void onSuccess(String result) {
				Log.e("result！", result);
				MngState emty = JSONUtil.parse(result, MngState.class);
				if (emty != null && emty.getStatus().equals("ok")&&emty.getData()!=null) {
					setState(emty.getData());
				}
			}
			@Override
			public void onFail(HttpException e, String err) {
			}
		});
	}
	private void setState(MngState.DataBean item){
		if(item.getIs_red_remind()==0){
			Constant.MSG_SETRED = false;
		}
		if(item.getIs_sound_remind()==0){
			Constant.MSG_SETSOUND = false;
		}
		String kind="";
		if(item.getIs_forum_order_push()==0){//收藏
			kind=kind+"收藏解决、";
		}
		if(item.getIs_lore_special_push()==0){//专题
			kind=kind+"专题更新、";
		}
		if(item.getIs_master_push()==0){//大师通知
			kind=kind+"大师通知、";
		}
		if(item.getIs_masterrank_push()==0){//大师排名
			kind=kind+"大师排名、";
		}
		if(item.getIs_daily_selection()==0){//每日精选
			kind=kind+"每日精选、";
		}
		if(item.getIs_daily_discuss()==0){//每日话题
			kind=kind+"今日话题、";
		}
		if(item.getIs_friend_dynamic()==0){//关注人动态
			kind=kind+"关注人动态、";
		}
		if(item.getIs_official_dynamic()==0){//官方动态
			kind=kind+"官方动态、";
		}
		long state=BaseCacheUtil.getLong(mcontext,"msgstateshow");
		long nowtime=System.currentTimeMillis()/1000-state;
		long tw=nowtime-(2*7*60*60*24);
		if(!kind.isEmpty()&&tw>=0) {
			kind=kind.substring(0,kind.length()-1);
			CustomDialog dialog = getNotifyDialog(kind);
			dialog.show();
			long time=System.currentTimeMillis()/1000;
			BaseCacheUtil.setLong(mcontext,"msgstateshow",time);
		}
	}
	public CustomDialog getNotifyDialog(final String data) {

		CustomDialog dialog = CommonDialogManager.getInstance().createDialog(mcontext, R.layout.no_open_push_dialog);
		dialog.setCdHelper(new CustomDialog.CustomDialogHelper() {

			@Override
			public void showDialog(final CustomDialog dialog) {
				TextView diss = (TextView) dialog.findViewById(R.id.tv_dismiss);
				TextView tv_content = (TextView) dialog.findViewById(R.id.tv_content);
				TextView tv_open = (TextView) dialog.findViewById(R.id.tv_open);
				tv_content.setText("未开启推送通知，您可能错过"+data+"消息");
				diss.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						dialog.dismiss();
					}
				});
				tv_open.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						Intent intent=new Intent(mcontext, MsgManageActivity.class);
						mcontext.startActivity(intent);
						dialog.dismiss();
					}
				});

			}
		});
		return dialog;
	}
}
