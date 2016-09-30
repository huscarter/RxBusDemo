package com.whh.rxbusdemo.rxbus;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Scheduler;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * @author huscarter@163.com
 * @title RxBus核心代码
 * @description
 * 1:本类记录了注册监听的对象,以及监听事件的类别,通过map将它们关联.
 * <p>
 * 2:实现了注册监听,取消注册和发送事件以及调用注册对象的回调.
 *
 * @date 9/10/16
 */
public class RxBus {
    private static final String TAG = RxBus.class.getSimpleName();
    private static RxBus instance;
    private ConcurrentHashMap<Object, List<Subject>> subject_map = new ConcurrentHashMap<>();
    private HashMap<Object, Subject> single_map = new HashMap<>();

    /**
     * 单例私有构造方法
     */
    private void RxBus() {
        //
    }

    public static RxBus getInstance() {
        if (instance == null) {
            synchronized (RxBus.class) {
                if (instance == null) {
                    instance = new RxBus();
                }
            }
        }
        return instance;
    }

    public Observable register(final RxEvent event, final Object register) {
        return register(event, register, Schedulers.io());
    }

    /**
     * 举例说明,现在有2个页面A和B,它们都需要监视改价这一动作,register将改价的标志(CHANGE_PRICE)当作key参数传入,
     * 返回被监视者,在A和B页面实现监视.当调用send方法时,通过传入key值可以获取到A和B页面的被监视者,
     * A和B页面的被监视者会发送内容为content的事件,从而触发A,B页面的事件回调实现事件监听.
     *
     * @param event 注册标示
     * @return register 注册的对象
     */
    public Observable register(final RxEvent event, final Object register, Scheduler scheduler) {
        String key = event.toString();
        String tag = key + register.getClass().getSimpleName();
        Subject subject = single_map.get(tag);
        List<Subject> subjects = subject_map.get(key);
        if (null == subject) {
            subject = PublishSubject.create();
            single_map.put(tag, subject);
        }
        if (null == subjects) {
            subjects = new ArrayList<>();
            subject_map.put(key, subjects);
        }
        if (!subjects.contains(subject)) {
            subjects.add(subject);
        }
        subject.subscribeOn(scheduler).observeOn(AndroidSchedulers.mainThread());
        subject.subscribe(new Action1<Object>() {
            @Override
            public void call(Object content) {
                try {
                    getMethod(register).invoke(register, event, content);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        Log.i(TAG, "register single_map size:" + single_map.size() + ",subject_map size:" + subject_map.size());
        return subject;
    }

    /**
     * 取消监视,删除被监视者.
     *
     * @param event
     * @param register
     */
    public void unregister(final RxEvent event, Object register) {
        String key = event.toString();
        String tag = key + register.getClass().getSimpleName();
        Subject subject = single_map.get(tag);
        List<Subject> subjects = subject_map.get(key);
        if (null != subjects) {
            if (null != subject) {
                subjects.remove(subject);
                single_map.remove(tag);
            }
            if (subjects.size() == 0) {
                subject_map.remove(key);
            }
        }
        Log.i(TAG, "unregister single_map size:" + single_map.size() + ",subject_map size:" + subject_map.size());
    }

    /**
     * 事件发送.
     *
     * @param type
     * @param content
     */
    public void send(int type, Object content) {
        send(RxEvent.getEvent(type), content);
    }

    /**
     * 事件发送.
     *
     * @param type
     * @param content
     */
    public void send(RxEvent type, Object content) {
        String key = type.toString();
        List<Subject> subjects = subject_map.get(key);

        if (subjects != null && subjects.size() > 0) {
            for (final Subject subject : subjects) {
                subject.onNext(content);
            }
        }
    }

    private Method getMethod(Object object) {
        Class clazz = object.getClass();
        Method method = null;
        try {
            method = clazz.getMethod("onRxEvent", RxEvent.class, Object.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

}
