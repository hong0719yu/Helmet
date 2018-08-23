package com.hong.activity;

import android.Manifest;
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

public class SiteLocationActivity extends AppCompatActivity implements View.OnClickListener{

    private ImageView mBackSiteLOcation;
    private TextView mPositionText;
    public LocationClient mLocationClient;
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
        setContentView(R.layout.activity_site_location);

        mBackSiteLOcation = (ImageView) findViewById(R.id.back_site_location);
        mPositionText = (TextView)findViewById(R.id.positoin_text_view);
        // 获取地图控件引用
        mMapView = (MapView)findViewById(R.id.site_location__map_view);
        mMapView.setVisibility(View.INVISIBLE);
        mBaiduMap = mMapView.getMap();
        mBaiduMap.setMyLocationEnabled(true);// 开启设备的位置在地图上显示功能
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);

        List<String> permissionList = new ArrayList<>();
        if(ContextCompat.checkSelfPermission(SiteLocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if(ContextCompat.checkSelfPermission(SiteLocationActivity.this,
                Manifest.permission.READ_PHONE_STATE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if(ContextCompat.checkSelfPermission(SiteLocationActivity.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if(!permissionList.isEmpty()){
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(SiteLocationActivity.this,permissions,1);
        }else{
            requestLocation();
        }

        mBackSiteLOcation.setOnClickListener(this);
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
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.back_site_location:
                finish();
                break;
            default:
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case 1:
                if(grantResults.length > 0){
                    for (int result : grantResults){
                        if(result != PackageManager.PERMISSION_GRANTED){
                            ToastUtils.showMsg(SiteLocationActivity.this,"必须同意所有权限才能使用本程序");
                            finish();
                            return;
                        }
                    }
                    requestLocation();
                }else {
                    ToastUtils.showMsg(SiteLocationActivity.this,"发生未知错误");
                }
                break;
            default:
                break;
        }
    }

    private class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(final BDLocation location) {
            if(location == null && mMapView == null){// 销毁后不在处理新接收的位置
                return ;
            }
            if(location.getLocType() == BDLocation.TypeGpsLocation
                    || location.getLocType() == BDLocation.TypeNetWorkLocation){
                navigateTo(location);
            }
            StringBuilder currentPosition = new StringBuilder();
            currentPosition.append("纬度：").append(location.getLatitude());
            currentPosition.append("经度：").append(location.getLongitude());
            currentPosition.append("定位精度：").append(location.getRadius());
            currentPosition.append("定位类型：").append(location.getLocType());
            currentPosition.append("国家：").append(location.getCountry());
            currentPosition.append("省：").append(location.getProvince());
            currentPosition.append("市：").append(location.getCity());
            currentPosition.append("区：").append(location.getDistrict());
            currentPosition.append("街道：").append(location.getStreet());
            currentPosition.append("定位方式：");
            if(location.getLocType() == BDLocation.TypeGpsLocation){
                currentPosition.append("GPS");
            }else if(location.getLocType() == BDLocation.TypeNetWorkLocation){
                currentPosition.append("网络");
            }
            mPositionText.setText(currentPosition);
        }
    }

}
