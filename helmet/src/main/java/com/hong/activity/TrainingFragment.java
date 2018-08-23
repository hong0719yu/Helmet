package com.hong.activity;

import android.view.View;

import com.hong.helmet.R;
import com.hong.util.BasePageTitleFragment;

/**
 * 培训
 */

public class TrainingFragment extends BasePageTitleFragment {
    @Override
    public View initView() {
        setTitleIcon("培训",true);
        View trainingFragment = View.inflate(getContext(), R.layout.activity_training, null);
        return trainingFragment;
    }

    @Override
    public void initData() {

    }
}
