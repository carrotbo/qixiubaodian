package com.gwkj.qixiubaodian.module.wallet.walletpassword;

import com.gwkj.qixiubaodian.mvp.BasePresenter;
import com.gwkj.qixiubaodian.mvp.BaseView;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class WalletPasswordContract {
    interface View extends BaseView {
        void MSMCode(String result);
        void getPassSet(String result);
        
    }

    interface  Presenter extends BasePresenter<View> {
        void setMSMCode(String phone);
        void setPassWord(String mobile,String pay_password,String vcode,String withdraw_password);
    }
}
