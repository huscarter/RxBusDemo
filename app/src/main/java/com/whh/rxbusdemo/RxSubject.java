package com.whh.rxbusdemo;


import rx.subjects.Subject;

/**
 * @author huscarter@163.com
 * @title
 * @description
 * @date 9/10/16
 */
public class RxSubject {
    public Subject subject;
    public Object register;

    public RxSubject(Subject subject, Object register) {
        this.subject = subject;
        this.register = register;
    }
}
