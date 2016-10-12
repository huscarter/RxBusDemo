package com.whh.rxbusdemo.rxbus;

/**
 * @author:huscarter
 * @description: 事件实体类包括2个属性,一个是位置信息(position),一个内容改变的内容(content)
 * @date:2016/09/27
 */
public class EventInfo {
    private static final String TAG = EventInfo.class.getSimpleName();

    /**
     * 列表position位置
     */
    private int position = -1;

    /**
     * 消息携带的信息,比如改价的价格或者整个订单
     */
    private Object content=null;


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

    public static String getTAG() {
        return TAG;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Object getContent() {
        return content;
    }

    public void setContent(Object content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return new StringBuffer()
                .append("postion:").append(position)
                .append(",content:").append(content).toString();
    }

}
