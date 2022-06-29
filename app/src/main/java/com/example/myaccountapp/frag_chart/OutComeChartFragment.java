package com.example.myaccountapp.frag_chart;

import android.graphics.Color;
import android.util.DisplayMetrics;
import android.util.Log;

import com.example.myaccountapp.db.BarChartItemBean;
import com.example.myaccountapp.db.ChartItemBean;
import com.example.myaccountapp.db.DBManager;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

public class OutComeChartFragment extends BaseChartFragment {
    int kind = 1;

    public void onResume() {
        super.onResume();
        loadData(year,month,kind);
    }

    @Override
    protected void setAxisData(int year, int month) {
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        int count = list.size();
//            System.out.println(count);
        int range = 100;
        PieData mPieData1 = getPieData1(count, range);
        showChart(pieChart, mPieData1);

    }

    public PieData getPieData1(int count, int range) {
        ArrayList<PieEntry> yValues = new ArrayList<PieEntry>();  //yVals用来表示封装每个饼块的实际数据
        List<ChartItemBean> list = DBManager.getChartListFromAccounttb(year, month, kind);
        PieDataSet pieDataSet = new PieDataSet(yValues, "收入种类"/*显示在比例图上*/);
        for (int i = 0; i < count; i++) {
//            xValues.add("Quarterly" + (i + 1));  //饼块上显示成list.size()个
            ChartItemBean itemBean = list.get(i);
            float ratio = itemBean.getRatio();
//            float pert = FloatUtils.ratio(ratio);
            yValues.add(new PieEntry(ratio, i));
//            Log.i("piechart", "insert : OK !");
        }

        // 饼图数据
        /**
         * 将一个饼形图分成四部分， 四部分的数值比例为14:14:34:38
         * 所以 14代表的百分比就是14%
         */
        //y轴的集合
        pieDataSet.setSliceSpace(0f); //设置个饼状图之间的距离

        ArrayList<Integer> colors = new ArrayList<Integer>();

        // 饼图颜色
        colors.add(Color.rgb(205, 205, 205));
        colors.add(Color.rgb(114, 188, 223));
        colors.add(Color.rgb(255, 123, 124));
        colors.add(Color.rgb(57, 135, 200));

        pieDataSet.setColors(colors);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float px = 5 * (metrics.densityDpi / 160f);
        pieDataSet.setSelectionShift(px); // 选中态多出的长度

        //设置描述的位置
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1Length(0.3f);//设置描述连接线长度
        //设置数据的位置
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart2Length(0.3f);//设置数据连接线长度
        //设置两根连接线的颜色
        pieDataSet.setValueLineColor(Color.WHITE);
        //设置数据的字体大小
        pieDataSet.setValueTextSize(10);

//        PieData pieData = new PieData(xValues, pieDataSet);
        PieData data = new PieData(pieDataSet);

        return data;
    }

    private void showChart(PieChart pieChart, PieData pieData) {
//        pieChart.setHoleColorTransparent(true);

        pieChart.setHoleRadius(50f);  //半径
        pieChart.setTransparentCircleRadius(65f); // 半透明圈
        pieChart.setNoDataText("暂无数据");// 如果没有数据的时候，会显示这个，类似ListView的EmptyView
        pieChart.setDrawCenterText(true);  //饼状图中间添加文字
        pieChart.setDrawHoleEnabled(true);
        pieChart.setRotationAngle(90); // 初始旋转角度
        pieChart.setRotationEnabled(true); // 可以手动旋转
        pieChart.setUsePercentValues(true);  //显示成百分比

        pieChart.setCenterText("本月总收入");  //饼状图中间的文字
        pieChart.setCenterTextColor(Color.rgb(185, 185, 156));
        pieChart.setCenterTextSize(25f);
        //设置描述的字体大小
        pieChart.setEntryLabelTextSize(20);



        //对于右下角一串字母的操作
        pieChart.getDescription().setEnabled(false);                  //是否显示右下角描述
        pieChart.getDescription().setText("这是修改那串英文的方法");    //修改右下角字母的显示
        pieChart.getDescription().setTextSize(20);                    //字体大小
        pieChart.getDescription().setTextColor(Color.RED);             //字体颜色

        //图例
        Legend legend=pieChart.getLegend();
        legend.setEnabled(true);    //是否显示图例
        legend.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);    //图例的位置

        //设置数据
        pieChart.setData(pieData);

        // undo all highlights
        Legend mLegend = pieChart.getLegend();  //设置比例图
//        mLegend.setPosition(LegendPosition.RIGHT_OF_CHART);  //最右边显示
        pieChart.animateXY(1000, 1000);  //设置动画
        // mChart.spin(2000, 0, 360);
    }






    public void setDate(int year, int month) {
        super.setDate(year, month);
        loadData(year,month,kind);
    }
}