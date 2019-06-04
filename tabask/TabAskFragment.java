package com.gwkj.qixiubaodian.fragment.tabask;


import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;

import com.gwkj.qixiubaodian.BR;
import com.gwkj.qixiubaodian.R;
import com.gwkj.qixiubaodian.activity.LoginActivity;
import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.databinding.TabAskFramentBinding;
import com.gwkj.qixiubaodian.fragment.tabask.adapter.ToastListAdapter;
import com.gwkj.qixiubaodian.fragment.tabask.item.AskNotify;
import com.gwkj.qixiubaodian.module.answers.AllAnswerBrandActivity;
import com.gwkj.qixiubaodian.module.answers.AnswerBrandActivity;
import com.gwkj.qixiubaodian.module.answers.AnswerHotActivity;
import com.gwkj.qixiubaodian.module.answers.AnswerSpaceActivity;
import com.gwkj.qixiubaodian.module.answers.AskAllActivity;
import com.gwkj.qixiubaodian.module.answers.adapter.AskBrandAdapter;
import com.gwkj.qixiubaodian.module.answers.frament.TabAskChildFragment;
import com.gwkj.qixiubaodian.module.data_kind.EditDataBrandActivity;
import com.gwkj.qixiubaodian.module.message.MsgBoxActivity;
import com.gwkj.qixiubaodian.module.motor.MotorActivity;
import com.gwkj.qixiubaodian.module.search_data.SearchDataActivity;
import com.gwkj.qixiubaodian.module.usersetting.SettingActivity;
import com.gwkj.qixiubaodian.mvp.MVPBaseFragment;
import com.gwkj.qixiubaodian.qxbd.MyApplication;
import com.gwkj.qixiubaodian.response.ActionNum;
import com.gwkj.qixiubaodian.response.AnswerCountResponse;
import com.gwkj.qixiubaodian.response.DataBrand;
import com.gwkj.qixiubaodian.response.MsgEvent;
import com.gwkj.qixiubaodian.response.User;
import com.gwkj.qixiubaodian.utils.BaseCacheUtil;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;
import com.gwkj.qixiubaodian.utils.PermissionUtils;
import com.gwkj.qixiubaodian.utils.RxBus;
import com.gwkj.qixiubaodian.utils.StartNewAsk;
import com.gwkj.qixiubaodian.view.SimpleViewPagerIndicator;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.HttpException;

import java.util.ArrayList;
import java.util.List;

import rx.Subscription;
import rx.functions.Action1;

import static android.app.Activity.RESULT_OK;
import static com.gwkj.qixiubaodian.constant.Constant.ASKBRANK_BACK;
import static com.gwkj.qixiubaodian.constant.Constant.MSG_RED_NUM;
import static com.gwkj.qixiubaodian.constant.Constant.NEWASK_BACK;

/**
 * MVPPlugin
 *  邮箱 784787081@qq.com
 */

public class TabAskFragment extends MVPBaseFragment<TabAskContract.View, TabAskPresenter> implements TabAskContract.View , View.OnClickListener, SimpleViewPagerIndicator.OnItemPageChangListener{

    private TabAskFramentBinding binding;
    private String[] mTitles = new String[]{"新提问", "新回复", "零回复", "有悬赏", "已解决"};

