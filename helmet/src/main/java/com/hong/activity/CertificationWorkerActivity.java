package com.hong.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hong.helmet.R;
import com.hong.util.NameUtils;
import com.hong.util.ToastUtils;
import com.hong.util.HttpUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CertificationWorkerActivity extends AppCompatActivity{
    //private static final String UP_LOAD ="api/fileupload/img";
    // 拍照回传码
    private static final int CODE_TAKE_PHOTO = 1;
    // 相册选择回传码
    private static final int CODE_CHOOSE_PHOTO = 2;
    private Button mTakeFront,mTakeBack,mChooseFrontPhoto,mChooseBackPhoto;
    private ImageView mIDCardFront,mIDCardBack,back;
    private TextView mSubmitAuthentication;
    private EditText mWorkerName,mWorkerIDCode;
    private LinearLayout jump;
    private Uri imageUri;
    private Integer flag;
    private String imgPath;
    private String result;
    // 存放拍照完后的图片路径
    private List<String> imgs = new ArrayList();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verification_worker);
        initView();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CertificationWorkerActivity.this,HomeActivity.class);
                startActivity(intent);
            }
        });
        mTakeFront.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=1;
                camera();
            }
        });
        mTakeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag=2;
                camera();
            }
        });
        mChooseFrontPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 3;
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CODE_CHOOSE_PHOTO);
            }
        });
        mChooseBackPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = 4;
                Intent intent = new Intent(Intent.ACTION_PICK,
                        android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, CODE_CHOOSE_PHOTO);
            }
        });

        mSubmitAuthentication.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new Thread(runnable).start();
            }
        });
    }

    /**
     * 初始化视图
     */
    private void initView() {
        back = (ImageView)findViewById(R.id.back_worker);
        jump = (LinearLayout)findViewById(R.id.jump_worker);
        mWorkerName = (EditText)findViewById(R.id.worker_name);
        mWorkerIDCode = (EditText)findViewById(R.id.worker_ID_code);
        mTakeFront = (Button)findViewById(R.id.take_front_worker);
        mTakeBack = (Button)findViewById(R.id.take_back_worker);
        mIDCardFront = (ImageView)findViewById(R.id.IDCard_front_worker);
        mIDCardBack = (ImageView)findViewById(R.id.IDCard_back_worker);
        mChooseFrontPhoto = (Button)findViewById(R.id.album_front_worker);
        mChooseBackPhoto = (Button)findViewById(R.id.album_back_worker);
        mSubmitAuthentication = (TextView) findViewById(R.id.submit_authentication_worker);
    }

    /**
     * 调用照相机拍照
     */
    public void camera(){
        // getExternalCacheDir()可以得到应用关联缓存目录
        File outputImage = new File(getExternalCacheDir(), NameUtils.getName()+".jpg");
        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            outputImage.createNewFile();
            imgPath = outputImage.getAbsolutePath();
            imgs.add(imgPath);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 如果运行设备的系统版本低于7.0，就调用Uri的fromFile()方法将File对象转换成Uri对象，
        // 这个Uri对象标识着图片的本地真实路径
        if(Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(CertificationWorkerActivity.this,"com.example.login.fileprovider",outputImage);
        }else{
            imageUri = Uri.fromFile(outputImage);
        }
        // 使用隐式Intent,启动相机程序
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        // 指定图片的输出地址
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent,CODE_TAKE_PHOTO);
    }

    /**
     * 拍照完后将结果返回到onActivityResult()方法中
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case CODE_TAKE_PHOTO:
                if(resultCode == RESULT_OK){
                    try {
                        // 将拍摄的照片显示出来
                        Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                        if(flag == 1){
                            mIDCardFront.setImageBitmap(bitmap);
                        }else if(flag == 2){
                            mIDCardBack.setImageBitmap(bitmap);
                        }

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                break;
            case CODE_CHOOSE_PHOTO:
                if (requestCode == CODE_CHOOSE_PHOTO && resultCode == Activity.RESULT_OK && data != null) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumns = {MediaStore.Images.Media.DATA};
                    Cursor c = getContentResolver().query(selectedImage, filePathColumns, null, null, null);
                    c.moveToFirst();
                    int columnIndex = c.getColumnIndex(filePathColumns[0]);
                    String imagePath = c.getString(columnIndex);
                    imgs.add(imagePath);
                    showImage(imagePath);
                    c.close();
                    new Thread(runnable);
                }
                break;
            default:
                break;
        }
    }

    //加载图片
    private void showImage(String imagePath){
        Bitmap bm = BitmapFactory.decodeFile(imagePath);
        if(flag == 3){
            mIDCardFront.setImageBitmap(bm);
        }else if(flag == 4){
            mIDCardBack.setImageBitmap(bm);
        }
    }

    /**
     * 上传图片类型
     * @param file
     * @return
     */
    private String getMIMEType(File file) {
        String fileName = file.getName();
        if(fileName.endsWith("png") || fileName.endsWith("PNG")) {
            return "image/png";
        }
        else {
            return "image/jpg";
        }
    }

    /**
     * 上传图片线程
     */
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            final String SERVER = "http://file.sxycy.cn:84/api/fileupload/img";
            final String BOUNDARY = UUID.randomUUID().toString();
            final String END = "\r\n";
            final String PREFIX = "--";
            String JsonString="";
            try {
                URL url = new URL(SERVER);
                HttpURLConnection conn = (HttpURLConnection)url.openConnection();
                conn.setRequestMethod("POST");
                // 设置每次传输的流大小，可以有效防止手机因为内存不足崩溃
                // 此方法用于在预先不知道内容长度时启用没有进行内部缓冲的 HTTP 请求正文的流。
                //conn.setChunkedStreamingMode(128 * 1024); // 128k
                conn.setConnectTimeout(5000*2);
                conn.setReadTimeout(5000*2);
                conn.setDoOutput(true);
                conn.setDoInput(true);
                conn.setUseCaches(false);
                conn.setRequestProperty("Connection", "Keep-Alive");
                conn.setRequestProperty("Charset", "UTF-8");
                conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + BOUNDARY);
                // 遍历List集合，取出图片路径
                if(imgs != null && imgs.size() > 0) {
                    // 获得输出流向服务器输出数据
                    DataOutputStream out = new DataOutputStream(conn.getOutputStream());
                    for (int i = 0; i < imgs.size(); i++) {
                        String a = imgs.get(i);
                        File file = new File(imgs.get(i));
                        //File file = new File(imgPath);
                        boolean imgExists = file.exists();
                        String fileName = file.getName();
                        StringBuffer buffer = new StringBuffer();
                        buffer.append(PREFIX).append(BOUNDARY).append(END);
                        buffer.append("Content-Disposition:form-data;"
                                + "name=\"file\";filename=\"" + file.getName() + "\"" + END);
                        buffer.append("Content-Type:" + getMIMEType(file) + END);
                        buffer.append(END);
                        out.write(buffer.toString().getBytes());
                        // 写图像字节内容
                        InputStream fis = new FileInputStream(file);
                        byte[] bytes = new byte[1024];
                        int length = 0;
                        if (fis != null) {
                            while ((length = fis.read(bytes)) != -1) {
                                out.write(bytes, 0, length);
                            }
                        }
                        fis.close();
                        out.write(END.getBytes());
                    }
                    byte[] end_data = (PREFIX + BOUNDARY + PREFIX + END).getBytes();
                    out.write(end_data);
                    out.flush();
                    int respoonseCode = conn.getResponseCode();
                    if(respoonseCode == 200){
                        JsonString = HttpUtils.changeInputStream(conn.getInputStream(),"utf-8");
                        if("true".equals(getJsonSuccessValue(JsonString))){
                            ToastUtils.showMsg(getApplicationContext(),"上传成功");
                            Intent intent = new Intent(CertificationWorkerActivity.this,HomeActivity.class);
                            startActivity(intent);
                        }else if("false".equals(getJsonSuccessValue(JsonString))){
                            ToastUtils.showMsg(getApplicationContext(),"上传失败");
                        }
                    }
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    };
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
}

