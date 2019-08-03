package com.example.yq.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by YQ on 2019/3/28.
 */

public class HttpUtil {
    /**
     * 加载网页资源
     * @param sendUrl
     * @return
     */
    public static String sendUrl(String sendUrl){
        StringBuffer  buffer = new StringBuffer();
        try {
            URL url = new URL(sendUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/Json; charset=UTF-8");
            InputStream inputStream = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));

            String str = null;
            while ((str = reader.readLine()) != null) {
                buffer.append(str);
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buffer.toString();
    }
    /**
     * 加载图片资源
     * @param sendUrl
     * @return
     */
    public static Bitmap loadImage(String sendUrl){
        StringBuffer  buffer = new StringBuffer();
        try {
            URL url = new URL(sendUrl);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setRequestMethod("GET");

            InputStream stream = conn.getInputStream();
            String fileName = String.valueOf(System.currentTimeMillis());
            FileOutputStream outputStream =null ;
            File fileDownLoad = null;
            if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
                //获取SD卡的根路径
                File parent = Environment.getExternalStorageDirectory();
                fileDownLoad = new File(parent,fileName);
                outputStream = new FileOutputStream(fileDownLoad);
            }
            byte[] bytes = new byte[2*1024];
            int len;
            if (outputStream != null){
                while ((len = stream.read(bytes)) != -1){
                    outputStream.write(bytes,0,len);
                }
                return BitmapFactory.decodeFile(fileDownLoad.getAbsolutePath());
            }else {
                return null;
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    return null;
    }
}
