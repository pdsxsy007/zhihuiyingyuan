package io.cordova.zhihuiyingyuan;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.TextView;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.Response;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.cordova.zhihuiyingyuan.jpushutil.Logger;
import io.cordova.zhihuiyingyuan.utils.MyApp;

/**
 * Created by Administrator on 2019/2/22 0022.
 */

public class MyTestActivity extends AppCompatActivity {
    @BindView(R.id.tv_title)
    TextView tvTitle;


    String type,msgType;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        overridePendingTransition(R.anim.bottom_in,R.anim.bottom_silent);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_jpush);
        ButterKnife.bind(this);

        initView();
        initData();
    }

    private void initData() {
        OkGo.<String>post(UrlRes.HOME_URL + UrlRes.jobTestUrl)
                .tag(this)
                .params("msg", MyApp.registrationId)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(Response<String> response) {
                        Logger.e("上传极光", response.body());




                    }

                    @Override
                    public void onError(Response<String> response) {
                        super.onError(response);
                        Log.e("onError", response.body());
                    }
                });
    }


    public void initView() {

        type = getIntent().getStringExtra("type");
        msgType = getIntent().getStringExtra("msgType");
        Log.e("type = ",type);
        tvTitle.setText(msgType);


    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent,R.anim.bottom_out);
    }



}
