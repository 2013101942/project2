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

import com.example.yq.pojo.PersonCompany;
import com.example.yq.util.Session;

import java.util.List;


/**
 * Created by YQ on 2019/4/21.
 */

public class BlankMsgActivity extends Activity {
    private ListView blankMsgList;
    private List<PersonCompany> dataSource = null;
    private Session session = null;
    private MyAdapter myAdapter = null;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blank_msg);
        initView();
        blankMsgList.setAdapter(myAdapter);
    }
    private void initView(){
        session = Session.getSession();
        blankMsgList = (ListView)findViewById(R.id.blankMsgListId);
        dataSource = (List<PersonCompany>) session.get("MsgList");
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
                view = inflater.inflate(R.layout.msg_item, parent, false);
                widgeWrapper = new WidgeWrapper(view);
                //用setTag（）：第一次的操作，为以后埋下伏笔（避免重复创建widgeWrapper）
                view.setTag(widgeWrapper);
            }else {
                //如果系统保存了view，view中就已经存在持有者
                widgeWrapper = (WidgeWrapper) view.getTag();
            }
            //操作新的view，查找相应控件（可以用一个类来拥有他们，避免大量消耗资源）

            widgeWrapper.getMsgEname().setText(dataSource.get(position).getUserName());
            widgeWrapper.getMsgContent().setText(dataSource.get(position).getMessage());
            widgeWrapper.getMsgTimes().setText(dataSource.get(position).getTimes());
            //点击“人事来信”、“已发消息”查看完整消息
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent();
                    intent.setClass(BlankMsgActivity.this,MsgContent.class);
                    intent.putExtra("msgIndex",position);
                    startActivity(intent);
                }
            });
            //返回新的view
            return view;
        }
    }
    //控件持有类 ，可减少CPU、内存等资源消耗
    class WidgeWrapper{
        TextView msgEname,msgContent,msgTimes;
        View view;
        //构造器，传入view（组合的item项）
        public WidgeWrapper(View view) {
            this.view = view;
        }

        public  TextView getMsgEname() {
            if (msgEname == null)  msgEname = (TextView) view.findViewById(R.id.msgEnameId);
            return msgEname;
        }

        public TextView getMsgContent() {
            if (msgContent == null) msgContent = (TextView) view.findViewById(R.id.msgContentId);
            return msgContent;
        }

        public TextView getMsgTimes() {
            if (msgTimes == null) msgTimes = (TextView) view.findViewById(R.id.msgTimesId);
            return msgTimes;
        }

    }
}
