package com.hong.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hong.helmet.R;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener{
    //初始化fragment
    private FirstPageFragment mFirstPageFragment;
    private NearbyFragment mNearbyFragment;
    private CommunicationFragment mCommunicationFragment;
    private TrainingFragment mTrainingFragment;
    private PersonalFragment mPersonalFragment;

    //片段内容
    private FrameLayout mFragmentContent;
    //底部4个按钮
    private RelativeLayout mFirstPageLayout;
    private RelativeLayout mNearbyLayout;
    private RelativeLayout mCommunicationLayout;
    private RelativeLayout mTrainingLayout;
    private RelativeLayout mPersonalLayout;

    private ImageView mIvFirstPage,mIvNearby,mIvCommunication,mIvTraining,mIvPersonal;
    private TextView mTvFirstPage,mTvNearby,mTvCommunication,mTvTraining,mTvPersonal;
    private FragmentManager mFragmentManager;
    private FragmentTransaction mTransaction;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mFragmentManager = getSupportFragmentManager();
        initView();     //初始化视图
    }

    private void initView() {
        mFragmentContent = (FrameLayout) findViewById(R.id.home_fragment_content);
        mFirstPageLayout = (RelativeLayout) findViewById(R.id.home_first_page_layout);
        mIvFirstPage = (ImageView) findViewById(R.id.home_first_pic);
        mTvFirstPage = (TextView) findViewById(R.id.home_first_words);
        mNearbyLayout = (RelativeLayout) findViewById(R.id.home_nearby_layout);
        mIvNearby = (ImageView) findViewById(R.id.home_nearby_pic);
        mTvNearby = (TextView) findViewById(R.id.home_nearby_words);
        mCommunicationLayout = (RelativeLayout) findViewById(R.id.home_communication_layout);
        mIvCommunication = (ImageView) findViewById(R.id.home_communication_pic);
        mTvCommunication = (TextView) findViewById(R.id.home_communication_words);
        mTrainingLayout = (RelativeLayout) findViewById(R.id.home_training_layout);
        mIvTraining = (ImageView) findViewById(R.id.home_training_pic);
        mTvTraining = (TextView) findViewById(R.id.home_training_words);
        mPersonalLayout = (RelativeLayout) findViewById(R.id.home_personal_layout);
        mIvPersonal = (ImageView) findViewById(R.id.home_personal_pic);
        mTvPersonal = (TextView) findViewById(R.id.home_personal_words);
        //给五个按钮设置监听器
        mFirstPageLayout.setOnClickListener(this);
        mNearbyLayout.setOnClickListener(this);
        mCommunicationLayout.setOnClickListener(this);
        mTrainingLayout.setOnClickListener(this);
        mPersonalLayout.setOnClickListener(this);
        //默认第一个首页被选中高亮显示
        mFirstPageLayout.setSelected(true);
        mTransaction = mFragmentManager.beginTransaction();
        mTransaction.replace(R.id.home_fragment_content, new FirstPageFragment());
        mTransaction.commit();
    }

    @Override
    public void onClick(View v) {
        mTransaction = mFragmentManager.beginTransaction(); //开启事务
        hideAllFragment(mTransaction);
        switch (v.getId()){
            //首页
            case R.id.home_first_page_layout:
                seleted();
                mFirstPageLayout.setSelected(true);
                if (mFirstPageFragment == null) {
                    mFirstPageFragment = new FirstPageFragment();
                    mTransaction.add(R.id.home_fragment_content,mFirstPageFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mFirstPageFragment);
                }
                break;
            //附近
            case R.id.home_nearby_layout:
                seleted();
                mNearbyLayout.setSelected(true);
                if (mNearbyFragment == null) {
                    mNearbyFragment = new NearbyFragment();
                    mTransaction.add(R.id.home_fragment_content,mNearbyFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mNearbyFragment);
                }
                break;
            //交流
            case R.id.home_communication_layout:
                seleted();
                mCommunicationLayout.setSelected(true);
                if (mCommunicationFragment == null) {
                    mCommunicationFragment = new CommunicationFragment();
                    mTransaction.add(R.id.home_fragment_content,mCommunicationFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mCommunicationFragment);
                }
                break;
            //培训
            case R.id.home_training_layout:
                seleted();
                mTrainingLayout.setSelected(true);
                if (mTrainingFragment == null) {
                    mTrainingFragment = new TrainingFragment();
                    mTransaction.add(R.id.home_fragment_content,mTrainingFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mTrainingFragment);
                }
                break;
            //个人中心
            case R.id.home_personal_layout:
                seleted();
                mPersonalLayout.setSelected(true);
                if (mPersonalFragment == null) {
                    mPersonalFragment = new PersonalFragment();
                    mTransaction.add(R.id.home_fragment_content,mPersonalFragment);    //通过事务将内容添加到内容页
                }else{
                    mTransaction.show(mPersonalFragment);
                }
                break;
            default:
                break;
        }
        mTransaction.commit();
    }

    //设置所有按钮都是默认都不选中
    private void seleted() {
        mFirstPageLayout.setSelected(false);
        mNearbyLayout.setSelected(false);
        mCommunicationLayout.setSelected(false);
        mTrainingLayout.setSelected(false);
        mPersonalLayout.setSelected(false);
    }

    //隐藏所有fragment
    private void hideAllFragment(FragmentTransaction transaction) {
        if (mFirstPageFragment != null) {
            transaction.hide(mFirstPageFragment);
        }
        if (mNearbyFragment != null) {
            transaction.hide(mNearbyFragment);
        }
        if (mCommunicationFragment != null) {
            transaction.hide(mCommunicationFragment);
        }
        if (mTrainingFragment != null) {
            transaction.hide(mTrainingFragment);
        }
        if (mPersonalFragment != null) {
            transaction.hide(mPersonalFragment);
        }
    }
}
