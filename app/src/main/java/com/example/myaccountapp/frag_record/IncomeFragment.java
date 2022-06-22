package com.example.myaccountapp.frag_record;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.example.myaccountapp.R;
import com.example.myaccountapp.db.DBManager;
import com.example.myaccountapp.db.TypeBean;

import java.util.List;

public class IncomeFragment extends BaseRecordFragment{

    //重写

    @Override
    public void loadDataTOGV() {
        super.loadDataTOGV();
        //获取数据库中的数据源
        List<TypeBean> inlist = DBManager.getTypeList(1);
        typeList.addAll(inlist);
        //提示数据库更新
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.in_qt_fs);

    }

    @Override
    public void saveAccoutToDB() {
        accountBean.setKind(1);
        DBManager.insertItemToAccounttb(accountBean);
    }
}