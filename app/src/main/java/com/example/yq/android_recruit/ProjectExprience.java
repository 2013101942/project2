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

public class ProjectExprience extends Activity {
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    private EditText projectExperienceName,projectExperienceDescribe,projectExperienceCompany;
    private Session session = null;
    private String userId,resumeName,projectExperienceTimeStartValue,projectExperienceTimeEndValue,projectExperienceNameValue,projectExperienceDescribeValue,projectExperienceCompanyValue;
    private DetailedResume resume;
    private int itemIndex;
    private String  proExpId;
    private TextView projectExperienceTimeStart,projectExperienceTimeEnd,projectExperienceSaveBtn;
    private LinearLayout projectExperienceParent;
    private RelativeLayout projectExperienceTimeStartBtn,projectExperienceTimeEndBtn;
    private DatePicker projectExperienceTimeStartDatePicker,projectExperienceTimeEndDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.project_experience);
        initView();
        initEvent();
    }
    private void initView(){
        projectExperienceParent = (LinearLayout)findViewById(R.id.projectExperienceParent);
        projectExperienceTimeStartBtn = (RelativeLayout)findViewById(R.id.projectExperienceTimeStartBtn);
        projectExperienceTimeEndBtn = (RelativeLayout)findViewById(R.id.projectExperienceTimeEndBtn);
        projectExperienceSaveBtn = (TextView)findViewById(R.id.projectExperienceSaveBtn);
        projectExperienceTimeStart = (TextView) findViewById(R.id.projectExperienceTimeStart);
        projectExperienceTimeEnd = (TextView) findViewById(R.id.projectExperienceTimeEnd);
        projectExperienceName = (EditText)findViewById(R.id.projectExperienceName);
        projectExperienceDescribe = (EditText)findViewById(R.id.projectExperienceDescribe);
        projectExperienceCompany = (EditText)findViewById(R.id.projectExperienceCompany);

        session = Session.getSession();
        int viewIndex = (Integer) session.get("resumeIndex");
        resume = ((List<DetailedResume>) session.get("resumeList")).get(viewIndex);
        resumeName = resume.getResumeBean().getResumeName();
        userId = (String) session.get("userId");

        Intent intent = getIntent();
        itemIndex = intent.getIntExtra("itemIndex",-1);
        if (itemIndex != -1){
            proExpId = resume.getProjectExprienceList().get(itemIndex).getProjectExpId();
            projectExperienceTimeStart.setText(StringUtils.dateFmtChange(resume.getProjectExprienceList().get(itemIndex).getTimeStart()));
            projectExperienceTimeEnd.setText(StringUtils.dateFmtChange(resume.getProjectExprienceList().get(itemIndex).getTimeEnd()));
            projectExperienceName.setText(resume.getProjectExprienceList().get(itemIndex).getProjectName());
            projectExperienceDescribe.setText(resume.getProjectExprienceList().get(itemIndex).getProDescribe());
            projectExperienceCompany.setText(resume.getProjectExprienceList().get(itemIndex).getCompanyName());
        }
    }

    private void initEvent(){
        projectExperienceTimeStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.project_experience, null);
                View view1 =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view1);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(projectExperienceParent, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                    projectExperienceTimeStartDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                    int year = projectExperienceTimeStartDatePicker.getYear();
                    int month = projectExperienceTimeStartDatePicker.getMonth()+1;
                    int dayOfMonth = projectExperienceTimeStartDatePicker.getDayOfMonth();
                    projectExperienceTimeStartValue = year+"-"+month+"-"+dayOfMonth;
                    projectExperienceTimeStart.setText(projectExperienceTimeStartValue);
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });
        projectExperienceTimeEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.project_experience, null);
                View view1 =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view1);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(projectExperienceParent, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        projectExperienceTimeEndDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                    int year = projectExperienceTimeEndDatePicker.getYear();
                    int month = projectExperienceTimeEndDatePicker.getMonth()+1;
                    int dayOfMonth = projectExperienceTimeEndDatePicker.getDayOfMonth();
                        projectExperienceTimeEndValue = year+"-"+month+"-"+dayOfMonth;
                    projectExperienceTimeEnd.setText(projectExperienceTimeEndValue);
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });

        projectExperienceSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(String.valueOf(projectExperienceTimeStart.getText()))){
                    projectExperienceTimeStartValue = String.valueOf(projectExperienceTimeStart.getText());
                }else {
                    projectExperienceTimeStartValue = null;
                }
                if (!"".equals(String.valueOf(projectExperienceTimeEnd.getText()))){
                    projectExperienceTimeEndValue = String.valueOf(projectExperienceTimeEnd.getText());
                }else {
                    projectExperienceTimeEndValue = null;
                }
                if (!"".equals(String.valueOf(projectExperienceName.getText()))){
                    projectExperienceNameValue = String.valueOf(projectExperienceName.getText());
                }else {
                    projectExperienceNameValue = null;
                }
                if (!"".equals(String.valueOf(projectExperienceDescribe.getText()))){
                    projectExperienceDescribeValue = String.valueOf(projectExperienceDescribe.getText());
                }else{
                    projectExperienceDescribeValue = null;
                }
                if (!"".equals(String.valueOf(projectExperienceCompany.getText()))){
                    projectExperienceCompanyValue = String.valueOf(projectExperienceCompany.getText());
                }else {
                    projectExperienceCompanyValue = null;
                }

                if ("".equals(projectExperienceTimeStartValue) || "".equals(projectExperienceTimeEndValue)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(ProjectExprience.this, "提示", "起止时间不能为空");
                    builder.create().show();
                    return;
                }
                if (itemIndex != -1){
                    projectExperienceSaveAsyncTask projectExperienceSaveAsyncTask = new projectExperienceSaveAsyncTask();
                    projectExperienceSaveAsyncTask.execute();
                }else {
                    projectExperienceCreateAsyncTask projectExperienceCreateAsyncTask = new projectExperienceCreateAsyncTask();
                    projectExperienceCreateAsyncTask.execute();
                }
            }
        });
    }
    class projectExperienceSaveAsyncTask extends AsyncTask<String,Void,String> {
        //要传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/updateProExp.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+proExpId);   //数据库中的经验id
            url.append("/"+projectExperienceTimeStartValue);
            url.append("/"+projectExperienceTimeEndValue);
            url.append("/"+projectExperienceCompanyValue);
            url.append("/"+projectExperienceNameValue);
            url.append("/"+projectExperienceDescribeValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(ProjectExprience.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(ProjectExprience.this,Fragment_mainActivity.class);

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
    class projectExperienceCreateAsyncTask extends AsyncTask<String,Void,String> {
        //不用传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/createProExp.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+projectExperienceTimeStartValue);
            url.append("/"+projectExperienceTimeEndValue);
            url.append("/"+projectExperienceCompanyValue);
            url.append("/"+projectExperienceNameValue);
            url.append("/"+projectExperienceDescribeValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            System.out.println("创建项目经验..........."+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(ProjectExprience.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(ProjectExprience.this,Fragment_mainActivity.class);

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
