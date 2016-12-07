package com.whh.rxbusdemo.rxbus;

import android.util.Log;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Scheduler;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * @author huscarter@163.com
 * @title RxBus核心代码
 * @description 1:本类记录了注册监听的对象,以及监听事件的类别,通过map将它们关联.
 * <p>
 * 2:实现了注册监听,取消注册和发送事件以及调用注册对象的回调.
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
    private RxBus() {
        //
    }

    /**
     * 获取单例RxBus，为了对事件有统一的管理，必须是单例模式
     *
     * @return
     */
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

    /**
     * 事件注册
     *
     * @param type
     * @param register
     * @return
     */
    public Observable register(final int type, final Object register) {
        return register(type, register, AndroidSchedulers.mainThread());
    }

    /**
     * 事件注册
     *
     * @param event
     * @param register
     * @return
     */
    public Observable register(final RxEvent event, final Object register) {
        return register(event.getType(), register, AndroidSchedulers.mainThread());
    }

    /**
     * 事件注册
     *
     * @param event
     * @param register
     * @param scheduler
     * @return
     */
    public synchronized Observable register(final int event, final Object register, Scheduler scheduler) {
        return register(event, register,
                new Action1<KeyValue>() {
                    @Override
                    public void call(KeyValue obj) {
                        try {
                            getMethod(register).invoke(register, new RxEvent(obj.getKey()), obj.getValue());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, scheduler);
    }

    /**
     * 举例说明,现在有2个页面A和B,它们都需要监视改价这一动作,register将改价的标志(CHANGE_PRICE)当作key参数传入,
     * 返回被监视者,在A和B页面实现监视.当调用send方法时,通过传入key值可以获取到A和B页面的被监视者,
     * A和B页面的被监视者会发送内容为content的事件,从而触发A,B页面的事件回调实现事件监听.
     *
     * @param event 注册标示
     * @return register 注册的对象
     */
    public synchronized Observable register(final int event, final Object register, final Action1 action1, Scheduler scheduler) {
        String key = event + "";
        String tag = getUniqueTag(event, register);
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

            subject.subscribeOn(Schedulers.io())
                    .observeOn(scheduler)
                    .subscribe(action1);

        }
        Log.i(TAG, "register:" + register.getClass().getSimpleName() + "," +
                "single_map size:" + single_map.size() + ",subject_map size:" + subject_map.size());
        return subject;
    }

    /**
     * 取消监听
     *
     * @param event
     * @param register
     */
    public void unregister(final RxEvent event, Object register) {
        unregister(event.getType(), register);
    }

    /**
     * 取消监视,删除被监视者.
     *
     * @param event
     * @param register
     */
    public void unregister(final int event, Object register) {
        String key = event + "";
        String tag = getUniqueTag(event, register);
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
        Log.i(TAG, "unregister:" + register.getClass().getSimpleName() + "," +
                "single_map size:" + single_map.size() + ",subject_map size:" + subject_map.size());
    }

    /**
     * 事件发送.
     *
     * @param event
     * @param content
     */
    public void send(RxEvent event, EventInfo content) {
        send(event.getType(), content, 0);
    }

    /**
     * 事件发送.
     *
     * @param type
     * @param content
     */
    public void send(int type, EventInfo content) {
        send(type, content, 0);
    }

    /**
     *
     * @param type 事件行为
     * @param content 事件传递的信息
     * @param delay 时间触发的时间
     */
    public void send(int type, EventInfo content, int delay) {
//        Log.i(TAG, "To send event");

        String key = type + "";
        innerSend(type, key, content, delay);

        if (type % 100 != 0) { // 属于行为,不属于事件
            key = ((type / 100) * 100) + "";
            innerSend(type, key, content, delay);
        }
    }

    private void innerSend(final int type, final String key, final EventInfo content, final int delay) {
        List<Subject> subjects = subject_map.get(key);
        if (subjects != null && subjects.size() > 0) {
            for (final Subject subject : subjects) {
                Observable.create(new Observable.OnSubscribe<Object>() {
                    @Override
                    public void call(Subscriber<? super Object> subscriber) {
                        subscriber.onNext(new KeyValue(type, content));
                    }
                }).delay(delay, TimeUnit.MILLISECONDS).subscribe(subject);
            }
        }
    }

    /**
     * 通过反射获取回调的方法
     *
     * @param object
     * @return
     */
    private Method getMethod(Object object) {
        Class clazz = object.getClass();
        Method method = null;
        try {
            method = clazz.getMethod("onRxEvent", RxEvent.class, EventInfo.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return method;
    }

    /**
     * 获取一个监听者的唯一标示,通过hashCode允许Activity多次打开进行多次监听
     *
     * @param event
     * @param register
     * @return
     */
    private String getUniqueTag(int event, Object register) {
        return new StringBuffer("").append(event).append(register.hashCode()).toString();
    }

    /**
     * 实体RxJava的Action1
     */
    public static class KeyValue implements Serializable {
        private int key;
        private EventInfo value;

        public KeyValue(int key, EventInfo value) {
            this.key = key;
            this.value = value;
        }

        public int getKey() {
            return key;
        }

        public void setKey(int key) {
            this.key = key;
        }

        public EventInfo getValue() {
            return value;
        }

        public void setValue(EventInfo value) {
            this.value = value;
        }

        public String toString() {
            return new StringBuffer().append(",key:").append(key)
                    .append("value:").append(value).toString();
        }
    }
}
