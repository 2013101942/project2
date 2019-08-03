package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yq.pojo.PersonCompany;
import com.example.yq.util.Session;

import java.util.List;


/**
 * Created by YQ on 2019/4/25.
 */

public class MsgContent extends Activity {
    private TextView msg_contentCompanyOrPersonName,msg_contentTime,msg_content_content;
    private Intent intent = null;
    private Session session = null;
    private  List<PersonCompany> msgList = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.msg_content);
        initView();
    }
    private void initView(){
        msg_contentCompanyOrPersonName = findViewById(R.id.msg_contentCompanyOrPersonName);
        msg_contentTime = findViewById(R.id.msg_contentTime);
        msg_content_content = findViewById(R.id.msg_content_content);
        session = Session.getSession();
        //点击人事来信HTTPURLConnection获取的是人事来信，点击已发信息HTTPURLConnection获取的是已发信息
        msgList= ( List<PersonCompany>)session.get("MsgList");
        intent = getIntent();
        int msgIndex = intent.getIntExtra("msgIndex", 0);
        msg_contentCompanyOrPersonName.setText(msgList.get(msgIndex).getUserName());
        msg_contentTime.setText(msgList.get(msgIndex).getTimes()+"");
        msg_content_content.setText(msgList.get(msgIndex).getMessage());

    }

}
