package io.cordova.zhihuiyingyuan.fragment;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.bumptech.glide.Glide;
import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.Transformer;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import io.cordova.zhihuiyingyuan.R;
import io.cordova.zhihuiyingyuan.UrlRes;
import io.cordova.zhihuiyingyuan.activity.LoginActivity2;
import io.cordova.zhihuiyingyuan.adapter.NewsAdapter2;
import io.cordova.zhihuiyingyuan.bean.BannerBean;
import io.cordova.zhihuiyingyuan.bean.HomeNewsBean;
import io.cordova.zhihuiyingyuan.bean.MyCollectionBean;
import io.cordova.zhihuiyingyuan.utils.AesEncryptUtile;
import io.cordova.zhihuiyingyuan.utils.JsonUtil;
import io.cordova.zhihuiyingyuan.utils.MyApp;
import io.cordova.zhihuiyingyuan.utils.SPUtils;
import io.cordova.zhihuiyingyuan.utils.StringUtils;
import io.cordova.zhihuiyingyuan.utils.ToastUtils;
import io.cordova.zhihuiyingyuan.utils.ViewUtils;
import io.cordova.zhihuiyingyuan.utils.netState;
import io.cordova.zhihuiyingyuan.web.BaseWebActivity4;
import io.cordova.zhihuiyingyuan.web.MyBaseWebUrl;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Administrator on 2019/8/21 0021.
 */

public class Home1Fragment extends BaseFragment {
    Banner banner;

    private ArrayList<Fragment> mFragments = new ArrayList<>();



    LinearLayout llMyCollection;

    RecyclerView myDataList;
    RecyclerView recyclerView;

    ImageView iv_qr;

    boolean isLogin =false;


    SmartRefreshLayout mSwipeRefreshLayout;

    int pos = 0;

    TextView tv_title;

