package com.ocean.engine.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.Formatter;

/**
 * 动作基类
 *
 * @author sunmingyuan
 */

@Data
public class Action {

    /**
     * 唯一标识，全局唯一
     */
    private String uuid;

    /**
     * 此Action的域名
     */
    private String host;

    /**
     * 此Action的地址
     */
    private String uri;

    /**
     * 平台
     */
    private String platform;

    /**
     * 会话ID，浏览器未关闭，同一用户唯一
     */
    private String sessionId;

    /**
     * 请求时间
     */
    private final long requestTime = System.currentTimeMillis();

    /**
     * 回应时间
     */
    private long responseTime;

    /**
     * 是否测试请求
     */
    private boolean test;

    /**
     * 是否调试请求
     */
    private boolean trace;

    /**
     * 调试信息
     */
    private ArrayList<TraceMsg> traceMsgs;


    private String subPlatform;

    public Action() {
    }

    public Action(String uuid) {
        this.uuid = uuid;
    }

    public boolean isTrace() {
        return trace;
    }


    /**
     * 记录调试信息
     *
     * @param
     */
    public void addTraceMsg(String format, Object... args) {
        if (isTrace()) {
            if (this.traceMsgs == null) {
                this.traceMsgs = new ArrayList<TraceMsg>();
            }
            StackTraceElement element = Thread.currentThread().getStackTrace()[2];// 0:线程,1:Action,2:调用类
            String content = null;
            if (format != null) {
                content = new Formatter().format(format, args).toString();
            }
            this.traceMsgs.add(new TraceMsg(System.currentTimeMillis(), element.getClassName(),
                    element.getMethodName(), element.getLineNumber(), content));
        }
    }

}
