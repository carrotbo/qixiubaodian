package com.gwkj.qixiubaodian.thread;

import android.content.Context;
import android.util.Log;

import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.thread.item.CacheItem;
import com.gwkj.qixiubaodian.utils.BaseCacheUtil;
import com.gwkj.qixiubaodian.utils.DataCleanManager;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;
import com.nostra13.universalimageloader.core.ImageLoader;

import org.apache.http.HttpException;

public class ClearCacheThread extends Thread
{
	private static ClearCacheThread instanceInitThread = null;
	private static Context mcontext;

	public static ClearCacheThread getInstnceInitThread(Context context)
	{

		if (instanceInitThread == null)
			instanceInitThread = new ClearCacheThread(context);
		return instanceInitThread;
	}

	private ClearCacheThread(Context context)
	{
		mcontext=context;
	}

	@Override
	public void run()
	{
		getCacheContent();

	}
  private void getCacheContent(){
	  String url = Constant.CLEAR_CACHE;
	  String json = NetInterfaceEngine.getEngine().jsonNoData();
	  NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
		  @Override
		  public void onSuccess(String result) {
			  Log.e("cache",result);
			  CacheItem emty= JSONUtil.parse(result,CacheItem.class);
			  if(emty!=null&&emty.getData()!=null&&emty.getData().getContent()!=null&&!emty.getData().getContent().isEmpty()){
				  cleanCache(emty.getData().getContent());
			  }
		  }
		  @Override
		  public void onFail(HttpException e, String err) {
		  }
	  });
  }
 private void cleanCache(String content){
          if(!content.isEmpty()&&content.length()>3){
			  content=content.substring(1,content.length()-1);
			  String[] list=content.split(",");
			  if(list.length>0){
				  for (String item:list){
					  if(item.equals("imageload")){
						  cleanImg();
					  }else if(item.equals("all")){
						  DataCleanManager.cleanApplicationData(mcontext);
					  }else{
						  BaseCacheUtil.setString(mcontext,item,"");
					  }
				  }
			  }
		  }
 }
 private void cleanImg(){
	    ImageLoader.getInstance().clearDiscCache();//清除磁盘缓存
        ImageLoader.getInstance().clearMemoryCache();//清除内存缓存
 }
}
