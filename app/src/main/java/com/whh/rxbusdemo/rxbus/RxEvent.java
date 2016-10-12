package com.whh.rxbusdemo.rxbus;

/**
 * @author huscarter@163.com
 * @title RxBus的事件类
 * @description RxEvent为基本事件, 可以自定义其他特定的事件比如OrderEvent.
 * <p>
 * 每种事件都有一个标示type,每种事件可以定义事件行为,比如订单事件(OrderEvent)的改价行为(CHANGE_PRICE).
 * <p>
 * 每种的事件的标示(type)相差100, 意味着每种事件里可以有99种不同的行为,因此也可以通过行为判定它属于哪种事件.
 * <p>
 * 比如订单事件OrderEvent的type为100,订单的改价和付款行为标示为101和102,最大的行为标示为199.
 * @date 9/30/16
 */
public class RxEvent {
    public static final OrderEvent ORDER = new OrderEvent();
    public static final AccountEvent ACCOUNT = new AccountEvent();
    public static final NoticeEvent NOTICE = new NoticeEvent();
    public static final LruEvent Lru = new LruEvent();

    /**
     * 事件的标示
     */
    private int type = 0;

    /**
     * 事件的发送者
     */
    private Object sender=null;

    public RxEvent(int type) {
        this.type = type;
    }

    public RxEvent(int type, Object sender) {
        this.type = type;
        this.sender = sender;
    }

    /**
     * 重写toString方法:展示为type值.
     *
     * @return
     */
    @Override
    public String toString() {
        return "event type:"+type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type=type;
    }

    public Object getSender() {
        return sender;
    }

    public void setSender(Object sender) {
        this.sender = sender;
    }

    /**
     * 订单事件
     */
    public static class OrderEvent extends RxEvent {
        public static final int VALUE = 100;

        public static final int CHANGE_PRICE = VALUE + 1;
        public static final int HAS_PAY = VALUE + 2;
        public static final int HAS_DISPATCH = VALUE + 3;

        public OrderEvent() {
            super(VALUE);
        }

    }

    /**
     * 账期事件
     */
    public static class AccountEvent extends RxEvent {
        public static final int VALUE = 200;

        public static final int HAS_PAY = VALUE + 1;

        public AccountEvent() {
            super(VALUE);
        }

    }

    /**
     * 通知消息事件
     */
    public static class NoticeEvent extends RxEvent {
        public static final int VALUE = 300;

        public static final int HAS_READ = VALUE + 1;

        public NoticeEvent() {
            super(VALUE);
        }

    }

    /**
     * 登录,注册事件(Login and register of user==Lru)
     */
    public static class LruEvent extends RxEvent {
        public static final int VALUE = 400;

        /**
         * 需要重新登录
         */
        public static final int RE_LOGIN = VALUE + 1;

        /**
         * 登录成功
         */
        public static final int LOGIN_SUCCESS = VALUE + 2;

        /**
         * 登录失败
         */
        public static final int LOGIN_FAILED = VALUE + 3;

        public LruEvent() {
            super(VALUE);
        }

    }

    /**
     * 空事件.
     * <p>
     * 如果通过事件的行为判定不出它属于哪种事件,那么它将被归属为空事件.
     *
     */
    public static class NullEvent extends RxEvent {
        public static final int VALUE = 0;

        /**
         * 默认构造函数
         */
        public NullEvent() {
            super(VALUE);
        }

        /**
         * @param value 事件行为标示
         */
        public NullEvent(int value) {
            super(value);
        }
    }

}
