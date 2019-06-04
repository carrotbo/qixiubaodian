package com.gwkj.qixiubaodian.module.vin;

import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.view.View;

import com.gwkj.qixiubaodian.IndexActivity.ask_data_activity.BrandTYpeActivity;
import com.gwkj.qixiubaodian.R;
import com.gwkj.qixiubaodian.databinding.ActivityVinCarBinding;
import com.gwkj.qixiubaodian.module.master.MasterBrandActivity;
import com.gwkj.qixiubaodian.module.search_data.SeekResultActivity;
import com.gwkj.qixiubaodian.module.vin.item.VinCar;
import com.gwkj.qixiubaodian.qxbd.BaseActivity;
import com.gwkj.qixiubaodian.utils.JSONUtil;
import com.gwkj.qixiubaodian.utils.PermissionUtils;

import static com.gwkj.qixiubaodian.constant.Constant.NEXT_BACK;

public class VinCarActivity extends BaseActivity implements View.OnClickListener{

    private ActivityVinCarBinding binding;
    private VinCar.DataBean.ListBean carItem;
    private String vin=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_vin_car);
        initIntent();
        initView();
    }
    private void initIntent(){
        Intent intent=getIntent();
        String result=intent.getStringExtra("result");
        vin=intent.getStringExtra("vin");
        VinCar emty= JSONUtil.parse(result,VinCar.class);
        if(emty!=null&&emty.getData()!=null&&emty.getData().getList()!=null) {
            VinCar.DataBean.ListBean car=emty.getData().getList();
            carItem=car;
            binding.setVincar(car);
            if(car.getGuiding_price()==null||car.getGuiding_price().isEmpty()){
                binding.llPrice.setVisibility(View.GONE);
                binding.linePrice.setVisibility(View.GONE);
            }
            if(car.getYear()==null||car.getYear().isEmpty()){
                binding.llYear.setVisibility(View.GONE);
                binding.lineYear.setVisibility(View.GONE);
            }
            if(car.getCar_body()==null||car.getCar_body().isEmpty()){
                binding.llBody.setVisibility(View.GONE);
                binding.lineBody.setVisibility(View.GONE);
            }
            if (car.getEngine_type() == null || car.getEngine_type().isEmpty()) {
                binding.llEngine.setVisibility(View.GONE);
                binding.lineEngine.setVisibility(View.GONE);
            }
            if (car.getSeat_num() == null || car.getSeat_num().isEmpty()) {
                binding.llSeatnum.setVisibility(View.GONE);
                binding.lineSeatnum.setVisibility(View.GONE);
            }
            if (car.getTransmission_type() == null || car.getTransmission_type().isEmpty()) {
                binding.llTransmission.setVisibility(View.GONE);
                binding.lineTransmission.setVisibility(View.GONE);
            }
            if (car.getCar_type() == null || car.getCar_type().isEmpty()) {
                binding.llCar.setVisibility(View.GONE);
                binding.lineCar.setVisibility(View.GONE);
            }
            if (car.getFuel_Type() == null || car.getFuel_Type().isEmpty()) {
                binding.llFuel.setVisibility(View.GONE);
                binding.lineFuel.setVisibility(View.GONE);
            }
            if (car.getFuel_num()== null || car.getFuel_num().isEmpty()) {
                binding.llFuelNum.setVisibility(View.GONE);
                binding.lineFuelNum.setVisibility(View.GONE);
            }
            if (car.getEffluent_standard() == null || car.getEffluent_standard().isEmpty()) {
                binding.llEffluent.setVisibility(View.GONE);
                binding.lineEffluent.setVisibility(View.GONE);
            }
            if (car.getCylinder_number() == null || car.getCylinder_number().isEmpty()) {
                binding.llCylinder.setVisibility(View.GONE);
                binding.lineCylinder.setVisibility(View.GONE);
            }
            if (car.getJet_type() == null || car.getJet_type().isEmpty()) {
                binding.llJet.setVisibility(View.GONE);
                binding.lineJet.setVisibility(View.GONE);
            }
            if (car.getMade_year() == null || car.getMade_year().isEmpty()) {
                binding.llMadeYear.setVisibility(View.GONE);
                binding.lineMadeYear.setVisibility(View.GONE);
            }
            if (car.getMade_month() == null || car.getMade_month().isEmpty()) {
                binding.llMadeMonth.setVisibility(View.GONE);
                binding.lineMadeMonth.setVisibility(View.GONE);
            }
            if (car.getOutput_volume() == null || car.getOutput_volume().isEmpty()) {
                binding.llOutput.setVisibility(View.GONE);
                binding.lineOutput.setVisibility(View.GONE);
            }
            if (car.getStop_year() == null || car.getStop_year().isEmpty()) {
                binding.llStop.setVisibility(View.GONE);
                binding.lineStop.setVisibility(View.GONE);
            }
            if (car.getVehicle_level() == null || car.getVehicle_level().isEmpty()) {
                binding.llVehicle.setVisibility(View.GONE);
                binding.lineVehicle.setVisibility(View.GONE);
            }
            if (car.getPower() == null || car.getPower().isEmpty()) {
                binding.llPower.setVisibility(View.GONE);
                binding.linePower.setVisibility(View.GONE);
            }
            if (car.getDrive_mode() == null || car.getDrive_mode().isEmpty()) {
                binding.llDrive.setVisibility(View.GONE);
                binding.lineDrive.setVisibility(View.GONE);
            }
            if(car.getPinpaiid()==null||car.getPinpaiid().isEmpty()){
                binding.reData.setVisibility(View.GONE);
                binding.reMaster.setVisibility(View.GONE);
            }

        }
    }
    private void initView(){
        binding.rlBack.setOnClickListener(this);
        binding.reData.setOnClickListener(this);
        binding.reAsk.setOnClickListener(this);
        binding.reMaster.setOnClickListener(this);
        binding.reKnowlege.setOnClickListener(this);
        binding.reNote.setOnClickListener(this);
        binding.imgErrcorrect.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        Intent intent;
        if(view==binding.rlBack){
            finish();
        }else if(view==binding.reData){
            if(carItem!=null&& carItem.getPinpaiid()!=null&&!carItem.getPinpaiid().isEmpty()) {
                intent = new Intent(this, BrandTYpeActivity.class);
                intent.putExtra("pinpai", carItem.getPinpaiid());
                intent.putExtra("title_name", carItem.getBrand_name());
                startActivity(intent);
            }else{
                toast("无相关数据");
            }
        }else if(view==binding.reAsk){
            if(carItem!=null) {
                intent = new Intent(this, SeekResultActivity.class);
                intent.putExtra("type", 4);
                intent.putExtra("table", "seekall");
                intent.putExtra("text", carItem.getBrand_name() + " " + carItem.getCar_line());
                startActivityForResult(intent, NEXT_BACK);
            }else{
                toast("无相关数据");
            }

        }else if(view==binding.reMaster){
            if(carItem!=null&& carItem.getPinpaiid()!=null&&!carItem.getPinpaiid().isEmpty()) {
            intent=new Intent(this, MasterBrandActivity.class);
            intent.putExtra("name",carItem.getBrand_name());
            intent.putExtra("pinpai_id",carItem.getPinpaiid());
            startActivity(intent);
            }else{
                toast("无相关数据");
            }
        }else if(view==binding.reKnowlege){
            if(carItem!=null) {
                intent = new Intent(this, SeekResultActivity.class);
                intent.putExtra("type", 6);
                intent.putExtra("table", "seekall");
                intent.putExtra("text", carItem.getBrand_name() + " " + carItem.getCar_line());
                startActivityForResult(intent, NEXT_BACK);
            }else{
                toast("无相关数据");
            }
//            if(carItem!=null&& carItem.getPinpaiid()!=null&&!carItem.getPinpaiid().isEmpty()) {
//            intent = new Intent(this, AnliBrandActivity.class);
//            intent.putExtra("pinpai_id", carItem.getPinpaiid());
//            intent.putExtra("name", carItem.getBrand_name());
//            intent.putExtra("type", "4");
//            startActivity(intent);
//        }else{
//            toast("无相关数据");
//        }
        }else if(view==binding.reNote){
            if(carItem!=null) {
                intent = new Intent(this, SeekResultActivity.class);
                intent.putExtra("type", 5);
                intent.putExtra("table", "seekall");
                intent.putExtra("text", carItem.getBrand_name() + " " + carItem.getCar_line());
                startActivityForResult(intent, NEXT_BACK);
            }else{
                toast("无相关数据");
            }
        }else if(view==binding.imgErrcorrect){
             PermissionUtils.getEngine().dataErrCorrect(this,vin,"0","24","hh_vin").show();

        }
    }
}
