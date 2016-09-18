package com.whh.rxbusdemo.rxbus;

/**
 * @author:huscarter
 * @description:
 * @date:
 */
public class EventInfo {
    private static final String TAG=EventInfo.class.getSimpleName();

    /**
     * 列表position位置
     */
    public int position=-1;
    /**
     * 对应具体的
     */
    public int type=-1;
    /**
     * 消息携带的信息,比如改价的价格或者整个订单
     */
    public Object content;

    public EventInfo() {
        //
    }

    public EventInfo(int type) {
        this.type = type;
    }

    public EventInfo(Object content) {
        this.content = content;
    }

    public EventInfo(int type, int position, Object content) {
        this.position = position;
        this.type = type;
        this.content = content;
    }

    @Override
    public String toString(){
        return new StringBuffer().append("type:").append(type)
                .append(",postion:").append(position)
                .append(",content:").append(content).toString();
    }

}
