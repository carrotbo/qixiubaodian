package com.gwkj.qixiubaodian.fragment.tabask;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.gwkj.qixiubaodian.activity.LoginActivity;
import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.mvp.BasePresenterImpl;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;

import org.apache.http.HttpException;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TabAskPresenter extends BasePresenterImpl<TabAskContract.View> implements TabAskContract.Presenter{

    @Override
    public void getBrandCart() {
        String url = Constant.DATAMYBRAND;
        String json = NetInterfaceEngine.getEngine().jsonNoData();
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result) {
                Log.e("result！", result);
                if(result!=null&&mView!=null){
                    mView.brandCart(result);
                }
            }

            @Override
            public void onFail(HttpException e, String err) {
            }
        });
    }

    @Override
    public void getNoLoginNum() {
        String url=Constant.ASKREPLYNUM;
        String json=NetInterfaceEngine.getEngine().jsonNoData();
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result){
                if(result!=null&&mView!=null){
                    mView.noLoginRenum(result);
                }
            }

            @Override
            public void onFail(HttpException e, String err){

            }
        });
    }

    @Override
    public void getAstNotify() {
        String url=Constant.ASK_NOTIFY;
        String json=NetInterfaceEngine.getEngine().jsonNoData();
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result){
                if(result!=null&&mView!=null){
                    mView.askNotifyToat(result);
                }
            }

            @Override
            public void onFail(HttpException e, String err){

            }
        });
    }

    @Override
    public void setTextClick(final Context mcontext, TextView tv) {
        String context="登录后可以关注品牌，并查看该品牌下的问答。\n您可以点击这里，完成登录或注册";
        final SpannableStringBuilder style = new SpannableStringBuilder();
        //设置文字
        style.append(context); //设置部分文字点击事件
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent=new Intent(mcontext, LoginActivity.class);
                mcontext.startActivity(intent);
            }
        };
        //设置部分文字颜色
        ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(Color.parseColor("#00bee6"));
        style.setSpan(foregroundColorSpan, context.length()-12, context.length()-8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        //配置给TextView
        style.setSpan(clickableSpan, context.length()-12, context.length()-8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        style.setSpan(new UnderlineSpan(), context.length()-12, context.length()-8, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        tv.setMovementMethod(LinkMovementMethod.getInstance());
        tv.setText(style);
    }
}
