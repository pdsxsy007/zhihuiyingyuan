package io.cordova.zhihuiyingyuan.web;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.location.LocationManager;
import android.media.AudioManager;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.DownloadListener;
import android.webkit.JavascriptInterface;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.google.gson.Gson;
import com.just.agentweb.AbsAgentWebSettings;
import com.just.agentweb.AgentWeb;
import com.just.agentweb.DefaultWebClient;
import com.just.agentweb.IAgentWebSettings;
import com.just.agentweb.PermissionInterceptor;
import com.just.agentweb.WebListenerManager;
import com.just.agentweb.download.AgentWebDownloader;
import com.just.agentweb.download.DefaultDownloadImpl;
import com.just.agentweb.download.DownloadListenerAdapter;
import com.just.agentweb.download.DownloadingService;
import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.lzy.okgo.request.base.Request;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;
import com.zhihu.matisse.Matisse;
import com.zhihu.matisse.MimeType;
import com.zhihu.matisse.engine.impl.GlideEngine;
import com.zhihu.matisse.internal.entity.CaptureStrategy;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import gdut.bsx.share2.Share2;
import gdut.bsx.share2.ShareContentType;
import io.cordova.zhihuiyingyuan.R;
import io.cordova.zhihuiyingyuan.UrlRes;
import io.cordova.zhihuiyingyuan.activity.FaceYiQingActivity;
import io.cordova.zhihuiyingyuan.activity.LoginActivity2;
import io.cordova.zhihuiyingyuan.adapter.BrowserAdapter;
import io.cordova.zhihuiyingyuan.bean.AppOrthBean;
import io.cordova.zhihuiyingyuan.bean.BaseBean;
import io.cordova.zhihuiyingyuan.bean.DownLoadBean;
import io.cordova.zhihuiyingyuan.bean.ImeiBean;
import io.cordova.zhihuiyingyuan.bean.LocalFaceBean;
import io.cordova.zhihuiyingyuan.bean.LocalVersonBean;
import io.cordova.zhihuiyingyuan.bean.LocationBean;
import io.cordova.zhihuiyingyuan.bean.LocationBean2;
import io.cordova.zhihuiyingyuan.bean.LogInTypeBean;
import io.cordova.zhihuiyingyuan.bean.NaturePicBean;
import io.cordova.zhihuiyingyuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyingyuan.utils.BaseActivity2;
import io.cordova.zhihuiyingyuan.utils.BitmapUtils;
import io.cordova.zhihuiyingyuan.utils.CookieUtils;
import io.cordova.zhihuiyingyuan.utils.DensityUtil;
import io.cordova.zhihuiyingyuan.utils.JsonUtil;
import io.cordova.zhihuiyingyuan.utils.MobileInfoUtils;
import io.cordova.zhihuiyingyuan.utils.MyApp;
import io.cordova.zhihuiyingyuan.utils.PermissionsUtil;
import io.cordova.zhihuiyingyuan.utils.SPUtils;
import io.cordova.zhihuiyingyuan.utils.ScreenSizeUtils;
import io.cordova.zhihuiyingyuan.utils.SoundPoolUtils;
import io.cordova.zhihuiyingyuan.utils.StringUtils;
import io.cordova.zhihuiyingyuan.utils.T;
import io.cordova.zhihuiyingyuan.utils.ToastUtils;
import io.cordova.zhihuiyingyuan.utils.ViewUtils;
import io.cordova.zhihuiyingyuan.widget.MyDialog;
import io.cordova.zhihuiyingyuan.widget.TestWebView;
import me.samlss.lighter.Lighter;
import me.samlss.lighter.interfaces.OnLighterListener;
import me.samlss.lighter.parameter.Direction;
import me.samlss.lighter.parameter.LighterParameter;
import me.samlss.lighter.parameter.MarginOffset;
import okhttp3.Headers;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

import static io.cordova.zhihuiyingyuan.UrlRes.HOME_URL;
import static io.cordova.zhihuiyingyuan.UrlRes.addPortalReadingAccessUrl;
import static io.cordova.zhihuiyingyuan.UrlRes.functionInvocationLogUrl;
import static io.cordova.zhihuiyingyuan.activity.SplashActivity.getLocalVersionName;
import static io.cordova.zhihuiyingyuan.utils.AesEncryptUtile.key;
import static io.cordova.zhihuiyingyuan.utils.MobileInfoUtils.getIMEI;
import static io.cordova.zhihuiyingyuan.utils.MyApp.getInstance;
import static io.cordova.zhihuiyingyuan.utils.PermissionsUtil.SETTINGS_REQ_CODE;

@SuppressLint("Registered")
public class BaseWebCloseActivity extends BaseActivity2 implements PermissionsUtil.IPermissionsCallback {
    protected AgentWeb mAgentWeb;

    @BindView(R.id.webView)
    TestWebView webView;

    @BindView(R.id.layout_close)
    RelativeLayout rvClose;

    @BindView(R.id.tv_title)
    TextView mTitleTextView;
    @BindView(R.id.rl_no)
    RelativeLayout rl_no;

    @BindView(R.id.rb_sc)
    ImageView rbSc;

    @BindView(R.id.ll_shoushi)
    RelativeLayout ll_shoushi;

    private LinearLayout mLinearLayout;
    String appServiceUrl, tgc,appId,search,oaMsg;
    private String time;
    boolean isFirst = true;
    String appName;
    String scan;
    private int flag = 0;

    /** Android 5.0以下版本的文件选择回调 */
    protected ValueCallback<Uri> mFileUploadCallbackFirst;
    /** Android 5.0及以上版本的文件选择回调 */
    protected ValueCallback<Uri[]> mFileUploadCallbackSecond;

    protected static final int REQUEST_CODE_FILE_PICKER = 51426;


    protected String mUploadableFileTypes = "*/*";

    private PermissionsUtil permissionsUtil;
    String downLoadType;

    @Override
    protected int getResourceId() {
        return R.layout.activity_web;
    }

    @Override
    protected void initView() {
        super.initView();
        downLoadType = (String) SPUtils.get(BaseWebCloseActivity.this, "downLoadType", "");
        ButterKnife.bind(this);
        mLocationClient = new LocationClient(this);
        mLocationClient.registerLocationListener(myListener);

        mLinearLayout = (LinearLayout) this.findViewById(R.id.container);
        rvClose.setVisibility(View.GONE);

        tgc = (String) SPUtils.get(getApplicationContext(), "TGC", "");
        appServiceUrl = getIntent().getStringExtra("appUrl");

        appId = getIntent().getStringExtra("appId");
        search = getIntent().getStringExtra("search");//微应用地址'
        oaMsg = getIntent().getStringExtra("oaMsg");//oa 消息

        appName = getIntent().getStringExtra("appName");
        scan = getIntent().getStringExtra("scan");

        mTitleTextView.setText(appName);
        if (!StringUtils.isEmpty(oaMsg)){
            if (!appServiceUrl.contains("fromnewcas=Y")){
                appServiceUrl = appServiceUrl + "&fromnewcas=Y";
            }
        }

        if(null != scan){
            if(scan.equals("scan")){
                rbSc.setVisibility(View.GONE);
            }
        }
        if(null != appId){
            rbSc.setVisibility(View.VISIBLE);
        }else {
            rbSc.setVisibility(View.GONE);
        }
        String tgc = (String) SPUtils.get(BaseWebCloseActivity.this, "TGC", "");
        CookieUtils.syncCookie(UrlRes.HOME2_URL,"CASTGC="+tgc,getApplication());

        if(appServiceUrl.contains("gilight://")){
            //gilight://url=weixin://123
            if(!appServiceUrl.contains("http")){
                final String endUrl = appServiceUrl.substring(10,appServiceUrl.length());
                String[] split = endUrl.split("//");
                String s = split[0];
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("是否打开"+s);
                builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.setPositiveButton("打开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String appServiceUrl2 = UrlRes.huanxingUrl+"?"+endUrl;
                        Log.e("appServiceUrl2",appServiceUrl2);
                        Uri uri = Uri.parse(appServiceUrl2);
                        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                        if (intent.resolveActivity(getPackageManager()) != null) {
                            // 网址正确 跳转成功
                            startActivity(intent);
                            finish();
                        } else {
                            //网址不正确 跳转失败 提示错误
                            //Toast.makeText(MainActivity.this, "网址输入错误，请重新输入！", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
                builder.create().show();
            }else {

                String endUrl = appServiceUrl.substring(10,appServiceUrl.length());
                String appServiceUrl2 = UrlRes.huanxingUrl+"?"+endUrl;
                Log.e("appServiceUrl2",appServiceUrl2);
                Uri uri = Uri.parse(appServiceUrl2);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // 网址正确 跳转成功
                    startActivity(intent);
                    finish();
                } else {
                    //网址不正确 跳转失败 提示错误
                    //Toast.makeText(MainActivity.this, "网址输入错误，请重新输入！", Toast.LENGTH_SHORT).show();

                }

            }



        }

        mAgentWeb = AgentWeb.with(this)
                .setAgentWebParent(mLinearLayout, new LinearLayout .LayoutParams(-1, -1))
                .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                //.setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .setWebChromeClient(new OpenFileChromeClient())
                .setWebViewClient(mWebViewClient)
                .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                .setWebLayout(new WebLayout4(this))
                .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                .setAgentWebWebSettings(getSettings())//设置 IAgentWebSettings。
                .createAgentWeb()
                .ready()
                .go(appServiceUrl);

        mAgentWeb.getWebCreator().getWebView().setOverScrollMode(WebView.OVER_SCROLL_NEVER);
        mAgentWeb.getJsInterfaceHolder().addJavaObject("android",new AndroidInterface());


        netWorkIsCollection();
        initListeners();

        String home05 = (String) SPUtils.get(MyApp.getInstance(), "home05", "");
        if(home05.equals("")){
            setGuideView();
        }

        sBegin = Calendar.getInstance().getTimeInMillis() + "";

        s1 = stringToDate(sBegin);

      /*  permissionsUtil = PermissionsUtil
                .with(this)
                .requestCode(0)
                .isDebug(true)
                .permissions(PermissionsUtil.Permission.Location.ACCESS_FINE_LOCATION)
                .request();*/

        registerBoradcastReceiver();
        registerBoradcastReceiver2();
    }



    private void registerBoradcastReceiver2() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("facereYiQing2");
        //注册广播
        registerReceiver(broadcastReceiver2, myIntentFilter);
    }


