package com.hong.activity;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.hong.helmet.R;
import com.hong.util.BasePageTitleFragment;

/**
 * 首页
 */
public class FirstPageFragment extends BasePageTitleFragment implements View.OnClickListener{
    private ImageView mEnterSite,mNewSite;
    private Intent intent;

    @Override
    protected View initView() {
        setTitleIcon("",false); //设置第一个首页不显示标题
        View firstpageFragment = View.inflate(getContext(), R.layout.activity_first_page, null);
        mEnterSite = firstpageFragment.findViewById(R.id.enter_site);
        mNewSite = firstpageFragment.findViewById(R.id.new_site);
        mEnterSite.setOnClickListener(this);
        mNewSite.setOnClickListener(this);
        return firstpageFragment ;
    }

    @Override
    protected void initData() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.enter_site:
                intent = new Intent(getContext(),EnterSiteActivity.class);
                startActivity(intent);
                break;
            case R.id.new_site:
                intent = new Intent(getContext(),NewSiteMapActivity.class);
                startActivity(intent);
                break;
            default:
                break;
        }
    }
}
