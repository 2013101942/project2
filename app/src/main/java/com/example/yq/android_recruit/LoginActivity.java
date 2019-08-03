package com.example.yq.android_recruit;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.pojo.Page;
import com.example.yq.util.DialogUtil;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.JsonUtil;
import com.example.yq.util.Session;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * 登录验证
 */
public class LoginActivity extends AppCompatActivity {
    private EditText userNameView,passwordView;
    private String userName,password;
    private Button loginBtn,registerBtn;
    LoginAsyncTask loginAsyncTask = null;
    static ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        initView();

        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //需要点击时再获取EditView中的值，避免多次点击使用的是第一次的值
                userName = userNameView.getText().toString();
                password = passwordView.getText().toString();
                //检验是否为空，先验证是否为空在请求服务器
                if ("".equals(userName)||userName == null ||"".equals(password) ||password == null){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(LoginActivity.this, "提示", "用户名或密码不能为空");
                    builder.create().show();
                }else{
                    loginAsyncTask = new LoginAsyncTask();
                    loginAsyncTask.execute(userName,password);
                }
            }
        });
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();  
        if (loginAsyncTask != null){
            loginAsyncTask.cancel(true);
        }
    }
   //初始化
    private void initView(){
        userNameView = (EditText)findViewById(R.id.userNameId);
        passwordView = (EditText) findViewById(R.id.passwordId);

        loginBtn = (Button)findViewById(R.id.loginSubId);
        registerBtn = (Button)findViewById(R.id.registerBtnId);
    };
    class LoginAsyncTask extends AsyncTask<String ,Void,String[]>{

        //传多个字符串，在后台验证，返回结果
       @Override
        protected String[] doInBackground(String... strings) {
            System.out.println("异步任务开始......");
            String userName = strings[0];
            String password = strings[1];
            System.out.println(userName);
            System.out.println(password);
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/android_recruit/appLogin.do");
            //设置头部信息
            url.append("/"+userName);
            url.append("/"+password);

            String result = HttpUtil.sendUrl(url.toString());
            System.out.println("result..."+result);

            return new String[]{userName,password,result};
            }
        //只能在此处改变 UI
        @Override
        protected void onPostExecute(String[] arr) {
            //得到结果之后再验证用户名、密码是否正确并改变 UI
            verify(arr[0],arr[1],arr[2]);
        }
    }

    //验证用户名和密码
    private void verify(String userName,  String password, String result){

        //验证是否正确
        if ("用户不存在".equals(result)||"用户名或密码错误".equals(result)){
            AlertDialog.Builder builder = DialogUtil.simpleDialog(LoginActivity.this, "提示", result);
            builder.create().show();
        }else {
            try {

                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString = objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);

                 //跳转
                Intent intent = new Intent();
                intent.setClass(LoginActivity.this,Fragment_mainActivity.class);
                Session session = Session.getSession();
                session.put("beansList",beansList);
                session.put("userId",recruitInfoPage.getUserId());
                session.put("resumeList",recruitInfoPage.getDetailedResumes());
                session.put("jobs", recruitInfoPage.getJobs());
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

