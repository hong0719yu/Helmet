package com.hong.util;

import com.alibaba.fastjson.JSON;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HttpUtils {
    // 请求服务器端的url
    private static String PATH="http://file.sxycy.cn:84/";
    private static Map<String,String> params = new HashMap<>();
   // private static StringBuffer bufferSendPostMsg;
   // 存放拍照完后的图片路径
   private List<String> imgs = new ArrayList();

    public static String sendGetMessage(Map<String,String> params,String str,String urlPath,String encode) {
        URL url = null;
        StringBuffer bufferGet = new StringBuffer(PATH);
        bufferGet.append(urlPath);
        if(str == null) {
            bufferGet.append("?");
            try {
                if (params != null && !params.isEmpty()) {
                    for (Map.Entry<String, String> entry : params.entrySet()) {
                        bufferGet.append(entry.getKey())
                                .append("=")
                                .append(URLEncoder.encode(entry.getValue(), encode))
                                .append("&");
                    }
                    bufferGet.deleteCharAt(bufferGet.length() - 1);
                }
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        try {
            url = new URL(bufferGet.toString());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            setGetMethod(conn);
            return getResponseStatusCode(conn,encode);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 发送GET请求
     * @param str 填写的url参数
     * @param urlPath  URL路径
     * @param encode 字节编码
     * @return  成功则调用将输入流转换成指定编码格式的字符串
     *           失败返回null
     */
    public static String sendGetWithHttpURLConnection(String str,String urlPath,String encode){
        StringBuffer bufferSendGet = new StringBuffer(PATH);
        bufferSendGet.append(urlPath).append(str);
        try {
            URL url = new URL(bufferSendGet.toString());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            setGetMethod(conn);
            // 获取发送验证码的sessionId
            String sessionId = conn.getHeaderField("Set-Cookie");
            params.put("Cookie",sessionId);
            return getResponseStatusCode(conn,encode);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }

    }

    /**
     * 发送GET请求,校验验证码
     * @param code 填写验证码
     * @param urlPath  URL路径
     * @param encode 字节编码
     * @return  成功则调用将输入流转换成指定编码格式的字符串
     *           失败返回null
     */
    public static String sendGetMessageCheckCode(String code,String urlPath,String encode){
        StringBuffer bufferCheckCode = new StringBuffer(PATH);
        bufferCheckCode.append(urlPath).append(code);
        try {
            // 设置URL，并打开连接
            URL url = new URL(bufferCheckCode.toString());
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            String cookies = params.get("Cookie");
            conn.setRequestProperty("Cookie", cookies);
            setGetMethod(conn);
            return getResponseStatusCode(conn,encode);
        } catch (IOException e) {
            e.printStackTrace();
            return "";
        }
    }

    /**
     * 发送POST请求
     * @param params 填写的url参数
     * @param urlPath URL路径
     * @param code 注册时填写的验证码  如果验证码为空 ，则为登录POST请求，否则为注册POST请求
     * @param encode 字节编码
     * @return 成功则调用将输入流转换成指定编码格式的字符串
     *           失败返回null
     */
    public static String sendPostWithHttpURLConnection(Map<String,String> params,String urlPath,String code,String encode) {
        StringBuffer bufferSendPostMsg = new StringBuffer(PATH);
        if(code != null){
            // 注册
            bufferSendPostMsg.append(urlPath).append(code);
            try {
                URL url = new URL(bufferSendPostMsg.toString());
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                setPostMethod(conn,bufferSendPostMsg,params);
                String sessionId =  conn.getHeaderField("Set-Cookie");
                params.put("Cookie",sessionId);
                return getResponseStatusCode(conn,encode);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            // 登录
            bufferSendPostMsg.append(urlPath);
            try {
                bufferSendPostMsg.append("?");
                if(params != null && !params.isEmpty()){
                    for (Map.Entry<String,String> entry: params.entrySet()) {
                        bufferSendPostMsg.append(entry.getKey())
                                .append("=")
                                .append(URLEncoder.encode(entry.getValue(),encode))
                                .append("&");
                    }
                }
                bufferSendPostMsg.deleteCharAt(bufferSendPostMsg.length() - 1);
                URL url = new URL(bufferSendPostMsg.toString());
                HttpURLConnection  conn = (HttpURLConnection)url.openConnection();
                setPostMethod(conn,bufferSendPostMsg,null);
                String sessionId = conn.getHeaderField("Set-Cookie");
                params.put("Cookie",sessionId);
                // 获得服务器响应结果和状态码
                return getResponseStatusCode(conn,encode);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /*public static String sendPostUploadPicture(String urlPath){
        StringBuffer bufferSendPostUpload = new StringBuffer(PATH);
        bufferSendPostUpload.append(urlPath);
        final String BOUNDARY = UUID.randomUUID().toString();
        final String END = "\r\n";
        final String PREFIX = "--";
        String JsonString="";
        try {
            URL url = new URL(bufferSendPostUpload.toString());
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
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }*/

    /**
     *  设置Get请求
     * @param conn
     * @return
     * @throws IOException
     */
    public static HttpURLConnection setGetMethod(HttpURLConnection conn) throws IOException {
        conn.setRequestMethod("GET");// 设置请求方式
        conn.setConnectTimeout(5000);// 设置请求超时时间
        conn.setReadTimeout(5000);// 设置读取服务器资源超时时间
        //conn.setDoOutput(true);// 表示向服务器写数据
        //conn.setDoInput(true);// 表示从服务器获取数据
        conn.connect();
        return conn;
    }

    /**
     * 设置Post请求,并获得输出流向服务器输出数据
     * @param conn  HttpURLConnection连接
     * @param bufferSendPost
     * @param registerData  如果registerData不为空则为注册，为空则为登录
     * @return
     * @throws IOException
     */
    public static HttpURLConnection setPostMethod(HttpURLConnection conn,StringBuffer bufferSendPost,Map<String,String> registerData) throws IOException {
        conn.setRequestMethod("POST");// 设置请求方式
        conn.setConnectTimeout(5000);// 设置请求超时时间
        conn.setReadTimeout(5000);// 设置读取服务器资源超时时间
        conn.setDoInput(true);// 表示从服务器获取数据
        conn.setDoOutput(true);// 表示向服务器写数据
        conn.setUseCaches(false);// Post请求不能使用缓存
        // 获得上传信息的字节大小以及长度
        byte[] mydata;
        String jsonStr = null;
        if(registerData!= null){
            // 将map集合转换成json格式
            jsonStr = JSON.toJSONString(registerData);
            mydata = jsonStr.getBytes();
            conn.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            conn.setRequestProperty("Accept","application/json");
        }else{
            mydata = bufferSendPost.toString().getBytes();
            // 表示设置请求体的类型是文本类型
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
        }
        conn.setRequestProperty("Connection", "Keep-Alive");
        conn.setRequestProperty("Content-Length", String.valueOf(mydata.length));
        conn.connect();
        // 获得输出流向服务器输出数据
        DataOutputStream  dataOutputStream = new DataOutputStream(conn.getOutputStream());
        if(registerData != null){
            dataOutputStream.writeBytes(jsonStr);
        }else{
            dataOutputStream.writeBytes(bufferSendPost.toString());
        }
        dataOutputStream.flush();
        dataOutputStream.close();
        return conn;
    }

    /**
     * 获得服务器响应结果和状态码,
     * 并将输入流转换成指定编码格式的字符串
     * @param conn
     * @return
     * @throws IOException
     */
    public static String getResponseStatusCode(HttpURLConnection conn,String encode) throws IOException {
        int respoonseCode = conn.getResponseCode();
        if(respoonseCode == 200){
            return changeInputStream(conn.getInputStream(),encode);
        }else{
            return "";
        }
    }

    /**
     * 将输入流转换成指定编码格式的字符串
     * @param inputStream   输入流
     * @param encode   字符编码
     * @return   转换后的字符串
     * @throws IOException  输入流读数据时可以会发生IO异常，抛出异常
     */
    public static String changeInputStream(InputStream inputStream,String encode) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        String result = "";
        if(inputStream != null){
            while((len = inputStream.read(data))!= -1){
                outputStream.write(data,0,len);
            }
                result = new String(outputStream.toByteArray(),encode);
        }
        outputStream.close();
        inputStream.close();
        System.out.println("------------------Json数据--------------------");
        System.out.println(result);
        System.out.println("------------------Json数据--------------------");
        return result;
    }

    /**
     * 上传图片类型
     * @param file
     * @return
     */
    private static  String getMIMEType(File file) {
        String fileName = file.getName();
        if(fileName.endsWith("png") || fileName.endsWith("PNG")) {
            return "image/png";
        }
        else {
            return "image/jpg";
        }
    }
}