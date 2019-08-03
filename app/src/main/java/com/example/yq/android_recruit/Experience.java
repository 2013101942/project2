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

public class Experience extends Activity {
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    private EditText experienceCompanyName,experienceJob,experienceJobDescribe;
    private Session session = null;
    private DetailedResume resume;
    private String userId,resumeName,experienceTimeStartValue,experienceTimeEndValue,experienceCompanyNameValue,experienceJobValue,experienceJobDescribeValue;
    private int itemIndex;
    private String expId;
    private TextView experienceTimeStart,experienceTimeEnd,experienceSaveBtn;
    private RelativeLayout experienceTimeStartBtn,experienceTimeEndBtn;
    private LinearLayout experienceParent;
    private DatePicker experienceTimeStartDatePicker,experienceTimeEndDatePicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.experience);
        initView();
        initEvent();
    }
    private void initView(){
        experienceParent = (LinearLayout)findViewById(R.id.experienceParent);
        experienceSaveBtn = (TextView)findViewById(R.id.experienceSaveBtn);
        experienceTimeStartBtn = (RelativeLayout)findViewById(R.id.experienceTimeStartBtn);
        experienceTimeEndBtn = (RelativeLayout)findViewById(R.id.experienceTimeEndBtn);
        experienceTimeStart = (TextView)findViewById(R.id.experienceTimeStart);
        experienceTimeEnd = (TextView)findViewById(R.id.experienceTimeEnd);
        experienceCompanyName = (EditText)findViewById(R.id.experienceCompanyName);
        experienceJob = (EditText)findViewById(R.id.experienceJob);
        experienceJobDescribe = (EditText)findViewById(R.id.experienceJobDescribe);

        session = Session.getSession();
        int viewIndex = (Integer) session.get("resumeIndex"); //当点击简历首页进入resumeList时，就改变了resumeIndex
        resume = ((List<DetailedResume>) session.get("resumeList")).get(viewIndex);
        userId = (String) session.get("userId");
        resumeName = resume.getResumeBean().getResumeName();

        Intent intent = getIntent();
        itemIndex = intent.getIntExtra("itemIndex",-1);
        if (itemIndex != -1 ){
            //当传递itemIndex时，就是显示和保存,否则就是添加item
            expId = resume.getExperienceList().get(itemIndex).getExpId();
            System.out.println("工作经验expId............"+resume.getExperienceList().get(itemIndex).getExpId());
            experienceTimeStart.setText(StringUtils.dateFmtChange(resume.getExperienceList().get(itemIndex).getYearsStart()));
            experienceTimeEnd.setText(StringUtils.dateFmtChange(resume.getExperienceList().get(itemIndex).getYearsEnd()));
            experienceCompanyName.setText(resume.getExperienceList().get(itemIndex).getCompanyName());
            experienceJob.setText(resume.getExperienceList().get(itemIndex).getJob());
            experienceJobDescribe.setText(resume.getExperienceList().get(itemIndex).getJobDescribe());
        }
    }
    private void initEvent(){

        experienceTimeStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.experience, null);
                View view1 =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view1);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(experienceParent, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                    System.out.println("onTouch...........");
                    experienceTimeStartDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                    int year = experienceTimeStartDatePicker.getYear();
                    int month = experienceTimeStartDatePicker.getMonth()+1;
                    int dayOfMonth = experienceTimeStartDatePicker.getDayOfMonth();
                    experienceTimeStartValue = year+"-"+month+"-"+dayOfMonth;

                    experienceTimeStart.setText(experienceTimeStartValue);
                    return false;
                    // 这里如果返回true的话，touch事件将被拦截
                    // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });
        experienceTimeEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.experience, null);
                View view1 =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view1);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(experienceParent, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        experienceTimeEndDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                        int year = experienceTimeEndDatePicker.getYear();
                        int month = experienceTimeEndDatePicker.getMonth()+1;
                        int dayOfMonth = experienceTimeEndDatePicker.getDayOfMonth();
                        experienceTimeEndValue = year+"-"+month+"-"+dayOfMonth;
                        experienceTimeEnd.setText(experienceTimeEndValue);
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });
        experienceSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(String.valueOf(experienceTimeStart.getText()))){
                    experienceTimeStartValue = String.valueOf(experienceTimeStart.getText());
                }else {
                    experienceTimeStartValue = null;
                }
                if (!"".equals(String.valueOf(experienceTimeEnd.getText()))){
                    experienceTimeEndValue = String.valueOf(experienceTimeEnd.getText());
                }else {
                    experienceTimeEndValue = null;
                }if (!"".equals(String.valueOf(experienceCompanyName.getText()))){
                    experienceCompanyNameValue = String.valueOf(experienceCompanyName.getText());
                }else {
                    experienceCompanyNameValue = null;
                }
               if (!"".equals(String.valueOf(experienceJob.getText()))){
                   experienceJobValue = String.valueOf(experienceJob.getText());
               }else {
                   experienceJobValue = null;
               }
                if (!"".equals(String.valueOf(experienceJobDescribe.getText()))){
                    experienceJobDescribeValue = String.valueOf(experienceJobDescribe.getText());
                }else {
                    experienceJobDescribeValue = null;
                }


                if ("".equals(experienceTimeStartValue) || "".equals(experienceTimeEndValue)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(Experience.this, "提示", "起止时间不能为空");
                    builder.create().show();
                    return;
                }

                //当传递itemIndex时，就是显示和保存,否则就是添加item
                if (itemIndex != -1){
                    //此时是编辑已有的工作经验
                    ExperienceSaveAsyncTask experienceSaveAsyncTask = new ExperienceSaveAsyncTask();
                    experienceSaveAsyncTask.execute();
                }else{
                    //此时是创建新的工作经验
                    ExperienceCreateAsyncTask experienceCreateAsyncTask = new ExperienceCreateAsyncTask();
                    experienceCreateAsyncTask.execute();
                }
            }
        });
    }
    class ExperienceSaveAsyncTask extends AsyncTask<String,Void,String> {
        //要传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/updateExperience.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+expId);   //数据库中的经验id
            url.append("/"+experienceTimeStartValue);
            url.append("/"+experienceTimeEndValue);
            url.append("/"+experienceCompanyNameValue);
            url.append("/"+experienceJobValue);
            url.append("/"+experienceJobDescribeValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(Experience.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(Experience.this,Fragment_mainActivity.class);

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
    class ExperienceCreateAsyncTask extends AsyncTask<String,Void,String> {
        //不用传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/createExperience.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+experienceTimeStartValue);
            url.append("/"+experienceTimeEndValue);
            url.append("/"+experienceCompanyNameValue);
            url.append("/"+experienceJobValue);
            url.append("/"+experienceJobDescribeValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(Experience.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(Experience.this,Fragment_mainActivity.class);

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
