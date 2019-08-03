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

public class Education extends Activity {
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    private EditText educationSchool,educationMajor;
    private Session session = null;
    private DetailedResume resume;
    private int itemIndex;
    private TextView educationTimeStart,educationTimeEnd,educationSaveBtn;
    private String userId,resumeName,educationTimeStartValue,educationTimeEndValue,educationSchoolValue,educationMajorValue;
    private RelativeLayout educationTimeStartBtn,educationTimeEndBtn;
    private LinearLayout educationParent;
    private DatePicker educationTimeStartDatePicker,educationTimeEndDatePicker;
    private String eduId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.education);
        initView();
        initEvent();
    }
    private void initView(){
        educationParent = (LinearLayout)findViewById(R.id.educationParent);
        educationTimeStartBtn = (RelativeLayout)findViewById(R.id.educationTimeStartBtn);
        educationTimeEndBtn = (RelativeLayout)findViewById(R.id.educationTimeEndBtn);
        educationSaveBtn = (TextView)findViewById(R.id.educationSaveBtn);
        educationTimeStart = (TextView)findViewById(R.id.educationTimeStart) ;
        educationTimeEnd = (TextView)findViewById(R.id.educationTimeEnd);
        educationSchool = (EditText)findViewById(R.id.educationSchool);
        educationMajor = (EditText)findViewById(R.id.educationMajor);

        session = Session.getSession();
        int viewIndex = (Integer) session.get("resumeIndex");
        resume = ((List<DetailedResume>) session.get("resumeList")).get(viewIndex);
        resumeName = resume.getResumeBean().getResumeName();
        userId = (String) session.get("userId");

        Intent intent = getIntent();
        itemIndex = intent.getIntExtra("itemIndex",-1);
        if (itemIndex != -1){
            eduId = resume.getEducationList().get(itemIndex).getEduId();
            educationTimeStart.setText(StringUtils.dateFmtChange(resume.getEducationList().get(itemIndex).getYearsStart()));
            educationTimeEnd.setText(StringUtils.dateFmtChange(resume.getEducationList().get(itemIndex).getYearsEnd()));
            educationSchool.setText(resume.getEducationList().get(itemIndex).getSchool());
            educationMajor.setText(resume.getEducationList().get(itemIndex).getMajor());
        }
    }
    private void initEvent(){
        educationTimeStartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.education, null);
                View view1 =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view1);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(educationParent, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        educationTimeStartDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                        int year = educationTimeStartDatePicker.getYear();
                        int month = educationTimeStartDatePicker.getMonth()+1;
                        int dayOfMonth = educationTimeStartDatePicker.getDayOfMonth();
                        educationTimeStartValue = year+"-"+month+"-"+dayOfMonth;
                        educationTimeStart.setText(educationTimeStartValue);
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });
        educationTimeEndBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.education, null);
                View view1 =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view1);
                popupWindow.setFocusable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(educationParent, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        educationTimeEndDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                        int year = educationTimeEndDatePicker.getYear();
                        int month = educationTimeEndDatePicker.getMonth()+1;
                        int dayOfMonth = educationTimeEndDatePicker.getDayOfMonth();
                        educationTimeEndValue = year+"-"+month+"-"+dayOfMonth;
                        educationTimeEnd.setText(educationTimeEndValue);
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });

        educationSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(String.valueOf(educationTimeStart.getText()))){
                    educationTimeStartValue = String.valueOf(educationTimeStart.getText());
                }else {
                    educationTimeStartValue = null;
                }
                if (!"".equals(String.valueOf(educationTimeEnd.getText()))){
                    educationTimeEndValue = String.valueOf(educationTimeEnd.getText());
                }else {
                    educationTimeEndValue = null;
                }
                if (!"".equals(String.valueOf(educationSchool.getText()))){
                    educationSchoolValue = String.valueOf(educationSchool.getText());
                }else {
                    educationSchoolValue = null;
                }
                if (!"".equals(String.valueOf(educationMajor.getText()))){
                    educationMajorValue = String.valueOf(educationMajor.getText());
                }else {
                    educationMajorValue = null;
                }

                if ("".equals(educationTimeStartValue) || "".equals(educationTimeEndValue)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(Education.this, "提示", "起止时间不能为空");
                    builder.create().show();
                    return;
                }

                if (itemIndex != -1){
                    EducationSaveAsyncTask educationSaveAsyncTask = new EducationSaveAsyncTask();
                    educationSaveAsyncTask.execute();
                }else {
                    EducationCreateAsyncTask educationCreateAsyncTask = new EducationCreateAsyncTask();
                    educationCreateAsyncTask.execute();
                }
            }
        });
    }
    class EducationSaveAsyncTask extends AsyncTask<String,Void,String> {
        //要传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/updateEducation.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+eduId);   //数据库中的经验id
            url.append("/"+educationTimeStartValue);
            url.append("/"+educationTimeEndValue);
            url.append("/"+educationSchoolValue);
            url.append("/"+educationMajorValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(Education.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(Education.this,Fragment_mainActivity.class);

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
    class EducationCreateAsyncTask extends AsyncTask<String,Void,String> {
        //不用传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/createEducation.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+educationTimeStartValue);
            url.append("/"+educationTimeEndValue);
            url.append("/"+educationSchoolValue);
            url.append("/"+educationMajorValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(Education.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(Education.this,Fragment_mainActivity.class);

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
