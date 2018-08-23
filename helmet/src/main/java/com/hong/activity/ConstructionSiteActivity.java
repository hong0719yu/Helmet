package com.hong.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.hong.helmet.R;

public class ConstructionSiteActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackConSite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_construction_site);

        mBackConSite = (ImageView) findViewById(R.id.construction_site_back);
        mBackConSite.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.construction_site_back:
                finish();
                break;
            default:
                break;
        }
    }
}
