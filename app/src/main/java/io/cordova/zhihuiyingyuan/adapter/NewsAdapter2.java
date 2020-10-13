package io.cordova.zhihuiyingyuan.adapter;

import android.content.Context;

import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

import io.cordova.zhihuiyingyuan.R;
import io.cordova.zhihuiyingyuan.bean.HomeNewsBean;

/**
 * Created by Administrator on 2019/4/17 0017.
 */

public class NewsAdapter2 extends CommonAdapter<HomeNewsBean.Obj> {
    Context mContext;
    int type = 0 ;
    public NewsAdapter2(Context context, int layoutId, List<HomeNewsBean.Obj> datas, int type) {
        super(context, layoutId, datas);
        mContext = context;
        this.type = type;
    }

    @Override
    protected void convert(ViewHolder holder, final HomeNewsBean.Obj s, int position) {

        holder.setText(R.id.tv_title,s.getNewsTitle());
        if(type == 1){
            holder.setText(R.id.tv_left,"公文公告");
        }else if(type == 2){
            holder.setText(R.id.tv_left,"综合新闻");
        }else if(type == 3){
            holder.setText(R.id.tv_left,"党办动态");
        }else if(type == 4){
            holder.setText(R.id.tv_left,"纪检动态");
        }

        holder.setText(R.id.tv_right,s.getNewsDate());

       /* holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MyApp.getInstance(), BaseWebActivity4.class);
                intent.putExtra("appUrl",s.getNewsHref());
                //intent.putExtra("appId","");
                intent.putExtra("appName",s.getNewsTitle());
                mContext.startActivity(intent);
            }
        });*/




    }

}