    private BroadcastReceiver broadcastReceiver2 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals("facereYiQing2")){
                LocalFaceBean localVersonBean = new LocalFaceBean();
                localVersonBean.setSuccess(false);
                localVersonBean.setMessage("用户取消了人脸识别");
                Gson gson = new Gson();
                String s = gson.toJson(localVersonBean);
                String jsonParams = s;
                String url2 = "javascript:getFaceAndroidParams('"+jsonParams+"')";
                mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.e("value",value);
                    }
                });
                SPUtils.put(getApplicationContext(),"isloading2","");

            }
        }
    };
    private BroadcastReceiver broadcastReceiver3 = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals("facereYiQing3")){
                LocalFaceBean localVersonBean = new LocalFaceBean();
                localVersonBean.setSuccess(false);
                localVersonBean.setMessage("当前网络异常，识别失败");
                Gson gson = new Gson();
                String s = gson.toJson(localVersonBean);
                String jsonParams = s;
                String url2 = "javascript:getFaceAndroidParams('"+jsonParams+"')";
                mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                    @Override
                    public void onReceiveValue(String value) {
                        //此处为 js 返回的结果
                        Log.e("value",value);
                    }
                });
                SPUtils.put(getApplicationContext(),"isloading2","");
            }
        }
    };

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("facereYiQing");
        registerReceiver(broadcastReceiver, myIntentFilter);

    }


    public LocationClient mLocationClient;
    private MyLocationListener myListener = new MyLocationListener();



    public class MyLocationListener extends BDAbstractLocationListener {
        @Override
        public void onReceiveLocation(BDLocation location){
            double latitude = location.getLatitude();    //获取纬度信息
            double longitude = location.getLongitude();    //获取经度信息
            float radius = location.getRadius();    //获取定位精度，默认值为0.0f

            String coorType = location.getCoorType();
            //获取经纬度坐标类型，以LocationClientOption中设置过的坐标类型为准

            int errorCode = location.getLocType();
            boolean b = location.hasAltitude();
            double altitude = location.getAltitude();
            float speed = location.getSpeed();
            float direction = location.getDirection();
            int locationWhere = location.getLocationWhere();

            Log.e("errorCode",errorCode+"");
            Log.e("latitude",latitude+"");
            Log.e("longitude",longitude+"");
            Log.e("altitude——base",altitude+"");
            Log.e("speed",speed+"");
            Log.e("direction",direction+"");
            Log.e("locationWhere",locationWhere+"");

            com.baidu.location.Address address = location.getAddress();
            String addressLine = address.address;
            String country = address.country;
            String countryCode = address.countryCode;
            String province = address.province;
            String city = address.city;
            String cityCode = address.cityCode;
            String district = address.district;
            String town = address.town;
            String adcode = address.adcode;
            String street = address.street;
            String streetNumber = address.streetNumber;
            if(addressLine != null){
                SPUtils.put(BaseWebCloseActivity.this,"addressLine",addressLine);
            }
            if(country != null){
                SPUtils.put(BaseWebCloseActivity.this,"country",country);
            }

            if(countryCode != null){
                SPUtils.put(BaseWebCloseActivity.this,"countryCode",countryCode);
            }

            if(province != null){
                SPUtils.put(BaseWebCloseActivity.this,"province",province);
            }

            if(city != null){
                SPUtils.put(BaseWebCloseActivity.this,"city",city);
            }

            if(cityCode != null){
                SPUtils.put(BaseWebCloseActivity.this,"cityCode",cityCode);
            }

            if(district != null){
                SPUtils.put(BaseWebCloseActivity.this,"district",district);
            }

            if(adcode != null){
                SPUtils.put(BaseWebCloseActivity.this,"adcode",adcode);
            }

            if(street != null){
                SPUtils.put(BaseWebCloseActivity.this,"street",street);
            }

            if(streetNumber != null){
                SPUtils.put(BaseWebCloseActivity.this,"streetNumber",streetNumber);
            }
            if(town != null){
                SPUtils.put(BaseWebCloseActivity.this,"town",town);
            }

            SPUtils.put(BaseWebCloseActivity.this,"latitude",latitude+"");
            SPUtils.put(BaseWebCloseActivity.this,"longitude",longitude+"");
            SPUtils.put(BaseWebCloseActivity.this,"altitude",altitude+"");
            SPUtils.put(BaseWebCloseActivity.this,"speed",speed+"");
            SPUtils.put(BaseWebCloseActivity.this,"direction",direction+"");
            SPUtils.put(BaseWebCloseActivity.this,"locationWhere",locationWhere+"");
        }
    }

    private void getMyLocation() {
        LocationClientOption option = new LocationClientOption();

        //设置坐标类型
        option.setCoorType("bd09ll");
        option.setIgnoreKillProcess(true);
        option.SetIgnoreCacheException(false);
        //设置是否需要地址信息，默认为无地址
        option.setIsNeedAddress(true);
        //设置是否打开gps进行定位
        //option.setOpenGps(true);
        //option.setWifiCacheTimeOut(20);
        option.setIsNeedAltitude(true);
        option.setIsNeedLocationDescribe(true);
        option.setNeedDeviceDirect(true);
        //设置扫描间隔为1秒
        option.setScanSpan(300000);
        //option.setScanSpan(1000);

        //传入设置好的信息
        mLocationClient.setLocOption(option);

        mLocationClient.start();


    }

    private void setGuideView() {

        // 使用图片
        Lighter.with(this)
                .setBackgroundColor(0xB9000000)
                .setOnLighterListener(new OnLighterListener() {
                    @Override
                    public void onShow(int index) {


                    }

                    @Override
                    public void onDismiss() {
                        SPUtils.put(MyApp.getInstance(),"home05","1");
                    }
                })
                .addHighlight(new LighterParameter.Builder()
                        .setHighlightedViewId(R.id.ll_shoushi)
                        .setTipLayoutId(R.layout.fragment_home_gl2)
                        //.setLighterShape(new RectShape(80, 80, 50))
                        .setTipViewRelativeDirection(Direction.BOTTOM)
                        .setTipViewRelativeOffset(new MarginOffset(0, 10, 0, 20))
                        .build()).show();
    }

    BaseBean appTime;
    private void networkAppStatTime() {
        OkGo.<String>post(HOME_URL+ UrlRes.APP_Time)
                .params( "responseTime",time )
                .params( "responseAppId",appId)
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        //handleResponse(response);
                        Log.e("Tag",response.body());
                        appTime = JSON.parseObject(response.body(),BaseBean.class);
                        if (appTime.isSuccess()) {
                            isFirst = false;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        isFirst = true;
                    }
                });


    }

    BaseBean baseBean;
    private void netWorkIsCollection() {
        OkGo.<String>post(HOME_URL+ UrlRes.Query_IsCollection)
                .params( "version","1.0" )
                .params( "collectionAppId",appId )
                .params( "userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        //handleResponse(response);
                        Log.e("tag",response.body());
                        baseBean = JSON.parseObject(response.body(), BaseBean.class);

                        if (baseBean.isSuccess()){
                            rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                            flag = 1;
                        }else {
                            rbSc.setBackgroundResource(R.mipmap.sc_icon);
                            flag = 0;
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });

    }

    private MyDialog m_Dialog_browser;
    private LinearLayoutManager mLinearLayoutManager;
    private void initListeners() {
        rbSc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flag == 0){
                    //请求收藏
                    networkCollection();
                }else {
                    //取消收藏
                    cancelCollection();
                }
            }
        });

        rbSc.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                m_Dialog_browser = new MyDialog(BaseWebCloseActivity.this, R.style.DialogTheme);

                View view1 = LayoutInflater.from(BaseWebCloseActivity.this).inflate(R.layout.dialog_browser, null);
                m_Dialog_browser.setContentView(view1);
                Window window = m_Dialog_browser.getWindow();
                window.setGravity(Gravity.BOTTOM);
                window.setWindowAnimations(R.style.main_menu_animStyle);
                window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
                m_Dialog_browser.show();
                m_Dialog_browser.setCanceledOnTouchOutside(true);
                LinearLayout ll_01 = view1.findViewById(R.id.ll_01);
                LinearLayout ll_02 = view1.findViewById(R.id.ll_02);
                LinearLayout ll_03 = view1.findViewById(R.id.ll_03);
                RecyclerView rv_content = view1.findViewById(R.id.rv_content);
                mLinearLayoutManager = new LinearLayoutManager(BaseWebCloseActivity.this, LinearLayout.HORIZONTAL,false);
                rv_content.setLayoutManager(mLinearLayoutManager);
                List<LogInTypeBean> list = new ArrayList<>();
                list.clear();
                list.add(new LogInTypeBean("在浏览器打开",R.mipmap.b_browser));
                list.add(new LogInTypeBean("复制地址",R.mipmap.b_copy));
                list.add(new LogInTypeBean("刷新",R.mipmap.b_refresh));
                BrowserAdapter browserAdapter = new BrowserAdapter(BaseWebCloseActivity.this,R.layout.item_browser,list);
                rv_content.setAdapter(browserAdapter);
                browserAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                        switch (position){
                            case 0:
                                m_Dialog_browser.dismiss();
                                Uri uri = Uri.parse(appServiceUrl);
                                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                                if (intent.resolveActivity(getPackageManager()) != null) {
                                    // 网址正确 跳转成功
                                    startActivity(intent);

                                } else {


                                }
                                break;

                            case 1:
                                m_Dialog_browser.dismiss();
                                copyString(appServiceUrl);
                                break;

                            case 2:
                                m_Dialog_browser.dismiss();
                                mAgentWeb.getWebCreator().getWebView().reload();
                                break;

                        }
                    }

                    @Override
                    public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                        return false;
                    }
                });
                TextView tv_cancel = view1.findViewById(R.id.tv_cancel);

                tv_cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        m_Dialog_browser.dismiss();

                    }
                });


                return false;
            }
        });

    }


    private void copyString(String appServiceUrl) {
        ClipboardManager clipManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);//获取剪切板管理对象
        ClipData clipData = ClipData.newPlainText("copy text", appServiceUrl);//将数据放到clip对象
        clipManager.setPrimaryClip(clipData);//将clip对象放到剪切板

        //判断剪贴板里是否有内容
        if (!clipManager.hasPrimaryClip()) {
            return;
        }
        ClipData clip = clipManager.getPrimaryClip();
        //获取 ClipDescription
        ClipDescription description = clip.getDescription();
        //获取 label
        String label = description.getLabel().toString();
        //获取 text
        String copyText = clip.getItemAt(0).getText().toString();
        ToastUtils.showToast(BaseWebCloseActivity.this,"复制成功!");
    }

    /**请求收藏*/
    private void networkCollection() {
        OkGo.<String>post(HOME_URL+ UrlRes.Add_Collection)
                .params( "version","1.0" )
                .params( "collectionAppId",appId )
                .params( "userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("tag",response.body());
                        baseBean = JSON.parseObject(response.body(), BaseBean.class);
                        if (baseBean.isSuccess()){
                            rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                            flag = 1;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                            Intent intent = new Intent();
                            intent.putExtra("refreshService","dongtai");
                            intent.setAction("refresh2");
                            sendBroadcast(intent);
                        }else {
                            rbSc.setBackgroundResource(R.mipmap.sc_icon);
                            flag = 0;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        rbSc.setBackgroundResource(R.mipmap.sc_icon);
                    }
                });
    }

    /**取消收藏*/
    private void cancelCollection() {
        OkGo.<String>post(HOME_URL+ UrlRes.Cancel_Collection)
                .params( "version","1.0" )
                .params( "collectionAppId",appId )
                .params( "userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback(){
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("tag",response.body());
                        baseBean = JSON.parseObject(response.body(), BaseBean.class);
                        if (baseBean.isSuccess()){
                            rbSc.setBackgroundResource(R.mipmap.sc_icon);
                            flag = 0;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                            Intent intent = new Intent();
                            intent.putExtra("refreshService","dongtai");
                            intent.setAction("refresh2");
                            sendBroadcast(intent);
                        }else {
                            rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                            flag = 1;
                            T.showShort(MyApp.getInstance(),baseBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        rbSc.setBackgroundResource(R.mipmap.sc_hover_icon);
                    }
                });

    }
    /**
     * IEventHandler 处理WebView相关返回事件
     */
    @OnClick({R.id.iv_back, R.id.iv_close})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (!mAgentWeb.back()){
                    BaseWebCloseActivity.this.finish();
                }
                break;
            case R.id.iv_close:
                BaseWebCloseActivity.this.finish();
                break;
        }
    }
    long start;
    long end;
    String urldown = "";
    String sBegin;//开始时间戳
    String s1;//开始时间
    @SuppressLint("WrongConstant")
    private WebViewClient mWebViewClient = new WebViewClient() {
        /*@Override
        public void onPageCommitVisible(WebView view, String url) {

            super.onPageCommitVisible(view, url);
        }*/

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                handler.proceed(); // 接受所有网站的证书
            }


        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            Log.e("加载完成url",url);
            boolean b = mAgentWeb.getWebCreator().getWebView().canGoBack();
            if(b){
                rvClose.setVisibility(View.VISIBLE);

            }else {
                rvClose.setVisibility(View.GONE);
            }

           /* WebSettings webSettings = view.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
            webSettings.setJavaScriptEnabled(true);//支持js
            webSettings.setBuiltInZoomControls(true); // 显示放大缩小
            webSettings.setSupportZoom(true); // 可以缩放
            webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能*/

            end =   Calendar.getInstance().getTimeInMillis();
            time =(end - start) +"";
            if (!StringUtils.isEmpty(time) && !StringUtils.isEmpty(appId) && isFirst){
                networkAppStatTime();
            }

            sBegin = Calendar.getInstance().getTimeInMillis() + "";

            s1 = stringToDate(sBegin);


        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url =  null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }



            if (url.contains("http://mobile.havct.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username","")) || tgc.equals("")){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }


            urldown = "";
            urldown =url;



            return super.shouldOverrideUrlLoading(view, request);
        }

        /**网址拦截*/
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {


            Log.e("测试网页1",url);

            String downLoadType = (String) SPUtils.get(BaseWebCloseActivity.this, "downLoadType", "");
            DownLoadBean downLoadBean = JsonUtil.parseJson(downLoadType,DownLoadBean.class);
            List<String> downLoadTypeList = downLoadBean.getString();
            for (int i = 0; i < downLoadTypeList.size(); i++) {
                if(url.contains(downLoadTypeList.get(i))){
                    Log.e("tag",downLoadTypeList.get(i));
                    logOut(url);
                    //getSettings();
                    return true;
                }else {

                }
            }


            if(url.contains("gilight://")){

                String endUrl = url.substring(10,url.length());
                String fullUrl = UrlRes.huanxingUrl+"?"+endUrl;

                //view.loadUrl(endUrl);
                Uri uri = Uri.parse(fullUrl);
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                if (intent.resolveActivity(getPackageManager()) != null) {
                    // 网址正确 跳转成功
                    startActivity(intent);
                    //finish();
                } else {
                    //网址不正确 跳转失败 提示错误
                    //Toast.makeText(MainActivity.this, "网址输入错误，请重新输入！", Toast.LENGTH_SHORT).show();

                }

                return true;

            }else {
                if (url.contains("http://mobile.havct.edu.cn/cas/login")) {
                    if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username","")) || tgc.equals("")){
                        Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                        startActivity(intent);
                        finish();

                        return true;
                    }
                }
                upLoadWebInfo();

            }
           /* WebSettings webSettings = view.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
            webSettings.setJavaScriptEnabled(true);//支持js
            webSettings.setBuiltInZoomControls(true); // 显示放大缩小
            webSettings.setSupportZoom(true); // 可以缩放
            webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能
            webSettings.setDomStorageEnabled(true);*/
            //webSettings.setCacheMode(LOAD_NO_CACHE);
            Log.e("shouldOverrideUrl",url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            CookieUtils.syncCookie("http://mobile.havct.edu.cn","CASTGC="+tgc,getApplication());
            if (!StringUtils.isEmpty(appId)){
                start =  Calendar.getInstance().getTimeInMillis() ;
                Log.i("Info", "start:  " + start );
            }

        }


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Nullable
        @Override
        public WebResourceResponse shouldInterceptRequest(WebView view, final String url) {
            Log.e("测试下载地址",url);

            return null;

        }
    };

    @SuppressLint("WrongConstant")
    private WebViewClient mWebViewClient2 = new WebViewClient() {
        @Override
        public void onPageCommitVisible(WebView view, String url) {

            super.onPageCommitVisible(view, url);
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                view.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
                handler.proceed(); // 接受所有网站的证书
            }


        }


        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            boolean b = mAgentWeb.getWebCreator().getWebView().canGoBack();
            if(b){
                rvClose.setVisibility(View.VISIBLE);

            }else {
                rvClose.setVisibility(View.GONE);
            }



           /* WebSettings webSettings = view.getSettings();
            webSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.SINGLE_COLUMN);//把html中的内容放大webview等宽的一列中
            webSettings.setJavaScriptEnabled(true);//支持js
            webSettings.setBuiltInZoomControls(true); // 显示放大缩小
            webSettings.setSupportZoom(true); // 可以缩放
            webSettings.setUseWideViewPort(true);  //为图片添加放大缩小功能*/

            end =   Calendar.getInstance().getTimeInMillis();
            time =(end - start) +"";
            if (!StringUtils.isEmpty(time) && !StringUtils.isEmpty(appId) && isFirst){
                networkAppStatTime();
            }

            sBegin = Calendar.getInstance().getTimeInMillis() + "";

            s1 = stringToDate(sBegin);

        }


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url =  null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                url = request.getUrl().toString();
            }



            if (url.contains("http://mobile.havct.edu.cn/cas/login")) {
                if (StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username","")) || tgc.equals("")){
                    Intent intent = new Intent(getApplicationContext(),LoginActivity2.class);
                    startActivity(intent);
                    finish();

                    return true;
                }
            }


            urldown = "";
            urldown =url;



            return super.shouldOverrideUrlLoading(view, request);
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {



            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {

            CookieUtils.syncCookie("http://mobile.havct.edu.cn","CASTGC="+tgc,getApplication());
            if (!StringUtils.isEmpty(appId)){
                start =  Calendar.getInstance().getTimeInMillis() ;
                Log.i("Info", "start:  " + start );
            }

        }


    };



    public static String stringToDate(String lo){
        long time = Long.parseLong(lo);
        Date date = new Date(time);
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sd.format(date);
    }

    /**
     * 结束时间
     */
    private void upLoadWebInfo() {
        String s = Calendar.getInstance().getTimeInMillis() + "";


        long sCha = Long.parseLong(s) - Long.parseLong(sBegin);
        String userId = (String) SPUtils.get(MyApp.getInstance(), "userId", "");
        try {
            String decrypt = AesEncryptUtile.decrypt(userId, key);
            Map<String,String> map=new HashMap<String,String>();
            map.put("portalReadingAccessEquipmentId",(String) SPUtils.get(MyApp.getInstance(),"imei",""));
            map.put("portalReadingAccessMemberId",decrypt);
            map.put("portalReadingAccessLongitude",(String) SPUtils.get(MyApp.getInstance(),"longitude",""));
            map.put("portalReadingAccessLatitude",(String) SPUtils.get(MyApp.getInstance(),"latitude",""));
            map.put("portalReadingAccessAddress",(String) SPUtils.get(MyApp.getInstance(),"addressLine",""));
            map.put("portalReadingAccessTime",s1);
            map.put("portalReadingAccessUrl",urldown);
            map.put("portalReadingAccessReadTime",sCha/1000+"");
            JSONObject json =new JSONObject(map);
            String s3 = json.toString();
            Log.e("content",json.toString());
            String content = AesEncryptUtile.encrypt(s3, key);

            OkGo.<String>post(HOME_URL+addPortalReadingAccessUrl)
                    .tag(this)
                    .params("json", content)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {
                            Log.e("s",response.toString());

                        }

                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



    private MyDialog m_Dialog;
    private int tag = 0;
    private void logOut(final String urldown) {
        m_Dialog = new MyDialog(this, R.style.dialogdialog);
        Window window = m_Dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_down, null);
        RelativeLayout rl_sure = view.findViewById(R.id.rl_sure);
        RelativeLayout rl_sure1 = view.findViewById(R.id.rl_sure1);

        int width = ScreenSizeUtils.getWidth(this);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width - DensityUtil.dip2px(this,24),
                LinearLayout.LayoutParams.WRAP_CONTENT);
        m_Dialog.setContentView(view, layoutParams);
        m_Dialog.show();
        m_Dialog.setCanceledOnTouchOutside(true);
        rl_sure1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Dialog.dismiss();

            }
        });
        rl_sure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                m_Dialog.dismiss();

                tag = 1;
                mAgentWeb = AgentWeb.with(BaseWebCloseActivity.this)
                        .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                        .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                        .setWebChromeClient(mWebChromeClient)
                        .setWebViewClient(mWebViewClient2)
                        .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                        //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                        .setWebLayout(new WebLayout(BaseWebCloseActivity.this))
                        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                        .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                        .setAgentWebWebSettings( getSettings2())//设置 IAgentWebSettings。
                        .createAgentWeb()
                        .ready()
                        .go(urldown);

                mAgentWeb = AgentWeb.with(BaseWebCloseActivity.this)
                        .setAgentWebParent(mLinearLayout, new LinearLayout.LayoutParams(-1, -1))
                        .useDefaultIndicator(-1, 3)//设置进度条颜色与高度，-1为默认值，高度为2，单位为dp。
                        .setWebChromeClient(mWebChromeClient)
                        .setWebViewClient(mWebViewClient)
                        .setPermissionInterceptor(mPermissionInterceptor) //权限拦截 2.0.0 加入。
                        //.setMainFrameErrorView(R.layout.agentweb_error_page, -1)
                        .setSecurityType(AgentWeb.SecurityType.STRICT_CHECK)
                        .setWebLayout(new WebLayout(BaseWebCloseActivity.this))
                        .setOpenOtherPageWays(DefaultWebClient.OpenOtherPageWays.ASK)//打开其他应用时，弹窗咨询用户是否前往其他应用
                        .interceptUnkownUrl() //拦截找不到相关页面的Scheme
                        .setAgentWebWebSettings( getSettings())//设置 IAgentWebSettings。
                        .createAgentWeb()
                        .ready()
                        .go(appServiceUrl);
            }
        });
    }


    private WebChromeClient mWebChromeClient = new WebChromeClient() {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            //   do you work

            if (newProgress == 100 && start > 0){
                end =   Calendar.getInstance().getTimeInMillis();
                time =(end - start) +"";
            }

            if (!StringUtils.isEmpty(time) && !StringUtils.isEmpty(appId) && isFirst){
                networkAppStatTime();
            }

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitleTextView != null) {
                mTitleTextView.setText(title);
            }
            if(appName != null){
                mTitleTextView.setText(appName);
            }
        }


    };
    protected PermissionInterceptor mPermissionInterceptor = new PermissionInterceptor() {

        /**
         * PermissionInterceptor 能达到 url1 允许授权， url2 拒绝授权的效果。
         * @param url
         * @param permissions
         * @param action
         * @return true 该Url对应页面请求权限进行拦截 ，false 表示不拦截。
         */
        @Override
        public boolean intercept(String url, String[] permissions, String action) {

            return false;
        }
    };


    DownloadListener downloadListener2 = new DownloadListener() {
        @Override
        public void onDownloadStart(final String url, String s1, String s2, String s3, long l) {
            Log.e("onDownloadStart",url);

            if(url.contains(".png")|| url.contains(".jpg") || url.contains(".jpeg")){

                OkGo.<String>get(url)
                        .execute(new StringCallback() {
                            @Override
                            public void onSuccess(Response<String> response) {


                                Headers headers = response.getRawResponse().headers();
                                String Disposition = headers.get("Content-Disposition");
                                if(Disposition != null){

                                    if(Disposition.contains("attachment")){
                                        logOut(url);
                                        return;
                                    }
                                }
                            }
                            @Override
                            public void onError(Response<String> response) {
                                super.onError(response);

                            }
                        });


            }else {
                logOut(url);
            }





        }


    };

    protected DownloadListenerAdapter mDownloadListenerAdapter = new DownloadListenerAdapter() {

        @Override
        public void onProgress(String url, long downloaded, long length, long usedTime) {
            super.onProgress(url, downloaded, length, usedTime);
            Log.e("onProgress下载",url);
        }

        @Override
        public void onBindService(String url, DownloadingService downloadingService) {
            super.onBindService(url, downloadingService);
            Log.e("onBindService下载",url);
            // downloadingService.shutdownNow();
        }

        @Override
        public void onUnbindService(String url, DownloadingService downloadingService) {
            super.onUnbindService(url, downloadingService);

        }

        @Override
        public boolean onStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength, AgentWebDownloader.Extra extra) {
            Log.e("onStart下载",url);

            ViewUtils.createLoadingDialog(BaseWebCloseActivity.this);
            extra.setOpenBreakPointDownload(true) // 是否开启断点续传
                    .setIcon(R.drawable.ic_file_download_black_24dp) //下载通知的icon
                    .setConnectTimeOut(6000) // 连接最大时长
                    .setBlockMaxTime(10 * 60 * 1000)  // 以8KB位单位，默认60s ，如果60s内无法从网络流中读满8KB数据，则抛出异常
                    .setDownloadTimeOut(Long.MAX_VALUE) // 下载最大时长
                    .setParallelDownload(false)  // 串行下载更节省资源哦
                    .setEnableIndicator(false)  // false 关闭进度通知
                    //.addHeader("Cookie", "xx") // 自定义请求头
                    .setAutoOpen(true) // 下载完成自动打开
                    .setForceDownload(true); // 强制下载，不管网络网络类型
            return false;
        }




        @Override
        public boolean onResult(final String path, String url, final Throwable throwable) {

            ViewUtils.cancelLoadingDialog();
            if (null == throwable) { //下载成功

                Uri shareFileUrl = FileUtil.getFileUri(getApplicationContext(), null, new File(path));
                new Share2.Builder(BaseWebCloseActivity.this)
                        .setContentType(ShareContentType.FILE)
                        .setShareFileUri(shareFileUrl)
                        .setTitle("Share File")
                        .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
                        .build()
                        .shareBySystem();

            } else {//下载成功
                Uri shareFileUrl = FileUtil.getFileUri(getApplicationContext(), null, new File(path));
                Log.e("path2", String.valueOf(shareFileUrl));
                new Share2.Builder(BaseWebCloseActivity.this)
                        .setContentType(ShareContentType.FILE)
                        .setShareFileUri(shareFileUrl)
                        .setTitle("Share File")
                        .setOnActivityResult(REQUEST_SHARE_FILE_CODE)
                        .build()
                        .shareBySystem();

            }
            return false; // true  不会发出下载完成的通知 , 或者打开文件
        }
    };


    public IAgentWebSettings getSettings() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;


            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            @Override
            public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {
                return super.setDownloader(webView, downloadListener2);
            }


        };
    }

    public IAgentWebSettings getSettings2() {
        return new AbsAgentWebSettings() {
            private AgentWeb mAgentWeb;


            @Override
            protected void bindAgentWebSupport(AgentWeb agentWeb) {
                this.mAgentWeb = agentWeb;
            }

            @Override
            public WebListenerManager setDownloader(WebView webView, DownloadListener downloadListener) {

                return super.setDownloader(webView,
                        DefaultDownloadImpl
                                .create((Activity) webView.getContext(),
                                        webView,
                                        mDownloadListenerAdapter,
                                        mDownloadListenerAdapter,
                                        this.mAgentWeb.getPermissionInterceptor()));
            }


        };
    }
    private static final int REQUEST_SHARE_FILE_CODE = 120;

    @SuppressLint("WrongConstant")
    private void toCleanWebCache() {

        if (this.mAgentWeb != null) {
            //清理所有跟WebView相关的缓存 ，数据库， 历史记录 等。
            this.mAgentWeb.clearWebCache();
            Toast.makeText(getApplicationContext(), "已清理缓存", Toast.LENGTH_SHORT).show();
            //清空所有 AgentWeb 硬盘缓存，包括 WebView 的缓存 , AgentWeb 下载的图片 ，视频 ，apk 等文件。
//            AgentWebConfig.clearDiskCache(this.getContext());
        }

    }

    /**震动响铃*/
    private Handler handler = new MyHandler(this);




    @SuppressLint("HandlerLeak")
    public class MyHandler extends Handler {
        private WeakReference<Context> reference;
        public MyHandler(Context context) {
            reference = new WeakReference<>(context);
        }
        @Override
        public void handleMessage(Message msg) {
            //关闭铃声
            SoundPoolUtils.stopRing();
            //关闭震动
            SoundPoolUtils.virateCancle(BaseWebCloseActivity.this);

        }
    }

    private static final int QR_CODE = 55846;
    private Handler deliver = new Handler(Looper.getMainLooper());
    public class AndroidInterface {


        /**震动响铃*/
        @JavascriptInterface
        public void playSoundAndVibration() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    setAlarmParams();
                    handler.sendEmptyMessageDelayed(0, 1100);
                }
            });

        }

        /**清理缓存*/
        @JavascriptInterface
        public void cleanUpAppCache() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    toCleanWebCache();
                }
            });

        }
        /**返回上一页*/
        @JavascriptInterface
        public void backToLastUrl() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    if (!mAgentWeb.back()){
                        finish();
                    }else {
                        mAgentWeb.back();
                    }

                }
            });

        }


        /**进入扫描二维码页面*/
        @JavascriptInterface
        public void nativeScanQRCode(final String invocationLogAppId,final String invocationLogFunction) {
            ceshiData(invocationLogAppId,invocationLogFunction,"nativeScanQRCode");

        }

        @JavascriptInterface
        public void nativeOpenSystemSetting(final String invocationLogAppId,final String invocationLogFunction) {
            Log.e("nativeOpenSystemSetting",invocationLogAppId+"");
            ceshiData(invocationLogAppId,invocationLogFunction,"nativeOpenSystemSetting");

        }


        /**手机定位坐标*/
        @JavascriptInterface
        public void nativeGetLocation(final String invocationLogAppId,final String invocationLogFunction,final String needExtraInfo) {

                if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                    ceshiData2(invocationLogAppId,invocationLogFunction, "nativeGetLocation",needExtraInfo);

                } else {//没有相应权限，获取相机权限

                    //TestShowDig.AskForPermission(BaseWebActivity4.this,"定位");
                    permissionsUtil = PermissionsUtil
                            .with(BaseWebCloseActivity.this)
                            .requestCode(0)
                            .isDebug(true)
                            .permissions(PermissionsUtil.Permission.Location.ACCESS_FINE_LOCATION)
                            .request();
                }





        }


        @JavascriptInterface
        public void nativeGetLocation(final String invocationLogAppId,final String invocationLogFunction) {
            if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                ceshiData(invocationLogAppId,invocationLogFunction, "nativeGetLocation");
            } else {
                permissionsUtil = PermissionsUtil
                        .with(BaseWebCloseActivity.this)
                        .requestCode(0)
                        .isDebug(true)
                        .permissions(PermissionsUtil.Permission.Location.ACCESS_FINE_LOCATION)
                        .request();
            }

        }



        @JavascriptInterface
        public void nativeGetPicture(final String invocationLogAppId,final String invocationLogFunction,final String ratio) {
            if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ceshiDataNativeGetPicture(invocationLogAppId,invocationLogFunction, "nativeGetPicture",ratio);
            } else {
                permissionsUtil = PermissionsUtil
                        .with(BaseWebCloseActivity.this)
                        .requestCode(0)
                        .isDebug(true)
                        .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                        .request();
            }

        }

        @JavascriptInterface
        public void nativeGetPicture(final String invocationLogAppId,final String invocationLogFunction) {
            if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                ceshiDataNativeGetPicture(invocationLogAppId,invocationLogFunction, "nativeGetPicture",0.8+"");
            } else {
                permissionsUtil = PermissionsUtil
                        .with(BaseWebCloseActivity.this)
                        .requestCode(0)
                        .isDebug(true)
                        .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                        .request();
            }

        }


        @JavascriptInterface
        public void nativeVerifyUserAndFace(final String invocationLogAppId,final String invocationLogFunction) {
            Log.e("invocationLogAppId",invocationLogAppId);

                if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    ceshiFaceData(invocationLogAppId,invocationLogFunction,"nativeVerifyUserAndFace");
                } else {
                    permissionsUtil = PermissionsUtil
                            .with(BaseWebCloseActivity.this)
                            .requestCode(0)
                            .isDebug(true)
                            .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                            .request();
                }



        }


        /**关闭当前页面*/
        @JavascriptInterface
        public void nativeCloseCurrentPage(final String invocationLogAppId,final String invocationLogFunction) {
            ceshiData(invocationLogAppId,invocationLogFunction, "nativeCloseCurrentPage");


        }

        /**获取设备imei*/
        @JavascriptInterface
        public void nativeGetEquipmentId(final String invocationLogAppId,final String invocationLogFunction) {
            if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.READ_PHONE_STATE)) {
                imeiTask(invocationLogAppId,invocationLogFunction);

            } else {//没有相应权限，获取相机权限

                //TestShowDig.AskForPermission(BaseWebActivity4.this,"定位");
                permissionsUtil = PermissionsUtil
                        .with(BaseWebCloseActivity.this)
                        .requestCode(2)
                        .isDebug(true)
                        .permissions(PermissionsUtil.Permission.Phone.READ_PHONE_STATE)
                        .request();
            }


        }


        @JavascriptInterface
        public void nativeGetLocalVersion(final String invocationLogAppId,final String invocationLogFunction) {
            ceshiData(invocationLogAppId,invocationLogFunction, "nativeGetLocalVersion");
        }


        /**关闭当前页面*/
        @JavascriptInterface
        public void closeCurrentPage() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    finish();
                }
            });
            Log.i("Info", "Thread:" + Thread.currentThread());
        }


        /**手机定位坐标*/
        @JavascriptInterface
        public void getLocation() {
            deliver.post(new Runnable() {
                @Override
                public void run() {
                    onLoctionCoordinate();
                }
            });
            Log.i("Info", "Thread:" + Thread.currentThread());
        }



        /**进入扫描二维码页面*/
        @JavascriptInterface
        public void ScanQRCode() {


            deliver.post(new Runnable() {
                @Override
                public void run() {
                    qrPermission();
                    if (allowedScan){
                        onScanQR();
                    }else {
                        Toast.makeText(getApplicationContext(),"请允许权限后尝试",Toast.LENGTH_SHORT).show();
                        qrPermission();
                    }


                }
            });

        }
    }

