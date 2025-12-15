package com.ocean.engine.model;


import lombok.Data;

import java.io.Serializable;

/**
 * 调试信息
 * 
 * @author sunmingyuan
 * 
 */
@Data
public class TraceMsg implements Serializable {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = -3451600937242824383L;

	/**
	 * 类
	 */
	private String cls;

	/**
	 * 方法
	 */
	private String method;

	/**
	 * 代码行
	 */
	private int line;

	/**
	 * 时间
	 */
	private long time;

	/**
	 * 描述
	 */
	private String description;

	/**
	 * Constructor
	 */
	public TraceMsg() {
	}

	/**
	 * Constructor
	 * 
	 * @param cls
	 *            类
	 * @param method
	 *            方法名
	 * @param line
	 *            代码行
	 * @param time
	 *            时间
	 * @param description
	 *            描述
	 */
	public TraceMsg(long time, String cls, String method, int line, String description) {
		this.time = time;
		this.cls = cls;
		this.method = method;
		this.line = line;
		this.description = description;
	}


}
