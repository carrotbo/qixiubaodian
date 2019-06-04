package com.gwkj.qixiubaodian.fragment.tabask.adapter;


import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.gwkj.qixiubaodian.R;
import com.gwkj.qixiubaodian.databinding.AskToastListItemBinding;
import com.gwkj.qixiubaodian.fragment.tabask.item.AskNotify;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用的adapter
 * Created by LQY on 2016/10/10.
 */
public class ToastListAdapter<T> extends BaseAdapter {
    private Context context;
    private List<T> list=new ArrayList<>();
    private int layoutId;//单布局
    private int variableId;

    public ToastListAdapter(Context context, int layoutId, int variableId){
        this.context=context;
        this.layoutId=layoutId;
        this.variableId=variableId;
    }
    public void setDataList(List<T> mlist) {
        if (list != null && !list.isEmpty()) {
            list.clear();
        }
       
        list.addAll(mlist);
        this.notifyDataSetChanged();
    }
    public void cleanList(){
        if (list != null && !list.isEmpty()) {
            list.clear();
            notifyDataSetChanged();
        }
    }
    public void addDataList(List<T> mlist) {
        if (list != null) {
            list.addAll(mlist);
            notifyDataSetChanged();
        }
    }
    @Override
    public int getCount(){
        if(list==null){
            return 0;
        }else{
            return list.size();
        }
    }
    
    @Override
    public Object getItem(int position){
        return list.get(position);
    }
    
    @Override
    public long getItemId(int position){
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent){
        AskToastListItemBinding binding=null;
        AskNotify.DataBean.ListBean item=(AskNotify.DataBean.ListBean)list.get(position);
        if(convertView==null){
            binding=DataBindingUtil.inflate(LayoutInflater.from(context), layoutId, parent, false);
        }else{
            binding=DataBindingUtil.getBinding(convertView);
        }
        binding.setVariable(variableId, list.get(position));
        try {
            GradientDrawable drawable = (GradientDrawable) context.getResources().getDrawable(R.drawable.share_while_red_3);
            int BankColor = Color.parseColor(item.getColor());
            drawable.setStroke(2,BankColor);
            binding.tvToastTag.setTextColor(Color.parseColor(item.getColor()));
            binding.tvToastTag.setBackgroundDrawable(drawable);
        }catch (Exception e){
        }
        return binding.getRoot();
    }
}