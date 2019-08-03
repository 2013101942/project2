package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.util.Session;

import java.util.List;


/**
 * Created by YQ on 2019/5/10.
 */

public class SuccessInfo extends Activity {
    private TextView recruitInfoJob,recruitInfoTimes,recruitInfoSalaryStart,recruitInfoSalaryEnd,recruitInfoStaffNum,recruitInfoExperience,recruitInfoAddress,recruitInfoCompanyName,recruitInfoCompanyProperty,recruitInfo_rules;
    private RelativeLayout recruitInfoCompany;
    private Button sendMessageBtn;
    private Session session = null;
    private List<DivAppRecruitInfo> beansList = null;
    private Intent intent = null;
    private int recruitIndex;
    private String personId,companyId,companyName,recruitId;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.success_info);
        initView();
        initEvent();
    }
    private void initView(){
        recruitInfoJob = (TextView)findViewById(R.id.successInfo_recruitInfoJob);
        recruitInfoTimes = (TextView)findViewById(R.id.successInfo_recruitInfoTimes);
        recruitInfoSalaryStart = findViewById(R.id.successInfo_recruitInfoSalaryStart);
        recruitInfoSalaryEnd = findViewById(R.id.successInfo_recruitInfoSalaryEnd);
        recruitInfoStaffNum = findViewById(R.id.successInfo_recruitInfoStaffNum);
        recruitInfoExperience = findViewById(R.id.successInfo_recruitInfoExperience);
        recruitInfoAddress = findViewById(R.id.successInfo_recruitInfoAddress);
        recruitInfoCompanyName = findViewById(R.id.successInfo_recruitInfoCompanyName);
        recruitInfoCompanyProperty = findViewById(R.id.successInfo_recruitInfoCompanyProperty);
        recruitInfo_rules = findViewById(R.id.successInfo_recruitInfo_rules);
        recruitInfoCompany = (RelativeLayout) findViewById(R.id.successInfo_recruitInfoCompany);
        sendMessageBtn = findViewById(R.id.successInfo_sendMessageBtn);

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
        companyName = beansList.get(recruitIndex).geteName();
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
    private void initEvent(){
        //给企业添加点击事件
        recruitInfoCompany.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SuccessInfo.this,Company.class);
                intent.putExtra("companyId",beansList.get(recruitIndex).geteId());
                startActivity(intent);
            }
        });
        sendMessageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SuccessInfo.this,MessageSend.class);
                intent.putExtra("personId",personId);
                intent.putExtra("companyId",companyId);
                intent.putExtra("companyName",companyName);
                intent.putExtra("recruitId",recruitId);
                startActivity(intent);
            }
        });
    }
}
