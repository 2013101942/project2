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

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.util.Session;
import com.example.yq.util.StringUtils;

import java.util.List;


/**
 * Created by YQ on 2019/4/21.
 */

public class BlankRecruitActivity extends Activity {
    //存放点击“我的收藏”、“已申请”、“已通过”后的简要招聘信息页面
    private ListView blankRecruitList;
    private List<DivAppRecruitInfo> dataSource = null;
    private Session session = null;
    private MyAdapter myAdapter = null;
    private Intent intent;
    private String titleName;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_recruit);
        initView();
        blankRecruitList.setAdapter(myAdapter);
    }
    private void initView(){
        session = Session.getSession();
        dataSource = (List<DivAppRecruitInfo>) session.get("collectOrApplyOrAdoptList");
        intent = getIntent();
        titleName = intent.getStringExtra("titleName");
        blankRecruitList = (ListView) findViewById(R.id.blankRecruitListId);
        myAdapter = new MyAdapter();

    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return dataSource.size();
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
            final LayoutInflater inflater = getLayoutInflater();;
            //组合，生成一个新的view（检查是否可以重复利用）
            if (view == null){
                view = inflater.inflate(R.layout.item_shouye_list, parent, false);
                widgeWrapper = new WidgeWrapper(view);
                //用setTag（）：第一次的操作，为以后埋下伏笔（避免重复创建widgeWrapper）
                view.setTag(widgeWrapper);
            }else {
                //如果系统保存了view，view中就已经存在持有者
                widgeWrapper = (WidgeWrapper) view.getTag();
            }
            //操作新的view，查找相应控件（可以用一个类来拥有他们，避免大量消耗资源）
            widgeWrapper.getRecruit_Job().setText(dataSource.get(position).getJob());
            if (!"".equals(dataSource.get(position).getSalaryStart())&& dataSource.get(position).getSalaryStart()!=null){
                widgeWrapper.getSalaryStart().setText(String.valueOf(dataSource.get(position).getSalaryStart()));
            }
            if (!"".equals(dataSource.get(position).getSalaryEnd())&& dataSource.get(position).getSalaryEnd()!=null){
                widgeWrapper.getSalaryEnd().setText(String.valueOf(dataSource.get(position).getSalaryEnd()));
            }
            widgeWrapper.geteName().setText(dataSource.get(position).geteName());
            widgeWrapper.geteCity().setText(dataSource.get(position).getCity());
            //给收藏、已申请、已通过的招聘信息列表项加事件
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    if ("我的收藏".equals(titleName)){
                        intent.setClass(BlankRecruitActivity.this,Collection.class);
                    }else if("已申请".equals(titleName)){
                        intent.setClass(BlankRecruitActivity.this,ApplyInfo.class);
                    }else if ("已通过".equals(titleName)){
                        intent.setClass(BlankRecruitActivity.this,SuccessInfo.class);
                    }

                    intent.putExtra("wodeFlag",true);
                    intent.putExtra("recruitIndex",position);
                    startActivity(intent);
                }
            });
            //返回新的view
            return view;
        }
    }
    //控件持有类 ，可减少CPU、内存等资源消耗
    class WidgeWrapper{
        TextView recruit_Job,salaryStart,salaryEnd,eName,eCity;
        View view;
        //构造器，传入view（组合的item项）
        public WidgeWrapper(View view) {
            this.view = view;
        }

        public  TextView getRecruit_Job() {
            if (recruit_Job == null)  recruit_Job = (TextView) view.findViewById(R.id.recruit_JobId);
            return recruit_Job;
        }

        public TextView getSalaryStart() {
            if (salaryStart == null) salaryStart = (TextView) view.findViewById(R.id.salaryStartId);
            return salaryStart;
        }

        public TextView getSalaryEnd() {
            if (salaryEnd == null) salaryEnd = (TextView) view.findViewById(R.id.salaryEndId);
            return salaryEnd;
        }

        public TextView geteName() {
            if (eName == null) eName = (TextView) view.findViewById(R.id.eNameId);
            return eName;
        }

        public TextView geteCity() {
            if (eCity == null) eCity = (TextView) view.findViewById(R.id.eCityId);
            return eCity;
        }
    }
}
