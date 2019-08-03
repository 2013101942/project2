package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.util.DialogUtil;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.Session;
import com.example.yq.util.StringUtils;

import java.util.List;


/**
 * Created by YQ on 2019/5/10.
 */

public class Collection extends Activity {
    private TextView recruitInfoJob,recruitInfoTimes,recruitInfoSalaryStart,recruitInfoSalaryEnd,recruitInfoStaffNum,recruitInfoExperience,recruitInfoAddress,recruitInfoCompanyName,recruitInfoCompanyProperty,recruitInfo_rules;
    private RelativeLayout recruitInfoCompany;
    private Button recruitInfo_applyBtn,recruitInfo_cancelCollectBtn;
    private Session session = null;
    private List<DivAppRecruitInfo> beansList = null;
    private Intent intent = null;
    private int recruitIndex;
    private String personId,companyId,recruitId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.collection);
        initView();
        initEvent();
    }
    private void initView(){

        recruitInfoJob = (TextView)findViewById(R.id.collection_recruitInfoJob);
        recruitInfoTimes = (TextView)findViewById(R.id.collection_recruitInfoTimes);
        recruitInfoSalaryStart = findViewById(R.id.collection_recruitInfoSalaryStart);
        recruitInfoSalaryEnd = findViewById(R.id.collection_recruitInfoSalaryEnd);
        recruitInfoStaffNum = findViewById(R.id.collection_recruitInfoStaffNum);
        recruitInfoExperience = findViewById(R.id.collection_recruitInfoExperience);
        recruitInfoAddress = findViewById(R.id.collection_recruitInfoAddress);
        recruitInfoCompanyName = findViewById(R.id.collection_recruitInfoCompanyName);
        recruitInfoCompanyProperty = findViewById(R.id.collection_recruitInfoCompanyProperty);
        recruitInfo_rules = findViewById(R.id.collection_recruitInfo_rules);
        recruitInfoCompany = (RelativeLayout) findViewById(R.id.collection_recruitInfoCompany);
        recruitInfo_applyBtn = findViewById(R.id.collection_recruitInfo_applyBtn);
        recruitInfo_cancelCollectBtn = findViewById(R.id.collection_cancelCollectBtn);

        session = Session.getSession();
        intent = getIntent();
        if (intent.getBooleanExtra("wodeFlag",false) == false){
            //从首页点击进来的没有wodeFlag，默认为false
            beansList = (List<DivAppRecruitInfo>) session.get("beansList");
        }else {
            //从“我的”点击进来的，放了true
            beansList = (List<DivAppRecruitInfo>) session.get("collectOrApplyOrAdoptList");
        }
        recruitIndex = intent.getIntExtra("recruitIndex",0);
        personId = (String) session.get("userId");
        companyId = beansList.get(recruitIndex).geteId();
        recruitId = beansList.get(recruitIndex).getRecruitId();

        recruitInfoJob.setText(beansList.get(recruitIndex).getJob());
        recruitInfoTimes.setText(String.valueOf(beansList.get(recruitIndex).getTimes()));
        recruitInfoSalaryStart.setText(beansList.get(recruitIndex).getSalaryStart()+"");
        recruitInfoSalaryEnd.setText(beansList.get(recruitIndex).getSalaryEnd()+"");
        recruitInfoStaffNum.setText(beansList.get(recruitIndex).getStaffNum());
        System.out.println("staffNum............"+beansList.get(recruitIndex).getStaffNum());
        recruitInfoExperience.setText(beansList.get(recruitIndex).getExperience());
        recruitInfoAddress.setText(beansList.get(recruitIndex).getCity());
        recruitInfoCompanyName.setText(beansList.get(recruitIndex).geteName());
        recruitInfoCompanyProperty.setText(beansList.get(recruitIndex).geteProperty());
        recruitInfo_rules.setText(beansList.get(recruitIndex).getRecruitInfo());
    }
    private void initEvent() {
        //给企业添加点击事件
        recruitInfoCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Collection.this, Company.class);
                intent.putExtra("companyId", beansList.get(recruitIndex).geteId());
                startActivity(intent);
            }
        });
        //申请
        recruitInfo_applyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(Collection.this,ToApply.class);
                intent.putExtra("personId",personId);
                intent.putExtra("companyId",companyId);
                intent.putExtra("recruitId",recruitId);
                startActivity(intent);
            }
        });
        //取消收藏
        recruitInfo_cancelCollectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CancelCollectionAsyncTask cancelCollectionAsyncTask = new CancelCollectionAsyncTask();
                cancelCollectionAsyncTask.execute(personId,companyId,recruitId);
            }
        });
    }
    class CancelCollectionAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String personId = strings[0];
            String companyId = strings[1];
            String recruitId = strings[2];
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/collection/cancelCollectRecruit.do");
            url.append("/"+personId);
            url.append("/"+companyId);
            url.append("/"+recruitId);
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
           if ("cancel success".equals(result)){
               AlertDialog.Builder builder = DialogUtil.simpleDialog(Collection.this, "提示", "取消收藏成功");
               builder.create().show();
           }
        }
    }
}
