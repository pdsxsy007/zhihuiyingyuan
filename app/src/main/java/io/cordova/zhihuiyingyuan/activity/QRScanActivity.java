package io.cordova.zhihuiyingyuan.activity;

import android.content.Intent;

import com.jwsd.libzxing.OnQRCodeListener;
import com.jwsd.libzxing.QRCodeManager;

import io.cordova.zhihuiyingyuan.R;
import io.cordova.zhihuiyingyuan.utils.BaseActivity;
import io.cordova.zhihuiyingyuan.utils.MyApp;
import io.cordova.zhihuiyingyuan.utils.SPUtils;
import io.cordova.zhihuiyingyuan.utils.StringUtils;
import io.cordova.zhihuiyingyuan.web.BaseWebActivity4;


/**
 * Created by Administrator on 2019/1/8 0008.
 */

public class QRScanActivity extends BaseActivity {
    boolean isLogin =false;

    @Override
    protected int getResourceId() {
        onScanQR();
        return R.layout.qr_scan_activity;

    }

    /**
     * 进入扫描二维码页面
     *
     * @param
     */
    public void onScanQR() {
        isLogin = !StringUtils.isEmpty((String) SPUtils.get(MyApp.getInstance(),"username",""));
        QRCodeManager.getInstance()
                .with(this)
                .setReqeustType(0)
                .setRequestCode(55846)
                .scanningQRCode(new OnQRCodeListener() {
                    @Override
                    public void onCompleted(String result) {
                        if(!isLogin){
                            Intent intent = new Intent(MyApp.getInstance(), LoginActivity2.class);
                            startActivity(intent);
                        }else {
                            Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                            intent.putExtra("appUrl",result);
                            intent.putExtra("scan","scan");
                            startActivity(intent);
                        }

                    }

                    @Override
                    public void onError(Throwable errorMsg) {

                    }

                    @Override
                    public void onCancel() {

                    }


                    @Override
                    public void onManual(int requestCode, int resultCode, Intent data) {

                    }
                });

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //注册onActivityResult
        QRCodeManager.getInstance().with(this).onActivityResult(requestCode, resultCode, data);
    }
}
