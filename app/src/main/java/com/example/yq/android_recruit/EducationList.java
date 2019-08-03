package com.example.yq.android_recruit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yq.pojo.*;
import com.example.yq.pojo.Education;
import com.example.yq.util.Session;

import java.util.List;


/**
 * Created by YQ on 2019/4/23.
 */

public class EducationList extends Activity {
    private ListView educationList;
    private Session session = null;
    private DetailedResume resume;
    private int resumeIndex;    //哪个简历
    private List<Education> educations;
    private TextView listCommonTitle, listCommonAdd;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_common);
        session = Session.getSession();
        Intent intent = getIntent();
        resumeIndex = (Integer) intent.getIntExtra("viewsIndex",0);
        //点击哪一页简历的按钮后，把这页的简历的下标传到session中
        session.put("resumeIndex",resumeIndex);
        resume = (DetailedResume) (((List<DetailedResume>)session.get("resumeList")).get(resumeIndex));
        educations = resume.getEducationList();

        educationList = (ListView)findViewById(R.id.listId);
        listCommonTitle = (TextView)findViewById(R.id.listCommonTitle);
        listCommonAdd = (TextView)findViewById(R.id.listCommonAdd);
        //赋值
        MyAdapter myAdapter = new MyAdapter();
        educationList.setAdapter(myAdapter);
        listCommonTitle.setText("教育经历");
        //加事件
        listCommonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setClass(EducationList.this, com.example.yq.android_recruit.Education.class);
                startActivity(intent);
            }
        });
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return educations.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        /**
         * setTag()：设置标记，可以存取持有者
         * 组合item
         * @param position
         * @param view  如果==null，则创建，否则重用他（系统会将之前创建的保存下来）
         * @param parent
         * @return
         */
        @Override
        public View getView(final int position, View view, ViewGroup parent) {
            WidgeWrapper widgeWrapper = null;;
            LayoutInflater inflater = getLayoutInflater();;
            //组合，生成一个新的view（检查是否可以重复利用）
            if (view == null){
                view = inflater.inflate(R.layout.item_education, parent, false);
                widgeWrapper =   new WidgeWrapper(view);
                //用setTag（）：第一次的操作，为以后埋下伏笔（避免重复创建widgeWrapper）
                view.setTag(widgeWrapper);
            }else {
                //如果系统保存了view，view中就已经存在持有者
                widgeWrapper = (WidgeWrapper) view.getTag();
            }
            //操作新的view，查找相应控件（可以用一个类来拥有他们，避免大量消耗资源）

            widgeWrapper.getEducationSchool().setText(educations.get(position).getSchool());
            widgeWrapper.getEducationTimeStart().setText(educations.get(position).getYearsStart().toString());
            widgeWrapper.getEducationTimeEnd().setText(educations.get(position).getYearsEnd().toString());
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(EducationList.this, com.example.yq.android_recruit.Education.class);
                    intent.putExtra("itemIndex",position);
                    System.out.println("resumeIndex...."+resumeIndex);
                    startActivity(intent);
                }
            });
            //返回新的view
            return view;
        }
    }
    //控件持有类 ，可减少CPU、内存等资源消耗
    class WidgeWrapper{
        TextView educationSchool,educationTimeStart,educationTimeEnd;
        View view;
        //构造器，传入view（组合的item项）
        public WidgeWrapper(View view) {
            this.view = view;
        }

        public  TextView getEducationSchool() {
            if (educationSchool == null)  educationSchool = (TextView) view.findViewById(R.id.educationItemSchool);
            return educationSchool;
        }

        public TextView getEducationTimeStart() {
            if (educationTimeStart == null) educationTimeStart = (TextView) view.findViewById(R.id.educationItemTimeStart);
            return educationTimeStart;
        }

        public TextView getEducationTimeEnd() {
            if (educationTimeEnd == null) educationTimeEnd = (TextView) view.findViewById(R.id.educationItemTimeEnd);
            return educationTimeEnd;
        }

    }
}
