package com.example.myaccountapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myaccountapp.R;
import com.example.myaccountapp.db.AccountBean;

import java.util.Calendar;
import java.util.List;

public class AccountAdapter extends BaseAdapter {
    //关联活动上下文
    Context context;
    //获取数据源
    List<AccountBean>mDatas;
    LayoutInflater inflater;
    int year,month,day;


    public AccountAdapter(Context context, List<AccountBean> mDatas) {
        this.context = context;
        this.mDatas = mDatas;

        //使用inflater获取上下文的布局文件信息,不再需要重复声明
        inflater = LayoutInflater.from(context);
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    //复写四种方法
    //    getCount : 要绑定的条目的数目，比如格子的数量
    //    getItem : 根据一个索引（位置）获得该位置的对象
    //    getItemId : 获取条目的 id
    //    getView : 获取该条目要显示的界面
    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //声明viewholder
        //使用holder 统一管理 convertveiw 中的控件, 无论convertveiw是否为空都能保证初始化声明
        ViewHolder holder = null;
        if (convertView == null) {

            //为空时加载item_mainlv布局
            //第一个参数：想要添加的布局
            //第二个参数：想要添加到哪个布局上面
            // null 和有值的区别 null 时第一个参数中最外层的布局大小无效，有值的时候最外层的布局大小有效）
            //第三个参数：是否直接添加到第二个参数布局上面
            // true 代表 layout 文件填充的 View 会被直接添加进 parent，而传入 false 则代表创建的 View 会以其他方式被添加进 parent）)
            // 推荐使用第四种方式 inflater.inflate (R.layout.item, parent, false);
            convertView = inflater.inflate(R.layout.item_mainlv,parent,false);
            //装入holder中加强鲁棒性
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }
        AccountBean bean = mDatas.get(position);
        holder.typeIv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getTypename());
        holder.beizhuTv.setText(bean.getBeizhu());
        holder.moneyTv.setText("￥ "+bean.getMoney());
        //判断是否为当前年月日,是则显示今天,否则直接显示具体时间 : 年月日与时分
        if (bean.getYear()==year&&bean.getMonth()==month&&bean.getDay()==day) {
            String time = bean.getTime().split(" ")[1];
            holder.timeTv.setText("今天"+time);
        }else {
            holder.timeTv.setText(bean.getTime());
        }

        return convertView;
    }

    //将布局页面每一个item传入
    class ViewHolder {
        ImageView typeIv;
        TextView typeTv, beizhuTv, timeTv, moneyTv;

        public ViewHolder(View view) {
            typeIv = view.findViewById(R.id.item_mainlv_iv);
            typeTv = view.findViewById(R.id.item_mainlv_tv_title);
            timeTv = view.findViewById(R.id.item_mainlv_tv_time);
            beizhuTv = view.findViewById(R.id.item_mainlv_tv_beizhu);
            moneyTv = view.findViewById(R.id.item_mainlv_tv_money);

        }
    }
}
