package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.pojo.Enterprise;
import com.example.yq.pojo.Page;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.JsonUtil;
import com.example.yq.util.StringUtils;

import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;


/**
 * Created by YQ on 2019/4/24.
 */

public class Company extends Activity {
    private TextView companyName,companyProperty,companyScale,companyCity,companyAddress,companyTel,companyInfo;
    private String companyId;
    private Intent intent;
    private ObjectMapper objectMapper;
    private Enterprise enterprise;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.company);
        objectMapper = JsonUtil.getObjectMapper();
        intent = getIntent();
        companyId = intent.getStringExtra("companyId");
        System.out.println("companyId...."+companyId);
        CompanyAsyncTask companyAsyncTask = new CompanyAsyncTask();
        companyAsyncTask.execute();
        initView();
    }
    private void initView(){
        companyName = findViewById(R.id.companyName);
        companyProperty = findViewById(R.id.companyProperty);
        companyScale = findViewById(R.id.companyScale);
        companyCity = findViewById(R.id.companyCity);
        companyAddress = findViewById(R.id.companyAddress);
        companyTel = findViewById(R.id.companyTel);
        companyInfo = findViewById(R.id.companyInfo);

    }
    class CompanyAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Enterprise/appCheckCompany.do");
            //设置头部信息
            url.append("/"+companyId);
            String result = HttpUtil.sendUrl(url.toString());
            System.out.println("enterprise......."+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                 enterprise = objectMapper.readValue(result, Enterprise.class);

                companyName.setText(enterprise.getE_name());
                companyProperty.setText(enterprise.getE_property());
                companyScale.setText(enterprise.getE_scale());
                companyCity.setText(enterprise.getE_city());
                companyAddress.setText(enterprise.getE_addr());
                companyTel.setText(enterprise.getE_tel());
                companyInfo.setText(enterprise.getE_info());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
