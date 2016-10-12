package com.whh.rxbusdemo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.support.design.widget.TabLayout;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.whh.rxbusdemo.R;
import com.whh.rxbusdemo.fragment.FirstFrament;
import com.whh.rxbusdemo.fragment.SecondFrament;
import com.whh.rxbusdemo.rxbus.EventInfo;
import com.whh.rxbusdemo.rxbus.RxEvent;

import java.util.ArrayList;
import java.util.List;

public class FragmentActivity extends BaseActivity {
    private static final String TAG=FragmentActivity.class.getSimpleName();

    private WebView wv;

    private TabLayout tab;
    private ViewPager viewpager;
    private List<Fragment> fragments=new ArrayList<>();
    private FragmentPagerAdapter adapter;

    @Override
    public int setLayout() {
        return R.layout.activity_fragment;
    }

    @Override
    public void init() {

        wv=(WebView)findViewById(R.id.wv);

        viewpager = (ViewPager) findViewById(R.id.viewpager);
        tab = (TabLayout) findViewById(R.id.tab);

        setUpViewPager();
    }


    private void setUpViewPager() {
        WebSettings set = wv.getSettings();
        set.setJavaScriptEnabled(true);
        set.setDomStorageEnabled(true);
        set.setDatabaseEnabled(true);
        set.setAllowFileAccess(true);
        wv.setWebViewClient(new MyWebViewClient());

        wv.loadUrl("http://www.baidu.com");

        fragments.add(new FirstFrament());
        fragments.add(new SecondFrament());
        adapter=new SimpleFragmentPagerAdapter(getSupportFragmentManager(),fragments);
        viewpager.setAdapter(adapter);
        tab.setupWithViewPager(viewpager);
    }

    /**
     * @param event
     * @param content
     */
    public void onRxEvent(RxEvent event, EventInfo content) {
        Log.i(TAG,"onRxEvent event:"+event+",info:"+content);
        viewpager.setCurrentItem(1);

        wv.loadUrl("http://www.github.com");
        switch (event.getType()){
            case RxEvent.LruEvent.VALUE:

                break;
            default:
                break;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        rxbus.register(RxEvent.Lru, this);
    }

    @Override
    protected void onDestroy() {
        rxbus.unregister(RxEvent.Lru, this);
        super.onDestroy();
    }

    /**
     * webview事件监听器
     */
    private class MyWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Log.i(TAG, "shouldOverrideUrlLoading url:" + url);
            wv.loadUrl(url);

            return true;

        }
    }

    private class SimpleFragmentPagerAdapter extends FragmentPagerAdapter{
        private List<Fragment> fragments = new ArrayList<>();

        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        public SimpleFragmentPagerAdapter(FragmentManager fragmentManager,  List<Fragment> fragments){
            super(fragmentManager);
            this.fragments = fragments;
        }


        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return position+" fragment";
        }
    }

}
