package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yq.util.DialogUtil;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.StringUtils;


/**
 * Created by YQ on 2019/5/8.
 */

public class RegisterActivity extends Activity {
    private EditText userNameEditText,passwordEditText,confirmPasswordEditText;
    private Button registerBtn;
    private String userName,password,conformPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        initView();
        initEvent();
    }
    private void initView(){
        userNameEditText = findViewById(R.id.registerUserName);
        passwordEditText = findViewById(R.id.registerPassword);
        confirmPasswordEditText = findViewById(R.id.registerConformPassword);
        registerBtn = findViewById(R.id.registerBtn);
    }
    private void initEvent(){
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //每次点击都要获取
                userName = String.valueOf(userNameEditText.getText());
                password = String.valueOf(passwordEditText.getText());
                conformPassword = String.valueOf(confirmPasswordEditText.getText());
                if ("".equals(userName)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(RegisterActivity.this, "提示", "用户名不能为空");
                    builder.create().show();
                    return;
                }else if ("".equals(password)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(RegisterActivity.this, "提示", "密码不能为空");
                    builder.create().show();
                    return;
                }else if ("".equals(conformPassword)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(RegisterActivity.this, "提示", "请再次确认密码");
                    builder.create().show();
                    return;
                }
                //到了这里都不为空
                if (!password.equals(conformPassword)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(RegisterActivity.this, "提示", "两次密码不一致");
                    builder.create().show();
                    return;
                }
                RegisterAsyncTask registerAsyncTask = new RegisterAsyncTask();
                registerAsyncTask.execute(userName,password,conformPassword);
            }
        });
    }
    class RegisterAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String userName = strings[0];
            String password = strings[1];
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/register/registerApp.do");
            url.append("/"+userName);
            url.append("/"+password);
            url.append("/0");    //说明这是求职者
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if ("register success".equals(result)){
                AlertDialog.Builder builder = DialogUtil.simpleDialog(RegisterActivity.this, "提示", "注册成功，您可以登录了");
                builder.create().show();
            }else if ("user isExisted".equals(result)){
                AlertDialog.Builder builder = DialogUtil.simpleDialog(RegisterActivity.this, "提示", "该用户已存在，请重新注册");
                builder.create().show();
                return;
            }
            Intent intent = new Intent();
            intent.setClass(RegisterActivity.this,LoginActivity.class);
            startActivity(intent);
        }
    }
}
