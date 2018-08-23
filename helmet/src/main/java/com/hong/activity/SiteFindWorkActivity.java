package com.hong.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hong.helmet.R;
import com.hong.util.ToastUtils;

public class SiteFindWorkActivity extends AppCompatActivity implements View.OnClickListener {

    private static final int MY_PERMISSIONS_REQUEST_CALL_PHONE = 1;
    private ImageView mBackSiteFindWork;
    private Button mSubmitApplication, mTelConsultatoin;
    private TextView mSiteFindWorkContactPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_site_find_work);

        mBackSiteFindWork = (ImageView) findViewById(R.id.back_site_find_work);
        mBackSiteFindWork.setOnClickListener(this);
        mSubmitApplication = findViewById(R.id.site_find_work_submit_application);
        mSubmitApplication.setOnClickListener(this);
        mTelConsultatoin = (Button) findViewById(R.id.site_find_work_tel_consultatoin);
        mTelConsultatoin.setOnClickListener(this);
        mSiteFindWorkContactPhone = (TextView)findViewById(R.id.site_find_work_contact_phone);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_site_find_work:
                finish();
                break;
            case R.id.site_find_work_tel_consultatoin:
                isHasPermission();
                break;
            default:
                break;
        }
    }

    public void isHasPermission(){
        String phoneNumber = (String) mSiteFindWorkContactPhone.getText();
        // 检查是否获得了权限（Android6.0运行时权限）
        if (ContextCompat.checkSelfPermission(SiteFindWorkActivity.this,
                Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED){
            // 没有获得授权，申请授权
            if (ActivityCompat.shouldShowRequestPermissionRationale(SiteFindWorkActivity.this,
                    Manifest.permission.CALL_PHONE)) {
                /*
                 * 返回值：
                 *       如果app之前请求过该权限,被用户拒绝, 这个方法就会返回true.
                 *       如果用户之前拒绝权限的时候勾选了对话框中”Don’t ask again”的选项,那么这个方法会返回false.
                 *       如果设备策略禁止应用拥有这条权限, 这个方法也返回false.
                 *       弹窗需要解释为何需要该权限，再次请求授权
                 * */
                ToastUtils.showMsg(SiteFindWorkActivity.this,"请授权！");
                // 帮跳转到该应用的设置界面，让用户手动授权
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
            }else{
                // 不需要解释为何需要该权限，直接请求授权
                ActivityCompat.requestPermissions(SiteFindWorkActivity.this,
                        new String[]{Manifest.permission.CALL_PHONE},
                        MY_PERMISSIONS_REQUEST_CALL_PHONE);
            }
        }else {
            // 已经获得授权，可以打电话
            callPhone(phoneNumber);
        }
    }

    public void callPhone(String phoneNumber) {
        // 拨号：激活系统的拨号组件
        Intent intent = new Intent(); // 意图对象：动作 + 数据
        intent.setAction(Intent.ACTION_CALL); // 设置动作
        Uri data = Uri.parse("tel:" + phoneNumber); // 设置数据
        intent.setData(data);
        startActivity(intent); // 激活Activity组件

    }

    /**
     * 动态请求拨打电话权限后，监听用户的点击事件
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,String[] permissions, int[] grantResults) {
        String phoneNumber = (String) mSiteFindWorkContactPhone.getText();
        switch (requestCode){
            case MY_PERMISSIONS_REQUEST_CALL_PHONE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // 授权成功，继续打电话
                    callPhone(phoneNumber);
                } else {
                    // 授权失败！
                    Toast.makeText(this, "授权失败！", Toast.LENGTH_LONG).show();
                }
                break;
            }
        }
    }

}
