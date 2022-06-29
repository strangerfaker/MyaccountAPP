package com.example.myaccountapp.frag_chart;

import android.graphics.Color;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;

import com.example.myaccountapp.R;
import com.example.myaccountapp.adapter.ChartItemAdapter;
import com.example.myaccountapp.db.ChartItemBean;
import com.example.myaccountapp.db.DBManager;
import com.example.myaccountapp.utils.FloatUtils;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

abstract public class BaseChartFragment extends Fragment {
    ListView chartLv;
    int year;
    int month;
    List<ChartItemBean>mDatas;
    private ChartItemAdapter itemAdapter;

    PieChart pieChart;     //代表饼图的控件
    TextView chartTv;     //如果没有收支情况，显示的TextView



    public void onResume() {

        super.onResume();
        loadData(year,month,0);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_income_chart,container,false);
        chartLv = view.findViewById(R.id.frag_chart_lv);

        Bundle bundle = getArguments();
        year = bundle.getInt("year");
        month = bundle.getInt("month");

        mDatas = new ArrayList<>();

        //设置 在frag_chart_lv显示数据的适配器 ChartItemAdapter
        itemAdapter = new ChartItemAdapter(getContext(), mDatas);
        chartLv.setAdapter(itemAdapter);

        //添加头布局
        addLVHeaderView();
        return view;
    }

    private void addLVHeaderView() {
        View headerView = getLayoutInflater().inflate(R.layout.item_chart_frag_pie,null);
        chartLv.addHeaderView(headerView);
        pieChart = headerView.findViewById(R.id.pie_chart);
        chartTv = headerView.findViewById(R.id.item_chartfrag_top_tv1);
        //获取数据
        setAxisData(year,month);

////            int year = 2022;
////            int month = 6;
//            List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, 0);
//            int count = list.size();
////            System.out.println(count);
//            int range = 100;
//            PieData mPieData1 = getPieData1(count, range);
//        showChart(pieChart, mPieData1);
    }

    protected abstract void setAxisData(int year, int month);

//    protected abstract void setData(int year, int month);



    public void loadData(int year, int month, int kind) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        mDatas.clear();
        mDatas.addAll(list);
        itemAdapter.notifyDataSetChanged();

    }

    public void setDate(int year,int month) {
        this.year = year;
        this.month = month;
        // 清空柱状图当中的数据
        pieChart.clear();
        pieChart.invalidate();  //重新绘制柱状图

        setAxisData(year,month);
    }
}