    private AskBrandAdapter adapter;
    private ToastListAdapter<AskNotify.DataBean.ListBean> toastAdapter;
    private TabAskChildFragment fragment0;
//    private KnowPullFragment fragment2;
    private TabAskChildFragment fragment1;
    private TabAskChildFragment fragment2;
    private TabAskChildFragment fragment3;
    private TabAskChildFragment fragment4;
    private User user;
    private String askLogin="1";
    private List<AskNotify.DataBean.ListBean> notifyList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container, Bundle savedInstanceState) {
        binding= DataBindingUtil.inflate(inflater, R.layout.tab_ask_frament, container, false);

        return binding.getRoot();
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        receiveMsg();
        initDatas();
        initEvents();
        islogin();
        initBaseCache();
        initPresenter();

    }
    private void initView(){
        adapter = new AskBrandAdapter(getContext());
        binding.gvBrand.setAdapter(adapter);
        toastAdapter=new ToastListAdapter<>(getActivity(),R.layout.ask_toast_list_item, BR.toast);
        binding.mlistview.setAdapter(toastAdapter);

        binding.tvEdit.setOnClickListener(this);
        binding.tvAllBrand.setOnClickListener(this);
        binding.tvNewAsk.setOnClickListener(this);
        binding.imgCenter.setOnClickListener(this);
        binding.imgInfo.setOnClickListener(this);
        binding.llAllAsk.setOnClickListener(this);
        binding.llMyAsk.setOnClickListener(this);
        binding.llSearch.setOnClickListener(this);
        binding.llBrandAsk.setOnClickListener(this);
        binding.llHot.setOnClickListener(this);
        binding.title2.setOnClickListener(this);
        binding.gvBrand.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                DataBrand.DataBean.ListBean item = adapter.getItem(position);
                if (item == null) {
                    return;
                }
                Intent intent;
                user=MyApplication.getInstances().getUser();
                if(user==null){
                    intent=new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (item.getImg() == null) {
                    intent = new Intent(getContext(), EditDataBrandActivity.class);
                    getActivity().startActivityForResult(intent, ASKBRANK_BACK);
                } else {
                    intent = new Intent(getContext(), AnswerBrandActivity.class);
                    intent.putExtra("pinpai_id", item.getPinpaiid());
                    intent.putExtra("name", item.getKeyword1());
                    intent.putExtra("type", "2");
                    getActivity().startActivityForResult(intent, ASKBRANK_BACK);
                }

            }
        });
        binding.mlistview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                AskNotify.DataBean.ListBean item=(AskNotify.DataBean.ListBean)toastAdapter.getItem(i);
                user=MyApplication.getInstances().getUser();
                if(user==null){
                    Intent intent=new Intent(getContext(),LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if(item!=null&&item.getUrl()!=null){
                    PermissionUtils.getEngine().startWebview(getContext(),item.getUrl());
                }
            }
        });
        binding.snlSl.setRefreshView( binding.refreshLayout);
        binding.refreshLayout.setEnableLoadMore(false);
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(1000/*,false*/);//传入false表示加载失败
                mPresenter.getAstNotify();
                refreshFrament();
                initAnswerCount();//我的问答红点提示 刷新
                initReplyCount();
                mPresenter.getBrandCart();
            }
        });

    }
    public void refreshData() {
        MobclickAgent.onEvent(getContext(),"index_three");
    }
    private void initPresenter(){
        mPresenter.getAstNotify();
        mPresenter.setTextClick(getContext(),binding.tvGv);
    }
    /**
     * 文章列表数据
     */
    private void initDatas() {
        binding.idStickynavlayoutIndicator.setTitles(mTitles, getActivity());
        binding.idStickynavlayoutViewpager.setAdapter(new MyPagerAdapter(getChildFragmentManager()));

        binding.idStickynavlayoutViewpager.setOffscreenPageLimit(mTitles.length);//预加载预防重新加载多次
        binding.idStickynavlayoutIndicator.setOnItemClickListener(this);

        binding.idStickynavlayoutViewpager.setCurrentItem(0);
        binding.idStickynavlayoutIndicator.setFocus(0);
        redNumShow(MSG_RED_NUM);
    }
    public void redNumShow(int num) {
        if (binding!=null&&binding.msgPointRed != null) {
            if (num > 0) {
                binding.msgPointRed.setText(String.valueOf(num));
                binding.msgPointRed.setVisibility(View.VISIBLE);
            } else {
                binding.msgPointRed.setVisibility(View.GONE);
            }
        }
    }

    private void receiveMsg() {//接收信息
        Subscription rxSbscription = RxBus.getInstance().toObserverable(MsgEvent.class).subscribe(new Action1<MsgEvent>() {
            @Override
            public void call(MsgEvent messageEvent) {
                switch (messageEvent.getType()) {
                    case 0:       //退出登录
                        askLogin="3";
                        islogin();
                        refreshFrament();
                        break;
                    case 1:        //登录成功
                        askLogin="1";
                        islogin();
                        mPresenter.getBrandCart();
                        refreshFrament();
                        break;

                }

            }
        });
    }
    private void initBaseCache() {
        String myBrand = BaseCacheUtil.getString(getContext(), "data_mybrand");
        if (!myBrand.isEmpty()) {
            DataBrand emty = JSONUtil.parse(myBrand, DataBrand.class);
            adapter.addList(emty.getData().getList());
        } else {
            adapter.addList(null);
        }
        mPresenter.getBrandCart();
    }
    private void initEvents() {
        binding.idStickynavlayoutViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                binding.idStickynavlayoutIndicator.setFocus(position);
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                binding.idStickynavlayoutIndicator.scroll(position, positionOffset);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    private void refreshFrament(){
        if(fragment0!=null){
            fragment0.refreshData(askLogin);
        }
        if(fragment1!=null){
            fragment1.refreshData(askLogin);
        }
        if(fragment2!=null){
            fragment2.refreshData(askLogin);
        }
        if(fragment3!=null){
            fragment3.refreshData(askLogin);
        }
        if(fragment4!=null){
            fragment4.refreshData(askLogin);
        }
    }
    //头像设置
    public void islogin() {
        user=MyApplication.getInstances().getUser();
        if (user == null) {
            adapter.addList(null);
            binding.imgCenter.setImageUrl("");
            mPresenter.getNoLoginNum();
            binding.tvAttention.setText("全部问答,登录后可以优先显示关注品牌的问答");
            binding.gvBrand.setVisibility(View.GONE);
            binding.imgBrand.setVisibility(View.VISIBLE);
            binding.tvGv.setVisibility(View.VISIBLE);
            binding.tvAllBrand.setVisibility(View.GONE);
            binding.imgRight2.setVisibility(View.GONE);
            binding.snlSl.refreshView();
        } else {
            binding.imgCenter.setImageUrl(MyApplication.getInstances().getUser().getData().getHeadimgurl());
            binding.tvAttention.setText("关注品牌相关的问答");
            binding.gvBrand.setVisibility(View.VISIBLE);
            binding.imgBrand.setVisibility(View.GONE);
            binding.tvGv.setVisibility(View.GONE);
            binding.tvAllBrand.setVisibility(View.VISIBLE);
            binding.imgRight2.setVisibility(View.VISIBLE);
            binding.snlSl.refreshView();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        initAnswerCount();//我的问答红点提示 刷新
        initReplyCount();
        mPresenter.getBrandCart();
    }

    @Override
    public void brandCart(String result) {
        DataBrand emty = JSONUtil.parse(result, DataBrand.class);
        if (emty != null && emty.getData() != null && emty.getData().getList() != null) {
            BaseCacheUtil.setString(getContext(), "data_mybrand", result);
            adapter.addList(emty.getData().getList());
            noReplyNum(emty.getData().getList());
        } else {
            adapter.addList(null);
        }
        binding.snlSl.refreshView();
    }

    @Override
    public void noLoginRenum(String result) {
        ActionNum emty=JSONUtil.parse(result,ActionNum.class);
        if(emty!=null&&emty.getData()!=null){
            binding.tvNum.setVisibility(View.VISIBLE);
            binding.tvNum.setText(emty.getData().getCount());
        }else{
            binding.tvNum.setVisibility(View.GONE);
        }
    }

    @Override
    public void askNotifyToat(String result) {
        AskNotify emty=JSONUtil.parse(result,AskNotify.class);
        if(emty!=null&&emty.getData()!=null&&emty.getData().getList()!=null&&emty.getData().getList().size()>0){
            binding.mlistview.setVisibility(View.VISIBLE);
            toastAdapter.setDataList(emty.getData().getList());

        }else{
           binding.mlistview.setVisibility(View.GONE);
        }
        binding.snlSl.refreshView();
    }

    private void noReplyNum(List<DataBrand.DataBean.ListBean> mlist) {
        int leng = mlist.size();
        int num = 0;
        try {
            for (int i = 0; i < leng; i++) {
                String zero = mlist.get(i).getForum_zero();
                if (zero.equals("99+")) {
                    binding.tvNum.setVisibility(View.VISIBLE);
                    binding.tvNum.setText("99+");
                    return;
                }
                num = num + Integer.parseInt(mlist.get(i).getForum_zero());
            }
            if (num != 0) {
                binding.tvNum.setVisibility(View.VISIBLE);
                if (num > 99) {
                    binding.tvNum.setText("99+");
                } else {
                    binding.tvNum.setText(String.valueOf(num));
                }
            } else {
                binding.tvNum.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            binding.tvNum.setVisibility(View.GONE);
        }
    }
    /**
     * 文章列表适配器
     */
    public class MyPagerAdapter extends FragmentPagerAdapter {

        private MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles[position];
        }

        @Override
        public int getCount() {
            return mTitles.length;
        }

        @Override
        public Fragment getItem(int position) {
            user=MyApplication.getInstances().getUser();
            if(user==null){
                askLogin="3";
            }else{
                askLogin="1";
            }
            switch (position) {
                case 0:
                    if (fragment0 == null) {
                        fragment0 = TabAskChildFragment.newInstance(askLogin, "", "1");
                    }
                    return fragment0;

                case 1:
                    if (fragment1 == null) {
                        fragment1 = TabAskChildFragment.newInstance(askLogin, "", "0");
                    }
                    return fragment1;
                case 2:
                    if (fragment2 == null) {
                        fragment2 = TabAskChildFragment.newInstance(askLogin, "", "8");
                    }
                    return fragment2;
                case 3:
                    if (fragment3 == null) {
                        fragment3 = TabAskChildFragment.newInstance(askLogin, "", "2");
                    }
                    return fragment3;
                case 4:
                    if (fragment4 == null) {
                        fragment4 = TabAskChildFragment.newInstance(askLogin, "", "4");
                    }
                    return fragment4;
                default:
                    return null;
            }
        }

    }
    @Override
    protected void refreshWebView(String info) {

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode) {
            case RESULT_OK:
                switch (requestCode) {
                    case ASKBRANK_BACK:
                        mPresenter.getBrandCart();
                        if (fragment2 != null) {
                            fragment2.refreshData(askLogin);
                        }
                        break;
                    case NEWASK_BACK:
                        if (fragment0 != null) {
                            fragment0.refreshData(askLogin);
                        }
                        Intent intent = new Intent(getContext(), AskAllActivity.class);
                        startActivity(intent);
                        break;

                }
                break;
        }
    }
    @Override
    public void onClick(View view) {
        Intent intent;
        user=MyApplication.getInstances().getUser();
        if(user==null) {
            intent = new Intent(getContext(), LoginActivity.class);
            startActivity(intent);
            return;
        }
        switch (view.getId()){
            case R.id.tv_all_brand: //全部品牌问答
                intent = new Intent(getContext(), AllAnswerBrandActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_edit:
                intent = new Intent(getContext(), EditDataBrandActivity.class);
                getActivity().startActivityForResult(intent, ASKBRANK_BACK);
                break;
            case R.id.tv_new_ask:
                StartNewAsk.getInstance().startNewAsk(getContext());
                break;
            case R.id.img_center:
                intent = new Intent(getContext(), SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.img_info:
                intent = new Intent(getActivity(), MsgBoxActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_all_ask:
                intent = new Intent(getContext(), AskAllActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_my_ask:
                intent = new Intent(getContext(), AnswerSpaceActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_search:
                intent = new Intent(getActivity(), SearchDataActivity.class);
                intent.putExtra("type", 2);
                startActivity(intent);
                break;
            case R.id.ll_brand_ask:
                intent = new Intent(getContext(), AllAnswerBrandActivity.class);
                startActivity(intent);
                break;

            case R.id.title2:
                intent = new Intent(getContext(), MotorActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_hot:
                intent = new Intent(getContext(), AnswerHotActivity.class);
                startActivity(intent);
                break;
        }

    }

    @Override
    public void onPageChang(int postion) {
        binding.idStickynavlayoutViewpager.setCurrentItem(postion);
    }

    //我的问答红点
    private void initAnswerCount() {
        String url = Constant.ISSIGN;
        String json = NetInterfaceEngine.getEngine().jsonNoData();
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result) {
                if (result == null) {   //第一层判断为空直接不往下走
                    return;
                }
                dismissProgressDialog();
                AnswerCountResponse emty = JSONUtil.parse(result, AnswerCountResponse.class);
                if (emty != null && emty.getData() != null && emty.getStatus().equals("ok")) {
                    if (emty != null && emty.getData().getCount() > 0) {
                        binding.newTvPointRed.setVisibility(View.VISIBLE);
                    } else {
                        binding.newTvPointRed.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFail(HttpException e, String err) {

            }
        });
    }

    private void initReplyCount() {
        String url = Constant.ASKREPLYNUM;
        String json = NetInterfaceEngine.getEngine().jsonNoData();
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @Override
            public void onSuccess(String result) {
                Log.e("result！", result);
                dismissProgressDialog();
                ActionNum emty = JSONUtil.parse(result, ActionNum.class);
                if (emty != null && emty.getData() != null && !emty.getData().getCount().equals("0")) {
                    binding.newTvReplyNum.setVisibility(View.VISIBLE);
                    binding.newTvReplyNum.setText(emty.getData().getCount());
                }
            }

            @Override
            public void onFail(HttpException e, String err) {

            }
        });
    }
}
