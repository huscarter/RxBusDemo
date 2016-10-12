package com.whh.rxbusdemo.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.whh.rxbusdemo.R;
import com.whh.rxbusdemo.rxbus.EventInfo;
import com.whh.rxbusdemo.rxbus.RxBus;
import com.whh.rxbusdemo.rxbus.RxEvent;

/**
 * @author huscarter@163.com
 * @title
 * @description
 * @date 10/11/16
 */
public class SecondFrament extends Fragment {
    private static final String TAG=SecondFrament.class.getSimpleName();
    private View root;

    private TextView tv;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_second, container, false);
        initView();
        return root;
    }

    private void initView() {
        tv = (TextView) root.findViewById(R.id.tv);
    }

    /**
     * @param event
     * @param content
     */
    public void onRxEvent(RxEvent event, EventInfo content) {
        Log.i(TAG,"onRxEvent event:"+event+",info:"+content);
        switch (event.getType()){
            case RxEvent.LruEvent.VALUE:

                break;
            default:
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        RxBus.getInstance().register(RxEvent.ORDER, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        RxBus.getInstance().unregister(RxEvent.ORDER, this);
    }


}
