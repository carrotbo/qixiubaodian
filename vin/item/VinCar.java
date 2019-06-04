package com.gwkj.qixiubaodian.module.vin.item;

/**
 * Created by carrot on 2018/10/31.
 */

public class VinCar {


    /**
     * status : ok
     * data : {"list":{"id":"21","vin":"LBVNU79049SB81546","brand_name":" 宝马","model_name":" 525Li","sale_name":" 2.5 手自一体 领先型","car_type":" 轿车","engine_type":" N52B25BF","car_line":" 5系","power":" 160","jet_type":"","fuel_Type":" 汽油","transmission_type":" 自动","cylinder_number":"6","cylinder_form":"","output_volume":" 2.5","made_year":"2009","made_month":"2","stop_year":"2009","effluent_standard":" 国4","fuel_num":" 97#","guiding_price":"55.56","year":"2009","air_bag":"","seat_num":"5","vehicle_level":" 中大型车","door_num":" 四门","car_body":" 三厢","manufacturer":" 华晨宝马","gears_num":"","car_weight":"","assembly_factory":"","is_insert":"1","drive_mode":" 后轮驱动"}}
     * token : {"loginid":"bf3047122a31d15f8a0bc9106c766fdd","openid":"3be01e7f47f7f34c89a827af0b010d7e","uid":"1219722"}
     */

    private String status;
    private DataBean data;
    private TokenBean token;
    private String message;
    private String errcode;

    public String getErrcode() {
        return errcode;
    }

