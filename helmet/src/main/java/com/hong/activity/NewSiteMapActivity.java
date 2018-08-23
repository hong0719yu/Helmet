package com.hong.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.hong.helmet.R;
import com.hong.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

public class NewSiteMapActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView mBackNewSite;
    private TextView mSiteFindWork;
    private LocationClient mLocationClient;
    private MyLocationListener myLocationListener = new MyLocationListener();
    private MapView mMapView;
    private BaiduMap mBaiduMap;
    private boolean mIsFirstLocation = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 在使用SDK各组件之前初始化context信息，接收Context参数，
        // 调用getApplicationContext来获取全局的Context
        // 该方法要在setContentView方法之前实现
        SDKInitializer.initialize(getApplicationContext());
        setContentView(R.layout.activity_new_site_map);
        // 获取地图控件引用
        mMapView = (MapView)findViewById(R.id.new_site_map_view);
        mBaiduMap = mMapView.getMap();
        mMapView.setVisibility(View.INVISIBLE);
        mBaiduMap.setMyLocationEnabled(true);// 开启设备的位置在地图上显示功能
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(NewSiteMapActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(NewSiteMapActivity.this,
                Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(NewSiteMapActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(NewSiteMapActivity.this,permissions,1);
        }else{
            requestLocation();
        }
        mBackNewSite = findViewById(R.id.back_new_site_map);
        mBackNewSite.setOnClickListener(this);
        mSiteFindWork = findViewById(R.id.site_find_work);
        mSiteFindWork.setOnClickListener(this);
    }

    private void requestLocation() {
        initLocation();
    }

    private void navigateTo(BDLocation location){
        if(mIsFirstLocation){
            LatLng latLng  = new LatLng(location.getLatitude(),location.getLongitude());
            MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
            mBaiduMap.animateMapStatus(mapStatusUpdate);
            mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16f);
            mBaiduMap.animateMapStatus(mapStatusUpdate);
            mIsFirstLocation = false;
            mMapView.setVisibility(View.VISIBLE);
            mSiteFindWork.setVisibility(View.VISIBLE);
        }
        MyLocationData.Builder locationBuilder = new MyLocationData.Builder();
        locationBuilder.accuracy(location.getRadius());// 此处设置开发者获取到的方向信息，顺时针0-360
        locationBuilder.latitude(location.getLatitude());
        locationBuilder.longitude(location.getLongitude());
        MyLocationData locationData = locationBuilder.build();
        mBaiduMap.setMyLocationData(locationData);
    }

    //实现地图生命周期管理
    private void initLocation() {
        mLocationClient = new LocationClient(getApplicationContext());
        mLocationClient.registerLocationListener(myLocationListener);
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);
        option.setScanSpan(1000);
        option.setIsNeedAddress(true);// 设置为true，表示需要当前位置的详细信息
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mLocationClient.unRegisterLocationListener(myLocationListener);
        mLocationClient.stop();
        mMapView.onDestroy();
        mBaiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for (int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            ToastUtils.showMsg(NewSiteMapActivity.this,"必须同意所有权限才能使用本程序");
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    ToastUtils.showMsg(NewSiteMapActivity.this,"发生未知错误");
                }
                break;
            default:
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_new_site_map:
                finish();
                break;
            case R.id.site_find_work:
                startActivity(new Intent(NewSiteMapActivity.this,LabourSiteActivity.class));
        }
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation location) {
            if(location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }
        }
    }
}
