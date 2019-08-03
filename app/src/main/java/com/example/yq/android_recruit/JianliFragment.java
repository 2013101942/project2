package com.example.yq.android_recruit;

import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
 * Created by YQ on 2019/4/17.
 */

public class JianliFragment extends Fragment {
    private Session session = Session.getSession();
    private ObjectMapper objectMapper = JsonUtil.getObjectMapper();
    //跟查看简历有关
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private View[] views = null;
    private LinearLayout resumeTopLinearLayout = null;
    private TextView[] textViews = null;
    private List<DetailedResume> resumeList = null;

    //此页展示创建简历时
    private EditText resumeNameEdit = null;
    private Switch isPublicSwitch = null;
    private TextView resumeSaveBtn = null;
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        resumeList = (List<DetailedResume>) session.get("resumeList");
        View view = null ;
        if (resumeList.size() == 0){
            view = inflater.inflate(R.layout.create_resume_setting, container, false);
            //此页展示创建简历时
            resumeNameEdit = view.findViewById(R.id.settingResumeName);
            isPublicSwitch = view.findViewById(R.id.settingIsPublic);
            resumeSaveBtn = view.findViewById(R.id.settingSaveBtn);

            resumeSaveBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //每点击一次都要获取页面数据
                    String userId = (String) session.get("userId");
                    String resumeName = resumeNameEdit.getText().toString();
                    String isPublic;
                    if(isPublicSwitch.isChecked()){
                         isPublic = "1";
                    }else {
                         isPublic = "0";
                    }
                    SaveASettingAsyncTask saveASettingAsyncTask = new SaveASettingAsyncTask();
                    saveASettingAsyncTask.execute(userId,resumeName,isPublic);
                }
            });


        }else {
            view = inflater.inflate(R.layout.tab_03, container, false);  //onCreateView默认要返回的view
            resumeTopLinearLayout = view.findViewById(R.id.resume_topId);
            viewPager = (ViewPager)view.findViewById(R.id.viewPagerId);
            views = new View[resumeList.size()];        //要创建的简历页面view

            //多个简历上面的圆圈
           textViews = new TextView[resumeList.size()];
            for (int i = 0; i<resumeList.size(); i++){
                textViews[i] = new TextView(getActivity());  //无需设置id
                textViews[i].setHeight(20);
                textViews[i].setWidth(20);
                textViews[i].setBackgroundResource(R.drawable.bg_resumetop);

                LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT,ViewPager.LayoutParams.WRAP_CONTENT);  //参数分别是textView[i]的宽高
                lp.setMargins(30,5,0,0);
                textViews[i].setLayoutParams(lp);
                resumeTopLinearLayout.addView(textViews[i]);
            }

            initView(resumeList.size());
            initEvent();
        }
        return view;
    }

    private void initView(int num){
        resetTextView();
        textViews[0].setBackgroundColor(Color.RED);
        LayoutInflater layoutInflater = LayoutInflater.from(getActivity());  //用来创建简历页面
        //创建简历的页面，num个
        for (int i=0; i<num;i++){
            views[i] = layoutInflater.inflate(R.layout.resume, null);
            //装载这个新建的简历页面
            loadResume(views[i],i);
            //给页面大项添加事件
            loadEvent(views[i],i);
        }
        pagerAdapter = new PagerAdapter() {
            //向容器中装载view（这只是其中一步，下一步是isViewFromObject（））
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = views[position];
                container.addView(view);
                return view;

            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView(views[position]);

            }

            @Override
            public int getCount() {
                return views.length;
            }

            //而isViewFromObject方法是用来判断pager的一个view是否和instantiateItem方法返回的object有关联，
            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;     //必不可少
            }
        };
        viewPager.setAdapter(pagerAdapter);
    }
    //装载这个新建的简历页面
   private void loadResume(View view,int i){
       TextView userName = view.findViewById(R.id.userName);
       userName.setText(resumeList.get(i).getResumeBean().getUserName());
       TextView gender = view.findViewById(R.id.gender);
       if ("1".equals(resumeList.get(i).getResumeBean().getGender())){
           gender.setText("男");
       }else if ("0".equals(resumeList.get(i).getResumeBean().getGender())){
           gender.setText("女");
       }
        //头部
       TextView resumeName = (TextView)view.findViewById(R.id.resumeNameId);
       resumeName.setText(resumeList.get(i).getResumeBean().getResumeName());
       TextView isPublic = (TextView)view.findViewById(R.id.isPublicId);
       if("1".equals(resumeList.get(i).getResumeBean().getIsvisible())){
           isPublic.setText("公开");
       }else {if ("0".equals(resumeList.get(i).getResumeBean().getIsvisible()))
           isPublic.setText("保密");
       }

       //内容
       TextView age = view.findViewById(R.id.age);
       age.setText(resumeList.get(i).getResumeBean().getAge());
       TextView city = view.findViewById(R.id.city);
       city.setText(resumeList.get(i).getResumeBean().getCity());
       TextView workYear = view.findViewById(R.id.workYear);
       workYear.setText(resumeList.get(i).getResumeBean().getWork_year());
       TextView tel = view.findViewById(R.id.tel);
       tel.setText(resumeList.get(i).getResumeBean().getTel());
       TextView state = view.findViewById(R.id.state);
       if ("1".equals(resumeList.get(i).getResumeBean().getState())){
           state.setText("目前正在找工作");
       }else if ("0".equals(resumeList.get(i).getResumeBean().getState())){
           state.setText("已找到工作");
       }

       //添加工作经验
       for (int n =0; n<resumeList.get(i).getExperienceList().size(); n++){
           LinearLayout experience = view.findViewById(R.id.workExperienceId);
           LinearLayout experienceItem = new LinearLayout(getActivity());
           experienceItem.setOrientation(LinearLayout.HORIZONTAL);
           TextView experienceTimeStart = new TextView(getActivity());
           experienceTimeStart.setText(resumeList.get(i).getExperienceList().get(n).getYearsStart());
           TextView divideMark = new TextView(getActivity());
           divideMark.setText("-");
           TextView experienceTimeEnd = new TextView(getActivity());
           experienceTimeEnd.setText(resumeList.get(i).getExperienceList().get(n).getYearsEnd());
           TextView experienceCompanyName = new TextView(getActivity());
           experienceCompanyName.setText(resumeList.get(i).getExperienceList().get(n).getCompanyName());
           LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT,ViewPager.LayoutParams.WRAP_CONTENT);  //参数分别是textView[i]的宽高
           lp.setMargins(10,0,0,0);
           experienceCompanyName.setLayoutParams(lp);
           experienceItem.addView(experienceTimeStart);
           experienceItem.addView(divideMark);
           experienceItem.addView(experienceTimeEnd);
           experienceItem.addView(experienceCompanyName);
           experience.addView(experienceItem);
       }
       System.out.println("工作 经验有多少个"+resumeList.get(i).getExperienceList().size());
    //添加教育经历
       for(int n=0; n<resumeList.get(i).getEducationList().size(); n++){
           LinearLayout education = view.findViewById(R.id.educationId);
           LinearLayout educationItem = new LinearLayout(getActivity());
           educationItem.setOrientation(LinearLayout.HORIZONTAL);
           TextView educationTimeStart = new TextView(getActivity());
           educationTimeStart.setText(resumeList.get(i).getEducationList().get(n).getYearsStart());
           TextView divideMark = new TextView(getActivity());
           divideMark.setText("-");
           TextView educationTimeEnd = new TextView(getActivity());
           educationTimeEnd.setText(resumeList.get(i).getEducationList().get(n).getYearsEnd());
           TextView educationSchool = new TextView(getActivity());
           educationSchool.setText(resumeList.get(i).getEducationList().get(n).getSchool());
           LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT,ViewPager.LayoutParams.WRAP_CONTENT);  //参数分别是textView[i]的宽高
           lp.setMargins(10,0,0,0);
           educationSchool.setLayoutParams(lp);
           educationItem.addView(educationTimeStart);
           educationItem.addView(divideMark);
           educationItem.addView(educationTimeEnd);
           educationItem.addView(educationSchool);
           education.addView(educationItem);
       }
       System.out.println("教育经验有多少个"+resumeList.get(i).getEducationList().size());
     //添加求职意向
        if (resumeList.get(i).getJobIntentBean() != null){
            TextView intentJob = view.findViewById(R.id.intentJob);
            intentJob.setText(resumeList.get(i).getJobIntentBean().getJob());
            TextView intentAddr = view.findViewById(R.id.intentAddr);
            intentAddr.setText(resumeList.get(i).getJobIntentBean().getCity());
            TextView intentSalary = view.findViewById(R.id.intentSalary);
            intentSalary.setText(resumeList.get(i).getJobIntentBean().getIntentSalary());
            TextView selfFlag = view.findViewById(R.id.self_flag);
            selfFlag.setText(resumeList.get(i).getJobIntentBean().getSelfTag());
        }
    //项目经验
       for (int n=0; n<resumeList.get(i).getProjectExprienceList().size(); n++){
         LinearLayout projectExperience = view.findViewById(R.id.projectExperienceId);
         LinearLayout projectExperienceItem = new LinearLayout(getActivity());
           projectExperienceItem.setOrientation(LinearLayout.HORIZONTAL);
         TextView projectTimesStart = new TextView(getActivity());
           projectTimesStart.setText(resumeList.get(i).getProjectExprienceList().get(n).getTimeStart());
         TextView divideMark = new TextView(getActivity());
           divideMark.setText("-");
         TextView projectTimesEnd = new TextView(getActivity());
           projectTimesEnd.setText(resumeList.get(i).getProjectExprienceList().get(n).getTimeEnd());
         TextView  projectName = new TextView(getActivity());
           projectName.setText(resumeList.get(i).getProjectExprienceList().get(n).getProjectName());
           LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT,ViewPager.LayoutParams.WRAP_CONTENT);  //参数分别是textView[i]的宽高
           lp.setMargins(10,0,0,0);
           projectName.setLayoutParams(lp);
           projectExperienceItem.addView(projectTimesStart);
           projectExperienceItem.addView(divideMark);
           projectExperienceItem.addView(projectTimesEnd);
           projectExperienceItem.addView(projectName);
           projectExperience.addView(projectExperienceItem);
       }
       System.out.println("项目经验有多少个"+resumeList.get(i).getProjectExprienceList().size());
       //添加在校及工作情况
       for (int n=0; n<resumeList.get(i).getEduJobConditionList().size(); n++){
           LinearLayout educationJobCondition = view.findViewById(R.id.educationJobConditionId);
           LinearLayout educationJobConditionItem = new LinearLayout(getActivity());
             educationJobConditionItem.setOrientation(LinearLayout.HORIZONTAL);
           TextView eduJobTimes = new TextView(getActivity());
             eduJobTimes.setText(resumeList.get(i).getEduJobConditionList().get(n).getTimes());
           TextView eduJobContent = new TextView(getActivity());
             eduJobContent.setText(resumeList.get(i).getEduJobConditionList().get(n).getConDescribe());
           LinearLayout.LayoutParams lp =  new LinearLayout.LayoutParams(ViewPager.LayoutParams.WRAP_CONTENT,ViewPager.LayoutParams.WRAP_CONTENT);  //参数分别是textView[i]的宽高
             lp.setMargins(10,0,0,0);
             eduJobContent.setLayoutParams(lp);
             educationJobConditionItem.addView(eduJobTimes);
             educationJobConditionItem.addView(eduJobContent);
             educationJobCondition.addView(educationJobConditionItem);
       }
       System.out.println("在校及工作情况有多少个"+resumeList.get(i).getEduJobConditionList().size());
   }
    //给页面中的每个大项添加事件
    private void loadEvent(View view,final int i){
        //给头部的设置加事件
        RelativeLayout resumeSetting = view.findViewById(R.id.resumeSettingId);
        resumeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),SettingActivity.class);
                intent.putExtra("resumeIndex",i);
                startActivity(intent);
            }
        });
        //给基本信息头部加事件
        LinearLayout baseInfoTitle = (LinearLayout) view.findViewById(R.id.baseInfoTitle);
        baseInfoTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),BaseInfo.class);
                intent.putExtra("viewsIndex",i);
                startActivity(intent);
            }
        });
        //给工作经验头部加事件
        LinearLayout experienceTitle = (LinearLayout) view.findViewById(R.id.experienceTitle);
        experienceTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ExperienceList.class);
                intent.putExtra("viewsIndex",i);
                startActivity(intent);
            }
        });
        LinearLayout educationTitle = (LinearLayout) view.findViewById(R.id.educationTitle);
        educationTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),EducationList.class);
                intent.putExtra("viewsIndex",i);
                startActivity(intent);
            }
        });
        LinearLayout intentJobTitle = (LinearLayout) view.findViewById(R.id.intentJobTitle);
        intentJobTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),IntentJob.class);
                intent.putExtra("viewsIndex",i);
                startActivity(intent);
            }
        });
        LinearLayout projectExperienceTitle = (LinearLayout) view.findViewById(R.id.projectExperienceTitle);
        projectExperienceTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),ProjectExperienceList.class);
                intent.putExtra("viewsIndex",i);
                startActivity(intent);
            }
        });
        LinearLayout educationJobConditionTitle = (LinearLayout) view.findViewById(R.id.educationJobConditionTitle);
        educationJobConditionTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(getActivity(),EducationJobList.class);
                intent.putExtra("viewsIndex",i);
                startActivity(intent);
            }
        });
    }
    //给viewPager加事件
    private void initEvent(){
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resetTextView();
                int  currentItem = viewPager.getCurrentItem();
                textViews[currentItem].setBackgroundColor(Color.RED);
                textViews[currentItem].setWidth(30);
                textViews[currentItem].setHeight(30);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
    //让简历上面的小圆圈都变暗
    private void resetTextView(){
        for (int i=0; i<textViews.length;i++){
            textViews[i].setBackgroundColor(Color.GRAY);
            textViews[i].setHeight(20);
            textViews[i].setWidth(20);
        }
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
            System.out.println("resule空。。。。。。"+result);
            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            AlertDialog.Builder builder = DialogUtil.simpleDialog(getActivity(), "提示", "创建成功，但是要完善哦");
            builder.create().show();
            try {
                Page<DivAppRecruitInfo> recruitInfoPage = objectMapper.readValue(result, Page.class);
                List<DivAppRecruitInfo> divBeans = recruitInfoPage.getDivBeans();
                String beansString =  objectMapper.writeValueAsString(divBeans);
                JavaType javaType = JsonUtil.getCollectionType(ArrayList.class, DivAppRecruitInfo.class);
                List<DivAppRecruitInfo> beansList =  objectMapper.readValue( beansString, javaType);
                Intent intent = new Intent();
                intent.setClass(getActivity(),Fragment_mainActivity.class);

                session.put("beansList",beansList);
                session.put("userId",recruitInfoPage.getUserId());
                session.put("resumeList",recruitInfoPage.getDetailedResumes());
                session.put("jobs", recruitInfoPage.getJobs());
                intent.putExtra("isCreateResume",true);
                startActivity(intent);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
