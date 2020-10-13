package io.cordova.zhihuiyingyuan.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadmoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhy.adapter.recyclerview.MultiItemTypeAdapter;

import java.util.List;

import io.cordova.zhihuiyingyuan.R;
import io.cordova.zhihuiyingyuan.UrlRes;
import io.cordova.zhihuiyingyuan.activity.LoginActivity2;
import io.cordova.zhihuiyingyuan.activity.MyToDoMsgActivity;
import io.cordova.zhihuiyingyuan.adapter.NewsAdapter2;
import io.cordova.zhihuiyingyuan.bean.CountBean;
import io.cordova.zhihuiyingyuan.bean.HomeNewsBean;
import io.cordova.zhihuiyingyuan.utils.BadgeView;
import io.cordova.zhihuiyingyuan.utils.JsonUtil;
import io.cordova.zhihuiyingyuan.utils.LighterHelper;
import io.cordova.zhihuiyingyuan.utils.MobileInfoUtils;
import io.cordova.zhihuiyingyuan.utils.MyApp;
import io.cordova.zhihuiyingyuan.utils.SPUtils;
import io.cordova.zhihuiyingyuan.utils.StringUtils;
import io.cordova.zhihuiyingyuan.utils.ToastUtils;
import io.cordova.zhihuiyingyuan.utils.ViewUtils;
import io.cordova.zhihuiyingyuan.web.MyBaseWebUrl;
import me.samlss.lighter.Lighter;
import me.samlss.lighter.interfaces.OnLighterListener;
import me.samlss.lighter.parameter.Direction;
import me.samlss.lighter.parameter.LighterParameter;
import me.samlss.lighter.parameter.MarginOffset;
import me.samlss.lighter.shape.CircleShape;

/**
 * Created by Administrator on 2019/8/21 0021.
 */

public class SecondFragment extends BaseFragment {



    SmartRefreshLayout mSwipeRefreshLayout;
    RecyclerView recyclerView;

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

    private int num = 1;

    RelativeLayout rl_more;

    RelativeLayout rl_msg_app;

    private BadgeView badge1;
    boolean isLogin = false;


    @Override
    protected int getLayoutResource() {
        return R.layout.fragment_second;
    }

    @Override
    protected void initView(final View view) {
        isLogin = !StringUtils.isEmpty((String)SPUtils.get(MyApp.getInstance(),"username",""));



        mSwipeRefreshLayout = view.findViewById(R.id.swipeLayout);

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
        rl_more = view.findViewById(R.id.rl_more);
        recyclerView = view.findViewById(R.id.recyclerview);
        rl_msg_app = view.findViewById(R.id.rl_msg_app1);
        mLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayout.VERTICAL,false);
        recyclerView.setLayoutManager(mLinearLayoutManager);

        isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"userId",""));
        rl_msg_app.setVisibility(View.VISIBLE);
        count = (String) SPUtils.get(getActivity(), "count", "");
        badge1 = new BadgeView(getActivity(), rl_msg_app);
        remind();
        if (!isLogin){
            badge1.hide();
        }else {
            if(count != null){
                if(count.equals("0")){

                    badge1.hide();
                }else {
                    badge1.show();
                }
            }else {
                badge1.hide();
            }



        }

        tv_01.setTextColor(Color.parseColor("#139d7e"));
        tv_02.setTextColor(Color.parseColor("#8f8f94"));
        tv_03.setTextColor(Color.parseColor("#8f8f94"));
        tv_04.setTextColor(Color.parseColor("#8f8f94"));
        ViewUtils.createLoadingDialog(getActivity());
        getNewsData(type);


        rl_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewUtils.createLoadingDialog(getActivity());
                type = 1;
                num = 1;
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
                type = 2;
                num = 1;
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
                type = 3;
                num = 1;
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
                type = 4;
                num = 1;
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


        mSwipeRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {

                num = 1;
                switch (type){
                    case 1:
                        getRefresh01(type);
                        break;

                    case 2:
                        getRefresh02(type);
                        break;

                    case 3:
                        getRefresh03(type);
                        break;

                    case 4:
                        getRefresh04(type);
                        break;
                }


                refreshlayout.finishRefresh();
            }
        });

        mSwipeRefreshLayout.setOnLoadmoreListener(new OnLoadmoreListener() {
            @Override
            public void onLoadmore(RefreshLayout refreshlayout) {


                switch (type){
                    case 1:
                        getLoad01(type,refreshlayout);
                        break;

                    case 2:
                        getLoad02(type,refreshlayout);
                        break;

                    case 3:
                        getLoad03(type,refreshlayout);
                        break;

                    case 4:
                        getLoad04(type,refreshlayout);
                        break;
                }
            }
        });


        rl_msg_app.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));
                if (isLogin){
                    Intent intent = new Intent(MyApp.getInstance(), MyToDoMsgActivity.class);
                    startActivity(intent);
                }else {
                    Intent intent = new Intent(MyApp.getInstance(), LoginActivity2.class);
                    startActivity(intent);
                }
            }
        });

        String home02 = (String) SPUtils.get(MyApp.getInstance(), "home02", "");
        if(home02.equals("")){
            setGuideView();
        }

    }

    private void setGuideView() {


        CircleShape circleShape = new CircleShape(10);
        circleShape.setPaint(LighterHelper.getDashPaint()); //set custom paint
        View tipView1 = getLayoutInflater().inflate(R.layout.fragment_home_gl, null);
        // 使用图片
        Lighter.with(getActivity()
        )
                .setBackgroundColor(0xB9000000)
                .setOnLighterListener(new OnLighterListener() {
                    @Override
                    public void onShow(int index) {


                    }

                    @Override
                    public void onDismiss() {
                        SPUtils.put(MyApp.getInstance(),"home02","1");
                    }
                })
                .addHighlight(new LighterParameter.Builder()
                        //.setHighlightedViewId(R.id.rl_msg_app)
                        .setHighlightedView(rl_msg_app)
                        .setTipLayoutId(R.layout.fragment_home_gl3)
                        //.setLighterShape(new RectShape(80, 80, 50))
                        //.setLighterShape(circleShape)
                        .setTipViewRelativeDirection(Direction.BOTTOM)
                        .setTipViewRelativeOffset(new MarginOffset(150, 0, 30, 0))
                        .build()).show();
    }

    private void remind() { //BadgeView的具体使用



        String count = (String) SPUtils.get(getActivity(), "count", "");
        if(count.equals("")){
            count = "0";
        }
        badge1.setText(count); // 需要显示的提醒类容
        badge1.setBadgePosition(BadgeView.POSITION_TOP_RIGHT);// 显示的位置.右上角,BadgeView.POSITION_BOTTOM_LEFT,下左，还有其他几个属性
        badge1.setTextColor(Color.WHITE); // 文本颜色
        badge1.setBadgeBackgroundColor(Color.RED); // 提醒信息的背景颜色，自己设置
        //badge1.setBackgroundResource(R.mipmap.icon_message_png); //设置背景图片
        badge1.setTextSize(10); // 文本大小
        badge1.setBadgeMargin(3, 3); // 水平和竖直方向的间距

        if(count.equals("0")){
            badge1.hide();
        }else {
            badge1.show();// 只有显示

        }

    }



    private void getLoad04(int type, final RefreshLayout refreshlayout) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",num)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){
                            List<HomeNewsBean.Obj> objMore = homeNewsBean.getObj();
                            if(objMore.size() > 0 ){
                                obj.addAll(objMore);
                                adapter2.notifyDataSetChanged();
                                num += 1;
                                refreshlayout.finishLoadmore();
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
                            }else {
                                ToastUtils.showToast(getActivity(),"暂无更多数据!");
                            }

                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void getLoad03(int type, final RefreshLayout refreshlayout) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",num)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){
                            List<HomeNewsBean.Obj> objMore = homeNewsBean.getObj();
                            if(objMore.size() > 0 ){
                                obj.addAll(objMore);
                                adapter2.notifyDataSetChanged();
                                num += 1;
                                refreshlayout.finishLoadmore();
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
                            }else {
                                ToastUtils.showToast(getActivity(),"暂无更多数据!");
                            }

                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void getRefresh04(final int type) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",1)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){

                            obj = homeNewsBean.getObj();
                            adapter2 = new NewsAdapter2(getActivity(),R.layout.list_item_new_news,obj, type);
                            recyclerView.setAdapter(adapter2);
                            num = 2;
                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void getRefresh03(final int type) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",1)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){

                            obj = homeNewsBean.getObj();
                            adapter2 = new NewsAdapter2(getActivity(),R.layout.list_item_new_news,obj, type);
                            recyclerView.setAdapter(adapter2);
                            num = 2;
                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void getLoad02(int type, final RefreshLayout refreshlayout) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",num)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){
                            List<HomeNewsBean.Obj> objMore = homeNewsBean.getObj();
                            if(objMore.size() > 0 ){
                                obj.addAll(objMore);
                                adapter2.notifyDataSetChanged();
                                num += 1;
                                refreshlayout.finishLoadmore();
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
                            }else {
                                ToastUtils.showToast(getActivity(),"暂无更多数据!");
                            }

                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void getRefresh02(final int type) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",1)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){

                            obj = homeNewsBean.getObj();
                            adapter2 = new NewsAdapter2(getActivity(),R.layout.list_item_new_news,obj, type);
                            recyclerView.setAdapter(adapter2);
                            num = 2;
                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private void getLoad01(int type, final RefreshLayout refreshlayout) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",num)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){
                            List<HomeNewsBean.Obj> objMore = homeNewsBean.getObj();
                            if(objMore.size() > 0 ){
                                obj.addAll(objMore);
                                adapter2.notifyDataSetChanged();
                                num += 1;
                                refreshlayout.finishLoadmore();
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
                            }else {
                                ToastUtils.showToast(getActivity(),"暂无更多数据!");
                            }

                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }
    List<HomeNewsBean.Obj> obj;
    private void getRefresh01(final int type) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",1)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){

                            obj = homeNewsBean.getObj();
                            adapter2 = new NewsAdapter2(getActivity(),R.layout.list_item_new_news,obj, type);
                            recyclerView.setAdapter(adapter2);
                            num = 2;
                        }


                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);


                    }
                });
    }

    private NewsAdapter2 adapter2;
    private void getNewsData(final int type) {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.findNewsUrl)
                .params("pageNum",1)
                .params("pageSize",25)
                .params("type",type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("news",response.body());
                        ViewUtils.cancelLoadingDialog();
                        HomeNewsBean homeNewsBean = JsonUtil.parseJson(response.body(),HomeNewsBean.class);
                        boolean success = homeNewsBean.getSuccess();
                        if(success){
                            obj = homeNewsBean.getObj();
                            adapter2 = new NewsAdapter2(getActivity(),R.layout.list_item_new_news,obj, type);
                            recyclerView.setAdapter(adapter2);
                            adapter2.setOnItemClickListener(new MultiItemTypeAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(View view, RecyclerView.ViewHolder holder, int position) {
                                    //Intent intent = new Intent(getActivity(),BaseWebActivity4.class);
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



    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (hidden) {   // 不在最前端显示 相当于调用了onPause();


        }else{  // 在最前端显示 相当于调用了onResume();
            isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));
            netInsertPortal("2");
            String count = (String) SPUtils.get(getActivity(),"count","");
            Log.e("count-------",count);
            //badge1.setText(count);

            if (!isLogin){
                badge1.hide();
            }else {
                netWorkSystemMsg();
                //remind(rl_msg_app);
            }


        }
    }

    private void netInsertPortal(final String insertPortalAccessLog) {
        String imei = MobileInfoUtils.getIMEI(getActivity());
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Four_Modules)
                .params("portalAccessLogMemberId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("portalAccessLogEquipmentId",(String) SPUtils.get(MyApp.getInstance(),"imei",""))//设备ID
                .params("portalAccessLogTarget", insertPortalAccessLog)//访问目标
                .params("portalAccessLogVersionNumber", (String) SPUtils.get(getActivity(),"versionName", ""))//版本号
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


    CountBean countBean1;
    private void netWorkSystemMsg() {

        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_countUnreadMessagesForCurrentUser)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s",response.toString());

                        countBean1 = JSON.parseObject(response.body(), CountBean.class);
                        //yy_msg_num.setText(countBean.getCount()+"");
                        netWorkOAToDoMsg();//OA待办

                    }
                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

    CountBean countBean2;
    /**OA消息列表*/
    private void netWorkOAToDoMsg() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type", "db")
                .params("workType", "workdb")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s",response.toString());

                        countBean2 = JSON.parseObject(response.body(), CountBean.class);
                        netWorkDyMsg();
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("sssssss",response.toString());
                    }
                });
    }

    CountBean countBean3;
    String count;
    private void netWorkDyMsg() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.Query_count)
                .params("userId",(String) SPUtils.get(MyApp.getInstance(),"userId",""))
                .params("type", "dy")
                .params("workType", "workdb")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Log.e("s",response.toString());

                        countBean3 = JSON.parseObject(response.body(), CountBean.class);

                        String s = countBean2.getCount() + Integer.parseInt(countBean1.getObj()) + countBean3.getCount() + "";


                        if(null == s){
                            s = "0";
                        }
                        SPUtils.put(MyApp.getInstance(),"count",s+"");

                        count = (String) SPUtils.get(getActivity(), "count", "");
                        if(!count.equals("") && !"0".equals(count)){
                            remind();
                            SPUtils.get(getActivity(),"count","");
                        }else {
                            badge1.hide();
                        }
                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);

                    }
                });
    }

}
