package com.whh.rxbusdemo.rxbus;

/**
 * @author:huscarter
 * @description: 消息的类型
 * @date:
 */
public enum EventType {
    ORDER,ACCOUNT,NOTICE;
    /**
     * 改价
     */
    public final static int CHANGE_PRICE=1101;
    /**
     * 支付成功
     */
    public int HAS_PAY=1102;
    /**
     * 发货成功
     */
    public int HAS_DISPATCH=1103;
}
