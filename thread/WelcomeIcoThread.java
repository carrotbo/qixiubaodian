package com.gwkj.qixiubaodian.thread;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.View;

import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.response.BannerResponse;
import com.gwkj.qixiubaodian.utils.BaseCacheUtil;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import org.apache.http.HttpException;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.List;

public class WelcomeIcoThread extends Thread
{
	private static WelcomeIcoThread instanceInitThread = null;
	private static Context mcontext;

	public static WelcomeIcoThread getInstnceInitThread(Context context)
	{

		if (instanceInitThread == null)
			instanceInitThread = new WelcomeIcoThread(context);
		return instanceInitThread;
	}

	private WelcomeIcoThread(Context context)
	{
		mcontext=context;
	}

	@Override
	public void run()
	{
		String url = Constant.BANNER;
		String json = NetInterfaceEngine.getEngine().jsonMapValue("tags","4");
		NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
			@Override
			public void onSuccess(String result) {
				BannerResponse emty = JSONUtil.parse(result, BannerResponse.class);
				if (emty != null && emty.getStatus().equals("ok")&&emty.getData().getList()!=null&&emty.getData().getList().size()>0) {
					String baseCach= BaseCacheUtil.getString(mcontext,"start_welcome");
//					if(!baseCach.isEmpty()&&baseCach.equals(result)){
//						return;
//					}
					BaseCacheUtil.setString(mcontext,"start_welcome",result);
					loadImge(emty.getData().getList());
				}
			}
			@Override
			public void onFail(HttpException e, String err) {
			}
		});
	}
	private  void loadImge(List<BannerResponse.DataBean.ListBean> list){
		if(list!=null&&list.size()>0){
			for(int i=0;i<list.size();i++){
				loadImgList(list.get(i).getImg_url(),i);
			}
		}
	}
	private void loadImgList(String imageUri, final int num){
		ImageLoader.getInstance().loadImage(imageUri,new SimpleImageLoadingListener(){
			@Override
			public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage){

				String fn =String.valueOf(num)+ "start_img.png";
				String path = mcontext.getFilesDir() + File.separator + fn;
				try{
					OutputStream os = new FileOutputStream(path);
					loadedImage.compress(Bitmap.CompressFormat.PNG, 100, os);
					os.close();
				}catch(Exception e){
					Log.e("TAG", "", e);
				}
			}
		});
	}
}
