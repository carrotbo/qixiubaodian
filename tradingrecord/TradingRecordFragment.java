package com.gwkj.qixiubaodian.module.wallet.tradingrecord;


import android.annotation.SuppressLint;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.gwkj.qixiubaodian.R;
import com.gwkj.qixiubaodian.databinding.FragmentReyclerviewBinding;
import com.gwkj.qixiubaodian.listenter.OnRecyclerviewItemClickListener;
import com.gwkj.qixiubaodian.module.wallet.adapter.RecordListAdapter;
import com.gwkj.qixiubaodian.mvp.MVPBaseFragment;
import com.gwkj.qixiubaodian.response.RecordListResponse;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

/**
 * MVPPlugin
 * 邮箱 784787081@qq.com
 */

public class TradingRecordFragment extends MVPBaseFragment<TradingRecordContract.View, TradingRecordPresenter>  implements TradingRecordContract.View,OnRecyclerviewItemClickListener,View.OnClickListener {


    private boolean isFirst=false;
    private FragmentReyclerviewBinding binding;
    private RecordListAdapter recordListAdapter;
    private String type;
    private int numPage = 1;
    private int totalPage = 0;
    private String createdat = "";

    public TradingRecordFragment() {
    }

    public static TradingRecordFragment newInstance(String type) {
        TradingRecordFragment fragmentOne = new TradingRecordFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", type);

        //fragment保存参数，传入一个Bundle对象
        fragmentOne.setArguments(bundle);
        return fragmentOne;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            if (!isFirst) {
                isFirst = true;
                myhandl.sendEmptyMessageDelayed(0,100);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding= DataBindingUtil.inflate(inflater,R.layout.fragment_reyclerview, container, false);
        if(getArguments()!=null){
            //取出保存的值
            type=getArguments().getString("type");
        }
        initView();
        initData();
        return binding.getRoot();
    }
    private void initView(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        binding.rcList.setLayoutManager(layoutManager);
        recordListAdapter = new RecordListAdapter(this);
        binding.rcList.setAdapter(recordListAdapter);
        binding.tvGotop.setOnClickListener(this);
        binding.rcList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(newState==0){
                    if (binding.rcList != null && binding.rcList.getChildCount() > 0) {
                        try {
                            int currentPosition = ((RecyclerView.LayoutParams) binding.rcList.getChildAt(0).getLayoutParams()).getViewAdapterPosition();
                            if(currentPosition>2){
                                binding.tvGotop.setVisibility(View.VISIBLE);
                            }else {
                                binding.tvGotop.setVisibility(View.GONE);
                            }
                        } catch (Exception e) {

                        }
                    }

                }
            }
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        binding.refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                binding.refreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
                getRecordList();
            }
        });
        binding.refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                binding.refreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
                refreshData();
            }
        });
    }
    public void refreshData() {
        binding.refreshLayout.setEnableLoadMore(true);
        if (recordListAdapter != null) {
            recordListAdapter.addButtomItem(false);
        }
        numPage = 1;
        createdat = "";
        getRecordList();
    }
    private void initData(){
        if(type.equals("0")){
            binding.tvText.setText("现在还没有支出");
        }else if(type.equals("1")){
            binding.tvText.setText("现在还没有收入");
        }else if(type.equals("2")){
            binding.tvText.setText("现在还没有转出");
        }else if(type.equals("3")){
            binding.tvText.setText("现在还没有转入");
        }
    }
    private void getRecordList(){
        if (totalPage != 0 && numPage > totalPage) {
            myhandl.sendEmptyMessageDelayed(3, 1000);
            return;
        }
        mPresenter.postRecord(type,createdat,numPage);
    }
    @Override
    public void getRecordData(String result) {
        RecordListResponse emty = JSONUtil.parse(result, RecordListResponse.class);
        if (emty != null && emty.getData() != null && emty.getData().getList().size() > 0) {
            binding.rcList.setVisibility(View.VISIBLE);
            binding.tvNoData.setVisibility(View.GONE);
            if (numPage == 1) {
                createdat = emty.getData().getList().get(0).getCreatedat();
                recordListAdapter.setDatas(emty.getData().getList());
                totalPage = emty.getData().getTotal_page();
                if(totalPage==1){
                    binding.refreshLayout.setEnableLoadMore(false);
                }
            } else {
                recordListAdapter.addDatas(emty.getData().getList());
            }
            numPage = numPage + 1;
        } else {
            if(numPage==1) {
                binding.rcList.setVisibility(View.GONE);
                binding.tvNoData.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View view) {
        if(view==binding.tvGotop){
            binding.rcList.scrollToPosition(0);
        }

    }
    @Override
    public void onItemClickListener(View v, int position) {

    }
    @Override
    protected void refreshWebView(String info) {

    }
    @SuppressLint("HandlerLeak")
    Handler myhandl=new Handler() {
        @Override
        public void handleMessage(Message msg){
            switch(msg.what){
                case 0:
                    if(type==null||type.isEmpty()){
                        myhandl.sendEmptyMessageDelayed(0,100);
                    }else{
                        getRecordList();
                    }
                    break;
                case 3:
                    recordListAdapter.addButtomItem(true);
                    binding.refreshLayout.finishLoadMore();
                    binding.refreshLayout.setEnableLoadMore(false);
                    break;

            }
            super.handleMessage(msg);
        }
    };

}
