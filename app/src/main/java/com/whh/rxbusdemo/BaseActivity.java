package com.whh.rxbusdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import rx.subscriptions.CompositeSubscription;

/**
 * @author huscarter@163.com
 * @title
 * @description
 * @date 9/10/16
 */
public abstract class BaseActivity extends AppCompatActivity{
    protected RxBus rxbus;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(setLayout());

        baseInit();

        init();
    }

    private void baseInit(){
        rxbus=RxBus.getInstance();
    }

    abstract void init();

    abstract int setLayout();
}