    private int type = 1;
    private LinearLayoutManager mLinearLayoutManager;
    RelativeLayout rl_01;
    RelativeLayout rl_02;
    RelativeLayout rl_03;
    RelativeLayout rl_04;
    TextView tv_01;
    TextView tv_02;
    TextView tv_03;
    TextView tv_04;
    TextView tv_011;
    TextView tv_012;
    TextView tv_013;
    TextView tv_014;

    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_first;
    }

    @Override
    protected void initView(final View view) {
        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));
        banner = view.findViewById(R.id.banner);

        llMyCollection = view.findViewById(R.id.ll_my_collection);
        myDataList = view.findViewById(R.id.my_collection_list);
        iv_qr = view.findViewById(R.id.iv_qr);
        mSwipeRefreshLayout = view.findViewById(R.id.swipeLayout);
        tv_title = view.findViewById(R.id.tv_title);
        rl_01 = view.findViewById(R.id.rl_01);
        rl_02 = view.findViewById(R.id.rl_02);
        rl_03 = view.findViewById(R.id.rl_03);
        rl_04 = view.findViewById(R.id.rl_04);
        tv_01 = view.findViewById(R.id.tv_01);
        tv_02 = view.findViewById(R.id.tv_02);
        tv_03 = view.findViewById(R.id.tv_03);
        tv_04 = view.findViewById(R.id.tv_04);
        tv_011 = view.findViewById(R.id.tv_011);
        tv_012 = view.findViewById(R.id.tv_022);
        tv_013 = view.findViewById(R.id.tv_033);
        tv_014 = view.findViewById(R.id.tv_044);
        recyclerView = view.findViewById(R.id.recyclerview);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(mLinearLayoutManager);
        tv_01.setTextColor(Color.parseColor("#139d7e"));
        tv_02.setTextColor(Color.parseColor("#8f8f94"));
        tv_03.setTextColor(Color.parseColor("#8f8f94"));
        tv_04.setTextColor(Color.parseColor("#8f8f94"));
        getBannerData();
        getHotData();
        ViewUtils.createLoadingDialog(getActivity());
        getNewsData(type);

        registerBoradcastReceiver();

        iv_qr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cameraTask();
            }
        });

        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                tv_01.setTextColor(Color.parseColor("#139d7e"));
                tv_02.setTextColor(Color.parseColor("#8f8f94"));
                tv_03.setTextColor(Color.parseColor("#8f8f94"));
                tv_04.setTextColor(Color.parseColor("#8f8f94"));
                getNewsData(1);
                tv_011.setVisibility(View.VISIBLE);
                tv_012.setVisibility(View.GONE);
                tv_013.setVisibility(View.GONE);
                tv_014.setVisibility(View.GONE);
                getBannerData();
                getHotData();
                refreshlayout.finishRefresh();
            }
        });
        rl_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.createLoadingDialog(getActivity());
                tv_01.setTextColor(Color.parseColor("#139d7e"));
                tv_02.setTextColor(Color.parseColor("#8f8f94"));
                tv_03.setTextColor(Color.parseColor("#8f8f94"));
                tv_04.setTextColor(Color.parseColor("#8f8f94"));
                tv_011.setVisibility(View.VISIBLE);
                tv_012.setVisibility(View.GONE);
                tv_013.setVisibility(View.GONE);
                tv_014.setVisibility(View.GONE);

                getNewsData(1);
            }
        });

        rl_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.createLoadingDialog(getActivity());
                tv_01.setTextColor(Color.parseColor("#8f8f94"));
                tv_02.setTextColor(Color.parseColor("#139d7e"));
                tv_03.setTextColor(Color.parseColor("#8f8f94"));
                tv_04.setTextColor(Color.parseColor("#8f8f94"));
                tv_011.setVisibility(View.GONE);
                tv_012.setVisibility(View.VISIBLE);
                tv_013.setVisibility(View.GONE);
                tv_014.setVisibility(View.GONE);
                getNewsData(2);
            }
        });

        rl_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.createLoadingDialog(getActivity());
                tv_01.setTextColor(Color.parseColor("#8f8f94"));
                tv_02.setTextColor(Color.parseColor("#8f8f94"));
                tv_03.setTextColor(Color.parseColor("#139d7e"));
                tv_04.setTextColor(Color.parseColor("#8f8f94"));
                tv_011.setVisibility(View.GONE);
                tv_012.setVisibility(View.GONE);
                tv_013.setVisibility(View.VISIBLE);
                tv_014.setVisibility(View.GONE);
                getNewsData(3);
            }
        });

        rl_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.createLoadingDialog(getActivity());
                tv_01.setTextColor(Color.parseColor("#8f8f94"));
                tv_02.setTextColor(Color.parseColor("#8f8f94"));
                tv_03.setTextColor(Color.parseColor("#8f8f94"));
                tv_04.setTextColor(Color.parseColor("#139d7e"));
                tv_011.setVisibility(View.GONE);
                tv_012.setVisibility(View.GONE);
                tv_013.setVisibility(View.GONE);
                tv_014.setVisibility(View.VISIBLE);
                getNewsData(4);
            }
        });


    }


    private NewsAdapter2 adapter2;
    private void getNewsData(final int type) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .tag(this)
                .params("pageNum",1)
                .params("pageSize",10)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        ViewUtils.cancelLoadingDialog();
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();

                        if(success){
                            final List<HomeNewsBean.Obj> obj = homeNewsBean.getObj();
                            adapter2 = new NewsAdapter2(getActivity(),R.layout.list_item_new_news,obj,type);
                            recyclerView.setAdapter(adapter2);
                            adapter2.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    String newsId = obj.get(position).getNewsId();

                                    getNewsDetails(newsId);
                                }

                                @Override
                                public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    return false;
                                }
                            });
                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        ViewUtils.cancelLoadingDialog();

                    }
                });
    }

    private void getNewsDetails(final String newsId) {
        ViewUtils.createLoadingDialog(getActivity());
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.getNewsDetailsUrl)
                .tag(this)
                .params("newsId",newsId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
                        Intent intent = new Intent(getActivity(),MyBaseWebUrl.class);
                        String url = "http://mobile.havct.edu.cn/portal/portal-yyzy/portal-app/app/newsDetail_native.html?id="+newsId;
                        Log.e("appurl",url);
                        intent.putExtra("appUrl",url);
                        startActivity(intent);


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("newsid",response.body());
                        ViewUtils.cancelLoadingDialog();
                    }
                });
    }



    MyCollectionBean collectionBean;

    private void getHotData() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findHeatAppListUrl)
                .tag(this)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("hot",response.body());

                        collectionBean = JSON.parseObject(response.body(), MyCollectionBean.class);
                        if (collectionBean.isSuccess()) {
                            if(collectionBean.getObj() != null){
                                if (collectionBean.getObj().size() > 0) {
                                    llMyCollection.setVisibility(View.VISIBLE);

                                    setCollectionList();

                                } else {
                                    llMyCollection.setVisibility(View.GONE);

                                }
                            }
                        }

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void getBannerData() {
        String s = UrlRes.HOME_URL + UrlRes.findBroadcastListUrl;
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findBroadcastListUrl)
                .tag(this)
                .params("broadcastState", 0)
                .params("broadcastEquipment",0)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("banner",response.body());
                        BannerBean bannerBean = JsonUtil.parseJson(response.body(),BannerBean.class);
                        boolean success = bannerBean.getSuccess();
                        if(success == true){
                            List<BannerBean.Banners> list = bannerBean.getObj().getList();
                            initBanner(list);
                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }







    private List<String> images = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private void initBanner(final List<BannerBean.Banners> list) {
        images.clear();
        titles.clear();
        for (int i = 0; i < list.size(); i++) {
            String broadcastUrl = list.get(i).getBroadcastImage();
            images.add(UrlRes.HOME3_URL+broadcastUrl);
            titles.add(list.get(i).getBroadcastTitle());
        }
        tv_title.setText(titles.get(0));
        //设置banner样式
        banner.setBannerStyle(BannerConfig.CIRCLE_INDICATOR);

        //设置图片加载器
        banner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        banner.setImages(images);
        //设置banner动画效果
        banner.setBannerAnimation(Transformer.Default);
        //设置标题集合（当banner样式有显示title时）
        banner.setBannerTitles(titles);
        //设置自动轮播，默认为true
        banner.isAutoPlay(true);
        //设置轮播时间
        banner.setDelayTime(3000);
        //设置指示器位置（当banner模式中有指示器时）
        banner.setIndicatorGravity(BannerConfig.CENTER);
        //banner设置方法全部调用完毕时最后调用
        banner.start();

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {

                String broadcastUrl = list.get(position).getBroadcastUrl();
                if(broadcastUrl.equals("")){

                    ToastUtils.showToast(getActivity(),"暂无详情地址");
                }else {
                    //跳转到轮播图详情页
                    Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                    intent.putExtra("appUrl",list.get(position).getBroadcastUrl());
                    //intent.putExtra("appId","");
                    intent.putExtra("appName",list.get(position).getBroadcastTitle());
                    startActivity(intent);
                }
            }
        });
        banner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tv_title.setText(titles.get(position));
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }


    public class GlideImageLoader extends ImageLoader {
        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            /**
             注意：
             1.图片加载器由自己选择，这里不限制，只是提供几种使用方法
             2.返回的图片路径为Object类型，由于不能确定你到底使用的那种图片加载器，
             传输的到的是什么格式，那么这种就使用Object接收和返回，你只需要强转成你传输的类型就行，
             切记不要胡乱强转！
             */


            //Glide 加载图片简单用法
            Glide.with(context).load(path).into(imageView);


        }

    }


    CommonAdapter<MyCollectionBean.ObjBean> collectionAdapter;
    private void setCollectionList() {
        myDataList.setLayoutManager(new GridLayoutManager(getActivity(), 4){
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
        collectionAdapter = new CommonAdapter<MyCollectionBean.ObjBean>(getActivity(), R.layout.item_service_app, collectionBean.getObj()) {
            @Override
            protected void convert(ViewHolder holder, MyCollectionBean.ObjBean objBean, int position) {


                holder.setText(R.id.tv_app_name,objBean.getAppName());

                /*appIntranet  1 需要内网*/
                if (objBean.getAppIntranet()==1){
                    holder.setVisible(R.id.iv_del,true);
                    Glide.with(getActivity())
                            .load(R.mipmap.nei_icon)
                            .error(R.color.white)
                            .into((ImageView) holder.getView(R.id.iv_del));
                }else {
                    holder.setVisible(R.id.iv_del,false);
                }

                if (!isLogin) {
                    if (objBean.getAppLoginFlag()==0){
                        holder.setVisible(R.id.iv_lock_close,true);
                        Glide.with(getActivity())
                                .load(R.mipmap.lock_icon)
                                .error(R.mipmap.lock_icon)
                                .into((ImageView) holder.getView(R.id.iv_lock_close));
                    }else {
                        holder.setVisible(R.id.iv_lock_close,false);
                    }
                }

                if (null != objBean.getPortalAppIcon() && null != objBean.getPortalAppIcon().getTempletAppImage()){

                    Glide.with(getActivity())
                            .load(UrlRes.HOME3_URL + objBean.getPortalAppIcon().getTempletAppImage())
                            .error(R.mipmap.zwt)
                            .into((ImageView) holder.getView(R.id.iv_app_icon));
                }else {
                    Glide.with(getActivity())
                            .load(UrlRes.HOME3_URL + objBean.getAppImages())
                            .error(R.mipmap.zwt)
                            .into((ImageView) holder.getView(R.id.iv_app_icon));
                }
            }
        };
        myDataList.setAdapter(collectionAdapter);
        collectionAdapter.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
//                collectionBean.getObj().get(position)
                LinearLayout ll_click = holder.itemView.findViewById(R.id.ll_click);
                ll_click.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                if (netState.isConnect(getActivity())){
                    netWorkAppClick(collectionBean.getObj().get(position).getAppId());
                }
                if(collectionBean.getObj().get(position).getAppUrl().equals("http://mobile.havct.edu.cn/portal/app/mailbox/qqEmailLogin")){
                                           /* webView.setWebViewClient(mWebViewClient);
                                            webView.loadUrl("http://iapp.zzuli.edu.cn/portal/login/appLogin");*/
                    Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                    intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                    intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                    intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                    startActivity(intent);
                }else {
                   /* Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                    intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                    intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                    intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                    startActivity(intent);*/

                    Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                    if (collectionBean.getObj().get(position).getAppUrl().contains("{memberid}")){
                        String appUrl = collectionBean.getObj().get(position).getAppUrl();
                        String s1= null;
                        try {
                            s1 = URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"personName",""), "UTF-8");
                            appUrl =  appUrl.replace("{memberid}", s1);
                            intent.putExtra("appUrl",appUrl);
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }

                    }else if(collectionBean.getObj().get(position).getAppUrl().contains("{memberAesEncrypt}")){
                        String appUrl = collectionBean.getObj().get(position).getAppUrl();
                        try {
                            String memberAesEncrypt = AesEncryptUtile.encrypt((String) SPUtils.get(MyApp.getInstance(),"personName",""), String.valueOf(collectionBean.getObj().get(position).getAppSecret()));
                            String s2=  URLEncoder.encode(memberAesEncrypt, "UTF-8");
                            appUrl =  appUrl.replace("{memberAesEncrypt}", s2);
                            intent.putExtra("appUrl",appUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else if(collectionBean.getObj().get(position).getAppUrl().contains("{quicklyTicket}")){
                        String appUrl = collectionBean.getObj().get(position).getAppUrl();
                        try {
                            String s3 =  URLEncoder.encode((String) SPUtils.get(MyApp.getInstance(),"TGC",""), "UTF-8");
                            appUrl = appUrl.replace("{quicklyTicket}",s3);
                            intent.putExtra("appUrl",appUrl);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    else{
                        intent.putExtra("appUrl",collectionBean.getObj().get(position).getAppUrl());
                    }
                    Log.e("url  ==",collectionBean.getObj().get(position).getAppUrl() + "");

                    //intent.putExtra("appUrl",appsBean.getAppUrl());
                    intent.putExtra("appId",collectionBean.getObj().get(position).getAppId()+"");
                    intent.putExtra("appName",collectionBean.getObj().get(position).getAppName()+"");
                    startActivity(intent);
                    ll_click.setBackgroundColor(getResources().getColor(R.color.white));
                }

            }

            @Override
            public boolean onItemLongClick(View view, RecyclerView.ViewHolder holder, int position) {
                return false;
            }
        });

        collectionAdapter.notifyDataSetChanged();
    }





    /**
     * 记录该微应用的的访问量
     * @param appId
     *
     * */
    private void netWorkAppClick(int appId) {
        OkGo.<String>get(UrlRes.HOME_URL +UrlRes.APP_Click_Number)

                .params("appId",appId)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))

                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("result1",response.body());
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("错误",response.body());
                    }
                });
    }

    public void registerBoradcastReceiver() {
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("refresh2");
        //注册广播
        getActivity().registerReceiver(broadcastReceiver, myIntentFilter);
    }


    private BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(action.equals("refresh2")){
                isLogin = true;
                initShowPage();
            }
        }
    };

    private void initShowPage() {
        getHotData();
    }

    private static final int RC_CAMERA_PERM = 123;

    @AfterPermissionGranted(RC_CAMERA_PERM)
    public void cameraTask() {
        if (EasyPermissions.hasPermissions(getActivity(), Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            // Have permission, do the thing!

            onScanQR();
            ;//调用相机照相
        } else {//没有相应权限，获取相机权限
            // Ask for one permission
            EasyPermissions.requestPermissions(this, "获取照相机权限",
                    RC_CAMERA_PERM, Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
    }

    /**
     * 进入扫描二维码页面
     *
     * @param
     */
    public void onScanQR() {
        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));
        Log.e("tag  = ","点击了");
        QRCodeManager.getInstance()
                .with(getActivity())
                .setReqeustType(0)
                .setRequestCode(55846)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
                        //controlLog.append("\n\n(结果)" + result);
                        Log.e("QRCodeManager = ",result);
                        if(!isLogin){
                            Intent intent = new Intent(MyApp.getInstance(), LoginActivity2.class);
                            startActivity(intent);
                        }else {

                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            intent.putExtra("appUrl",result);
                            intent.putExtra("scan","scan");
                            startActivity(intent);
                        }

//                    intent.putExtra("appId",listBean.getAppId()+"");

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
    /**二维码返回结果接收*/
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);
        //注册onActivityResult
        if (requestCode == 55846){
            QRCodeManager.getInstance().with(getActivity()).onActivityResult(requestCode, resultCode, intent);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(broadcastReceiver);
    }
}
