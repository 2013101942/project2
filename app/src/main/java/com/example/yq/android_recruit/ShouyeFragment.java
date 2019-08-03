package com.example.yq.android_recruit;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.yq.pojo.DivAppRecruitInfo;
import com.example.yq.util.Session;

import java.util.List;

/**
 * Created by YQ on 2019/4/17.
 */

public class ShouyeFragment extends Fragment {
    private EditText jobText;
    private ImageButton searchBtn;
    private LinearLayout schoolRecruit,jobSearchMethod;
    private ListView recruitInfoList;
    private List<DivAppRecruitInfo> beansList = null;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Session session = Session.getSession();
        beansList = (List<DivAppRecruitInfo>) session.get("beansList");
        View view = inflater.inflate(R.layout.tab_01, container, false);
        recruitInfoList = (ListView) view.findViewById(R.id.recruitInfo_ListId);
        MyAdapter myAdapter = new MyAdapter();
        recruitInfoList.setAdapter(myAdapter);
        initView(view);
        initEvent();
        return view;
    }
    private void initView(View view){
        jobText = view.findViewById(R.id.jobId);
        searchBtn = view.findViewById(R.id.searchJobId);
    }
    private void initEvent(){
        jobText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean b) {
                if (b){
                    System.out.println("获得了焦点.....");
                    Intent intent = new Intent();
                    System.out.println("intent");
                    intent.setClass(getActivity(),SearchActivity.class);
                    System.out.println("setClass()......");
                    startActivity(intent);
                    System.out.println("startActivity.....");
                }else {
                    System.out.println("失去了焦点....");
                }
            }
        });
    }
    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return beansList.size();
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
            final LayoutInflater inflater = getActivity().getLayoutInflater();;
            //组合，生成一个新的view（检查是否可以重复利用）
            if (view == null){
                view = inflater.inflate(R.layout.item_shouye_list, parent, false);
                widgeWrapper =   new WidgeWrapper(view);
                //用setTag（）：第一次的操作，为以后埋下伏笔（避免重复创建widgeWrapper）
                view.setTag(widgeWrapper);
            }else {
                //如果系统保存了view，view中就已经存在持有者
                widgeWrapper = (WidgeWrapper) view.getTag();
            }
            //操作新的view，查找相应控件（可以用一个类来拥有他们，避免大量消耗资源）

            widgeWrapper.getRecruit_Job().setText(beansList.get(position).getJob());
            widgeWrapper.getSalaryStart().setText(beansList.get(position).getSalaryStart().toString());
            widgeWrapper.getSalaryEnd().setText(beansList.get(position).getSalaryEnd().toString());
            widgeWrapper.geteName().setText(beansList.get(position).geteName());
            widgeWrapper.geteCity().setText(beansList.get(position).getCity());
            view.setBackgroundResource(R.drawable.bg_linearbutton);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(getActivity(),RecruitInfo.class);
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
