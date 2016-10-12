package com.whh.rxbusdemo.activity;

import android.content.Context;
import android.support.v4.app.FragmentTabHost;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TabHost;
import android.widget.Toast;

import com.whh.rxbusdemo.R;
import com.whh.rxbusdemo.fragment.FirstFrament;
import com.whh.rxbusdemo.fragment.SecondFrament;
import com.whh.rxbusdemo.rxbus.EventInfo;
import com.whh.rxbusdemo.rxbus.RxEvent;
import com.whh.rxbusdemo.view.MyFragmentTabHost;


public class TabhostFragmentActivity extends BaseActivity {
    private static final String TAG = TabhostFragmentActivity.class.getSimpleName();

    private Context context;
    private MyFragmentTabHost tabhost;
    private final int[] tv_arr = new int[]{R.string.first_fragment, R.string.second_fragment};
    private Class[] fragment_arr = new Class[]{FirstFrament.class, SecondFrament.class};

    private Button btn_first;
    private Button btn_second;

    @Override
    public int setLayout() {
        return R.layout.activity_tabhost_fragment;
    }

    @Override
    public void init() {
        //
        context=this;
        tabhost = (MyFragmentTabHost) findViewById(android.R.id.tabhost);
        tabhost.setup(this, getSupportFragmentManager(), R.id.fl_content);
        tabhost.getTabWidget().setVisibility(View.GONE);

        btn_first = (Button) findViewById(R.id.btn_first);
        btn_first.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabhost.setCurrentTab(0);
            }
        });
        btn_second = (Button) findViewById(R.id.btn_second);
        btn_second.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tabhost.setCurrentTab(1);
            }
        });

        for (int i = 0; i < fragment_arr.length; i++) {
            TabHost.TabSpec spec = tabhost.newTabSpec(getString(tv_arr[i])).setIndicator(getString(tv_arr[i]));
            tabhost.addTab(spec, fragment_arr[i], null);
        }

        tabhost.setCurrentTab(0);
        Log.i(TAG, "to register");
//        rxbus.register(RxEvent.Lru.getType(), this, new Action1() {
//            @Override
//            public void call(Object o) {
//                Log.i(TAG, "call call event:" + o + ",info:" + o);
//                Toast.makeText(context,"test",Toast.LENGTH_LONG).show();
//                tabhost.setCurrentTab(1);
//            }
//        });
        rxbus.register(RxEvent.Lru, this);
    }

    /**
     * @param event
     * @param info
     */
    public void onRxEvent(RxEvent event, EventInfo info) {
        Log.i(TAG, "onRxEvent event:" + event + ",info:" + info);
        if (tabhost.getCurrentTab() == 0) {
            tabhost.setCurrentTab(1);
        } else {
            tabhost.setCurrentTab(0);
        }
        switch (event.getType()) {
            case RxEvent.LruEvent.VALUE:
                //
                break;
            default:
                break;
        }

    }

    /** List点击时会发送些事件，接收到事件后更新详情 */
    public void onEventMainThread(Object item) {
        if (tabhost.getCurrentTab() == 0) {
            tabhost.setCurrentTab(1);
        } else {
            tabhost.setCurrentTab(0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        rxbus.unregister(RxEvent.Lru, this);
        super.onDestroy();
    }

}
