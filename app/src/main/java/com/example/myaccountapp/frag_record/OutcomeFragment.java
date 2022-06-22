package com.example.myaccountapp.frag_record;

import com.example.myaccountapp.R;
import com.example.myaccountapp.db.DBManager;
import com.example.myaccountapp.db.TypeBean;

import java.util.List;

public class OutcomeFragment extends BaseRecordFragment{

    //重写
    @Override
    public void loadDataTOGV() {
        super.loadDataTOGV();
        //获取数据库中的数据源
        List<TypeBean> outlist = DBManager.getTypeList(0);
        typeList.addAll(outlist);
        //提示数据库更新
        adapter.notifyDataSetChanged();
        typeTv.setText("其他");
        typeIv.setImageResource(R.mipmap.ic_qita_fs);
    }

    @Override
    public void saveAccoutToDB() {
        //检查 BaseRecordFragment 后AccountBean只有备注与kind未设置
        accountBean.setKind(0);
        DBManager.insertItemToAccounttb(accountBean);
    }
}
