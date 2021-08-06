package com.msj.demo;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.net.URL;

public class WebDownloader {
    //下载方法
    public void downloder(String url,String name){
        try{
            FileUtils.copyURLToFile(new URL(url),new File(name));
        }catch(Exception e){
            e.printStackTrace();
            System.out.println("IO异常，download方法出现问题");
        }
    }
}
