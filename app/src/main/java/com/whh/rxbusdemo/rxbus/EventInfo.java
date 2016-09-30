package com.whh.rxbusdemo.rxbus;

/**
 * @author:huscarter
 * @description:
 * @date:
 */
public class EventInfo {
    private static final String TAG = EventInfo.class.getSimpleName();

    /**
     * 列表position位置
     */
    public int position = -1;
    /**
     * 消息携带的信息,比如改价的价格或者整个订单
     */
    public Object content;

    public EventInfo() {
        //
    }

    public EventInfo(Object content) {
        this.content = content;
    }

    public EventInfo(int position, Object content) {
        this.position = position;
        this.content = content;
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append(",postion:").append(position)
                .append(",content:").append(content).toString();
    }

}
