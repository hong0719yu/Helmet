package com.hong.util;

public class MsgUtils {
    private static String message;
    private final static boolean isTrue=false;
    public static int getMsg(){
        if(isTrue){
            return 1;
        }else{
            return 0;
        }
    }
}
