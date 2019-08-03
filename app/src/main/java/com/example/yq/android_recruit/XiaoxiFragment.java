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

public class XiaoxiFragment extends Fragment implements View.OnClickListener{
    private    LinearLayout arriedMsg,sendedMsg;
    private  TextView arrivedMsgText,sendedMsgText;
    private static ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tab_02, container, false);
        initView(view);
        initEvent();
        return view;
    }
    private void initView(View view){
        arriedMsg = (LinearLayout) view.findViewById(R.id.arrivedMsgId);
        sendedMsg = (LinearLayout) view.findViewById(R.id.sendedMsgId);
        arrivedMsgText = (TextView) view.findViewById(R.id.arrivedMsgText);
        sendedMsgText = (TextView) view.findViewById(R.id.sendedMsgText);
        arriedMsg.setTag(arrivedMsgText);
        sendedMsg.setTag(sendedMsgText);
    };
    private void initEvent(){
        arriedMsg.setOnClickListener(this);
        sendedMsg.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        MsgAsyncTask msgAsyncTask = new MsgAsyncTask();
        TextView subView = (TextView) view.getTag();
        msgAsyncTask.execute(subView.getText().toString());
        }
    class MsgAsyncTask extends AsyncTask<String ,Void,String>{
        Session session = Session.getSession();
        String userId = (String) session.get("userId");
        Intent intent = new Intent();
        FragmentActivity fragmentActivity = getActivity();
        @Override
        protected String doInBackground(String... strings) {

            StringBuffer url = null;
            String text = strings[0];
            if ("人事来信".equals(text)){
                url = new StringBuffer("http://192.168.56.1:8099/message.do/app_received");
                url.append("/"+userId);
                String result = HttpUtil.sendUrl(url.toString());
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, PersonCompany.class);
                try {
                    List<PersonCompany> receivedMsgList =  objectMapper.readValue( result, javaType);
                    session.put("MsgList",receivedMsgList);
                    intent.setClass(fragmentActivity,BlankMsgActivity.class);
                    startActivity(intent);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else if ("已发信息".equals(text)){
                url = new StringBuffer("http://192.168.56.1:8099/message.do/app_sended");
                url.append("/"+userId);
                String result = HttpUtil.sendUrl(url.toString());
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, PersonCompany.class);
                try {
                    List<PersonCompany> sendedMsgList =  objectMapper.readValue( result, javaType);
                    session.put("MsgList",sendedMsgList);
                    intent.setClass(fragmentActivity,BlankMsgActivity.class);
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



