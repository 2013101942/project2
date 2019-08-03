package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class BaseInfo extends Activity {
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    private LinearLayout baseInfoParentId;
    private EditText tel,age,city,qualifications,workYear;
    private DatePicker birthdayDatePicker;
    private Spinner state;
    private TextView userName,birthday;
    private RelativeLayout birthdayBtn;
    private RadioGroup rgGender;
    private RadioButton gender0,gender1;
    private Session session = null;
    private TextView saveBtn;
    private int resumeIndex;
    private DetailedResume resume;
    private String gender,birthdayValue,ageValue,telValue,stateValue,cityValue,qualificationsValue,workYearValue;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_info);
        initView();
        initEvent();
    }
    private void initView(){
        session = Session.getSession();
        Intent intent = getIntent();
        resumeIndex = (Integer) intent.getIntExtra("viewsIndex",0);
        resume = ((List<DetailedResume>) session.get("resumeList")).get(resumeIndex);

        rgGender = (RadioGroup) findViewById(R.id.rgGender);
        baseInfoParentId = findViewById(R.id.baseInfoParentId);
        saveBtn = (TextView) findViewById(R.id.baseInfoSaveBtn);
        userName = (TextView) findViewById(R.id.baseInfoName);
        gender0 = (RadioButton)findViewById(R.id.radio0);  //女
        gender1 = (RadioButton)findViewById(R.id.radio1);  //男
        birthday = (TextView)findViewById(R.id.birthday);
        birthdayDatePicker = (DatePicker)findViewById(R.id.birthdayDatePicker);
        birthdayBtn = (RelativeLayout) findViewById(R.id.birthdayBtn);
        age = (EditText)findViewById(R.id.baseInfoAge);
        tel = (EditText)findViewById(R.id.baseInfoTel);
        state = (Spinner) findViewById(R.id.baseInfoState);
        city = (EditText)findViewById(R.id.baseInfoCity);
        qualifications = (EditText)findViewById(R.id.baseInfoQualifications);
        workYear = (EditText)findViewById(R.id.baseInfoWorkYear);
        userName.setText(resume.getResumeBean().getUserName());
        if ("1".equals(resume.getResumeBean().getGender())){
            gender1.isSelected();
        }else if ("0".equals(resume.getResumeBean().getGender())){
            gender0.isSelected();
        }
        age.setText(resume.getResumeBean().getAge());
        tel.setText(resume.getResumeBean().getTel());
        state.setAdapter(new ArrayAdapter<String>(BaseInfo.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                new String[]{"目前正在找工作","已找到工作",""}));
        if ("1".equals(resume.getResumeBean().getState())){
            state.setSelection(0);
        }else if ("0".equals(resume.getResumeBean().getState())){
            state.setSelection(1);
        }else {
            state.setSelection(2);
        }
        System.out.println("birthday..........."+resume.getResumeBean().getBirthday());
        if (!"".equals(resume.getResumeBean().getBirthday())&&resume.getResumeBean().getBirthday()!=null){
            birthday.setText(resume.getResumeBean().getBirthday().substring(0,10));
        }
        city.setText(resume.getResumeBean().getCity());
        qualifications.setText(resume.getResumeBean().getQualifications());
        workYear.setText(resume.getResumeBean().getWork_year());

    }

    private void initEvent(){
        //填写日历
        birthdayBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view1) {
                System.out.println("poputwindw..............");
                final PopupWindow popupWindow = new PopupWindow(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
                View view2 = (LinearLayout) getLayoutInflater().inflate(R.layout.base_info, null);
                View view =  (RelativeLayout) getLayoutInflater().inflate(R.layout.calendar, null);
                popupWindow.setContentView(view);
                popupWindow.setFocusable(true);
                popupWindow.setTouchable(true);
                popupWindow.setOutsideTouchable(true);
                popupWindow.showAsDropDown(baseInfoParentId, 0, 0);

                //这些为了点击非PopupWindow区域，PopupWindow会消失的，如果没有下面的
                //代码的话，你会发现，当你把PopupWindow显示出来了，无论你按多少次后退键
                //PopupWindow并不会关闭，而且退不出程序，加上下述代码可以解决这个问题
                popupWindow.setTouchable(true);
                //设置PopupWindow外部可点击

                popupWindow.setTouchInterceptor(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        System.out.println("onTouch...........");

                        birthdayDatePicker = (DatePicker)(popupWindow.getContentView()).findViewById(R.id.birthdayDatePicker);
                        int year = birthdayDatePicker.getYear();
                        int month = birthdayDatePicker.getMonth()+1;
                        int dayOfMonth = birthdayDatePicker.getDayOfMonth();
                        birthdayValue = year+"-"+month+"-"+dayOfMonth;
                        if (birthdayValue == null) birthdayValue = "1990-01-01";
                        birthday.setText(birthdayValue);
                        return false;
                        // 这里如果返回true的话，touch事件将被拦截
                        // 拦截后 PopupWindow的onTouchEvent不被调用，这样点击外部区域无法dismiss
                    }
                });
                popupWindow.showAtLocation(view2, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 50, 0);
            }
        });



            //点击保存
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            //给请求服务器的参数赋值每次点击都要获取值
                gender =  (String)((RadioButton)findViewById(rgGender.getCheckedRadioButtonId())).getText();
                System.out.println("gender................"+gender);
                if ("女".equals(gender)){
                    gender = "0";
                }else if ("男".equals(gender)){
                    gender = "1";
                }
                System.out.println("gender............"+gender);
                if (!"".equals(String.valueOf(age.getText()))){
                    ageValue = String.valueOf(age.getText());
                }else {
                    ageValue = null;
                }
                if (!"".equals(String.valueOf(tel.getText()))){
                    telValue = String.valueOf(tel.getText());
                }else {
                    telValue = null;
                }
                if ("目前正在找工作".equals(String.valueOf(state.getSelectedItem()))){
                    stateValue = "1";
                }else if ("已找到工作".equals(String.valueOf(state.getSelectedItem()))){
                    stateValue = "0";
                }else {
                    stateValue = "";
                }
                if (!"".equals(String.valueOf(city.getText()))){
                    cityValue = String.valueOf(city.getText());
                }else {
                    cityValue = null;
                }
                if (!"".equals(String.valueOf(qualifications.getText()))){
                    qualificationsValue = String.valueOf(qualifications.getText());
                }else {
                    qualificationsValue = null;
                }
                if (!"".equals(String.valueOf(workYear.getText()))){
                    workYearValue = String.valueOf(workYear.getText());
                }else {
                    workYearValue = null;
                }
                if ("".equals(birthdayValue) || "".equals(stateValue)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(BaseInfo.this, "提示", "出生年月和求职状态不能为空");
                    builder.create().show();
                    return;
                }
                BaseInfoSaveAsyncTask baseInfoSaveAsyncTask = new BaseInfoSaveAsyncTask();
                baseInfoSaveAsyncTask.execute();
            }
        });

    }
    class BaseInfoSaveAsyncTask extends AsyncTask<String,Void,String> {

        @Override
        protected String doInBackground(String... strings) {
            String userId = (String) session.get("userId");
            String resumeName = resume.getResumeBean().getResumeName();

            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/updateResumeBaseInfo.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+gender);
            url.append("/"+birthdayValue);
            url.append("/"+ageValue);
            url.append("/"+telValue);
            url.append("/"+stateValue);
            url.append("/"+cityValue);
            url.append("/"+qualificationsValue);
            url.append("/"+workYearValue);
            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(BaseInfo.this, "提示", "保存成功");
            builder.create().show();

            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(BaseInfo.this,Fragment_mainActivity.class);

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
