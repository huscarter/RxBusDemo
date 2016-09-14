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
    public void init() {
        btn = (Button) findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxbus.send(EventType.ORDER, System.currentTimeMillis());
            }
        });
        tv = (TextView) findViewById(R.id.tv);
    }

    /**
     *
     * @param content
     */
    public void onRxEvent(Object content){
        tv.setText(content+",hello");
    }

    @Override
    protected void onResume() {
        super.onResume();
        rxbus.register(EventType.ORDER, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxbus.unregister(EventType.ORDER, this);
    }

}
