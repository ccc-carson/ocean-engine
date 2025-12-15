package com.ocean.engine.BIndex;

import com.ocean.engine.pool.FixedCapacityPool;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Bit流池
 * 
 * @author sunmingyuan
 *
 */
public class BitsPool extends FixedCapacityPool<long[][]> {

	/**
	 * 一维长度
	 */
	private final int d1Length;

	/**
	 * 二维长度
	 */
	private final int d2Length;

	/**
	 * 日志
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 构造池
	 * 
	 * @param capacity
	 *            池容量
	 * @param d1Length
	 *            数组1维长度
	 * @param d2Length
	 *            数组2维长度
	 */
	public BitsPool(int capacity, int d1Length, int d2Length) {
		super(capacity);
		this.d1Length = d1Length;
		this.d2Length = d2Length;
		for (long i = 0; i < capacity; i++) {
			put(new long[d1Length][d2Length]);
		}
	}

	@Override
	public long[][] get() {
		long[][] ret = super.get();
		if (ret == null) {
			ret = new long[d1Length][d2Length];
			logger.info("Bit流池已空，创建新实例 new long[{}][{}].", d1Length, d2Length);
		}
		return ret;
	}

}
