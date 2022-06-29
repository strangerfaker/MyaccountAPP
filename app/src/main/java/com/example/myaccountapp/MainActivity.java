package com.example.myaccountapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.example.myaccountapp.adapter.AccountAdapter;
import com.example.myaccountapp.db.AccountBean;
import com.example.myaccountapp.db.DBManager;
import com.example.myaccountapp.utils.BudgetDialog;
import com.example.myaccountapp.utils.MoreDialog;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    //展示今日收支情况
    ListView todayLv;
    ImageView searchIv;
    Button editBtn;
    ImageButton moreBtn;
    //声明数据源
    List<AccountBean>mDatas;
    AccountAdapter adapter;
    int year,month,day;

    //头布局相关控件
    View headerView;
    TextView topOutTv,topInTv,topbudgetTv,topConTv;
    ImageView topShowIv;
    //注意此处很可能忘记在 oncreate 中初始化 SharedPreferences
    SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化时间
        initTime();
        initView();
        //初始化SharedPreferences
        preferences = getSharedPreferences("budget", Context.MODE_PRIVATE);

        todayLv = findViewById(R.id.main_lv);
        //添加listview头布局
        addListViewHead();
        mDatas = new ArrayList<>();
        //设置适配器,加载每一行数据到列表之中
        adapter = new AccountAdapter(this,mDatas);
        todayLv.setAdapter(adapter);
    }

    /** 初始化自带的View的方法*/
    private void initView() {
        todayLv = findViewById(R.id.main_lv);
        editBtn = findViewById(R.id.main_btn_edit);
        moreBtn = findViewById(R.id.main_btn_more);
        searchIv = findViewById(R.id.main_iv_search);
        editBtn.setOnClickListener(this);
        moreBtn.setOnClickListener(this);
        searchIv.setOnClickListener(this);
        setLVLongClickListener();

    }

    private void setLVLongClickListener() {
        todayLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    return false;
                }
                int pos = position - 1;
                AccountBean clickBean = mDatas.get(pos);

                showDeleteItemDialog(clickBean);

                return false;
            }
        });
    }

    private void showDeleteItemDialog(final AccountBean clickBean) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("提示").setMessage("确定删除吗?")
                .setNegativeButton("取消",null)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        int click_id = clickBean.getId();
                        //删除操作,在DBmanger中实现
                        DBManager.deleteItemFromAccountById(click_id);
                        //此处需要删除整个bean,故进行优化,方法传入clickbean后再获取id
                        mDatas.remove(clickBean);
                        adapter.notifyDataSetChanged();
                        //改变头布局textview显示内容
                        setTopTvShow();

                    }
                });
        builder.create().show();   //显示对话框
    }

    //头布局
    private void addListViewHead() {
        //此处因为插入布局文件错误导致 出现以下错误
        // Attempt to invoke virtual method 'void android.widget.TextView.setOnClickListener
        // (android.view.View$OnClickListener)' on a null object reference
        // 应该插入
        headerView = getLayoutInflater().inflate(R.layout.item_mainlv_top, null);
        //设置头布局  使用listview中自带的头布局添加方法
        todayLv.addHeaderView(headerView);
        //查找头布局可用控件
        topOutTv = headerView.findViewById(R.id.item_mainlv_top_tv_out);
        topInTv = headerView.findViewById(R.id.item_mainlv_top_tv_in);
        topbudgetTv = headerView.findViewById(R.id.item_mainlv_top_tv_budget);
        topConTv = headerView.findViewById(R.id.item_mainlv_top_tv_day);
        topShowIv = headerView.findViewById(R.id.item_mainlv_top_iv_hide);

        //添加需要的点击按钮的响应
        topbudgetTv.setOnClickListener(this);
        headerView.setOnClickListener(this);
        topShowIv.setOnClickListener(this);

    }

    /* 获取今日的具体时间*/
    private void initTime() {
        Calendar calendar = Calendar.getInstance();
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH)+1;
        day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    // 当activity获取焦点时，会调用的方法
    @Override
    protected void onResume() {
        super.onResume();
        loadDBData();
        //显示头布局信息
        setTopTvShow();
    }

    /* 设置头布局当中文本内容的显示*/
    private void setTopTvShow() {
        float incomeOneDay = DBManager.getSumMoneyOneDay(year,month,day,1);
        float outcomeOneDay = DBManager.getSumMoneyOneDay(year,month,day,0);
        //将获取的 当天收入与支出 显示再 topConTv控件上
        String infoOneDay = "今日支出 ￥" + outcomeOneDay + "  收入 ￥" + incomeOneDay;
        topConTv.setText(infoOneDay);

        //获取本月收入与支出 显示在topInTv 与 topOutTv 上
        float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 1);
        float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
        topInTv.setText("￥"+incomeOneMonth);
        topOutTv.setText("￥"+outcomeOneMonth);

        //设置显示运算剩余
        float bmoney = preferences.getFloat("bmoney", 0);//预算
        if (bmoney == 0) {
            topbudgetTv.setText("￥ 0");
        }else{
            float syMoney = bmoney+incomeOneMonth-outcomeOneMonth;
            topbudgetTv.setText("￥"+syMoney);
        }

    }

    private void loadDBData() {
        //从数据库中获取当前时间集合 放入 list 中
        List<AccountBean> list = DBManager.getAccountListOneDayFromAccounttb(year, month, day);
        //将原数据清空否则会保存原数据
        mDatas.clear();
        mDatas.addAll(list);
        adapter.notifyDataSetChanged();
    }



    //设置点击事件操作
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.main_iv_search:
                Intent sr = new Intent(this, SearchActivity.class);  //跳转界面
                startActivity(sr);
                break;
            case R.id.main_btn_edit:
                Intent it1 = new Intent(this, RecordActivity.class);  //跳转界面
                startActivity(it1);
                break;
            case R.id.main_btn_more:
                MoreDialog moreDialog = new MoreDialog(this);
                moreDialog.show();
                moreDialog.setDialogSize();
                break;
            case R.id.item_mainlv_top_tv_budget:
                showBudgetDialog();
                break;
            case R.id.item_mainlv_top_iv_hide:
                // 切换TextView明文和密文
                toggleShow();
                break;
        }
        if (v == headerView) {
            //头布局被点击了
            Intent intent = new Intent();
            intent.setClass(this,MonthChartActivity.class);
            startActivity(intent);
        }
    }

    //显示预算方法
    private void showBudgetDialog() {
        BudgetDialog dialog = new BudgetDialog(this);
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BudgetDialog.OnEnsureListener() {
            @Override
            public void onEnsure(float money) {
                //将预算金额写入到共享参数当中，进行存储
                SharedPreferences.Editor editor = preferences.edit();
                editor.putFloat("bmoney",money);
                editor.commit();
                //计算剩余金额
                float incomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float outcomeOneMonth = DBManager.getSumMoneyOneMonth(year, month, 0);
                float syMoney = money+incomeOneMonth-outcomeOneMonth;//预算剩余 = 预算-支出
                topbudgetTv.setText("￥"+syMoney);


            }
        });
    }

    boolean isShow = true;
    //切换明文与密文
    private void toggleShow() {
        if (isShow) {   //明文====》密文
            //PasswordTransformationMethod.getInstance(); alt + enter 自动调用该系统自带的方法
            //PasswordTransformationMethod 是用于密码隐藏显示的方法
            PasswordTransformationMethod passwordMethod = PasswordTransformationMethod.getInstance();
            topInTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topOutTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(passwordMethod);   //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_hide);
            isShow = false;   //设置标志位为隐藏状态
        }else{  //密文---》明文
            //HideReturns 用于将隐藏显示的方法
            HideReturnsTransformationMethod hideMethod = HideReturnsTransformationMethod.getInstance();
            topInTv.setTransformationMethod(hideMethod);   //设置隐藏
            topOutTv.setTransformationMethod(hideMethod);   //设置隐藏
            topbudgetTv.setTransformationMethod(hideMethod);   //设置隐藏
            topShowIv.setImageResource(R.mipmap.ih_show);
            isShow = true;   //设置标志位为隐藏状态
        }
    }
}