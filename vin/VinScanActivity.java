package com.gwkj.qixiubaodian.module.vin;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.baidu.ocr.sdk.OCR;
import com.baidu.ocr.sdk.OnResultListener;
import com.baidu.ocr.sdk.exception.OCRError;
import com.baidu.ocr.sdk.model.AccessToken;
import com.baidu.ocr.ui.camera.CameraActivity;
import com.baidu.ocr.ui.camera.CameraNativeHelper;
import com.gwkj.qixiubaodian.R;
import com.gwkj.qixiubaodian.activity.LoginActivity;
import com.gwkj.qixiubaodian.adapter.BaseListAdapter;
import com.gwkj.qixiubaodian.constant.Constant;
import com.gwkj.qixiubaodian.databinding.ActivityVinScanBinding;
import com.gwkj.qixiubaodian.module.ation_space.ActionSpaceActivity;
import com.gwkj.qixiubaodian.module.code_search.item.Hiscode;
import com.gwkj.qixiubaodian.module.usersetting.FileUtil;
import com.gwkj.qixiubaodian.module.vin.helper.VinHisHelper;
import com.gwkj.qixiubaodian.module.vin.item.ScanVin;
import com.gwkj.qixiubaodian.module.vin.item.VinCar;
import com.gwkj.qixiubaodian.module.vin.utils.RecognizeService;
import com.gwkj.qixiubaodian.module.vipmenber.RenewActivity;
import com.gwkj.qixiubaodian.module.vipmenber.Vipactivity;
import com.gwkj.qixiubaodian.qxbd.BaseActivity;
import com.gwkj.qixiubaodian.qxbd.MyApplication;
import com.gwkj.qixiubaodian.response.User;
import com.gwkj.qixiubaodian.response.VipInfoResponse;
import com.gwkj.qixiubaodian.utils.Code;
import com.gwkj.qixiubaodian.utils.ImagerPicUtil;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.NetHelper;
import com.gwkj.qixiubaodian.utils.NetInterfaceEngine;
import com.gwkj.qixiubaodian.utils.PermissionUtils;
import com.gwkj.qixiubaodian.utils.Utils;
import com.gwkj.qixiubaodian.view.CommonDialogManager;
import com.gwkj.qixiubaodian.view.CustomDialog;
import com.umeng.analytics.MobclickAgent;

import org.apache.http.HttpException;

import java.util.ArrayList;
import java.util.List;