String ratios = null;
    private void ceshiDataNativeGetPicture(String appid, String function, String nativeGetPicture, final String ratio) {
        ratios = ratio;
        String username = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
        String userId  = null;
        try {
            userId = AesEncryptUtile.encrypt(username+ "_"+ Calendar.getInstance().getTimeInMillis(),key);
            OkGo.<String>post(HOME_URL+functionInvocationLogUrl)
                    .params("invocationLogAppId",appid)
                    .params("invocationLogMember",userId)
                    .params("invocationLogFunction",function)
                    .params("domainName",appServiceUrl)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Log.e("js调用扫码",response.body());

                            AppOrthBean appOrthBean = JsonUtil.parseJson(response.body(),AppOrthBean.class);
                            boolean success = appOrthBean.getSuccess();
                            if(success == true){
                                deliver.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        dialogss(ratio);
                                    }
                                });
                            }else {
                                ToastUtils.showToast(BaseWebCloseActivity.this,"没有使用该功能的权限!");
                                /*deliver.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLoctionCoordinate();
                                    }
                                });*/
                            }
                        }
                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private AlertDialog.Builder builder;
    private void dialogss(final String ratio) {



        final String items[] = {"打开相机", "打开相册"};
        builder = new AlertDialog.Builder(this);  //先得到构造器
        builder.setIcon(R.mipmap.ic_launcher);//设置图标，图片id即可

        builder.setSingleChoiceItems(items, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (items[which].equals("打开相机")) {

                    photoTask();

                } else if (items[which].equals("打开相册")) {
                    getAlbum(Float.parseFloat(ratio));
                }
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    int REQUEST_CODE_CHOOSE = 2;
    //打开相册
    private void getAlbum(float ratio) {
        Matisse
                .from(this)
                //选择图片
                .choose(MimeType.ofImage())
                //是否只显示选择的类型的缩略图，就不会把所有图片视频都放在一起，而是需要什么展示什么
                .showSingleMediaType(true)
                //这两行要连用 是否在选择图片中展示照相 和适配安卓7.0 FileProvider
                .capture(true)
                .captureStrategy(new CaptureStrategy(true,"PhotoPicker"))
                //有序选择图片 123456...
                .countable(true)
                //最大选择数量为9
                .maxSelectable(1)
                //选择方向
                .restrictOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)
                //界面中缩略图的质量
                .thumbnailScale(ratio)
                //黑色主题
                .theme(R.style.Matisse_Dracula)
                //Glide加载方式
                .imageEngine(new GlideEngine())
                //请求码
                .forResult(REQUEST_CODE_CHOOSE);
    }


    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void photoTask() {
        if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing

            testTakePhoto();
            ;//调用相机照相
        } else {//没有相应权限，获取相机权限
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "获取照相机权限",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    private Uri fileUri1;
    File mPhotoFile;
    //打开系统相机
    private void testTakePhoto() {





        //指定相机拍照的存储路径
        mPhotoFile = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            //判断文件是否存在，存在删除，不存在创建
            if (mPhotoFile.exists()){
                mPhotoFile.delete();
            }
            mPhotoFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //指定intent跳转到系统相机
        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(Build.VERSION.SDK_INT>=24){
            fileUri1= FileProvider.getUriForFile(BaseWebCloseActivity.this,"io.cordova.zhihuiyingyuan.provider",mPhotoFile);

        }else {
            fileUri1 = Uri.fromFile(mPhotoFile);
        }


        //设置存储路径
        captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri1);
        try {
            //开启相机
            startActivityForResult(captureIntent, 5);
        } catch (Exception e) {
            Log.e("相机异常",e.getMessage());
            Toast.makeText(this, "跳转系统相机异常", Toast.LENGTH_SHORT).show();
        }
    }


    private int imageid = 0;
    /**
     * 验证人脸
     * @param
     * @param invocationLogAppId
     * @param invocationLogFunction
     * @param
     */
    private void ceshiFaceData(String invocationLogAppId, String invocationLogFunction, final String typeName) {

        String username = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
        String userId  = null;
        try {
            userId = AesEncryptUtile.encrypt(username+ "_"+ Calendar.getInstance().getTimeInMillis(),key);
            Log.e("userId",userId);
            OkGo.<String>post(HOME_URL+functionInvocationLogUrl)
                    .params("invocationLogAppId",invocationLogAppId)
                    .params("invocationLogMember",userId)
                    .params("invocationLogFunction",invocationLogFunction)
                    .params("domainName",appServiceUrl)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Log.e("js调用扫码人脸",response.body());

                            AppOrthBean appOrthBean = JsonUtil.parseJson(response.body(),AppOrthBean.class);
                            boolean success = appOrthBean.getSuccess();
                            if(success == true){
                                String invocationLogFunction = appOrthBean.getObj().getInvocationLogFunction();
                                if(typeName.equals(invocationLogFunction)){
                                    if(invocationLogFunction.equals("nativeVerifyUserAndFace")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                SPUtils.put(BaseWebCloseActivity.this,"bitmap","");
                                                Intent intent = new Intent(BaseWebCloseActivity.this,FaceYiQingActivity.class);
                                                startActivityForResult(intent,99);
                                                imageid = 0;
                                            }
                                        });
                                    }else{
                                        Log.e("结果是","棒棒哒");
                                    }

                                }
                            }else {
                                ToastUtils.showToast(BaseWebCloseActivity.this,"没有使用该功能的权限!");
                                /*deliver.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLoctionCoordinate();
                                    }
                                });*/
                            }
                        }
                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if(action.equals("facereYiQing")){
                Log.e("imageid",imageid+"");
                String FaceActivity = intent.getStringExtra("FaceActivity");
                if(imageid == 0){
                    if(FaceActivity != null){
                        imageid = 1;

                        String s = (String)SPUtils.get(BaseWebCloseActivity.this, "bitmap", "");
                        String imei = (String) SPUtils.get(BaseWebCloseActivity.this, "imei", "");
                        String username = (String) SPUtils.get(BaseWebCloseActivity.this, "phone", "");

                        try {
                            //String secret  = AesEncryptUtile.encrypt(Calendar.getInstance().getTimeInMillis()+ "_"+"123456",key);
                            String secret = AesEncryptUtile.encrypt(username,key);
                            OkGo.<String>post(UrlRes.HOME2_URL+"/authentication/api/face/distinguishFace")
                            //OkGo.<String>post("http://192.168.30.68:8084/authentication/api/face/distinguishFace")
                                    .params( "openId",AesEncryptUtile.openid)
                                    .params( "memberId",secret)
                                    .params( "img",s )
                                    .params( "equipmentId",imei)
                                    .execute(new StringCallback(){

                                        @Override
                                        public void onStart(Request<String, ? extends Request> request) {
                                            super.onStart(request);
                                            //ViewUtils.createLoadingDialog2(LoginActivity2.this,true,"人脸识别中");

                                        }

                                        @Override
                                        public void onSuccess(Response<String> response) {
                                            SPUtils.put(getApplicationContext(),"isloading2","");
                                            ViewUtils.cancelLoadingDialog();
                                            Log.e("tag",response.body());
                                            BaseBean baseBean = JsonUtil.parseJson(response.body(),BaseBean.class);
                                            boolean success = baseBean.isSuccess();

                                            LocalFaceBean localVersonBean = new LocalFaceBean();
                                            localVersonBean.setSuccess(success);
                                            localVersonBean.setMessage(baseBean.getMsg());
                                            Gson gson = new Gson();
                                            String s = gson.toJson(localVersonBean);
                                            String jsonParams = s;
                                            String url2 = "javascript:getFaceAndroidParams('"+jsonParams+"')";
                                            Log.e("url",url2);
                                            mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String value) {
                                                    //此处为 js 返回的结果
                                                    Log.e("value",value);
                                                }
                                            });


                                        }

                                        @Override
                                        public void onError(Response<String> response) {
                                            super.onError(response);
                                            SPUtils.put(getApplicationContext(),"isloading2","");
                                            LocalFaceBean localVersonBean = new LocalFaceBean();
                                            localVersonBean.setSuccess(false);
                                            localVersonBean.setMessage("当前网络异常，识别失败");
                                            Gson gson = new Gson();
                                            String s = gson.toJson(localVersonBean);
                                            String jsonParams = s;
                                            String url2 = "javascript:getFaceAndroidParams('"+jsonParams+"')";
                                            mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                                                @Override
                                                public void onReceiveValue(String value) {
                                                    //此处为 js 返回的结果
                                                    Log.e("value",value);
                                                }
                                            });
                                            ViewUtils.cancelLoadingDialog();
                                            imageid = 0;

                                        }
                                    });
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        imageid = 0;
                    }
                }

            }
        }
    };


    public void imeiTask(String invocationLogAppId, String invocationLogFunction) {
        ceshiData(invocationLogAppId,invocationLogFunction, "nativeGetEquipmentId");
    }



    /**
     * 二维码扫描权限申请
     * 应用内获取通知消息*/
    boolean allowedScan = false;
    private void qrPermission() {
        //同时请求多个权限
        RxPermissions rxPermission = new RxPermissions(this);
        rxPermission
                .requestEach(
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CAMERA
                )
                .subscribe(new io.reactivex.functions.Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.e("用户已经同意该权限", permission.name + " is granted.");
                            //   Log.d(TAG, permission.name + " is granted.");
                            allowedScan =true;

                        } else if (permission.shouldShowRequestPermissionRationale) {
                            allowedScan =false;
                            Log.e("用户拒绝了该权限", permission.name + " is denied. More info should be provided.");
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时，还会提示请求权限的对话框
                            //   Log.d(TAG, permission.name + " is denied. More info should be provided.");

                        } else {
                            allowedScan =false;
                            // 用户拒绝了该权限，并且选中『不再询问』
                            //   Log.d(TAG, permission.name + " is denied.");
                            Log.e("用户拒绝了该权限", permission.name + permission.name + " is denied.");
//                            BaseWebActivity.this.finish();
                        }
                    }
                });

    }


    /**
     * 进入扫描二维码页面
     * @param
     */
    String loginQrUrl;
    public void onScanQR() {

        QRCodeManager.getInstance()
                .with(this)
                .setReqeustType(0)
                .setRequestCode(QR_CODE)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
                        //controlLog.append("\n\n(结果)" + result);
                        Log.e("QRCodeManager = ",result);
                        loginQrUrl = result;
                        mAgentWeb.getWebCreator().getWebView().loadUrl(result);

                    }

                    @Override
                    public void onError(Throwable errorMsg) {
                        //   controlLog.append("\n\n(错误)" + errorMsg.toString());
                        Log.e("QRCodeManager = = ",errorMsg.toString());
                    }

                    @Override
                    public void onCancel() {
                        //controlLog.append("\n\n(取消)扫描任务取消了");
                        Log.e("QRCodeManager = = = ","扫描任务取消了");
                    }

                    /**
                     * 当点击手动添加时回调
                     *
                     * @param requestCode
                     * @param resultCode
                     * @param data
                     */
                    @Override
                    public void onManual(int requestCode, int resultCode, Intent data) {
                        Log.e("QRCodeManager","点击了手动添加了");
                    }
                });

    }
    private Location location;
    private LocationManager locationManager;
    private String locationProvider;       //位置提供器

    /**手机定位坐标*/
    @SuppressLint("WrongConstant")
    public void onLoctionCoordinate(){
        String latitude =(String) SPUtils.get(MyApp.getInstance(), "latitude", "");
        Log.e("获取坐标",latitude);

        String longitude =(String) SPUtils.get(MyApp.getInstance(), "longitude", "");
        String direction =(String) SPUtils.get(MyApp.getInstance(), "direction", "");
        String altitude =(String) SPUtils.get(MyApp.getInstance(), "altitude", "");
        String speed =(String) SPUtils.get(MyApp.getInstance(), "speed", "");
        String locationWhere =(String) SPUtils.get(MyApp.getInstance(), "locationWhere", "");

        String countryName =(String) SPUtils.get(MyApp.getInstance(), "country", "");
        String countryCode =(String) SPUtils.get(MyApp.getInstance(), "countryCode", "");
        String province =(String) SPUtils.get(MyApp.getInstance(), "province", "");
        String city =(String) SPUtils.get(MyApp.getInstance(), "city", "");
        String cityCode =(String) SPUtils.get(MyApp.getInstance(), "cityCode", "");
        String district =(String) SPUtils.get(MyApp.getInstance(), "district", "");
        String town =(String) SPUtils.get(MyApp.getInstance(), "town", "");
        String adcode =(String) SPUtils.get(MyApp.getInstance(), "adcode", "");
        String street =(String) SPUtils.get(MyApp.getInstance(), "street", "");
        String streetNumber =(String) SPUtils.get(MyApp.getInstance(), "streetNumber", "");

        Log.e("获取坐标2",longitude);
        LocationBean locationBean = new LocationBean();
        locationBean.setSuccess(true);
        locationBean.setMessage("成功");
        locationBean.setIsBaidu("1");
        locationBean.setAltitude(altitude);
        locationBean.setSignRecordEquipmentId(MobileInfoUtils.getIMEI(BaseWebCloseActivity.this));
        //locationBean.setLatitude(location.getLatitude()+"");
        locationBean.setLatitude(latitude+"");
        //locationBean.setLongitude(location.getLongitude()+"");
        locationBean.setLongitude(longitude+"");
        locationBean.setAddress((String) SPUtils.get(MyApp.getInstance(), "addressLine", ""));

        locationBean.setCountryName(countryName);
        locationBean.setCityCode(countryCode);
        locationBean.setProvince(province);
        locationBean.setCity(city);
        locationBean.setCityCode(cityCode);
        locationBean.setDistrict(district);
        locationBean.setTown(town);
        locationBean.setAdcode(adcode);
        locationBean.setStreet(street);
        locationBean.setStreetNumber(streetNumber);

        Gson gson = new Gson();
        String s = gson.toJson(locationBean);
        String jsonParams = s;
        String url2 = "javascript:getAndroidParams('"+jsonParams+"')";
        Log.e("url",url2);
        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
                Log.e("value",value);
            }
        });


    }

    @SuppressLint("WrongConstant")
    public void onLoctionCoordinate2(){
        String latitude =(String) SPUtils.get(MyApp.getInstance(), "latitude", "");
        Log.e("获取坐标",latitude);

        String longitude =(String) SPUtils.get(MyApp.getInstance(), "longitude", "");
        String direction =(String) SPUtils.get(MyApp.getInstance(), "direction", "");
        String altitude =(String) SPUtils.get(MyApp.getInstance(), "altitude", "");
        String speed =(String) SPUtils.get(MyApp.getInstance(), "speed", "");
        String locationWhere =(String) SPUtils.get(MyApp.getInstance(), "locationWhere", "");

        String countryName =(String) SPUtils.get(MyApp.getInstance(), "country", "");
        String countryCode =(String) SPUtils.get(MyApp.getInstance(), "countryCode", "");
        String province =(String) SPUtils.get(MyApp.getInstance(), "province", "");
        String city =(String) SPUtils.get(MyApp.getInstance(), "city", "");
        String cityCode =(String) SPUtils.get(MyApp.getInstance(), "cityCode", "");
        String district =(String) SPUtils.get(MyApp.getInstance(), "district", "");
        String town =(String) SPUtils.get(MyApp.getInstance(), "town", "");
        String adcode =(String) SPUtils.get(MyApp.getInstance(), "adcode", "");
        String street =(String) SPUtils.get(MyApp.getInstance(), "street", "");
        String streetNumber =(String) SPUtils.get(MyApp.getInstance(), "streetNumber", "");

        Log.e("获取坐标2",longitude);
        LocationBean2 locationBean = new LocationBean2();
        locationBean.setSuccess(true);
        locationBean.setMessage("成功");
        locationBean.setIsBaidu("1");
        locationBean.setAltitude(altitude);
        locationBean.setCourse(direction);
        if(locationWhere.equals("1")){
            locationBean.setChina(true);
        }else {
            locationBean.setChina(false);
        }
        locationBean.setSpeed(speed);
        locationBean.setSignRecordEquipmentId(MobileInfoUtils.getIMEI(BaseWebCloseActivity.this));
        //locationBean.setLatitude(location.getLatitude()+"");
        locationBean.setLatitude(latitude+"");
        //locationBean.setLongitude(location.getLongitude()+"");
        locationBean.setLongitude(longitude+"");
        locationBean.setAddress((String) SPUtils.get(MyApp.getInstance(), "addressLine", ""));

        locationBean.setCountryName(countryName);
        locationBean.setCountryCode(countryCode);
        locationBean.setProvince(province);
        locationBean.setCity(city);
        locationBean.setCityCode(cityCode);
        locationBean.setDistrict(district);
        locationBean.setTown(town);
        locationBean.setAdcode(adcode);
        locationBean.setStreet(street);
        locationBean.setStreetNumber(streetNumber);

        Gson gson = new Gson();
        String s = gson.toJson(locationBean);
        String jsonParams = s;
        String url2 = "javascript:getAndroidParams('"+jsonParams+"')";
        Log.e("url",url2);
        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
                Log.e("value",value);
            }
        });


    }


    /** 响铃震动提示*/
    @SuppressLint("WrongConstant")
    private void setAlarmParams() {
        //AudioManager provides access to volume and ringer mode control.
        AudioManager volMgr = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        switch (volMgr.getRingerMode()) {//获取系统设置的铃声模式
            case AudioManager.RINGER_MODE_SILENT://静音模式，值为0，这时候不震动，不响铃
                break;
            case AudioManager.RINGER_MODE_VIBRATE://震动模式，值为1，这时候震动，不响铃
                SoundPoolUtils.vibrate(this, new long[]{1000, 500, 1000, 500}, 0);
                break;
            case AudioManager.RINGER_MODE_NORMAL://常规模式，值为2，分两种情况：1_响铃但不震动，2_响铃+震动
                SoundPoolUtils.playRing(this);
                SoundPoolUtils.vibrate(this, new long[]{500, 1000, 500, 1000}, 0);
                break;
            default:
                break;
        }
    }


    private void ceshiData(String appid, String function, final String typeName) {
        Log.e("function",function);
        String username = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
        String userId  = null;
        try {
            userId = AesEncryptUtile.encrypt(username+ "_"+ Calendar.getInstance().getTimeInMillis(),key);
            OkGo.<String>post(HOME_URL+functionInvocationLogUrl)
                    .params("invocationLogAppId",appid)
                    .params("invocationLogMember",userId)
                    .params("invocationLogFunction",function)
                    .params("domainName",appServiceUrl)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Log.e("js调用扫码",response.body());

                            AppOrthBean appOrthBean = JsonUtil.parseJson(response.body(),AppOrthBean.class);
                            boolean success = appOrthBean.getSuccess();
                            if(success == true){
                                String invocationLogFunction = appOrthBean.getObj().getInvocationLogFunction();
                                if(typeName.equals(invocationLogFunction)){
                                    if(invocationLogFunction.equals("nativeScanQRCode")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                cameraTask();



                                            }
                                        });
                                    }  else if(invocationLogFunction.equals("nativeGetLocation")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onLoctionCoordinate();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeCloseCurrentPage")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeGetEquipmentId")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onNativeGetImei();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeGetLocalVersion")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onNativeGetLocalVersion();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeOpenSystemSetting")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                                Uri uri = Uri.fromParts("package", getPackageName(), null);
                                                intent.setData(uri);
                                                startActivityForResult(intent, SETTINGS_REQ_CODE);
                                            }
                                        });
                                    }

                                }
                            }else {
                                ToastUtils.showToast(BaseWebCloseActivity.this,"没有使用该功能的权限!");
                                /*deliver.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLoctionCoordinate();
                                    }
                                });*/
                            }
                        }
                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void ceshiData2(String appid, String function, final String typeName, final String needExtraInfo) {
        Log.e("function",function);
        String username = (String) SPUtils.get(MyApp.getInstance(), "personName", "");
        String userId  = null;
        try {
            userId = AesEncryptUtile.encrypt(username+ "_"+ Calendar.getInstance().getTimeInMillis(),key);
            OkGo.<String>post(HOME_URL+functionInvocationLogUrl)
                    .params("invocationLogAppId",appid)
                    .params("invocationLogMember",userId)
                    .params("invocationLogFunction",function)
                    .params("domainName",appServiceUrl)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(Response<String> response) {

                            Log.e("js调用扫码",response.body());

                            AppOrthBean appOrthBean = JsonUtil.parseJson(response.body(),AppOrthBean.class);
                            boolean success = appOrthBean.getSuccess();
                            if(success == true){
                                String invocationLogFunction = appOrthBean.getObj().getInvocationLogFunction();
                                if(typeName.equals(invocationLogFunction)){
                                    if(invocationLogFunction.equals("nativeScanQRCode")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                cameraTask();



                                            }
                                        });
                                    }  else if(invocationLogFunction.equals("nativeGetLocation")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                if(needExtraInfo!= null){
                                                    if(needExtraInfo.equals("0")){
                                                        onLoctionCoordinate();
                                                    }else {
                                                        onLoctionCoordinate2();
                                                    }
                                                }else {
                                                    onLoctionCoordinate();
                                                }


                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeCloseCurrentPage")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                finish();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeGetEquipmentId")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onNativeGetImei();
                                            }
                                        });
                                    }else if(invocationLogFunction.equals("nativeGetLocalVersion")){
                                        deliver.post(new Runnable() {
                                            @Override
                                            public void run() {
                                                onNativeGetLocalVersion();
                                            }
                                        });
                                    }

                                }
                            }else {
                                ToastUtils.showToast(BaseWebCloseActivity.this,"没有使用该功能的权限!");
                                /*deliver.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        onLoctionCoordinate();
                                    }
                                });*/
                            }
                        }
                        @Override
                        public void onError(Response<String> response) {
                            super.onError(response);
                            Log.e("s",response.toString());
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private void onNativeGetLocalVersion() {
        String localVersionName = getLocalVersionName(this);
        LocalVersonBean localVersonBean = new LocalVersonBean();
        localVersonBean.setSuccess(1);
        localVersonBean.setMessage("成功");
        localVersonBean.setLocalVersionName(localVersionName);
        Gson gson = new Gson();
        String s = gson.toJson(localVersonBean);
        String jsonParams = s;
        String url2 = "javascript:getAndroidVersionParams('"+jsonParams+"')";
        Log.e("url",url2);
        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
                Log.e("value",value);
            }
        });
    }

    private void onNativeGetImei() {

        String imei = getIMEI(this);
        ImeiBean imeiBean = new ImeiBean();
        imeiBean.setSuccess(1);
        imeiBean.setMessage("成功");
        imeiBean.setEquipmentId(imei);
        Gson gson = new Gson();
        String s = gson.toJson(imeiBean);
        String jsonParams = s;
        String url2 = "javascript:getAndroidImeiParams('"+jsonParams+"')";
        Log.e("url",url2);
        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
            @Override
            public void onReceiveValue(String value) {
                //此处为 js 返回的结果
                Log.e("value",value);
            }
        });
    }


    private static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(BaseWebCloseActivity.this, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            onScanQR();

        } else {//没有相应权限，获取相机权限
         /*
            EasyPermissions.requestPermissions(this, "获取照相机权限",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);*/
            permissionsUtil=  PermissionsUtil
                    .with(this)
                    .requestCode(1)
                    .isDebug(true)//开启log
                    .permissions(PermissionsUtil.Permission.Camera.CAMERA,PermissionsUtil.Permission.Storage.READ_EXTERNAL_STORAGE,PermissionsUtil.Permission.Storage.WRITE_EXTERNAL_STORAGE)
                    .request();
        }
    }


    @Override
    protected void onPause() {
        mAgentWeb.getWebLifeCycle().onPause();
        mAgentWeb.getWebCreator().getWebView().pauseTimers();
        super.onPause();
        SPUtils.put(getApplicationContext(),"isloading2","");
        //upLoadWebInfo();
    }

    @Override
    protected void onResume() {
        mAgentWeb.getWebLifeCycle().onResume();
        mAgentWeb.getWebCreator().getWebView().resumeTimers();
        super.onResume();
        netInsertPortal("5");
        String isLoading2 = (String) SPUtils.get(BaseWebCloseActivity.this, "isloading2", "");
        Log.e("isLoading2",isLoading2);
        if(!isLoading2 .equals("")){
            ViewUtils.createLoadingDialog2(BaseWebCloseActivity.this,true,"人脸识别中");
            SPUtils.put(getApplicationContext(),"isloading2","");


        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mAgentWeb.getWebLifeCycle().onDestroy();
        mLocationClient.stop();
        unregisterReceiver(broadcastReceiver);
        unregisterReceiver(broadcastReceiver2);
        //mAgentWeb.getWebCreator().getWebView().destroy();
        WebView mwebView = mAgentWeb.getWebCreator().getWebView();
        if (mwebView != null) {

            mwebView.stopLoading();
            mwebView.onPause();

            mwebView.clearFormData();

            mwebView.destroyDrawingCache();
            mwebView.removeAllViews();
            //WebStorage.getInstance().deleteAllData();
            mwebView.destroy();
        }
    }

    private class OpenFileChromeClient extends WebChromeClient {

        //  Android 2.2 (API level 8)到Android 2.3 (API level 10)版本选择文件时会触发该隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooser(uploadMsg, null);
        }

        // Android 3.0 (API level 11)到 Android 4.0 (API level 15))版本选择文件时会触发，该方法为隐藏方法
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooser(uploadMsg, acceptType, null);
        }

        // Android 4.1 (API level 16) -- Android 4.3 (API level 18)版本选择文件时会触发，该方法为隐藏方法
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileInput(uploadMsg, null, false);
        }

        // Android 5.0 (API level 21)以上版本会触发该方法，该方法为公开方法
        @SuppressWarnings("all")
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            if (Build.VERSION.SDK_INT >= 21) {
                final boolean allowMultiple = fileChooserParams.getMode() == FileChooserParams.MODE_OPEN_MULTIPLE;//是否支持多选
                openFileInput(null, filePathCallback, allowMultiple);
                return true;
            }
            else {
                return false;
            }
        }

        @Override
        public void onProgressChanged(WebView view, int newProgress) {

        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            if (mTitleTextView != null) {
                mTitleTextView.setText(title);
            }
            if(appName != null){
                mTitleTextView.setText(appName);
            }
        }


    }


    @SuppressLint("NewApi")
    protected void openFileInput(final ValueCallback<Uri> fileUploadCallbackFirst, final ValueCallback<Uri[]> fileUploadCallbackSecond, final boolean allowMultiple) {
        //Android 5.0以下版本
        if (mFileUploadCallbackFirst != null) {
            mFileUploadCallbackFirst.onReceiveValue(null);
        }
        mFileUploadCallbackFirst = fileUploadCallbackFirst;

        //Android 5.0及以上版本
        if (mFileUploadCallbackSecond != null) {
            mFileUploadCallbackSecond.onReceiveValue(null);
        }
        mFileUploadCallbackSecond = fileUploadCallbackSecond;

        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);

        if (allowMultiple) {
            if (Build.VERSION.SDK_INT >= 18) {
                i.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
            }
        }

        i.setType(mUploadableFileTypes);

        startActivityForResult(Intent.createChooser(i, "选择文件"), REQUEST_CODE_FILE_PICKER);

    }




    public void netInsertPortal(final String insertPortalAccessLog) {
        String imei = getIMEI(this);
        OkGo.<String>post(HOME_URL + UrlRes.Four_Modules)
                .params("portalAccessLogMemberId",(String) SPUtils.get(getInstance(),"userId",""))
                .params("portalAccessLogEquipmentId",(String) SPUtils.get(getInstance(),"imei",""))//设备ID
                .params("portalAccessLogTarget", insertPortalAccessLog)//访问目标
                .params("portalAccessLogVersionNumber", (String) SPUtils.get(this,"versionName", ""))//版本号
                .params("portalAccessLogOperatingSystem", "ANDROID")//版本号
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("sdsaas",response.body());

                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        //需要调用onRequestPermissionsResult
        permissionsUtil.onRequestPermissionsResult(requestCode, permissions, grantResults);
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }



    public void onActivityResult(final int requestCode, final int resultCode, final Intent intent) {
        QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, intent);
        if(permissionsUtil != null){
            permissionsUtil.onActivityResult(requestCode, resultCode, intent);
        }

        if (requestCode == REQUEST_CODE_FILE_PICKER) {
            if (resultCode == Activity.RESULT_OK) {
                if (intent != null) {
                    //Android 5.0以下版本
                    if (mFileUploadCallbackFirst != null) {
                        mFileUploadCallbackFirst.onReceiveValue(intent.getData());
                        mFileUploadCallbackFirst = null;
                    }
                    else if (mFileUploadCallbackSecond != null) {//Android 5.0及以上版本
                        Uri[] dataUris = null;

                        try {
                            if (intent.getDataString() != null) {
                                dataUris = new Uri[] { Uri.parse(intent.getDataString()) };
                            }
                            else {
                                if (Build.VERSION.SDK_INT >= 16) {
                                    if (intent.getClipData() != null) {
                                        final int numSelectedFiles = intent.getClipData().getItemCount();

                                        dataUris = new Uri[numSelectedFiles];

                                        for (int i = 0; i < numSelectedFiles; i++) {
                                            dataUris[i] = intent.getClipData().getItemAt(i).getUri();
                                        }
                                    }
                                }
                            }
                        }
                        catch (Exception ignored) { }
                        mFileUploadCallbackSecond.onReceiveValue(dataUris);
                        mFileUploadCallbackSecond = null;
                    }
                }
            }
            else {
                //这里mFileUploadCallbackFirst跟mFileUploadCallbackSecond在不同系统版本下分别持有了
                //WebView对象，在用户取消文件选择器的情况下，需给onReceiveValue传null返回值
                //否则WebView在未收到返回值的情况下，无法进行任何操作，文件选择器会失效
                if (mFileUploadCallbackFirst != null) {
                    mFileUploadCallbackFirst.onReceiveValue(null);
                    mFileUploadCallbackFirst = null;
                }
                else if (mFileUploadCallbackSecond != null) {
                    mFileUploadCallbackSecond.onReceiveValue(null);
                    mFileUploadCallbackSecond = null;
                }
            }

        }

        if (requestCode == REQUEST_CODE_CHOOSE) {
            if (resultCode == RESULT_OK) {//打开相册返回
                //图片路径 同样视频地址也是这个 根据requestCode
                List<String> pathList = Matisse.obtainPathResult(intent);

                for (int i = 0; i < pathList.size(); i++) {
                    String albumUri = pathList.get(i);

                    File file = new File(albumUri);

                    //String image = getFileBase64(file);
                    Bitmap bitmap = BitmapFactory.decodeFile(albumUri);

                    double v = 500 * Double.parseDouble(ratios);

                    Bitmap scaledBitmap = BitmapUtils.compressByWidth(bitmap, (int)v);
                    String image = BitmapUtils.bitmapToBase64(scaledBitmap);

                    NaturePicBean naturePicBean = new NaturePicBean();
                    naturePicBean.setSuccess(true);
                    naturePicBean.setMessage("成功");

                    naturePicBean.setImage(URLEncoder.encode( "data:image/" + getFileExtension(file) + ";base64," +image));


                    Gson gson = new Gson();
                    String s = gson.toJson(naturePicBean);
                    String jsonParams = s;

                    String url2 = "javascript:getNativeGetPicture('"+jsonParams+"')";
                    Log.e("url",url2);
                    mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String value) {
                            //此处为 js 返回的结果
                            Log.e("value",value);
                        }
                    });
                }
            }
        }



        if (requestCode == 5) {
            if (resultCode == RESULT_OK) {//拍照返回


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        String extension = mPhotoFile.getAbsolutePath();
                        extension = extension.substring(extension.lastIndexOf("."));
                        extension = extension.replace(".","");
                        Bitmap bitmap = BitmapFactory.decodeFile(mPhotoFile.getAbsolutePath());
                        double v = 500 * Double.parseDouble(ratios);

                        Bitmap scaledBitmap = BitmapUtils.compressByWidth(bitmap, (int)v);
                        String image = BitmapUtils.bitmapToBase64(scaledBitmap);
                        NaturePicBean naturePicBean = new NaturePicBean();
                        naturePicBean.setSuccess(true);
                        naturePicBean.setMessage("成功");
                        naturePicBean.setImage( URLEncoder.encode( "data:image/" + extension + ";base64," +image));
                        Gson gson = new Gson();
                        String s = gson.toJson(naturePicBean);
                        String jsonParams = s;

                        String url2 = "javascript:getNativeGetPicture('"+jsonParams+"')";
                        Log.e("url",url2);
                        mAgentWeb.getWebCreator().getWebView().evaluateJavascript(url2, new ValueCallback<String>() {
                            @Override
                            public void onReceiveValue(String value) {
                                //此处为 js 返回的结果
                                Log.e("value",value);
                            }
                        });
                    }
                }).run();

                //                cutImage(fileUri1);
            }
        }

    }

    public String getFileExtension(File file) {
        String extension = "";
        try {
            if (file != null && file.exists()) {
                String name = file.getName();//a.jpg
                extension = name.substring(name.lastIndexOf("."));
                extension = extension.replace(".","");
            }
        } catch (Exception e) {
            extension = "";
        }
        return extension;
    }

    @Override
    public void onPermissionsGranted(int requestCode, String... permission) {
        Log.e("权限获取回调222","onPermissionsGranted");
        mLocationClient.stop();

        if(requestCode == 0){
            getMyLocation();
        }else if(requestCode == 1){
            onScanQR();
        }

    }

    @Override
    public void onPermissionsDenied(int requestCode, String... permission) {
        Log.e("权限被拒绝回调","onPermissionsDenied");
        mLocationClient.stop();
        if(requestCode == 0){
            getMyLocation();
        }
    }

}
