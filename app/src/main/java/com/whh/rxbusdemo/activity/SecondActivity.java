package com.whh.rxbusdemo.activity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.whh.rxbusdemo.R;
import com.whh.rxbusdemo.rxbus.EventInfo;
import com.whh.rxbusdemo.rxbus.EventType;
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
                rxbus.send(EventType.ORDER.HAS_DISPATCH, System.currentTimeMillis()+",second");
            }
        });
        tv = (TextView) findViewById(R.id.tv);
    }

    /**
     *
     * @param content
     */
    public void onRxEvent(RxEvent type, Object content){
        Log.i(TAG,"type:"+type.equals(EventType.ORDER));
        tv.setText(content+"");
    }

    @Override
    protected void onResume() {
        super.onResume();
        rxbus.register(EventType.ORDER, this);
    }

    @Override
    protected void onDestroy() {
        rxbus.unregister(EventType.ORDER, this);
        super.onDestroy();
    }

}
