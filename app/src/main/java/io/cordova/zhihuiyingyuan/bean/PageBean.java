package io.cordova.zhihuiyingyuan.bean;

import java.util.List;

public class PageBean {
    private boolean success;

    private String msg;

    private List<Obj> obj ;

    private String count;

    private String attributes;

    public void setSuccess(boolean success){
        this.success = success;
    }
    public boolean getSuccess(){
        return this.success;
    }
    public void setMsg(String msg){
        this.msg = msg;
    }
    public String getMsg(){
        return this.msg;
    }
    public void setObj(List<Obj> obj){
        this.obj = obj;
    }
    public List<Obj> getObj(){
        return this.obj;
    }
    public void setCount(String count){
        this.count = count;
    }
    public String getCount(){
        return this.count;
    }
    public void setAttributes(String attributes){
        this.attributes = attributes;
    }
    public String getAttributes(){
        return this.attributes;
    }


    public class Obj {
        private int welcomePageId;

        private String welcomePageImg;

        private int welcomePageEquipment;

        private String welcomePageStartTime;

        private String welcomePageEndTime;

        private String welcomePageSize;

        private int welcomePageStatus;

        private String welcomePageCreatTime;

        private String welcomePageMemberId;

        private int welcomePageType;

        public void setWelcomePageId(int welcomePageId){
            this.welcomePageId = welcomePageId;
        }
        public int getWelcomePageId(){
            return this.welcomePageId;
        }
        public void setWelcomePageImg(String welcomePageImg){
            this.welcomePageImg = welcomePageImg;
        }
        public String getWelcomePageImg(){
            return this.welcomePageImg;
        }
        public void setWelcomePageEquipment(int welcomePageEquipment){
            this.welcomePageEquipment = welcomePageEquipment;
        }
        public int getWelcomePageEquipment(){
            return this.welcomePageEquipment;
        }


        public void setWelcomePageSize(String welcomePageSize){
            this.welcomePageSize = welcomePageSize;
        }
        public String getWelcomePageSize(){
            return this.welcomePageSize;
        }
        public void setWelcomePageStatus(int welcomePageStatus){
            this.welcomePageStatus = welcomePageStatus;
        }
        public int getWelcomePageStatus(){
            return this.welcomePageStatus;
        }

        public void setWelcomePageMemberId(String welcomePageMemberId){
            this.welcomePageMemberId = welcomePageMemberId;
        }
        public String getWelcomePageMemberId(){
            return this.welcomePageMemberId;
        }
        public void setWelcomePageType(int welcomePageType){
            this.welcomePageType = welcomePageType;
        }
        public int getWelcomePageType(){
            return this.welcomePageType;
        }

        public String getWelcomePageStartTime() {
            return welcomePageStartTime;
        }

        public void setWelcomePageStartTime(String welcomePageStartTime) {
            this.welcomePageStartTime = welcomePageStartTime;
        }

        public String getWelcomePageEndTime() {
            return welcomePageEndTime;
        }

        public void setWelcomePageEndTime(String welcomePageEndTime) {
            this.welcomePageEndTime = welcomePageEndTime;
        }

        public String getWelcomePageCreatTime() {
            return welcomePageCreatTime;
        }

        public void setWelcomePageCreatTime(String welcomePageCreatTime) {
            this.welcomePageCreatTime = welcomePageCreatTime;
        }
    }

}
