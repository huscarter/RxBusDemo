package com.whh.rxbusdemo.rxbus;

/**
 * @author:huscarter@163.com
 * @description: 消息的类型
 * @date:
 */
public class EventType {
    /**
     * 差值
     */
    private static final int DVALUE = 100;
    public static final RxEvent.OrderEvent ORDER = new RxEvent.OrderEvent();
    public static final RxEvent.AccountEvent ACCOUNT = new RxEvent.AccountEvent();
    public static final RxEvent.NoticeEvent NOTICE = new RxEvent.NoticeEvent();

    public static RxEvent getEvent(int type) {
        RxEvent rxevent = new RxEvent.NullEvent();
        if (type - ORDER.getType() < DVALUE) {
            rxevent = ORDER;
        } else if (type - ACCOUNT.getType() < DVALUE) {
            rxevent = ACCOUNT;
        } else if (type - NOTICE.getType() < DVALUE) {
            rxevent = NOTICE;
        }
        if (rxevent instanceof RxEvent.NullEvent) {
            new Exception("No event match this type error,type:" + type).printStackTrace();
        }
        return rxevent;
    }

}
