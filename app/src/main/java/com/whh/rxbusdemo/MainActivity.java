package com.whh.rxbusdemo;

import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import rx.functions.Action1;

public class MainActivity extends BaseActivity {
    private Button btn;
    private TextView tv;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init(){
        btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxbus.send(RxEvent.ORDER,"the rx event"+ System.currentTimeMillis());
            }
        });
        tv=(TextView) findViewById(R.id.tv);

    }

    @Override
    protected void onResume() {
        super.onResume();
        rxbus.register(RxEvent.ORDER).subscribe(new Action1<Object>() {
            @Override
            public void call(Object obj) {
                tv.setText(obj+"");
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
