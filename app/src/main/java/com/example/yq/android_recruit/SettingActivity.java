package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import com.example.yq.pojo.DetailedResume;
import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.pojo.Page;
import com.example.yq.pojo.Resume;
import com.example.yq.util.DialogUtil;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.JsonUtil;
import com.example.yq.util.Session;
import com.example.yq.util.StringUtils;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by YQ on 2019/4/26.
 */

public class SettingActivity extends Activity {
    private TextView settingResumeName,settingSaveBtn;
    private Switch settingIsPublic;
    private Button createResumeBtn,DeleteResumeBtn;
    private int resumeIndex;
    private Session session;
    private DetailedResume resume;
    private String userId;
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.setting);
        initView();
        initEvent();
    }
    private void initView(){
        session = Session.getSession();
        resume = ((List<DetailedResume>)session.get("resumeList")).get(resumeIndex);
        userId = (String) session.get("userId");
        Intent intent = getIntent();
        resumeIndex = intent.getIntExtra("resumeIndex",0);
        settingResumeName = (TextView)findViewById(R.id.settingResumeName);
        settingSaveBtn = (TextView)findViewById(R.id.settingSaveBtn);
        settingIsPublic = (Switch) findViewById(R.id.settingIsPublic);
        createResumeBtn = (Button)findViewById(R.id.createResumeBtn);
        DeleteResumeBtn = (Button)findViewById(R.id.DeleteResumeBtn);

        settingResumeName.setText(resume.getResumeBean().getResumeName());
        if ("1".equals(resume.getResumeBean().getIsvisible())){
            settingIsPublic.setChecked(true);
        }else if ("0".equals(resume.getResumeBean().getIsvisible())){
            settingIsPublic.setChecked(false);
        }

    }
    private void initEvent(){
        //添加保存事件
        settingSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newResumeName =  settingResumeName.getText().toString();
                boolean isVisible = (settingIsPublic.isChecked());
                //1为公开，0为保密
                if (isVisible){
                    SaveASettingAsyncTask saveASettingAsyncTask = new SaveASettingAsyncTask();
                    saveASettingAsyncTask.execute(newResumeName,"1");
                }else {
                    SaveASettingAsyncTask saveASettingAsyncTask = new SaveASettingAsyncTask();
                    saveASettingAsyncTask.execute(newResumeName,"0");
                }

            }
        });
        //创建简历
        createResumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this,CreateResume.class);
                startActivity(intent);
            }
        });
        //删除简历
        DeleteResumeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DeleteResumeAsyncTask deleteResumeAsyncTask = new DeleteResumeAsyncTask();
                deleteResumeAsyncTask.execute();
            }
        });
    }
    //保存设置
    class SaveASettingAsyncTask extends AsyncTask<String,Void,String>{
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/updateResumeSetting.do");
            String oldResume = resume.getResumeBean().getResumeName();
            String newResumeName =  strings[0];
            String isVisible = strings[1];
            System.out.println("isVisible......."+isVisible);
            //设置头部信息
            url.append("/"+ userId);
            url.append("/"+ oldResume);
            url.append("/"+ newResumeName);
            url.append("/"+ isVisible);
            String result = HttpUtil.sendUrl(url.toString());
            System.out.println("resule空。。。。。。"+result);
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(SettingActivity.this, "提示", "保存成功");
            builder.create().show();
        }
    }

    //删除简历
    class DeleteResumeAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String userId = resume.getResumeBean().getUserId();
            String resumeName = resume.getResumeBean().getResumeName();
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/deleteResume.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            System.out.println("删掉了简历.................."+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(SettingActivity.this, "提示", "删除成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(SettingActivity.this,Fragment_mainActivity.class);

                session.put("beansList",beansList);
                session.put("userId",recruitInfoPage.getUserId());
                session.put("resumeList",recruitInfoPage.getDetailedResumes());
                session.put("jobs", recruitInfoPage.getJobs());
                intent.putExtra("isCreateOrDeleteResume",true);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
