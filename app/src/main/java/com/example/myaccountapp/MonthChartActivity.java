package com.example.myaccountapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.myaccountapp.adapter.CalendarAdapter;
import com.example.myaccountapp.adapter.ChartVPAdapter;
import com.example.myaccountapp.db.DBManager;
import com.example.myaccountapp.frag_chart.InComeChartFragment;
import com.example.myaccountapp.frag_chart.OutComeChartFragment;
import com.example.myaccountapp.frag_record.IncomeFragment;
import com.example.myaccountapp.frag_record.OutcomeFragment;
import com.example.myaccountapp.utils.CalendarDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MonthChartActivity extends AppCompatActivity {
    int year,month;
    TextView tvdate,tvin,tvout;
    ViewPager chartVp;
    Button btnin,btnout;
    private int selectPos = -1,selectMonth = -1;
    List<Fragment> charFraglist;
    private InComeChartFragment incomChartFragment;
    private OutComeChartFragment outcomChartFragment;

    private ChartVPAdapter chartVPAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_month_chart);
        initView();
        initTime();
        initStatistics(year,month);
        initFrag();
        //切换fragment联动按钮,切换选中日期,fragitemlist也联动
        setVPSelectListener();
    }

    private void setVPSelectListener() {
        chartVp.addOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener(){
            @Override
            public void onPageSelected(int position) {
                setButtonStyle(position);
            }
        });
    }

    private void initFrag() {
        charFraglist = new ArrayList<>();

        incomChartFragment = new InComeChartFragment();
        outcomChartFragment = new OutComeChartFragment();
        //使用bundle打包数据(便于活动与fragment传值)再添加于fragment
        Bundle bundle = new Bundle();
        bundle.putInt("year",year);
        bundle.putInt("month",month);
        incomChartFragment.setArguments(bundle);
        outcomChartFragment.setArguments(bundle);
        //添加到 list 中
        charFraglist.add(incomChartFragment);
        charFraglist.add( outcomChartFragment);

        //使用适配器显示
        chartVPAdapter = new ChartVPAdapter(getSupportFragmentManager(), charFraglist);
        chartVp.setAdapter(chartVPAdapter);

        //fragment 加载到activity

    }

    private void initStatistics(int year, int month) {
        float inMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outMoneyOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        int incountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 1);
        int outcountItemOneMonth = DBManager.getCountItemOneMonth(year, month, 0);
        tvdate.setText(year+"年"+month+"月账单");
        tvin.setText("共"+incountItemOneMonth+"笔收入, ￥"+inMoneyOneMonth);
        tvout.setText("共"+outcountItemOneMonth+"笔收入, ￥"+outMoneyOneMonth);
    }

    private void initView() {
        btnin = findViewById(R.id.chart_btn_in);
        btnout = findViewById(R.id.chart_btn_out);
        tvdate = findViewById(R.id.chart_tv_date);
        tvin = findViewById(R.id.chart_tv_in);
        tvout = findViewById(R.id.chart_tv_out);
        chartVp = findViewById(R.id.chart_vp);
    }

    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
    }

    public void onClick(View view) {
        switch (view.getId()){
            case R.id.chart_btn_out:
                setButtonStyle(0);
                chartVp.setCurrentItem(0);
                break;
            case R.id.chart_btn_in:
                setButtonStyle(1);
                chartVp.setCurrentItem(1);
                break;
            case R.id.chart_iv_back:
                finish();
                break;
            case R.id.chart_rili:
                showCalendarDialog();
                break;
        }
    }

    private void showCalendarDialog() {
        //组件重载
        CalendarDialog dialog = new CalendarDialog(this,selectPos,selectMonth);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnRefreshListener(new CalendarDialog.OnRefreshListener() {
            @Override
            public void onRefresh(int selPos, int year, int month) {
                MonthChartActivity.this.selectPos = selPos;
                MonthChartActivity.this.selectMonth = month;
                initStatistics(year, month);
                //联动时间设置,改变listitem
                incomChartFragment.setDate(year,month);
                outcomChartFragment.setDate(year,month);
            }
        });
    }

    private void setButtonStyle(int i) {
        if (i == 0) {
            btnout.setBackgroundResource(R.drawable.main_record_btn_bg);
            btnout.setTextColor(Color.WHITE);
            btnin.setBackgroundResource(R.drawable.dialog_btn_bg);
            btnin.setTextColor(Color.BLACK);
        }else if (i == 1){
            btnin.setBackgroundResource(R.drawable.main_record_btn_bg);
            btnin.setTextColor(Color.WHITE);
            btnout.setBackgroundResource(R.drawable.dialog_btn_bg);
            btnout.setTextColor(Color.BLACK);
        }
    }
}