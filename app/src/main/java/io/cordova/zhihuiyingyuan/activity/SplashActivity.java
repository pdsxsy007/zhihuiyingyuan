package io.cordova.zhihuiyingyuan.activity;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.umeng.analytics.MobclickAgent;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.lang.ref.WeakReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import butterknife.BindView;
import cn.jpush.android.api.JPushInterface;
import io.cordova.zhihuiyingyuan.MainActivity;
import io.cordova.zhihuiyingyuan.R;
import io.cordova.zhihuiyingyuan.UrlRes;
import io.cordova.zhihuiyingyuan.bean.Constants;
import io.cordova.zhihuiyingyuan.bean.LoginBean;
import io.cordova.zhihuiyingyuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyingyuan.utils.CookieUtils;
import io.cordova.zhihuiyingyuan.utils.MyApp;
import io.cordova.zhihuiyingyuan.utils.SPUtil;
import io.cordova.zhihuiyingyuan.utils.SPUtils;
import io.cordova.zhihuiyingyuan.utils.StringUtils;
import io.cordova.zhihuiyingyuan.utils.T;

import static io.cordova.zhihuiyingyuan.utils.AesEncryptUtile.key;



/**
 * Created by Administrator on 2018/12/11 0011.
 */

public class SplashActivity extends AppCompatActivity {

    ImageView imageView;
    private String s1;
    private String s2;
    private Handler handler = new MyHandler(this);
    private static String localVersionName;
    @BindView(R.id.webView)
    WebView webView;
    public static final int PASSWORD_LOGIN_FLAG = 0x0004;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        imageView =  (ImageView) findViewById(R.id.iv_bg);

            getLocalVersionName(getApplicationContext());
            //getMsg();
        String infoType = (String) SPUtils.get(MyApp.getInstance(), "InfoType", "");
        //ToastUtils.showToast(this,infoType+"infoType");
        if(infoType.equals("1")){
            getMsg();
            SPUtils.put(MyApp.getInstance(),"InfoType","");
        }else {
            handleOpenClick();
        }

        String welcomePageStartTime = (String) SPUtils.get(this, "welcomePageStartTime", "");
        String welcomePageEndTime = (String) SPUtils.get(this, "welcomePageEndTime", "");

        initView();

