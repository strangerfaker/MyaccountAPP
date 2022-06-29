package com.example.myaccountapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myaccountapp.R;
import com.example.myaccountapp.db.ChartItemBean;
import com.example.myaccountapp.utils.FloatUtils;

import java.util.List;

public class ChartItemAdapter extends BaseAdapter {
    Context context;
    List<ChartItemBean> mDatats;
    LayoutInflater inflater;

    public ChartItemAdapter(Context context,List<ChartItemBean> mDatats) {
        this.context = context;
        this.mDatats = mDatats;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return mDatats.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatats.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder{
        TextView typeTv,ratioTv,totalTv;
        ImageView iv;
        public ViewHolder(View view){
            typeTv = view.findViewById(R.id.item_chartfrag_tv_type);
            ratioTv = view.findViewById(R.id.item_chartfrag_tv_pert);
            totalTv = view.findViewById(R.id.item_chartfrag_tv_sum);
            iv = view.findViewById(R.id.item_chartfrag_iv);
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.item_chartfrag_lv,parent,false);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder) convertView.getTag();
        }

        //显示内容
        ChartItemBean bean = mDatats.get(position);
        holder.iv.setImageResource(bean.getsImageId());
        holder.typeTv.setText(bean.getType());
        float ratio = bean.getRatio();
        String pert = FloatUtils.ratioToPercent(ratio);
        holder.ratioTv.setText(pert);
        holder.totalTv.setText("￥ "+bean.getTotalMoney());
        return convertView;


    }
}
