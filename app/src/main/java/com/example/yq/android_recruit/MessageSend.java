package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.yq.util.DialogUtil;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.Session;
import com.example.yq.util.StringUtils;


/**
 * Created by YQ on 2019/5/11.
 */

public class MessageSend extends Activity {
    private TextView companyNameTextView;
    private EditText messageEditText;
    private Button sendBtn;
    private String companyName,message,personId,companyId,recruitId;
    private Intent intent = null;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.message_send);
        initView();
        initEvent();
    }
    private void initView(){
        companyNameTextView = findViewById(R.id.messageSendCompanyName);
        messageEditText = findViewById(R.id.messageSend_content);
        sendBtn = findViewById(R.id.messageSendBtn);

        intent = getIntent();
        personId = intent.getStringExtra("personId");
        companyName = intent.getStringExtra("companyName");
        companyId = intent.getStringExtra("companyId");
        recruitId = intent.getStringExtra("recruitId");
        message = String.valueOf(messageEditText.getText());

        companyNameTextView.setText(companyName);

    }
    private void initEvent(){
        sendBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ("".equals(String.valueOf(messageEditText.getText()))){
                    message = null;
                }else {
                    message = String.valueOf(messageEditText.getText());
                }
                SendMsgAsyncTask sendMsgAsyncTask = new SendMsgAsyncTask();
                sendMsgAsyncTask.execute(personId,companyId,recruitId,message);
            }
        });
    }
    class SendMsgAsyncTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {
            String personId = strings[0];
            String companyId = strings[1];
            String recruitId = strings[2];
            String message = strings[3];
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/message.do/app_send");
            url.append("/"+personId);
            url.append("/"+companyId);
            url.append("/"+recruitId);
            url.append("/"+message);
            String result = HttpUtil.sendUrl(url.toString());
            System.out.println("发送留言........."+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            if ("send success".equals(result)){
                AlertDialog.Builder builder = DialogUtil.simpleDialog(MessageSend.this, "提示", "发送成功");
                builder.create().show();
            }
        }
    }
}
