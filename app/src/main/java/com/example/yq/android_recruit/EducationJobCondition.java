package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yq.pojo.DetailedResume;
import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.pojo.Page;
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
 * Created by YQ on 2019/4/23.
 */

public class EducationJobCondition extends Activity {
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    private EditText projectExperienceDescribe;
    private Session session = null;
    private DetailedResume resume;
    private String userId,resumeName,educationJobConditionTimeValue,projectExperienceDescribeValue;
    private int itemIndex;
    private String eduJobConId;
    private TextView educationJobConditionTime,eduJobConditionSaveBtn;
    private LinearLayout eduJobConditionParent;
    private RelativeLayout educationJobConditionTimeBtn;
    private DatePicker eduJobConditionTimeDatePicker;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education_job_condition);
        initView();
        initEvent();
    }
    private void initView(){
        eduJobConditionParent = (LinearLayout)findViewById(R.id.eduJobConditionParent);
        educationJobConditionTimeBtn = (RelativeLayout)findViewById(R.id.educationJobConditionTimeBtn);
        eduJobConditionSaveBtn = (TextView)findViewById(R.id.eduJobConditionSaveBtn);
        educationJobConditionTime = (TextView) findViewById(R.id.educationJobConditionTime);
        projectExperienceDescribe = (EditText)findViewById(R.id.projectExperienceDescribe);

        Intent intent = getIntent();
        session = Session.getSession();
        int viewIndex = (Integer) session.get("resumeIndex");
        resume = ((List<DetailedResume>) session.get("resumeList")).get(viewIndex);
        resumeName = resume.getResumeBean().getResumeName();
        userId = (String) session.get("userId");

        itemIndex = intent.getIntExtra("itemIndex",-1);
        if (itemIndex != -1){
            eduJobConId = resume.getEduJobConditionList().get(itemIndex).getEduJobConditionId();
            educationJobConditionTime.setText(StringUtils.dateFmtChange(resume.getEduJobConditionList().get(itemIndex).getTimes()));
            projectExperienceDescribe.setText(resume.getEduJobConditionList().get(itemIndex).getConDescribe());
        }
    }
    private void initEvent(){
        educationJobConditionTimeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.experience, null);
                View view1 =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view1);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(eduJobConditionParent, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                    eduJobConditionTimeDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                    int year = eduJobConditionTimeDatePicker.getYear();
                    int month = eduJobConditionTimeDatePicker.getMonth()+1;
                    int dayOfMonth = eduJobConditionTimeDatePicker.getDayOfMonth();
                    educationJobConditionTimeValue = year+"-"+month+"-"+dayOfMonth;
                    educationJobConditionTime.setText(educationJobConditionTimeValue);
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });

        eduJobConditionSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                educationJobConditionTimeValue = String.valueOf(educationJobConditionTime.getText());
                projectExperienceDescribeValue = String.valueOf(projectExperienceDescribe.getText());
                if ("".equals(educationJobConditionTimeValue)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(EducationJobCondition.this, "提示", "时间不能为空");
                    builder.create().show();
                    return;
                }
                if (itemIndex != -1){
                    EduJobConditionSaveAsyncTask eduJobConditionSaveAsyncTask = new EduJobConditionSaveAsyncTask();
                    eduJobConditionSaveAsyncTask.execute();
                }else {
                    EduJobConditionCreateAsyncTask eduJobConditionCreateAsyncTask = new EduJobConditionCreateAsyncTask();
                    eduJobConditionCreateAsyncTask.execute();
                }
            }
        });
    }
    class EduJobConditionSaveAsyncTask extends AsyncTask<String,Void,String> {
        //要传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/updateEduJobCondition.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+eduJobConId);   //数据库中的经验id
            url.append("/"+educationJobConditionTimeValue);
            url.append("/"+projectExperienceDescribeValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(EducationJobCondition.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(EducationJobCondition.this,Fragment_mainActivity.class);

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
    class EduJobConditionCreateAsyncTask extends AsyncTask<String,Void,String> {
        //不用传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/createEduJobCondition.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+educationJobConditionTimeValue);
            url.append("/"+projectExperienceDescribeValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(EducationJobCondition.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(EducationJobCondition.this,Fragment_mainActivity.class);

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
