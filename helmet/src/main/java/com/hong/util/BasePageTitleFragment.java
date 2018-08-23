package com.hong.util;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.hong.helmet.R;

/**
 * 通用 Fragment的基类
 */

public abstract class BasePageTitleFragment extends Fragment {
    private View mFragmentView;//父控件(由父控件找到子控件)
    private TextView mTitlePage;
    private FrameLayout mTitleContentPage;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.base_top_title_page, container, false);   //通用布局(图片)
        mTitlePage = (TextView) mFragmentView.findViewById(R.id.tv_title_page);
        mTitleContentPage = (FrameLayout) mFragmentView.findViewById(R.id.fl_title_content_page);
        View view = initView();
        mTitleContentPage.addView(view);
        return mFragmentView;
    }

    public void setTitleIcon(String msg, boolean show) {    //设置标题和图标
        mTitlePage.setText(msg);  //设置标题
        mTitlePage.setVisibility(show ? View.VISIBLE : View.GONE);     //设置图片显示  true就是显示  false就是不显示
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initData();
    }

    protected abstract View initView();
    protected abstract void initData();
}
