package com.hong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.hong.helmet.R;
import com.hong.pojo.Site;
import com.hong.service.SiteAdapter;

import java.util.ArrayList;
import java.util.List;

public class EnterSiteActivity extends AppCompatActivity implements View.OnClickListener{

    // 定义List来接收传入的工地数据
    private List<Site> siteList = new ArrayList<>();
    // 定义属性接收布局中的实例
    private ImageView mBackMySites,mGarbageBin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_enter_site);
        initSites();//初始化工地数据
        // 找到RecyclerView布局实例
        RecyclerView recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        // 网格布局,构造函数第一个参数指定context上下文，传入当前活动即可；第二个参数指定布局的列数
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(),2);
        // 瀑布流布局，构造函数第一个参数指定布局的列数，第二个参数指定布局的排列方向
        //StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL);
        // 线性布局
       // LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        // 默认是纵向排列
        //layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);//设置横向排列
        //调用RecyclerView的setLayoutManager来给RecyclerView设置布局
        recyclerView.setLayoutManager(gridLayoutManager);
        // 通过适配器类SiteAdapter的构造函数将数据源传进去
        SiteAdapter siteAdapter = new SiteAdapter(EnterSiteActivity.this,siteList);
        // 调用RecyclerView的setAdapter来完成适配器设置，这样RecyclerView和数据之间的关联就建成了
        recyclerView.setAdapter(siteAdapter);
    }

    private void initSites() {
        mBackMySites = (ImageView)findViewById(R.id.back_enter_site);
        mGarbageBin = (ImageView)findViewById(R.id.my_garbage_bin);
        mBackMySites.setOnClickListener(this);
        mGarbageBin.setOnClickListener(this);

        //通过定义的Site来初始化所有工地数据
        Site site1 = new Site(R.drawable.site_pic_1,R.drawable.location,"F0001","滨河大道一号");
        siteList.add(site1);
        Site site2 = new Site(R.drawable.site_pic_2,R.drawable.location,"F0002","滨河大道二号");
        siteList.add(site2);
        Site site3 = new Site(R.drawable.site_pic_3,R.drawable.location,"F0003","滨河大道三号");
        siteList.add(site3);
        Site site4 = new Site(R.drawable.site_pic_4,R.drawable.location,"F0004","滨河大道四号");
        siteList.add(site4);
        Site site5 = new Site(R.drawable.site_pic_5,R.drawable.location,"F0005","滨河大道五号");
        siteList.add(site5);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_enter_site:
                finish();
                break;
            case R.id.my_garbage_bin:
                break;
            default:
                break;
        }
    }
}