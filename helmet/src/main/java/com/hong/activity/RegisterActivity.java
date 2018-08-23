package com.hong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

import com.hong.helmet.R;
import com.hong.util.HttpUtils;
import com.hong.util.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static android.content.ContentValues.TAG;

public class RegisterActivity extends AppCompatActivity {
    //private final static String PATH="http://file.sxycy.cn:84/";
    private final static String SEND_SM_PATH = "api/sms/bindphone/";
    private final static String CHECK_CODE = "api/sms/checkcode/";
    private final static String REGISTER_PATH = "api/sysuser/insert/";
    private final static String CHECK_PHONE = "api/sysuser/checkphonehasreg";
    private Integer workType;
    private ImageView back;
    private LinearLayout registerNow;
    private Button getCode;
    private EditText phoneNumber, registerPwd, validateCode;
    private RadioButton btn_foreman,btn_worker;
    private CheckBox checkbox;
    private TextView registerWord;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        getCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable1).start();
                handler1.removeCallbacks(runnable1);
            }
        });
        registerNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegisterActivity.this,CertificationWorkerActivity.class);
                startActivity(intent);
                /*new Thread(runnable2).start();
                handler2.removeCallbacks(runnable2);*/
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        back = (ImageView) findViewById(R.id.back);
        phoneNumber = (EditText) findViewById(R.id.phone_number);
        validateCode = (EditText) findViewById(R.id.verification_code);
        getCode = (Button) findViewById(R.id.button_get_code);
        registerPwd = (EditText) findViewById(R.id.register_pwd);
        btn_foreman = (RadioButton) findViewById(R.id.radio_foreman);
        btn_worker = (RadioButton) findViewById(R.id.radio_work);
        checkbox = (CheckBox) findViewById(R.id.checkbox);
        registerNow = (LinearLayout) findViewById(R.id.register_now);
        registerWord = (TextView) findViewById(R.id.register_now_word);
    }

    /**
     * 获取验证码线程
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
        @Override
        public void run() {
            // TODO: http request.
            Looper.prepare();
            Message msg = new Message();
            Bundle data = new Bundle();
            String phone = phoneNumber.getText().toString().trim();
            String result = null;
            String value = null;
            if(isValidPhone(phone)){
                result = HttpUtils.sendGetWithHttpURLConnection(phone,SEND_SM_PATH,"utf-8");
                value = getJsonSuccessValue(result);
                if("true".equals(value)){
                    ToastUtils.showMsg(getApplicationContext(),"验证码已发送，请查收");
                }else if("false".equals(value)){
                    ToastUtils.showMsg(getApplicationContext(),getJsonErrorMsgValue(result));
                }
            }else{
                value = "手机号已注册";
            }
            data.putString("value",value);
            msg.setData(data);
            handler1.sendMessage(msg);
            Looper.loop();
        }
    };

    /**
     * 注册线程
     */
    Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            Bundle data = msg.getData();
            String val = data.getString("value");
            Log.i(TAG, "请求结果:" + val);
        }
    };
    Runnable runnable2 = new Runnable() {
        @Override
        public void run() {
            // TODO: http request.
            Looper.prepare();
            Message msg = new Message();
            Bundle data = new Bundle();
            String phone = phoneNumber.getText().toString().trim();
            String password = registerPwd.getText().toString().trim();
            String code = validateCode.getText().toString().trim();
            String result = null;
            String value = null;
            if(isRegister(code)){
                Map<String,String> params = new HashMap<>();
                params.put("phone",phone);
                params.put("mima",password);
                params.put("workType",String.valueOf(workType));
                result = HttpUtils.sendPostWithHttpURLConnection(params,REGISTER_PATH,code,"utf-8");
                value = getJsonSuccessValue(result);
                if("true".equals(value)){
                    ToastUtils.showMsg(getApplicationContext(),"注册成功");
                    Intent intent = null;
                    if(workType == 1){
                        intent = new Intent(RegisterActivity.this, CertificationWorkerActivity.class);
                    }else if(workType == 2){
                        intent = new Intent(RegisterActivity.this, CertificationForemanActivity.class);
                    }
                    startActivity(intent);
                }else if("false".equals(value)){
                    ToastUtils.showMsg(getApplicationContext(),"注册失败");
                }
            }else{
                value = "注册有误";
            }
            data.putString("value", value);
            msg.setData(data);
            handler2.sendMessage(msg);
            Looper.loop();
        }
    };

    /**
     * 检验手机号是否有效
     * @param phone  手机号
     * @return
     */
    public boolean isValidPhone(String phone){
        Map<String,String> params = new HashMap<>();
        params.put("phone",phone);
        String result = HttpUtils.sendGetMessage(params,null,CHECK_PHONE,"utf-8");
        getJsonResValue(result);
        if("false".equals(getJsonResValue(result))){
            return true;
        }else if("true".equals(getJsonResValue(result))){
            ToastUtils.showMsg(getApplicationContext(),"手机号已注册");
            return false;
        }
        return false;
    }

    /**
     * 检验验证码是否有效
     * @param code  验证码
     * @return
     */
    public boolean isValidCode(String code){
        String result = HttpUtils.sendGetMessageCheckCode(code, CHECK_CODE, "utf-8");
        if("true".equals(getJsonSuccessValue(result))){
            ToastUtils.showMsg(getApplicationContext(),"验证码验证成功");
            return true;
        }else{
            ToastUtils.showMsg(getApplicationContext(),"验证码验证失败，请重试！");
            return false;
        }
    }

    /**
     * 密码是否为空
     * @return
     */
    public boolean isPasswordEmpty(){
        if(registerPwd.getText().toString().trim().length()!=0){
            return true;
        }else{
            ToastUtils.showMsg(getApplicationContext(),"密码不能为空");
            return false;
        }
    }

    /**
     * 是否选择所属职位
     * @return  true or false
     */
    public boolean isSelectedType(){
        if(btn_worker.isChecked()){
            workType = 1;
            return true;
        }else if(btn_foreman.isChecked()){
            workType = 2;
            return true;
        }else{
            ToastUtils.showMsg(getApplicationContext(),"未选择职位");
            return false;
        }
    }

    /**
     * 是否同意协议
     * @return  true or false
     */
    public boolean isSelectedAgreement(){
        if(checkbox.isChecked()){
            return true;
        }else{
            ToastUtils.showMsg(getApplicationContext(),"未同意协议");
            return false;
        }
    }

    /**
     * 是否可以注册
     * @param code  填写的验证码
     * @return
     */
    public boolean isRegister(String code){
        /*if(isPasswordEmpty()&&isValidCode(code)&&isSelectedType()&&isSelectedAgreement()){*/
        if(isPasswordEmpty()&&isSelectedType()&&isSelectedAgreement()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * 解析json中的res数据
     * @param jsonString 服务器传回的json数据
     * @return
     */
    public static String getJsonResValue(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String  resIs = jsonObject.getString("res");
            System.out.println("res:"+resIs);
            return resIs;
        } catch (JSONException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 解析json中的success数据
     * @param jsonString 服务器传回的json数据
     * @return
     */
    public static String getJsonSuccessValue(String jsonString){
        try {
            JSONObject jsonObject = new JSONObject(jsonString);
            String  successIs = jsonObject.getString("success");
            System.out.println("success:"+successIs);
            return successIs;
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
}