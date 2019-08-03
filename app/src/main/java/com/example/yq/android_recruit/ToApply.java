package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yq.pojo.DetailedResume;
import com.example.yq.util.DialogUtil;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.Session;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by YQ on 2019/5/11.
 */

public class ToApply extends Activity {
    private Session session = null;
    private Spinner resumeNameSpinner;
    private Button toApplyBtn;
    private List<String> resumeNameList = new ArrayList<>();  //数据源
    private List<DetailedResume> resumeList = null;
    private Intent intent = null;
    private String personId,companyId,recruitId;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.to_apply);
        initView();
        initEvent();
    }
    private void initView(){
        resumeNameSpinner = findViewById(R.id.toApplyResumeNameList);
        toApplyBtn = findViewById(R.id.toApplyBtn);
        //获取所需的值
        session = Session.getSession();
        resumeList = (List<DetailedResume>) session.get("resumeList");
        for (DetailedResume resume: resumeList){
            resumeNameList.add(resume.getResumeBean().getResumeName());
        }
        intent = getIntent();
        personId = intent.getStringExtra("personId");
        companyId = intent.getStringExtra("companyId");
        recruitId = intent.getStringExtra("recruitId");
        //设置选项菜单
        resumeNameSpinner.setAdapter(new ArrayAdapter<String>(ToApply.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                resumeNameList));
        resumeNameSpinner.setSelection(0);
    }
    private void initEvent(){
        toApplyBtn.setOnClickListener(new View.OnClickListener() {
            String resumeName = null;
            @Override
            public void onClick(View view) {
                resumeName = (String) resumeNameSpinner.getSelectedItem();
                ToApplayAsyncTask toApplayAsyncTask = new ToApplayAsyncTask();
                toApplayAsyncTask.execute(personId,companyId,recruitId,resumeName);
            }
        });
    }

    class ToApplayAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            System.out.println("点击了投递....");
            String personId = strings[0];
            String companyId = strings[1];
            String recruitId = strings[2];
            String resumeName = strings[3];
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/apply");
            url.append("/"+personId);
            url.append("/"+companyId);
            url.append("/"+recruitId);
            url.append("/"+resumeName);
            String result = HttpUtil.sendUrl(url.toString());
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if ("deliver success".equals(result)){
                AlertDialog.Builder builder = DialogUtil.simpleDialog(ToApply.this, "提示", "投递成功");
                builder.create().show();
            }
        }
    }
}