        getNowTime2(welcomePageStartTime,welcomePageEndTime);
        //getNowTime();
    }

    private static SimpleDateFormat sf = null;
     public static String getDateToString(long time) {
                 Date d = new Date(time);
                 sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                 return sf.format(d);
             }


    private void getNowTime2(String welcomePageStartTime, String welcomePageEndTime) {
        if(!welcomePageStartTime.equals("") && !welcomePageEndTime.equals("")){

            long l1 = Long.parseLong(welcomePageStartTime);
            long l2 = Long.parseLong(welcomePageEndTime);

            String startTimeImage = getDateToString(l1);
            String endTimeImage = getDateToString(l2);
            Log.e("startTimeImage",startTimeImage);
            Log.e("endTimeImage",endTimeImage);
            SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
            Date startTime = null;
            try {
               /* startTime = ft.parse("2020-09-21 14:50:54");
                Date endTime = ft.parse("2020-09-21 18:54:54");*/
                startTime = ft.parse(startTimeImage);
                Date endTime = ft.parse(endTimeImage);
                Date nowTime = new Date();
                boolean effectiveDate = isEffectiveDate(nowTime, startTime, endTime);
                if (effectiveDate) {
                    Glide.with(this).load(new File(Environment.getExternalStorageDirectory()+"/downImage.png")).into(imageView);
                }else {

                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

    }

    public void getNowTime()  {
        SimpleDateFormat ft = new SimpleDateFormat ("yyyy-MM-dd hh:mm:ss");
        Date startTime = null;
        try {
            startTime = ft.parse("2020-08-17 14:50:54");
            Date endTime = ft.parse("2020-08-17 15:54:54");
            Date nowTime = new Date();
            boolean effectiveDate = isEffectiveDate(nowTime, startTime, endTime);
            if (effectiveDate) {
                switchIcon(1);
            }else {
                switchIcon(2);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    /**
     *
     * @param nowTime   当前时间
     * @param startTime	开始时间
     * @param endTime   结束时间
     * @return
     * @author sunran   判断当前时间在时间区间内
     */
    public static boolean isEffectiveDate(Date nowTime, Date startTime, Date endTime) {
        if (nowTime.getTime() == startTime.getTime()
                || nowTime.getTime() == endTime.getTime()) {
            return true;
        }

        Calendar date = Calendar.getInstance();
        date.setTime(nowTime);

        Calendar begin = Calendar.getInstance();
        begin.setTime(startTime);

        Calendar end = Calendar.getInstance();
        end.setTime(endTime);

        if (date.after(begin) && date.before(end)) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * @param useCode 1、为活动图标 2 为用普通图标 3、不启用判断
     */
    private void switchIcon(int useCode) {

        try {
            //要跟manifest的activity-alias 的name保持一致
            String icon_tag = getPackageName()+".icon_tag";
            String icon_tag_1212 = getPackageName()+".icon_tag_1212";

            if (useCode != 3) {

                PackageManager pm = getPackageManager();

                ComponentName normalComponentName = new ComponentName(
                        getBaseContext(),
                        icon_tag);
                //正常图标新状态，此处使用用来修改清单文件中activity-alias下的android:enable的值
                int normalNewState = useCode == 2 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                //新状态跟当前状态不一样才执行
                if (pm.getComponentEnabledSetting(normalComponentName) != normalNewState) {
                    //PackageManager.DONT_KILL_APP表示执行此方法时不杀死当前的APP进程
                    pm.setComponentEnabledSetting(
                            normalComponentName,
                            normalNewState,
                            PackageManager.DONT_KILL_APP);
                }

                ComponentName actComponentName = new ComponentName(
                        getBaseContext(),
                        icon_tag_1212);
                //活动图标新状态
                int actNewState = useCode == 1 ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED
                        : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
                //新状态跟当前状态不一样才执行
                if (pm.getComponentEnabledSetting(actComponentName) != actNewState) {
                    pm.setComponentEnabledSetting(
                            actComponentName,
                            actNewState,
                            PackageManager.DONT_KILL_APP);
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);

    }
    protected void initView() {
        handler.sendEmptyMessageDelayed(0, 2000);
    }
    /**
     * 暂时闪屏界面不需要执行什么操作，所以先发个2秒的延时空消息,其实可以把软件所需的申请权限放和检查版本更新放这
     *
     */
    private class MyHandler extends Handler {

        private WeakReference<Context> reference;

        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""))){
                Intent intent = new Intent(getApplicationContext(),MainActivity.class);
                intent.putExtra("splash","splash");
                String openlogin = getIntent().getStringExtra("openlogin");
                if(openlogin != null){
                    intent.putExtra("openlogin",openlogin);
                }
                startActivity(intent);
                finish();
            }else {

                if (SPUtil.getInstance().getBoolean(Constants.SP_HAD_OPEN_FINGERPRINT_LOGIN)) {
                   Intent intent = new Intent(SplashActivity.this,LoginActivity3.class);
                   startActivity(intent);
                   finish();
                }else {
                    netWorkLogin();
                }

            }

          /*  Intent intent = new Intent(getApplicationContext(),Main2Activity.class);
            startActivity(intent);
            finish();*/
        }
    }

    LoginBean loginBean;
    String tgt;
    private void netWorkLogin() {
        s1 = (String) SPUtils.get(MyApp.getInstance(),"username","");
        s2 =  (String) SPUtils.get(MyApp.getInstance(),"password","");

        try {
            String imei = AesEncryptUtile.encrypt((String) SPUtils.get(this, "imei", ""), key);
            OkGo.<String>get(UrlRes.HOME2_URL +UrlRes.loginUrl)
                    .params("openid",AesEncryptUtile.openid)
                    .params("username",s1)
                    .params("password",s2)
                    .params("type","10")
                    .params("equipmentId",imei)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            loginBean = JSON.parseObject(response.body(),LoginBean.class);
                            if (loginBean.isSuccess() ) {
                                try {

                                    tgt = AesEncryptUtile.decrypt(loginBean.getAttributes().getTgt(),key);
                                    String userName = AesEncryptUtile.decrypt(loginBean.getAttributes().getUsername(),key) ;
                                    String userId  = AesEncryptUtile.encrypt(userName+ "_"+ Calendar.getInstance().getTimeInMillis(),key);

                                    SPUtils.put(MyApp.getInstance(),"userId",userId);
//                                SPUtils.put(MyApp.getInstance(),"tgt",tgt);
                                    SPUtils.put(getApplicationContext(),"TGC",tgt);
                                    SPUtils.put(getApplicationContext(),"username",s1);
                                    SPUtils.put(getApplicationContext(),"password",s2);
//                                   webView.setWebViewClient(mWebViewClient);
//                                    webView = findViewById(R.id.webView);
//                                  webView.loadUrl(HOME_URL+"/portal/login/appLogin");

                                    Intent intent = new Intent(MyApp.getInstance(),MainActivity.class);
                                    intent.putExtra("userId",userName);
                                    intent.putExtra("splash","splash");
                                    startActivity(intent);
                                    finish();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }else {
                                T.showShort(MyApp.getInstance(),loginBean.getMsg());

                                SPUtils.put(MyApp.getInstance(),"userId","");
//                                SPUtils.put(MyApp.getInstance(),"tgt",tgt);
                                SPUtils.put(getApplicationContext(),"TGC","");
                                SPUtils.put(getApplicationContext(),"username","");
                                SPUtils.put(getApplicationContext(),"password","");
                                Intent intent = new Intent(MyApp.getInstance(),MainActivity.class);
                                intent.putExtra("splash","splash");
                                startActivity(intent);
                                finish();

                            }
                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Intent intent = new Intent(MyApp.getInstance(),MainActivity.class);
                            startActivity(intent);
                            finish();

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    /**华为 OPPO  Fac*/
    private static final String TAG = "HuaWei";
    /**消息Id**/
    private static final String KEY_MSGID = "msg_id";
    private static final String KEY_TYPE = "msg_type";
    /**该通知的下发通道**/
    private static final String KEY_WHICH_PUSH_SDK = "rom_type";
    /**通知标题**/
    private static final String KEY_TITLE = "n_title";
    /**通知内容**/
    private static final String KEY_CONTENT = "n_content";
    /**通知附加字段**/
    private static final String KEY_EXTRAS = "n_extras";

    /**
     * 处理点击事件，当前启动配置的Activity都是使用
     * Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK
     * 方式启动，只需要在onCreat中调用此方法进行处理
     */
    private void handleOpenClick() {
        Log.d(TAG, "用户点击打开了通知");
        String data = null;
        //获取华为平台附带的jpush信息
        if (getIntent().getData() != null) {
            data = getIntent().getData().toString();
        }

        //获取fcm或oppo平台附带的jpush信息
        if (TextUtils.isEmpty(data) && getIntent().getExtras() != null) {
            data = getIntent().getExtras().getString("JMessageExtra");
        }

        Log.w(TAG, " -消息体- " + String.valueOf(data));
        if (TextUtils.isEmpty(data)) return;
        try {
            JSONObject jsonObject = new JSONObject(data);
            String msgId = jsonObject.optString(KEY_MSGID);
            String msgType = jsonObject.optString(KEY_TYPE);
            byte whichPushSDK = (byte) jsonObject.optInt(KEY_WHICH_PUSH_SDK);
            String title = jsonObject.optString(KEY_TITLE);
            String content = jsonObject.optString(KEY_CONTENT);
            String extras = jsonObject.optString(KEY_EXTRAS);

            try {
                JSONObject extraJson = new JSONObject(extras);
                if (extraJson.length() > 0) {
                    //Log.e("extras", extras);
                    //Log.e("extraJson", extraJson.getString("messageId"));
                    msgId =  extraJson.getString("messageId");
                    msgType =  extraJson.getString("messageType");
                    //Log.e("msgId", msgId);
                    SPUtils.put(MyApp.getInstance(),"msgId",msgId);
                    SPUtils.put(MyApp.getInstance(),"msgType",msgType);
                }
            } catch (JSONException ignored) {
                //Log.e("JPush",ignored.getMessage());
            }

//            StringBuilder sb = new StringBuilder();
//            sb.append("msgId:");
//            sb.append(String.valueOf(msgId));
//            sb.append("\n");
//            sb.append("title:");
//            sb.append(String.valueOf(title));
//            sb.append("\n");
//            sb.append("content:");
//            sb.append(String.valueOf(content));
//            sb.append("\n");
//            sb.append("extras:");
//            sb.append(String.valueOf(extras));
//            sb.append("\n");
//            sb.append("platform:");
//            sb.append(getPushSDKName(whichPushSDK));
////            mTextView.setText(sb.toString());
//            Log.w(TAG, sb.toString()+"  - 华为推送");
            //上报点击事件
            JPushInterface.reportNotificationOpened(this, msgId, whichPushSDK);
        } catch (JSONException e) {
            Log.w(TAG, "parse notification error");
        }


    }

    /**
     * 推送消息获取
     * msgType 消息类型(-1:删除，0:系统消息,1:待办,2:待阅,3:已办,4:已阅,5:我的申请)
     *
     * */
    private void getMsg() {
        Intent intent = getIntent();
        if (null != intent) {
            Bundle bundle = getIntent().getExtras();
            String title = null;
            String content = null;
            String extrasBean = null;
            String extrasBean2 = null;
            String msgId = null;
            String msgType = null;
            if(bundle!=null){

                //Log.e("bundle",bundle+"");
               // ToastUtils.showToast(SplashActivity.this,bundle+"");
                title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                content = bundle.getString(JPushInterface.EXTRA_ALERT);
                extrasBean = bundle.getString(JPushInterface.EXTRA_EXTRA);
                extrasBean2 = bundle.getString("JMessageExtra");
                if(!StringUtils.isEmpty(extrasBean)){
                    try {
                        JSONObject extraJson = new JSONObject(extrasBean);
                        if (extraJson.length() > 0) {
                            //Log.e("extras", extrasBean);
                            //Log.e("extraJson", extraJson.getString("messageId"));
                            msgId =  extraJson.getString("messageId");
                            msgType =  extraJson.getString("messageType");
                            //Log.e("msgId", msgId);
                            SPUtils.put(MyApp.getInstance(),"msgId",msgId);
                            SPUtils.put(MyApp.getInstance(),"msgType",msgType);

                        }
                    } catch (JSONException ignored) {
                        //Log.e("JPush",ignored.getMessage());
                    }
                }
                /*else if(!StringUtils.isEmpty(extrasBean2)){

                    try {

                        JSONObject extraJson2 = null;
                        extraJson2 = new JSONObject(extrasBean2);
                       *//* //Log.e("extras", extrasBean);
                        //Log.e("extraJson", extraJson.getString("messageId"));*//*
                        String n_extras = extraJson2.getString("n_extras");
                        JSONObject n_extras2 = new JSONObject(n_extras);
                        msgId =  n_extras2.getString("messageId");
                        msgType =  n_extras2.getString("messageType");
                        //Log.e("msgId", msgId);
                        SPUtils.put(MyApp.getInstance(),"msgId",msgId);
                        SPUtils.put(MyApp.getInstance(),"msgType",msgType);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }else {

                }*/
            }
            //Log.e("JPush","Title : " + title + "  " + "Content : " + content + "   msgId  : " + msgId + "   msgType  : " + msgType);


        }
    }

    /**
     * 获取本地软件版本号
     */
//    public static float getLocalVersion(Context ctx) {
//
//        try {
//            PackageInfo packageInfo = ctx.getApplicationContext()
//                    .getPackageManager()
//                    .getPackageInfo(ctx.getPackageName(), 0);
//            float localVersion = packageInfo.versionCode;
//            Log.d("TAG", "本软件的版本号 = " + localVersion);
//
////            SPUtils.put(ctx,"localVersion",localVersion);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        return localVersion;
//    }

    /**
     * 获取本地软件版本号名称
     */

    public static String getLocalVersionName(Context ctx) {
        try {
            PackageInfo packageInfo = ctx.getApplicationContext()
                    .getPackageManager()
                    .getPackageInfo(ctx.getPackageName(), 0);
            localVersionName = packageInfo.versionName;
            Log.d("TAG", "本软件的版本号 = " + localVersionName);
              SPUtils.put(ctx.getApplicationContext(),"VersionName", localVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return localVersionName;
    }


    private void setPermission() {
        //同时请求多个权限
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .subscribe(new io.reactivex.functions.Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            //Log.e("用户已经同意该权限", permission.name + " is granted.");
                            //   Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            //Log.e("用户拒绝了该权限", permission.name + " is denied. More info should be provided.");
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            //   Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，并且选中『不再询问』
                            //   Log.d(TAG, permission.name + " is denied.");
                            //Log.e("用户拒绝了该权限", permission.name + permission.name + " is denied.");
                        }
                    }
                });
    }


    private WebViewClient mWebViewClient = new WebViewClient() {
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.i("userAgent4",  view.getSettings().getUserAgentString());


        }

        //        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url =  null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }


            if (url.contains("http://mobile.havct.edu.cn/portal/login/appLogin")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""))){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }
            return super.shouldOverrideUrlLoading(view, request);
        }

        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.contains("http://mobile.havct.edu.cn/portal/login/appLogin")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""))){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }

            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            CookieUtils.syncCookie("http://mobile.havct.edu.cn","CASTGC="+tgt,getApplication());


        }

    };


    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this); // 不能遗漏
    }



}
