package io.cordova.zhihuiyingyuan.jpushutil;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import io.cordova.zhihuiyingyuan.activity.MyShenqingActivity;
import io.cordova.zhihuiyingyuan.activity.OaMsgActivity;
import io.cordova.zhihuiyingyuan.activity.SystemMsgActivity;
import io.cordova.zhihuiyingyuan.utils.MyApp;
import io.cordova.zhihuiyingyuan.utils.SPUtils;
import io.cordova.zhihuiyingyuan.utils.StringUtils;
import io.cordova.zhihuiyingyuan.web.BaseWebActivity4;
import io.cordova.zhihuiyingyuan.web.BaseWebCloseActivity;

public class GeTuiNotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String message = intent.getStringExtra("message");
        Log.e("message",message);
        if(!message.equals("")){
            processCustomMessage(context,message);
        }

    }

    private void processCustomMessage(Context context, String message) {



        String extrasBean = message;

        String msgId = null;
        String msgType = null;

        if(!StringUtils.isEmpty(extrasBean)){
            try {
                JSONObject extraJson = new JSONObject(extrasBean);
                if (extraJson.length() > 0) {
                    Log.e("extraJson", extrasBean);

                    msgId =  extraJson.getString("messageId");
                    msgType =  extraJson.getString("messageType");
                    Log.e("msgId", msgId);
                    Log.e("msgType", msgType);
                    SPUtils.put(MyApp.getInstance(),"msgId",msgId);
                    SPUtils.put(MyApp.getInstance(),"msgType",msgType);

                }
            } catch (JSONException ignored) {
                Log.e("JPush",ignored.getMessage());
            }
        }

        /*当前页面后台隐藏*/
        if (!StringUtils.isEmpty(msgType)){
            Intent intent;
            if (msgType.equals("0")){
                intent = new Intent(MyApp.getInstance(), SystemMsgActivity.class);
                intent.putExtra("msgType","系统消息");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else if (msgType.equals("1")){
                intent = new Intent(MyApp.getInstance(), OaMsgActivity.class);
                intent.putExtra("type","db");
                intent.putExtra("msgType","待办消息");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else if (msgType.equals("2")){
                intent = new Intent(MyApp.getInstance(), OaMsgActivity.class);
                intent.putExtra("type","dy");
                intent.putExtra("msgType","待阅消息");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else if (msgType.equals("3")){
                intent = new Intent(MyApp.getInstance(), OaMsgActivity.class);
                intent.putExtra("type","yb");
                intent.putExtra("msgType","已办消息");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else if (msgType.equals("4")){
                intent = new Intent(MyApp.getInstance(), OaMsgActivity.class);
                intent.putExtra("type","yy");
                intent.putExtra("msgType","已阅消息");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else if (msgType.equals("5")){
                intent = new Intent(MyApp.getInstance(), MyShenqingActivity.class);
                intent.putExtra("type","sq");
                intent.putExtra("msgType","我的申请");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }else if (msgType.equals("6")){
                String isOpen = (String) SPUtils.get(MyApp.getInstance(), "isOpen", "");
                if(isOpen.equals("") || isOpen.equals("1")){
                    intent = new Intent(MyApp.getInstance(), BaseWebCloseActivity.class);
                }else {
                    intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                }
                intent.putExtra("appUrl","http://kys.zzuli.edu.cn/cas/login?service=http://mail.zzuli.edu.cn/coremail/cmcu_addon/sso.jsp?face=hxphone");
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
            SPUtils.remove(MyApp.getInstance(),"msgType");
            SPUtils.remove(MyApp.getInstance(),"msgId");

        }
    }
}
