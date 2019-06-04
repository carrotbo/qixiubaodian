package com.gwkj.qixiubaodian.module.wallet.tradingrecord;

import com.gwkj.qixiubaodian.mvp.BasePresenter;
import com.gwkj.qixiubaodian.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TradingRecordContract {
    interface View extends BaseView {
        void getRecordData(String result);
        
    }

    interface  Presenter extends BasePresenter<View> {
        void postRecord(String type,String first_time,int this_page);
    }
}
