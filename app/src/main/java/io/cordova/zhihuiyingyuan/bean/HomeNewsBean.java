package io.cordova.zhihuiyingyuan.bean;

import java.util.List;

/**
 * Created by Administrator on 2019/8/28 0028.
 */

public class HomeNewsBean {
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
        private String newsTitle;

        private String newsHref;

        private String newsDate;

        private String newsId;

        public void setNewsTitle(String newsTitle){
            this.newsTitle = newsTitle;
        }
        public String getNewsTitle(){
            return this.newsTitle;
        }
        public void setNewsHref(String newsHref){
            this.newsHref = newsHref;
        }
        public String getNewsHref(){
            return this.newsHref;
        }
        public void setNewsDate(String newsDate){
            this.newsDate = newsDate;
        }
        public String getNewsDate(){
            return this.newsDate;
        }
        public void setNewsId(String newsId){
            this.newsId = newsId;
        }
        public String getNewsId(){
            return this.newsId;
        }

    }
}
