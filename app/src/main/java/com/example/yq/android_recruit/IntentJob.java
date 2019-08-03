package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yq.pojo.DetailedResume;
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
import java.util.Arrays;
import java.util.List;


/**
 * Created by YQ on 2019/4/23.
 */

public class IntentJob extends Activity {
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    private EditText intentJobAddress,intentJob,intentJobArrivalTime,intentJobSelfEvaluation,intentJobSelfTag;
    private String userId,resumeName,intentJobAddressValue,intentJobValue,intentSalaryValue,intentJobArrivalTimeValue,intentJobSelfEvaluationValue,intentJobSelfTagValue;
    private Session session;
    private Spinner intentJobSalary;
    private List<String> intentSalaryList = new ArrayList<String>();
    private TextView intentJobSaveBtn;
    private DetailedResume resume;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.intent_job);
        initView();
        initEvent();
    }

    private void initView(){
        session = Session.getSession();
        Intent intent = getIntent();
        Integer viewIndex = (Integer) intent.getIntExtra("viewsIndex",0);
        resume = ((List<DetailedResume>) session.get("resumeList")).get(viewIndex);
        resumeName = resume.getResumeBean().getResumeName();
        userId = (String) session.get("userId");
        intentJobSaveBtn = (TextView)findViewById(R.id.intentJobSaveBtn);
        intentJobAddress = (EditText)findViewById(R.id.intentJobAddress);
        intentJob = (EditText)findViewById(R.id.intentJob);
        intentJobSalary = (Spinner) findViewById(R.id.intentJobSalary);
        intentJobArrivalTime = (EditText)findViewById(R.id.intentJobArrivalTime);
        intentJobSelfEvaluation = (EditText)findViewById(R.id.intentJob_SelfEvaluation);
        intentJobSelfTag = (EditText)findViewById(R.id.intentJob_selfTag);
        for (int i =0; i<getResources().getStringArray(R.array.intentSalary).length;i++){
            intentSalaryList.add(getResources().getStringArray(R.array.intentSalary)[i]);
        }
        intentSalaryList.add("");
        intentJobSalary.setAdapter(new ArrayAdapter<String>(IntentJob.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                intentSalaryList));

        if (!"".equals(resume.getJobIntentBean()) && resume.getJobIntentBean() != null){
            intentJobAddress.setText(resume.getJobIntentBean().getCity());
            intentJob.setText(resume.getJobIntentBean().getJob());
            intentJobArrivalTime.setText(resume.getJobIntentBean().getArrivalTime());
            intentJobSelfEvaluation.setText(resume.getJobIntentBean().getSelfEvaluation());
            intentJobSelfTag.setText(resume.getJobIntentBean().getSelfTag());
            System.out.println("求职意向薪资........."+resume.getJobIntentBean().getIntentSalary());
            if (resume.getJobIntentBean().getIntentSalary() == null || "".equals(resume.getJobIntentBean().getIntentSalary())){
                intentJobSalary.setSelection(intentSalaryList.size()-1);
            }else {
                for (int i = 0;i<intentSalaryList.size();i++){
                    if (intentSalaryList.get(i).equals(resume.getJobIntentBean().getIntentSalary())){
                        System.out.println("i.............."+i);
                        intentJobSalary.setSelection(i);
                    }
                }

            }
        }
    }
    private void initEvent(){
        //创建简历时就已经创建，此处是修改
        intentJobSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!"".equals(String.valueOf(intentJobAddress.getText()))){
                    intentJobAddressValue = String.valueOf(intentJobAddress.getText());
                }else {
                    intentJobAddressValue = null;
                }
                if (!"".equals(String.valueOf(intentJob.getText()))){
                    intentJobValue = String.valueOf(intentJob.getText());
                }else {
                    intentJobValue = null;
                }
                if (!"".equals(String.valueOf(intentJobSalary.getSelectedItem()))){
                    intentSalaryValue = String.valueOf(intentJobSalary.getSelectedItem());
                }else {
                    intentSalaryValue = null;
                }
                if (!"".equals(String.valueOf(intentJobArrivalTime.getText()))){
                    intentJobArrivalTimeValue = String.valueOf(intentJobArrivalTime.getText());
                }else {
                    intentJobArrivalTimeValue = null;
                }
                if (!"".equals(String.valueOf(intentJobSelfEvaluation.getText()))){
                    intentJobSelfEvaluationValue = String.valueOf(intentJobSelfEvaluation.getText());
                }else {
                    intentJobSelfEvaluationValue = null;
                }
               if (!"".equals(String.valueOf(intentJobSelfTag.getText()))){
                   intentJobSelfTagValue = String.valueOf(intentJobSelfTag.getText());
               }else {
                   intentJobSelfTagValue = null;
               }

                if ("".equals(intentSalaryValue)){
                    AlertDialog.Builder builder = DialogUtil.simpleDialog(IntentJob.this, "提示", "意向薪资不能为空");
                    builder.create().show();
                    return;
                }
                IntentJobSaveAsyncTask intentJobSaveAsyncTask = new IntentJobSaveAsyncTask();
                intentJobSaveAsyncTask.execute();

            }
        });
    }
    class IntentJobSaveAsyncTask extends AsyncTask<String,Void,String> {
        //要传experienceId参数
        @Override
        protected String doInBackground(String... strings) {
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/updateJobIntent.do");
            url.append("/"+userId);
            url.append("/"+resumeName);
            url.append("/"+intentJobAddressValue);
            url.append("/"+intentJobValue);
            url.append("/"+intentSalaryValue);
            url.append("/"+intentJobArrivalTimeValue);
            url.append("/"+intentJobSelfEvaluationValue);
            url.append("/"+intentJobSelfTagValue);

            System.out.println("url.toString()..........."+url.toString());
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(IntentJob.this, "提示", "保存成功");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(IntentJob.this,Fragment_mainActivity.class);

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
