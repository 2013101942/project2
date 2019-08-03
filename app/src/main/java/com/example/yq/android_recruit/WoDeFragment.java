package com.example.yq.android_recruit;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.pojo.PersonCompany;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.JsonUtil;
import com.example.yq.util.Session;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by YQ on 2019/4/17.
 */

public class WoDeFragment extends Fragment implements View.OnClickListener {
    private LinearLayout collected,applyed,successed;
    private TextView collectText,applyText,successText;
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_04, container, false);
        initView(view);
        initEvent();
        return view;
    }
    private void initView(View view){
        collected = view.findViewById(R.id.collectId);
        applyed = view.findViewById(R.id.applyId);
        successed = view.findViewById(R.id.successId);

        collectText = view.findViewById(R.id.collectTextId);
        applyText = view.findViewById(R.id.applyTextId);
        successText = view.findViewById(R.id.successTextId);
        //" 我的"里的三个列表，向三个列表里加子标签（TextView标签）作为tag属性值
        collected.setTag(collectText);
        applyed.setTag(applyText);
        successed.setTag(successText);
    }

    private void initEvent(){
        collected.setOnClickListener(this);
        applyed.setOnClickListener(this);
        successed.setOnClickListener(this);
    }
   //点击按钮时触发异步任务
    @Override
    public void onClick(View view) {
        MsgAsyncTask msgAsyncTask = new MsgAsyncTask();
        TextView subView = (TextView) view.getTag();
        //把点击了的那项的子标签TextView作为参数给异步任务
        msgAsyncTask.execute(subView.getText().toString());
    }
    //用异步任务进行网络请求
    class MsgAsyncTask extends AsyncTask<String ,Void,String> {
        Session session = Session.getSession();
        String userId = (String) session.get("userId");
        Intent intent = new Intent();
        FragmentActivity fragmentActivity = getActivity();
        @Override
        protected String doInBackground(String... strings) {

            StringBuffer url = null;
            String text = strings[0];
            if ("我的收藏".equals(text)){
                url = new StringBuffer("http://192.168.56.1:8099/collection/collectRecruitList");
                url.append("/"+userId);
                String result = HttpUtil.sendUrl(url.toString());
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                try {
                    List<DivAppRecruitInfo> collectList =  objectMapper.readValue( result, javaType);
                    //如果点击“我的收藏”，则collectList覆盖之前的collectOrApplyOrAdoptList
                    session.put("collectOrApplyOrAdoptList",collectList);
                    intent.setClass(fragmentActivity,BlankRecruitActivity.class);
                    intent.putExtra("titleName","我的收藏");  //根据此属性，决定在下一个列表页点击后进入哪个详细信息页
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if ("已申请".equals(text)){
                url = new StringBuffer("http://192.168.56.1:8099/Recruit/checkApplyRecruit.do");
                url.append("/"+userId);
                String result = HttpUtil.sendUrl(url.toString());
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                try {
                    List<DivAppRecruitInfo> appliedRecruitList =  objectMapper.readValue( result, javaType);
                    //如果点击“已申请”，则appliedRecruitList覆盖之前的collectOrApplyOrAdoptList
                    session.put("collectOrApplyOrAdoptList",appliedRecruitList);
                    intent.setClass(fragmentActivity,BlankRecruitActivity.class);
                    intent.putExtra("titleName","已申请");
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if ("已通过".equals(text)){
                url = new StringBuffer("http://192.168.56.1:8099/Recruit/checkAdoptedRecruit.do");
                url.append("/"+userId);
                String result = HttpUtil.sendUrl(url.toString());
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                try {
                    List<DivAppRecruitInfo> adoptedRecruitList =  objectMapper.readValue( result, javaType);
                    //如果点击“已通过”，则adoptedRecruitList覆盖之前的collectOrApplyOrAdoptList
                    session.put("collectOrApplyOrAdoptList",adoptedRecruitList);
                    intent.setClass(fragmentActivity,BlankRecruitActivity.class);
                    intent.putExtra("titleName","已通过");
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
}
