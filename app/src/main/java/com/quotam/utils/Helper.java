package com.quotam.utils;



public class Helper {

    public static int transform(String url) {
        String replaced = url.replaceFirst("drawable://","");
        return Integer.valueOf(replaced);
    }



}
