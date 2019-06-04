package com.gwkj.qixiubaodian.fragment.tabask;

import android.content.Context;
import android.widget.TextView;

import com.gwkj.qixiubaodian.mvp.BasePresenter;
import com.gwkj.qixiubaodian.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TabAskContract {
    interface View extends BaseView {
        void brandCart(String result);
        void noLoginRenum(String result);
        void askNotifyToat(String result);
        
    }

    interface  Presenter extends BasePresenter<View> {
        void getBrandCart();
        void getNoLoginNum();
        void getAstNotify();
        void setTextClick(Context context,TextView tv);
    }
}
