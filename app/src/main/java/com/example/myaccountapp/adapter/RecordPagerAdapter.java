package com.example.myaccountapp.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

//继承了 FragmentPagerAdapter 使用 alt + enter 重写其中的三个方法
public class RecordPagerAdapter extends FragmentPagerAdapter {

    //将集合返回于此
    List<Fragment>fragmentList;
    //联动标题头
    String[]titles = {"支出","收入"};

    public RecordPagerAdapter(@NonNull FragmentManager fm,List<Fragment>fragmentList){
        super(fm);
        this.fragmentList = fragmentList;
    }
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    public CharSequence getPageTitle(int position){
        return titles[position];
    }
}
