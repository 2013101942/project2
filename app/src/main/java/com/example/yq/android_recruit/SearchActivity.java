package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.pojo.Page;
import com.example.yq.util.HttpUtil;
import com.example.yq.util.JsonUtil;
import com.example.yq.util.Session;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.type.JavaType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;


/**
 * Created by YQ on 2019/4/26.
 */

public class SearchActivity extends Activity {
    private Spinner searchJobs,spinnerSalaryStart,spinnerSalaryEnd;
    private String[] salaryStarts,salaryEnds;
    private List<String> jobs ;
    private Session session;
    private List<String> salaryStartList = new ArrayList<String>(),salaryEndList = new ArrayList<String>();
    private ImageButton  searchJobImg;
    private RadioButton companyPropertySiQi,companyPropertyGuoQi,companyProperty;
    private RadioGroup  companyPropertyGroup;
    private ObjectMapper objectMapper;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search);
        initView();

    }
    private void initView(){
        objectMapper = JsonUtil.getObjectMapper();
        searchJobs = findViewById(R.id.searchJobs);
        spinnerSalaryStart = findViewById(R.id.searchSalaryStart);
        spinnerSalaryEnd = findViewById(R.id.searchSalaryEnd);
        searchJobImg = findViewById(R.id.search_searchJobBtn);
        salaryStarts =   getResources().getStringArray(R.array.salaryStart);
        salaryEnds = getResources().getStringArray(R.array.salaryEnd);
        companyPropertyGroup = (RadioGroup) findViewById(R.id.search_companyProperty);
        companyPropertySiQi = findViewById(R.id.searchCompanyPropertySiQi);
        companyPropertyGuoQi = findViewById(R.id.searchCompanyPropertyGuoQi);

        for (String str:salaryStarts){
            salaryStartList.add(str);
        }
        for (String str:salaryEnds){
            salaryEndList.add(str);
        }
        session = Session.getSession();
        jobs = (List<String>) session.get("jobs");
        jobs.add("");
        spinnerSalaryStart.setAdapter(new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                salaryStartList));
        spinnerSalaryEnd.setAdapter(new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                salaryEndList));
        searchJobs.setAdapter(new ArrayAdapter<String>(SearchActivity.this,
                android.R.layout.simple_list_item_1,
                android.R.id.text1,
                jobs));
        spinnerSalaryStart.setSelection(0);
        spinnerSalaryEnd.setSelection(0);
        searchJobs.setSelection(jobs.size()-1);

        //给搜索按钮（小搜索图片）加事件
        searchJobImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //用户选择了之后需要再次获取布局文件中的值
                companyProperty =  (RadioButton)findViewById(companyPropertyGroup.getCheckedRadioButtonId());
                String value = "?property="+companyProperty.getText()+"&job="+searchJobs.getSelectedItem().toString()+"&salaryStart="+spinnerSalaryStart.getSelectedItem().toString()+"&salaryEnd="+spinnerSalaryEnd.getSelectedItem().toString();
                System.out.println(value);
                //用异步任务
                SearchAsyncTask searchAsyncTask = new SearchAsyncTask();
                searchAsyncTask.execute(value);
            }
        });
        //可以不用监听器，如果有相关的业务也可以用
//        companyPropertyGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            //  一个监听器可以监听多个RadioGroup ，通过RadioGroup参数指定监听的是哪个
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                if (i == companyPropertyGuoQi.getId()){
//                    companyProperty = companyPropertyGuoQi;
//                    System.out.println("选中了国企");
//                }else if (i == companyPropertySiQi.getId()){
//                    companyProperty = companyPropertySiQi;
//                    System.out.println("选中了私企");
//                }
//            }
//        });
    }
    class SearchAsyncTask extends AsyncTask<String ,Void,String> {

        //传多个字符串，在后台验证，返回结果
        @Override
        protected String doInBackground(String... strings) {

            String value = strings[0];
            StringBuffer url = new StringBuffer("http://192.168.56.1:8099/android_recruit/appRecruitInfoPage");
            //设置头部信息
            url.append(value);
            System.out.println(value);
            String result = HttpUtil.sendUrl(url.toString());
            System.out.println("result..."+result);
            return result;
        }
        //只能在此处改变 UI
        @Override
        protected void onPostExecute(String result) {
            //将page字符串转化成对象，在跳转activity
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString = objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                session.put("beansList",beansList);
                Intent intent = new Intent();
                intent.setClass(SearchActivity.this,Fragment_mainActivity.class);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
