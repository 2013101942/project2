package com.example.yq.util;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.example.yq.android_recruit.LoginActivity;

/**
 * Created by YQ on 2019/4/20.
 */

public class DialogUtil {

    public static AlertDialog.Builder simpleDialog(final Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("完成", null)
                .setNegativeButton("取消", null);
        return builder;
    }
    public static AlertDialog.Builder okDialog(final Context context, String title, String message){
        AlertDialog.Builder builder = new AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("确定", null);
        return builder;
    }

    //自定义对话框,对话框用法
    public void myDialog(Context context,String title,String message, String ok,String cancel){
        AlertDialog.Builder builder = simpleDialog(context, title, message);
        builder.setPositiveButton(ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                //确定
            }
        })
                .setNegativeButton(cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        //取消
                    }
                });
        builder.create().show();
    }
}
