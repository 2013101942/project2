package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
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
import java.util.List;


/**
 * Created by YQ on 2019/4/22.
 */

//没法在有简历的情况下跳到Fragment_mainActivity创建简历，只能另开一个界面创建
public class CreateResume extends Activity {
    private Session session = Session.getSession();
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    private EditText settingResumeName;
    private Switch settingIsPublic;
    private TextView settingSaveBtn;
    private List<DetailedResume> resumeList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_resume_setting);
        initView();
        initEvent();
    }

    private void initView(){
        settingResumeName = findViewById(R.id.settingResumeName);
        settingIsPublic = findViewById(R.id.settingIsPublic);
        settingSaveBtn = findViewById(R.id.settingSaveBtn);
        resumeList = (List<DetailedResume>) session.get("resumeList");
    }
    private void initEvent(){
        settingSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //每点击一次都要获取页面数据
                String userId = (String) session.get("userId");
                //在有简历的情况下还让新创建的简历名不能与之前的相同
                String resumeName = settingResumeName.getText().toString();
                for (int i =0; i<resumeList.size();i++){
                    System.out.println("resumeList.get(i)............"+resumeList.get(i).getResumeBean().getResumeName());
                    if (resumeList.get(i).getResumeBean().getResumeName().equals(resumeName)){

                        AlertDialog.Builder builder = DialogUtil.simpleDialog(CreateResume.this, "提示", "简历名不能相同");
                        builder.create().show();
                        return;
                    }
                }

                String isVisible = null;
                if (settingIsPublic.isSelected()){
                    isVisible = "1";
                }else {
                    isVisible = "0";
                }
                SaveASettingAsyncTask saveASettingAsyncTask = new SaveASettingAsyncTask();
                saveASettingAsyncTask.execute(userId,resumeName,isVisible);
            }
        });
    }
    class SaveASettingAsyncTask extends AsyncTask<String,Void,String> {
        @Override
        protected String doInBackground(String... strings) {
            //重新从服务器获取所有数据
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/Resume.do/createResume.do");
            String userId = strings[0];
            String resumeName = strings[1];
            String isVisible = strings[2];
            System.out.println("isVisible......."+isVisible);
            //设置头部信息
            url.append("/"+ userId);
            url.append("/"+ resumeName);
            url.append("/"+ isVisible);
            String result = HttpUtil.sendUrl(url.toString());

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(CreateResume.this, "提示", "创建成功，但是要完善哦");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(CreateResume.this,Fragment_mainActivity.class);

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
