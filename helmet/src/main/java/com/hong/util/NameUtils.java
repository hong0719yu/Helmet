package com.hong.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class NameUtils {
    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    public static String getName(){
        Date date = new Date();
        StringBuffer bufferName=new StringBuffer(sdf.format(date));
        Random random = new Random();
        for(int i = 0; i < 4; i++){
            bufferName.append(random.nextInt(10));
        }
        System.out.println(bufferName.toString());
        return bufferName.toString();
    }
    public static void main(String[] args){
        getName();
    }
}
