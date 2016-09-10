package com.whh.rxbusdemo;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.Subject;

/**
 * @author huscarter@163.com
 * @title
 * @description
 * @date 9/10/16
 */
public class RxBus {
    private static RxBus instance;
    private ConcurrentHashMap<Object, List<Subject>> subject_map = new ConcurrentHashMap<>();

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

    /**
     * 举例说明,现在有2个页面A和B,它们都需要监视改价这一动作,register将改价的标示(CHANGE_PRICE)当作key参数传入,
     * 返回被监视者,被监视者在A和B页面实现监视.当调用send方法时,,通过传入key值可以获取到A和B页面的被监视者,
     * A和B页面的被监视者会发送内容为content的事件,从而触发A/B页面的事件回调实现事件监听.
     *
     * @param key 注册标示
     * @return Observable 被监视者
     */
    public <T> Observable<T> register(Object key) {
        List<Subject> subjects = subject_map.get(key);
        if (null == subjects) {
            subjects = new ArrayList<>();
            subject_map.put(key, subjects);
        }

        Subject<T, T> subject = PublishSubject.create();

        subjects.add(subject);
        return subject;
    }

    /**
     * 取消监视,删除被监视者.
     * @param key
     * @param observable
     */
    public void unregister(Object key, Observable observable) {
        List<Subject> subjects = subject_map.get(key);
        if (null != subjects) {
            subjects.remove(observable);
            if (subjects.size() == 0) {
                subject_map.remove(key);
            }
        }
    }

    /**
     * 事件发送.
     * @param key
     * @param content
     */
    public void send(Object key, Object content) {
        List<Subject> subjects = subject_map.get(key);

        if (subjects != null && subjects.size() > 0) {
            for (Subject subject : subjects) {
                subject.onNext(content);
            }
        }
    }

}
