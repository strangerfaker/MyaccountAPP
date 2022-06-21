package com.example.myaccountapp.frag_record;

import android.inputmethodservice.KeyboardView;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myaccountapp.R;
import com.example.myaccountapp.utils.KeyBoardUtils;

/**
    记录页面中的支出模块
 */
public class OutcomeFragment extends Fragment {
    //声明页面内容
    KeyboardView keyboardView;
    EditText moneyEt;
    ImageView typeIv;
    TextView typeTv,beizhuTv,timeTv;
    GridView typeGv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_outcome, container, false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        keyboardView = view.findViewById(R.id.frag_record_keyboard);
        moneyEt = view.findViewById(R.id.frag_record_et_money);
        typeIv = view.findViewById(R.id.frag_record_iv);
        typeTv = view.findViewById(R.id.frag_record_tv_type);
        beizhuTv = view.findViewById(R.id.frag_record_tv_beizhu);
        timeTv = view.findViewById(R.id.frag_record_tv_time);
        typeGv = view.findViewById(R.id.frag_record_gv);

        //自定义软键盘显示 (直接调用keyboard中的方法)
        KeyBoardUtils boardUtils = new KeyBoardUtils(keyboardView,moneyEt);
        boardUtils.showKeyboard();

        //设置接口,监听确定按钮被点击
        boardUtils.setOnEnsureListener(new KeyBoardUtils.OnEnsureListener() {
            @Override
            public void onEnsure() {
                //点击确定按钮
                //获取记录信息,存储到数据库
                //返回上一级页面
            }
        });
    }


}