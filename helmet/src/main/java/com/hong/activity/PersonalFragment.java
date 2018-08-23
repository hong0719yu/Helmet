package com.hong.activity;

import android.view.View;

import com.hong.helmet.R;
import com.hong.util.BasePageTitleFragment;

/**
 * 个人中心
 */

public class PersonalFragment extends BasePageTitleFragment {
    @Override
    public View initView() {
        setTitleIcon("个人中心",true);
        View personalFragment = View.inflate(getContext(), R.layout.activity_personal, null);
        return personalFragment;
    }

    @Override
    public void initData() {

    }
}
