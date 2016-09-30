package com.whh.rxbusdemo.activity;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whh.rxbusdemo.R;
import com.whh.rxbusdemo.rxbus.EventInfo;
import com.whh.rxbusdemo.rxbus.RxEvent;
import com.whh.rxbusdemo.rxbus.RxEvent;

public class MainActivity extends BaseActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    private Button btn_send;
    private TextView tv;
    private Button btn_start;

    @Override
    public int setLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void init() {

        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxbus.send(RxEvent.ORDER, new EventInfo(System.currentTimeMillis() + ",main"));
            }
        });
        tv = (TextView) findViewById(R.id.tv);
        btn_start = (Button) findViewById(R.id.btn_start);
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(context, SecondActivity.class));
            }
        });
    }

    /**
     * @param event
     * @param content
     */
    public void onRxEvent(RxEvent event, Object content) {
        Log.i(TAG, "event:" + event.equals(RxEvent.ORDER));
        tv.setText(content + "");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG,"RxEvent.ORDER:"+RxEvent.ORDER.toString());
        rxbus.register(RxEvent.ORDER, this);
    }

    @Override
    protected void onDestroy() {
        rxbus.unregister(RxEvent.ORDER, this);
        super.onDestroy();
    }

}
