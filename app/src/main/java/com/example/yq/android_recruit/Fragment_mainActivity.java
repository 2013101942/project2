package com.example.yq.android_recruit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.LinearLayout;


/**
 * Created by YQ on 2019/4/16.
 */

public class Fragment_mainActivity extends FragmentActivity implements View.OnClickListener {
    private LinearLayout tab_ShouYe,tab_XiaoXi,tab_JianLi,tab_WoDe;
    private ImageButton shouYe_img,xiaoXi_img,jianLi_img,woDe_img;
    private Fragment tab_01,tab_02,tab_03,tab_04;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.index);
        initView();
        initEvent();
        //如果是创建简历，则一开始就显示简历栏
        Intent intent = getIntent();
        boolean isCreateResume = intent.getBooleanExtra("isCreateOrDeleteResume",false);
        if (isCreateResume){
            setSelect(2);
        }else {
            setSelect(0);
        }

    }
    //加事件
    private void initEvent() {
        tab_ShouYe.setOnClickListener(this);
        tab_XiaoXi.setOnClickListener(this);
        tab_JianLi.setOnClickListener(this);
        tab_WoDe.setOnClickListener(this);
    }

    private void initView() {
        tab_ShouYe = (LinearLayout)findViewById(R.id.tab_ShouYe);
        tab_XiaoXi = (LinearLayout)findViewById(R.id.tab_XiaoXi);
        tab_JianLi = (LinearLayout)findViewById(R.id.tab_JianLi);
        tab_WoDe = (LinearLayout)findViewById(R.id.tab_WoDe);

        shouYe_img = (ImageButton)findViewById(R.id.ShouYeImg);
        xiaoXi_img = (ImageButton)findViewById(R.id.XiaoXiImg);
        jianLi_img = (ImageButton)findViewById(R.id.JianLiImg);
        woDe_img = (ImageButton)findViewById(R.id.WoDeImg);

        //从此FragmentManager便知道了tab_01、tab_02、tab_03、tab_04

    }

    private  void  setSelect(int i){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //设置所有图片暗色
        resetImg();
        //把所有fragment隐藏
        hideFragment(transaction);
        switch (i){
            case 0:
                if (tab_01 != null){
                    //显示tab_01
                    transaction.show(tab_01);
                }else {
                    tab_01 = new ShouyeFragment();
                    transaction.add(R.id.indexFrameId,tab_01);
                }
                shouYe_img.setImageResource(R.drawable.shouye);
                break;
            case 1:
                if (tab_02 != null){
                    //显示tab_01
                    transaction.show(tab_02);
                }else {
                    tab_02 = new XiaoxiFragment();
                    transaction.add(R.id.indexFrameId,tab_02);
                }
                xiaoXi_img.setImageResource(R.drawable.icon_message_fill);
                break;
            case 2:
                if (tab_03 != null){
                    //显示tab_01
                    transaction.show(tab_03);
                }else {
                    tab_03 = new JianliFragment();
                    transaction.add(R.id.indexFrameId,tab_03);
                }
                jianLi_img.setImageResource(R.drawable.wodejianli_1);
                break;
            case 3:
                if (tab_04 != null){
                    //显示tab_01
                    transaction.show(tab_04);
                }else {
                    tab_04 = new WoDeFragment();
                    transaction.add(R.id.indexFrameId,tab_04);
                }
                woDe_img.setImageResource(R.drawable.wode);
                break;
        }
        transaction.commit();
    }
    //设置事件触发内容
    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.tab_ShouYe:
                setSelect(0);
                break;
            case R.id.tab_XiaoXi:
                setSelect(1);
                break;
            case R.id.tab_JianLi:
                setSelect(2);
                break;
            case R.id.tab_WoDe:
                setSelect(3);
                break;
        }
    }
    //隐藏所有fragment(如果是null，就无法hide，所以要判断一下)
    private void hideFragment(FragmentTransaction transaction) {
        if (tab_01 != null){
            transaction.hide(tab_01);
        }
        if (tab_02 != null){
            transaction.hide(tab_02);
        }
        if (tab_03 != null){
            transaction.hide(tab_03);
        }
        if (tab_04 != null){
            transaction.hide(tab_04);
        }
    }

    private void resetImg() {
        shouYe_img.setImageResource(R.drawable.xiazai45);
        xiaoXi_img.setImageResource(R.drawable.icon_message);
        jianLi_img.setImageResource(R.drawable.wodejianli);
        woDe_img.setImageResource(R.drawable.my);
    }
}

