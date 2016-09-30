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
    /**
     * 差值,每一种RxEvent的起始值差为100
     */
    private static final int DVALUE = 100;

    /**
     * 事件的标示
     */
    private int type = 0;

    public static final OrderEvent ORDER = new OrderEvent();
    public static final AccountEvent ACCOUNT = new AccountEvent();
    public static final NoticeEvent NOTICE = new NoticeEvent();

    public RxEvent(int type) {
        this.type = type;
    }

    /**
     * 重写toString方法:展示为type值.
     *
     * @return
     */
    @Override
    public String toString() {
        return type + "";
    }

    public int getType() {
        return type;
    }

    /**
     * 通过事件的行为获取该行为归属的事件,如果找不到则返回为空事件
     *
     * @param option 事件的行为
     * @return
     */
    public static RxEvent getEvent(int option) {
        RxEvent rxevent = new RxEvent.NullEvent();
        if (option - ORDER.getType() < DVALUE) {
            rxevent = ORDER;
        } else if (option - ACCOUNT.getType() < DVALUE) {
            rxevent = ACCOUNT;
        } else if (option - NOTICE.getType() < DVALUE) {
            rxevent = NOTICE;
        }
        if (rxevent instanceof RxEvent.NullEvent) {
            new Exception("No event match this type error,type:" + option).printStackTrace();
        }
        return rxevent;
    }

    /**
     * 订单事件
     */
    public static class OrderEvent extends RxEvent {
        private static final int VALUE = 100;

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
        private static final int VALUE = 200;

        public static final int HAS_PAY = VALUE + 1;

        public AccountEvent() {
            super(VALUE);
        }

    }

    /**
     * 通知消息事件
     */
    public static class NoticeEvent extends RxEvent {
        private static final int VALUE = 300;

        public static final int HAS_READ = VALUE + 1;

        public NoticeEvent() {
            super(VALUE);
        }

    }

    /**
     * 空事件.
     * <p>
     * 如果通过事件的行为判定不出它属于哪种事件,那么它将被归属为空事件.
     */
    public static class NullEvent extends RxEvent {
        private static final int VALUE = 0;

        public NullEvent() {
            super(VALUE);
        }
    }
}