    public void setErrcode(String errcode) {
        this.errcode = errcode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public TokenBean getToken() {
        return token;
    }

    public void setToken(TokenBean token) {
        this.token = token;
    }

    public static class DataBean {
        /**
         * list : {"id":"21","vin":"LBVNU79049SB81546","brand_name":" 宝马","model_name":" 525Li","sale_name":" 2.5 手自一体 领先型","car_type":" 轿车","engine_type":" N52B25BF","car_line":" 5系","power":" 160","jet_type":"","fuel_Type":" 汽油","transmission_type":" 自动","cylinder_number":"6","cylinder_form":"","output_volume":" 2.5","made_year":"2009","made_month":"2","stop_year":"2009","effluent_standard":" 国4","fuel_num":" 97#","guiding_price":"55.56","year":"2009","air_bag":"","seat_num":"5","vehicle_level":" 中大型车","door_num":" 四门","car_body":" 三厢","manufacturer":" 华晨宝马","gears_num":"","car_weight":"","assembly_factory":"","is_insert":"1","drive_mode":" 后轮驱动"}
         */

        private ListBean list;
        private String status;
        private String read_count;
        private String title;

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getRead_count() {
            return read_count;
        }

        public void setRead_count(String read_count) {
            this.read_count = read_count;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public ListBean getList() {
            return list;
        }

        public void setList(ListBean list) {
            this.list = list;
        }

        public static class ListBean {
            /**
             * id : 21
             * vin : LBVNU79049SB81546
             * brand_name :  宝马
             * model_name :  525Li
             * sale_name :  2.5 手自一体 领先型
             * car_type :  轿车
             * engine_type :  N52B25BF
             * car_line :  5系
             * power :  160
             * jet_type :
             * fuel_Type :  汽油
             * transmission_type :  自动
             * cylinder_number : 6
             * cylinder_form :
             * output_volume :  2.5
             * made_year : 2009
             * made_month : 2
             * stop_year : 2009
             * effluent_standard :  国4
             * fuel_num :  97#
             * guiding_price : 55.56
             * year : 2009
             * air_bag :
             * seat_num : 5
             * vehicle_level :  中大型车
             * door_num :  四门
             * car_body :  三厢
             * manufacturer :  华晨宝马
             * gears_num :
             * car_weight :
             * assembly_factory :
             * is_insert : 1
             * drive_mode :  后轮驱动
             */

            private String id;
            private String vin;
            private String brand_name;
            private String model_name;
            private String sale_name;
            private String car_type;
            private String engine_type;
            private String car_line;
            private String power;
            private String jet_type;
            private String fuel_Type;
            private String transmission_type;
            private String cylinder_number;
            private String cylinder_form;
            private String output_volume;
            private String made_year;
            private String made_month;
            private String stop_year;
            private String effluent_standard;
            private String fuel_num;
            private String guiding_price;
            private String year;
            private String air_bag;
            private String seat_num;
            private String vehicle_level;
            private String door_num;
            private String car_body;
            private String manufacturer;
            private String gears_num;
            private String car_weight;
            private String assembly_factory;
            private String is_insert;
            private String drive_mode;
            private String pinpaiid;

            public String getPinpaiid() {
                return pinpaiid;
            }

            public void setPinpaiid(String pinpaiid) {
                this.pinpaiid = pinpaiid;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getVin() {
                return vin;
            }

            public void setVin(String vin) {
                this.vin = vin;
            }

            public String getBrand_name() {
                return brand_name;
            }

            public void setBrand_name(String brand_name) {
                this.brand_name = brand_name;
            }

            public String getModel_name() {
                return model_name;
            }

            public void setModel_name(String model_name) {
                this.model_name = model_name;
            }

            public String getSale_name() {
                return sale_name;
            }

            public void setSale_name(String sale_name) {
                this.sale_name = sale_name;
            }

            public String getCar_type() {
                return car_type;
            }

            public void setCar_type(String car_type) {
                this.car_type = car_type;
            }

            public String getEngine_type() {
                return engine_type;
            }

            public void setEngine_type(String engine_type) {
                this.engine_type = engine_type;
            }

            public String getCar_line() {
                return car_line;
            }

            public void setCar_line(String car_line) {
                this.car_line = car_line;
            }

            public String getPower() {
                return power;
            }

            public void setPower(String power) {
                this.power = power;
            }

            public String getJet_type() {
                return jet_type;
            }

            public void setJet_type(String jet_type) {
                this.jet_type = jet_type;
            }

            public String getFuel_Type() {
                return fuel_Type;
            }

            public void setFuel_Type(String fuel_Type) {
                this.fuel_Type = fuel_Type;
            }

            public String getTransmission_type() {
                return transmission_type;
            }

            public void setTransmission_type(String transmission_type) {
                this.transmission_type = transmission_type;
            }

            public String getCylinder_number() {
                return cylinder_number;
            }

            public void setCylinder_number(String cylinder_number) {
                this.cylinder_number = cylinder_number;
            }

            public String getCylinder_form() {
                return cylinder_form;
            }

            public void setCylinder_form(String cylinder_form) {
                this.cylinder_form = cylinder_form;
            }

            public String getOutput_volume() {
                return output_volume;
            }

            public void setOutput_volume(String output_volume) {
                this.output_volume = output_volume;
            }

            public String getMade_year() {
                return made_year;
            }

            public void setMade_year(String made_year) {
                this.made_year = made_year;
            }

            public String getMade_month() {
                return made_month;
            }

            public void setMade_month(String made_month) {
                this.made_month = made_month;
            }

            public String getStop_year() {
                return stop_year;
            }

            public void setStop_year(String stop_year) {
                this.stop_year = stop_year;
            }

            public String getEffluent_standard() {
                return effluent_standard;
            }

            public void setEffluent_standard(String effluent_standard) {
                this.effluent_standard = effluent_standard;
            }

            public String getFuel_num() {
                return fuel_num;
            }

            public void setFuel_num(String fuel_num) {
                this.fuel_num = fuel_num;
            }

            public String getGuiding_price() {
                return guiding_price;
            }

            public void setGuiding_price(String guiding_price) {
                this.guiding_price = guiding_price;
            }

            public String getYear() {
                return year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getAir_bag() {
                return air_bag;
            }

            public void setAir_bag(String air_bag) {
                this.air_bag = air_bag;
            }

            public String getSeat_num() {
                return seat_num;
            }

            public void setSeat_num(String seat_num) {
                this.seat_num = seat_num;
            }

            public String getVehicle_level() {
                return vehicle_level;
            }

            public void setVehicle_level(String vehicle_level) {
                this.vehicle_level = vehicle_level;
            }

            public String getDoor_num() {
                return door_num;
            }

            public void setDoor_num(String door_num) {
                this.door_num = door_num;
            }

            public String getCar_body() {
                return car_body;
            }

            public void setCar_body(String car_body) {
                this.car_body = car_body;
            }

            public String getManufacturer() {
                return manufacturer;
            }

            public void setManufacturer(String manufacturer) {
                this.manufacturer = manufacturer;
            }

            public String getGears_num() {
                return gears_num;
            }

            public void setGears_num(String gears_num) {
                this.gears_num = gears_num;
            }

            public String getCar_weight() {
                return car_weight;
            }

            public void setCar_weight(String car_weight) {
                this.car_weight = car_weight;
            }

            public String getAssembly_factory() {
                return assembly_factory;
            }

            public void setAssembly_factory(String assembly_factory) {
                this.assembly_factory = assembly_factory;
            }

            public String getIs_insert() {
                return is_insert;
            }

            public void setIs_insert(String is_insert) {
                this.is_insert = is_insert;
            }

            public String getDrive_mode() {
                return drive_mode;
            }

            public void setDrive_mode(String drive_mode) {
                this.drive_mode = drive_mode;
            }
        }
    }

    public static class TokenBean {
        /**
         * loginid : bf3047122a31d15f8a0bc9106c766fdd
         * openid : 3be01e7f47f7f34c89a827af0b010d7e
         * uid : 1219722
         */

        private String loginid;
        private String openid;
        private String uid;

        public String getLoginid() {
            return loginid;
        }

        public void setLoginid(String loginid) {
            this.loginid = loginid;
        }

        public String getOpenid() {
            return openid;
        }

        public void setOpenid(String openid) {
            this.openid = openid;
        }

        public String getUid() {
            return uid;
        }

        public void setUid(String uid) {
            this.uid = uid;
        }
    }
}
