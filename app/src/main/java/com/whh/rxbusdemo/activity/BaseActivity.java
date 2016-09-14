package com.whh.rxbusdemo.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.whh.rxbusdemo.rxbus.RxBus;

/**
 * @author huscarter@163.com
 * @title
 * @description
 * @date 9/10/16
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected RxBus rxbus;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());

        baseInit();

        init();
    }

    private void baseInit(){
        context=this;
        rxbus=RxBus.getInstance();
    }

    abstract void init();

    abstract int setLayout();
}
