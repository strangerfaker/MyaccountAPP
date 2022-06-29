package com.example.myaccountapp.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myaccountapp.R;
import com.example.myaccountapp.db.AccountBean;
import com.example.myaccountapp.db.TypeBean;
import com.example.myaccountapp.utils.BeiZhuDialog;
import com.example.myaccountapp.utils.KeyBoardUtils;
import com.example.myaccountapp.utils.SelectTimeDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
    记录页面中的支出模块
 */
public abstract class BaseRecordFragment extends Fragment implements View.OnClickListener{
    //声明页面内容
    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;
    List<TypeBean>typeList;
    TypeBaseAdapter adapter;
    AccountBean accountBean;    //插入数据库中的数据保存为对象形式

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        accountBean = new AccountBean();
        accountBean.setTypename("其他");
        accountBean.setsImageId(R.mipmap.ic_qita_fs);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        setInitTime();
        //调用GridView 填充数据的方法
        loadDataTOGV();
        //设置GridView每一项按键点击监听
        setGVListener();
        return view;
    }

    //获取当前时间,显示在timeTv
    private void setInitTime(){
        Date date = new Date();
        //转换
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = sdf.format(date);
        timeTv.setText(time);
        accountBean.setTime(time);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        //此处不+1会显示0月
        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        accountBean.setYear(year);
        accountBean.setMonth(month);
        accountBean.setDay(day);

    }

    //GridView每一项按键点击监听方法
    private void setGVListener() {
        typeGv.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                adapter.selectPos = position;
                adapter.notifyDataSetChanged(); //提示绘制发生了变化
                //设置 typeTv 为点击内容的名称
                TypeBean typeBean = typeList.get(position);
                String typename = typeBean.getTypename();
                typeTv.setText(typename);

                //获取图标id 显示为点击后的图片
                int simageId = typeBean.getSimgaeId();
                typeIv.setImageResource(simageId);

                //accouBean 对象也发生变化
                accountBean.setTypename(typename);
                accountBean.setsImageId(simageId);
            }
        });
    }

    //GridView 填充数据的方法
    public void loadDataTOGV() {
        typeList = new ArrayList<>();
        //new TypeBaseAdapter(getContext(), typeList).fie; 可以生成adapter
        adapter = new TypeBaseAdapter(getContext(), typeList);
        typeGv.setAdapter(adapter);

    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typeGv = view.findViewById(R.id.frag_record_gv);

        //重写了beizhudialog的窗口方法
        beizhuTv.setOnClickListener(this);
        timeTv.setOnClickListener(this);

        //自定义软键盘显示 (直接调用keyboard中的方法)
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView,moneyEt);
        boardUtils.showKeyboard();

        //设置接口,监听确定按钮被点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //点击确定按钮,获取金额
                String moneyStr = moneyEt.getText().toString();
                if (TextUtils.isEmpty(moneyStr)||moneyStr.equals("0")) {
                    getActivity().finish();
                    return;
                }
                float money = Float.parseFloat(moneyStr);
                accountBean.setMoney(money);


                //获取记录信息,存储到数据库
                saveAccoutToDB();

                //返回上一级页面
                getActivity().finish();
            }
        });
    }

    //多态化保存记账到数据库中  子类必须要重写该方法(更为严谨)
    //抽象方法必要添加抽象类故而将BaseRecordFragment 变为抽象类
    //子类必须具体实现父类抽象方法的一种提醒机制。
    public abstract void saveAccoutToDB();

    public void onClick(View v){
        switch (v.getId()){
            case R.id.frag_record_tv_time:
                showTimeDialog();
                break;
            case R.id.frag_record_tv_beizhu:
                showBZDialog();
                break;
        }
    }
    //弹出显示时间对话框
    private  void showTimeDialog(){
        SelectTimeDialog dialog = new SelectTimeDialog(getContext());
        dialog.show();
        //设置按钮点击监听
        dialog.setOnEnsureListener(new SelectTimeDialog.OnEnsureListener() {
            @Override
            public void onEnsure(String time, int year, int month, int day) {
                timeTv.setText(time);
                accountBean.setTime(time);
                accountBean.setYear(year);
                accountBean.setMonth(month);
                accountBean.setDay(day);
            }
        });
    }
    //弹出备注对话框
    private void showBZDialog() {
        final BeiZhuDialog dialog = new BeiZhuDialog(getContext());
        dialog.show();
        dialog.setDialogSize();
        dialog.setOnEnsureListener(new BeiZhuDialog.OnEnsureListener() {
            @Override
            public void onEnsure() {
                String msg = dialog.getEditText();
                if (!TextUtils.isEmpty(msg)) {
                    beizhuTv.setText(msg);
                    accountBean.setBeizhu(msg);
                }
                dialog.cancel();
            }
        });
    }

}