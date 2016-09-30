package com.whh.rxbusdemo.rxbus;

/**
 * @author huscarter@163.com
 * @title
 * @description
 * @date 9/30/16
 */
public class RxEvent {

    public RxEvent(int type) {
        this.type = type;
    }

    private int type = 0;

    @Override
    public String toString() {
        return type + "";
    }

    public int getType() {
        return type;
    }

    public static class OrderEvent extends RxEvent {
        private static final int VALUE = 100;

        public OrderEvent() {
            super(VALUE);
        }

        public static final int CHANGE_PRICE = VALUE + 1;
        public static final int HAS_PAY = VALUE + 2;
        public static final int HAS_DISPATCH = VALUE + 3;
    }

    public static class AccountEvent extends RxEvent {
        private static final int VALUE = 200;

        public AccountEvent() {
            super(VALUE);
        }

        public static final int HAS_PAY = VALUE+1;
    }

    public static class NoticeEvent extends RxEvent {
        private static final int VALUE = 300;

        public NoticeEvent() {
            super(VALUE);
        }

        public static final int HAS_READ = VALUE + 1;
    }

    public static class NullEvent extends RxEvent { 
        private static final int VALUE = 0;

        public NullEvent() {
            super(VALUE);
        }
    }
}
