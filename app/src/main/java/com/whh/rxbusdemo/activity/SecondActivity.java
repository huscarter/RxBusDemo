package com.whh.rxbusdemo.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whh.rxbusdemo.R;
import com.whh.rxbusdemo.rxbus.EventInfo;
import com.whh.rxbusdemo.rxbus.RxEvent;


public class SecondActivity extends BaseActivity {
    private static final String TAG=SecondActivity.class.getSimpleName();
    private Button btn_send;
    private TextView tv;

    @Override
    public int setLayout() {
        return R.layout.activity_second;
    }

    @Override
    public void init() {
        btn_send = (Button) findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rxbus.send(RxEvent.ORDER.HAS_DISPATCH, new EventInfo(System.currentTimeMillis() + ",second"));
                rxbus.send(RxEvent.Lru, new EventInfo(System.currentTimeMillis() + ",second"));
                //EventBus.getDefault().post("test");
            }
        });
        tv = (TextView) findViewById(R.id.tv);
    }

    /**
     * @param event
     * @param content
     */
    public void onRxEvent(RxEvent event, EventInfo content) {
        Log.i(TAG,"onRxEvent event:"+event+",info:"+content);
        tv.setText(content + "");
        switch (event.getType()){
            case RxEvent.OrderEvent.VALUE:
                //
                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        rxbus.register(RxEvent.ORDER, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        rxbus.unregister(RxEvent.ORDER, this);
    }

}
