package com.hong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.hong.helmet.R;
import com.hong.pojo.LabourSite;
import com.hong.service.LabourSiteAdapter;

import java.util.ArrayList;
import java.util.List;

public class LabourSiteActivity extends AppCompatActivity implements View.OnClickListener {

    private List<LabourSite> labourSiteList = new ArrayList<>();
    private ImageView mBackLabourSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_site);

        initLabourSites();//初始化用人工地数据
        RecyclerView recyclerView = findViewById(R.id.labour_site_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        LabourSiteAdapter labourSiteAdapter = new LabourSiteAdapter(labourSiteList);
        recyclerView.setAdapter(labourSiteAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_labour_site:
                finish();
                break;
            case R.id.labour_site_project_name:
                startActivity(new Intent(LabourSiteActivity.this,LabourSiteActivity.class));
            default:
                break;
        }
    }

    private void initLabourSites() {

        mBackLabourSite = (ImageView)findViewById(R.id.back_labour_site);
        mBackLabourSite.setOnClickListener(this);

        LabourSite labourSite1 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite1);
        LabourSite labourSite2 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite2);
        LabourSite labourSite3 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite3);
        LabourSite labourSite4 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite4);
        LabourSite labourSite5 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite5);
        LabourSite labourSite6 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite6);
        LabourSite labourSite7 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite7);
        LabourSite labourSite8 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite8);
        LabourSite labourSite9 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite9);
        LabourSite labourSite10 = new LabourSite("恒大御景湖2期","53","80","8.0");
        labourSiteList.add(labourSite10);
    }

}
