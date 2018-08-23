package com.hong.activity;

import android.view.View;

import com.hong.helmet.R;
import com.hong.util.BasePageTitleFragment;

/**
 * 交流
 */

public class CommunicationFragment extends BasePageTitleFragment {
    @Override
    public View initView() {
        setTitleIcon("交流",true);
        View communicationFragment = View.inflate(getContext(), R.layout.activity_communication, null);
        return communicationFragment;
    }

    @Override
    public void initData() {

    }
}

