package com.hong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.hong.helmet.R;
import com.hong.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;
import static com.hong.util.HttpUtils.sendPostWithHttpURLConnection;

public class LoginActivity extends AppCompatActivity {
    private final static String LOGIN_PATH = "api/login";
    private TextView register,findPwd,editAccount,editPassword;
    private Button mBtnLogin;
    private boolean isExit;
    private CheckBox rememberMe;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        OnClick onClick = new OnClick();
        register.setOnClickListener(onClick);
        findPwd.setOnClickListener(onClick);

        /* 点击登录 */
        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
                /*new Thread(runnable1).start();
                handler1.removeCallbacks(runnable1);*/
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        register = (TextView) findViewById(R.id.register);
        mBtnLogin = (Button)findViewById(R.id.btn_login);
        findPwd = (TextView)findViewById(R.id.find_password);
        rememberMe = (CheckBox)findViewById(R.id.remember);
        editAccount = (EditText)findViewById(R.id.edit_account);
        editPassword = (EditText)findViewById(R.id.edit_password);
    }

    /**
     * 登录线程
     */
    Handler handler1 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i(TAG, "请求结果:" + val);
        }
    };
    Runnable runnable1 = new Runnable() {
        private Map<String,String> params = new HashMap<>();
        @Override
        public void run() {
            // TODO: http request.
            Looper.prepare();
            Message msg = new Message();
            Bundle data = new Bundle();
            String account = editAccount.getText().toString().trim();
            String password = editPassword.getText().toString().trim();
            params.put("username",account);
            params.put("password",password);
            String jsonResult = sendPostWithHttpURLConnection(params,LOGIN_PATH,null,"utf-8");
            String isSuccess = getJsonSuccessValue(jsonResult);
            if("true".equals(isSuccess)){
                ToastUtils.showMsg(getApplicationContext(),"登录成功");
                Intent intent = new Intent(LoginActivity.this,HomeActivity.class);
                startActivity(intent);
            }else{
                ToastUtils.showMsg(getApplicationContext(),getJsonErrorMsgValue(jsonResult));
            }
            data.putString("value", getJsonSuccessValue(jsonResult));
            msg.setData(data);
            handler1.sendMessage(msg);
            Looper.loop();
        }
    };

        /* editAccount = findViewById(R.id.edit_account);
        editAccount.setKeyListener(new NumberKeyListener() {
            @NonNull

            *//* 返回希望被输入的字符，默认不允许输入 *//*
            @Override
            protected char[] getAcceptedChars() {
                char[] chars = {0,1,2,3,4,5,6,7,8,9};
                return chars;
            }

            *//*
     * 0：无键盘，键盘弹不出来
     * 1：英文键盘
     * 2：模拟键盘
     * 3：数字键盘
     * *//*
            @Override
            public int getInputType() {
                return 3;
            }
        });*/

    class OnClick implements View.OnClickListener{
        Intent intent = new Intent();
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.find_password:
                    intent.setClass(LoginActivity.this,FindPwdActivity.class);
                    startActivity(intent);
                    break;
                case R.id.register:
                    intent.setClass(LoginActivity.this,RegisterActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.login_quit:
                finish();
                break;
            default:
        }
        return true;
    }

    /**
     * 解析json中的success数据
     * @param jsonString 服务器传回的json数据
     * @return
     */
    public static String getJsonSuccessValue(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String  isSuccess = jsonObject.getString("success");
            System.out.println("success:"+isSuccess);
            return isSuccess;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解析json中的errorMsg数据
     * @param jsonString 服务器传回的json数据
     * @return
     */
    public static String getJsonErrorMsgValue(String jsonString) {
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String errorMsgIs = jsonObject.getString("errorMsg");
            System.out.println("errorMsg:" + errorMsgIs);
            return errorMsgIs;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 是否记住密码
     * @return
     */
    public boolean isTrue(){
        if(rememberMe.isSelected()){
            return true;
        }else{
            return false;
        }
    }

        /**
         * 双击返回键退出
         */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isExit) {
                this.finish();

            } else {
                Toast.makeText(this, "再按一次退出", Toast.LENGTH_SHORT).show();
                isExit = true;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        isExit = false;
                    }
                }, 2000);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