public class VinScanActivity extends BaseActivity implements View.OnClickListener{
    private boolean hasGotToken = false;
    private ActivityVinScanBinding binding;
    private static final int REQUEST_CODE_GENERAL_BASIC = 106;
    private static final int REQUEST_CODE_ACCURATE_BASIC = 107;
    public List<Hiscode> vin_history = new ArrayList<>();
    private BaseListAdapter<Hiscode> adapter;
    private String getCode = "";
    private String vip="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_vin_scan);
        initView();
        initAccessTokenWithAkSk();

    }
    private void initView(){
        MobclickAgent.onEvent(this,"vip_vin");
        binding.tvVin.setOnClickListener(this);
        binding.vipRenew.setOnClickListener(this);
        binding.searchTvdele.setOnClickListener(this);
        binding.tvSeek.setOnClickListener(this);
        binding.rlBack.setOnClickListener(this);
        binding.vipHead.setOnClickListener(this);
        binding.tvVip.setOnClickListener(this);
        binding.vipRude.setOnClickListener(this);
        binding.tvKnow.setOnClickListener(this);
        adapter = new BaseListAdapter<>(this, R.layout.code_his_list, BR.code);
        binding.lvHis.setAdapter(adapter);
        binding.lvHis.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Hiscode item=(Hiscode) adapter.getItem(i);
                if(vip.isEmpty()){
                    PermissionUtils.getEngine().vipShowDialog(VinScanActivity.this);
                    return;
                }
                if(item!=null){
                    vipNum(item.getCode());
                }
            }
        });
    }
    private void getVipInfo() {
        String url = Constant.VIP_INFO;
        String json = NetInterfaceEngine.getEngine().jsonNoData();
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String result) {
                dismissProgressDialog();
                VipInfoResponse emty = JSONUtil.parse(result, VipInfoResponse.class);
                if (emty != null && emty.getData() != null && emty.getStatus().equals("ok")) {
                    if (emty.getData() != null) {
                        ImagerPicUtil.getImageViewLoad(binding.vipHead, emty.getData().getHeadimgurl(), R.mipmap.login_logo);
                        String name=emty.getData().getNickname();
                        if(name.isEmpty()){
                            name="汽修人";
                        }
                        if (!emty.getData().getIs_master().equals("1")) {
                            binding.vipHead.setBorderWidth(0);
                            binding.tvVipMaster.setVisibility(View.GONE);
                        } else {
                            binding.vipHead.setBorderWidth(4);
                            if (emty.getData().getJingyan() < 100) {
                                binding.tvVipMaster.setVisibility(View.GONE);
                            } else {
                                binding.tvVipMaster.setVisibility(View.VISIBLE);
                            }
                        }
                        if (emty != null && emty.getData() != null && emty.getData().getVipinfo() != null) {
                            VipInfoResponse.DataBean.VipinfoBean vipinfoBean=emty.getData().getVipinfo();
                            binding.tvUserName.setText("尊敬的"+name+",您是"+vipinfoBean.getTitle());
                            binding.imgVipIco.setVisibility(View.VISIBLE);
                            if(vipinfoBean.getEnd_days()!=0) {
                                binding.vipTime.setText( "会员有效期还有" + vipinfoBean.getEnd_days()+"天。");
                            }else{
                                binding.vipTime.setText("会员有效期" + vipinfoBean.getEnd_time());
                            }
                            vip=vipinfoBean.getTitle();
//        vipLa
                        } else {
                            binding.imgVipIco.setVisibility(View.GONE);
                            if (emty != null && emty.getData() != null && emty.getData().getVip() != null && emty.getData().getVip().equals("0")) {
                                binding.tvUserName.setText("尊敬的"+name);
                                binding.vipTime.setText("您还不是会员，可以马上点击购买会员");
                                binding.vipRenew.setText("购买\n会员");
                                binding.vipRude.setVisibility(View.GONE);
                                User user=MyApplication.getInstances().getUser();
                                if(user!=null) {
                                    int vipnum = user.getData().getIs_vip();
                                    if(vipnum>0){
                                        MyApplication.getInstances().getUser().getData().setIs_vip(0);
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if (emty!=null&&emty.getMessage() != null) {
                        toast(emty.getMessage());
                    }
                }
                myhandl.sendEmptyMessageDelayed(0,1000*60);//一分钟后再请求
            }

            @Override
            public void onFail(HttpException e, String err) {
//                Toast.makeText(getActivity(), err, Toast.LENGTH_LONG).show();
                myhandl.sendEmptyMessageDelayed(0,1000*60);
            }
        });
    }
    private void SearchVin(final String vin) {
        String url = Constant.VIP_VIN;
        String json = NetInterfaceEngine.getEngine().jsonMapValue("vincode",vin);
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String result) {
                dismissProgressDialog();
                VinCar emty=JSONUtil.parse(result,VinCar.class);
                if(emty!=null&&emty.getData()!=null&&emty.getData().getList()!=null&&emty.getData().getList().getBrand_name()!=null){
                    InsertCodeFault(vin);
                    Intent intent=new Intent(VinScanActivity.this,VinCarActivity.class);
                    intent.putExtra("result",result);
                    intent.putExtra("vin",vin);
                    startActivity(intent);
                }else{
                    if(emty!=null&&emty.getMessage()!=null){
                        if(emty.getErrcode()!=null&&emty.getErrcode().equals("104")){
                            PermissionUtils.getEngine().vipShowDialog(VinScanActivity.this);
                            getVipInfo();
                        }else {
                            toast(emty.getMessage());
                        }
                    }else {
                        toast("没有VIN码数据");
                    }
                }

            }

            @Override
            public void onFail(HttpException e, String err) {
                dismissProgressDialog();
            }
        });
    }
    private void vipNum(final String vin) {
        String url = Constant.VIP_NUM;
        showProgressDialog();
        String json = NetInterfaceEngine.getEngine().jsonMapValue("vincode",vin);
        NetInterfaceEngine.getEngine().commitOKHttpJson(url, json, new NetHelper() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String result) {

                VinCar emty=JSONUtil.parse(result,VinCar.class);
                if(emty!=null&&emty.getData()!=null&&emty.getStatus().equals("ok")){
                    if(emty.getData().getStatus()!=null&&emty.getData().getStatus().equals("1")) {
                        SearchVin(vin);
                    }else{
                        dismissProgressDialog();
                        InsertCodeFault(vin);
                        Utils.vipDetailsNum(VinScanActivity.this,vip,emty.getData().getRead_count());
                    }
                }else{
                    dismissProgressDialog();
                    if(emty!=null&&emty.getMessage()!=null){
                        toast(emty.getMessage());
                    }
                }

            }

            @Override
            public void onFail(HttpException e, String err) {
                dismissProgressDialog();
            }
        });
    }
    public void initHisData() {
        if (vin_history.size() > 0) {
            vin_history.clear();
        }
        SQLiteDatabase db = VinHisHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.rawQuery(
                "select * from vin order by date desc limit 0, 5", null);
        while (cursor.moveToNext()) {
            Hiscode hiscode = new Hiscode();
            hiscode.setCode(cursor.getString(1));
            vin_history.add(hiscode);
        }
        cursor.close();
        db.close();
        if (vin_history.size() > 0) {
            adapter.setDataList(vin_history);
        }
        if (vin_history.size() > 0) {
            binding.llhiscode.setVisibility(View.VISIBLE);
        } else {
            binding.llhiscode.setVisibility(View.GONE);
        }

    }

    public void InsertCodeFault(String name) {
        SQLiteDatabase db = VinHisHelper.getInstance(this).getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from vin where name = '"
                + name + "'", null);
        if (cursor.getCount() > 0) { //
            db.delete("vin", "name = ?", new String[]{name});
        }
        db.execSQL("insert into vin(name, date) values('" + name + "', "
                + System.currentTimeMillis() + ")");
        cursor.close();
        db.close();
    }

    private void deletehisCode() {
        SQLiteDatabase db = VinHisHelper.getInstance(this).getReadableDatabase();
        db.execSQL("delete from vin");
        db.close();
        binding.llhiscode.setVisibility(View.GONE);
        vin_history.clear();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onResume() {
        super.onResume();
        getVipInfo();
        initHisData();
    }

    @Override
    public void onPause() {
        super.onPause();
        myhandl.removeMessages(0);
    }

    @Override
    protected void onDestroy() {
        // 释放本地质量控制模型
        CameraNativeHelper.release();
        OCR.getInstance().release();
        myhandl.removeMessages(0);
        super.onDestroy();
    }

    private void initAccessTokenWithAkSk() {
        OCR.getInstance().initAccessTokenWithAkSk(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken result) {
                String token = result.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
                Log.e("AK，SK方式获取token失败", error.getMessage());
            }
        }, getApplicationContext(), "biOG42huI8LubrYqSZn8BwFw", "pHnrXRx2PxWRAIbRs4kocN8qARD7TSP7");
    }

    private void initAccessToken() {
        OCR.getInstance().initAccessToken(new OnResultListener<AccessToken>() {
            @Override
            public void onResult(AccessToken accessToken) {
                String token = accessToken.getAccessToken();
                hasGotToken = true;
            }

            @Override
            public void onError(OCRError error) {
                error.printStackTrace();
            }
        }, getApplicationContext());
    }

    private boolean checkTokenStatus() {
        if (!hasGotToken) {
            Toast.makeText(getApplicationContext(), "token还未成功获取,请稍候再试", Toast.LENGTH_LONG).show();
        }
        return hasGotToken;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            initAccessToken();
        } else {
            Toast.makeText(getApplicationContext(), "需要android.permission.READ_PHONE_STATE", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onActivityResult(final int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // 识别成功回调，通用文字识别
        if (requestCode == REQUEST_CODE_GENERAL_BASIC && resultCode == Activity.RESULT_OK) {
            RecognizeService.recGeneralBasic(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                    new RecognizeService.ServiceListener() {
                        @Override
                        public void onResult(String result) {
                            Log.e("vin:",result);
                            if(result!=null){
                                ScanVin emty= JSONUtil.parse(result,ScanVin.class);
                                if(emty!=null&&emty.getWords_result()!=null&&emty.getWords_result().size()>0){
                                    for(ScanVin.WordsResultBean bean:emty.getWords_result()){
                                        String vin=bean.getWords().replaceAll(" ","");
                                        binding.editVin.setText(vin);
                                    }
                                }
                            }

                        }
                    });
        }

        // 识别成功回调，通用文字识别（高精度版）
        if (requestCode == REQUEST_CODE_ACCURATE_BASIC && resultCode == Activity.RESULT_OK) {
            showCode();
        }

    }
    private void idenVin(){
        RecognizeService.recAccurateBasic(this, FileUtil.getSaveFile(getApplicationContext()).getAbsolutePath(),
                new RecognizeService.ServiceListener() {
                    @Override
                    public void onResult(String result) {
                        Log.e("vin:",result);
                        if(result!=null){
                            ScanVin emty= JSONUtil.parse(result,ScanVin.class);
                            boolean isvin=false;
                            if(emty!=null&&emty.getWords_result()!=null&&emty.getWords_result().size()>0){
                                for(ScanVin.WordsResultBean bean:emty.getWords_result()){
                                    String vin=bean.getWords().replaceAll(" ","");
                                    if(vin.length()>10&&vin.length()<=17) {
                                        showVinDialog(vin);
                                        isvin=true;
                                        break;
                                    }else if(vin.length()>17){
                                        vin=vin.substring(0,17);
                                        showVinDialog(vin);
                                        isvin=true;
                                        break;
                                    }
                                }
                                if(!isvin){
                                    toast("请上传清淅的VIN码图片");
                                }
                            }else{
                                toast("请上传清淅的VIN码图片");
                            }
                        }
                    }
                });
    }
    private void showVinDialog(final String vin){
        CustomDialog dialog = CommonDialogManager.getInstance().createDialog(this, R.layout.vin_identify_dialog_show);
        dialog.setCdHelper(new CustomDialog.CustomDialogHelper() {
            @Override
            public void showDialog(final CustomDialog dialog) {
                TextView tv_vin = (TextView) dialog.findViewById(R.id.tv_vin);
                TextView bt_newcommit = (TextView) dialog.findViewById(R.id.bt_dimiss);
                TextView bt_ok = (TextView) dialog.findViewById(R.id.bt_ok);
                tv_vin.setText(vin);
                bt_newcommit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!checkTokenStatus()) {
                            return;
                        }
                        Intent intent=new Intent();
                        intent.setClass(VinScanActivity.this, CameraActivity.class);
                        intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                                FileUtil.getSaveFile(getApplication()).getAbsolutePath());
                        intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                                CameraActivity.CONTENT_TYPE_GENERAL);
                        startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
                        dialog.dismiss();
                    }
                });
                bt_ok.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        binding.editVin.setText(vin);
                        dialog.dismiss();
                        vipNum(vin);

                    }
                });

            }
        });
        dialog.show();
    }
    private void showCode() {
        CustomDialog dialog = CommonDialogManager.getInstance().createDialog(this, R.layout.iden_code_show);
        dialog.setCdHelper(new CustomDialog.CustomDialogHelper() {
            @Override
            public void showDialog(final CustomDialog dialog) {
                final EditText et_code = (EditText) dialog.findViewById(R.id.et_code);
                TextView tv_title = (TextView) dialog.findViewById(R.id.tv_title);
                TextView tv_code_iden = (TextView) dialog.findViewById(R.id.tv_code_iden);
                final ImageView vc_image = (ImageView) dialog.findViewById(R.id.vc_image);
                tv_title.setText("请输入图形验证码！");
                vc_image.setImageBitmap(Code.getInstance().getBitmap());
                getCode = Code.getInstance().getCode(); // 获取显示的验证码
                tv_code_iden.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String code = et_code.getText().toString().trim().toUpperCase();
                        getCode = getCode.toUpperCase();
                        if (code != null && !code.isEmpty()) {
                            if (code.equals(getCode)) {
                                idenVin();
                                dialog.dismiss();
                            } else {
                                toast("图形验证码错误！");
                            }
                        } else {
                            toast("请输入图形验证码，谢谢！");
                        }

                    }
                });
                vc_image.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        vc_image.setImageBitmap(Code.getInstance().getBitmap());
                        getCode = Code.getInstance().getCode();
                    }
                });

            }
        });
        dialog.show();
    }

    @Override
    public void onClick(View view) {
        Intent intent=new Intent();
        User user= MyApplication.getInstances().getUser();
        if(user==null&&view!=binding.rlBack){
            intent.setClass(this,LoginActivity.class);
            startActivity(intent);
            return;
        }
        if(view==binding.tvVin){
            if (!checkTokenStatus()) {
                return;
            }
            if(vip.isEmpty()){
                PermissionUtils.getEngine().vipShowDialog(this);
                return;
            }
            intent.setClass(this, CameraActivity.class);
            intent.putExtra(CameraActivity.KEY_OUTPUT_FILE_PATH,
                    FileUtil.getSaveFile(getApplication()).getAbsolutePath());
            intent.putExtra(CameraActivity.KEY_CONTENT_TYPE,
                    CameraActivity.CONTENT_TYPE_GENERAL);
            startActivityForResult(intent, REQUEST_CODE_ACCURATE_BASIC);
        }else if (view == binding.vipRenew) {
            intent.setClass(this, RenewActivity.class);
            startActivity(intent);
        }else if(view==binding.searchTvdele){
            deletehisCode();
        }else if(view==binding.tvSeek){
            if(vip.isEmpty()){
                PermissionUtils.getEngine().vipShowDialog(this);
                return;
            }
            String vin=binding.editVin.getText().toString();
            if(!vin.isEmpty()&&vin.length()==17){
                vipNum(vin);
            }else{
                toast("请输入17位VIN码");
            }
        }else if(view==binding.rlBack){
            finish();
        }else if(view==binding.vipHead){
            String openid= MyApplication.getInstances().getOpenid();
            if(openid!=null) {
                intent.setClass(this, ActionSpaceActivity.class);
                intent.putExtra("openid", openid);
                startActivity(intent);
            }
        }else if(view==binding.tvVip){
            intent.setClass(this, Vipactivity.class);
            startActivity(intent);
        }else if(view==binding.vipRude){
            PermissionUtils.getEngine().startWebview(this,Constant.WEB_VIP_POWER);
        }else if(view==binding.tvKnow){
            intent.setClass(this, Vipactivity.class);
            startActivity(intent);
        }
    }
    @SuppressLint("HandlerLeak")
    Handler myhandl = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    getVipInfo();
                    break;

            }
            super.handleMessage(msg);
        }
    };
}
