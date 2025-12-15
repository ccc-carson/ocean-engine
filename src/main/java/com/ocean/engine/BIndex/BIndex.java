package com.ocean.engine.BIndex;

import java.util.Arrays;
import java.util.Map;
import java.util.Map.Entry;

/**
 * 基于Bit流的索引
 * 
 * @author sunmingyuan
 * 
 * @param <K>
 *            Key类型
 */
public class BIndex<K> {

	/**
	 * 白表，当Key在此表时，命中对应元素
	 */
	private final Map<K, long[]> whiteTable;

	/**
	 * 绿表，无论Key是什么,命中绿表所有元素
	 */
	private final long[] greenTable;

	/**
	 * 黑表，当Key在此表时，排除对应元素,黑表优先级最高,即使白表或绿表命中,也可能会被黑表排除
	 */
	private final Map<K, long[]> blackTable;

	/**
	 * 构造实例
	 * 
	 * @param whiteTable
	 *            白表
	 * @param blackTable
	 *            黑表
	 * @param greenTable
	 *            绿表
	 */
	public BIndex(Map<K, long[]> whiteTable, Map<K, long[]> blackTable, long[] greenTable) {
		if (whiteTable == null || whiteTable.isEmpty()) {
			whiteTable = null;
		}
		if (blackTable == null || blackTable.isEmpty()) {
			blackTable = null;
		}
		if (greenTable == null || greenTable.length == 0) {
			greenTable = null;
		}
		this.whiteTable = whiteTable;
		this.blackTable = blackTable;
		this.greenTable = greenTable;
	}

	/**
	 * 检索
	 * 
	 * @param ret
	 *            用于存储计算结果
	 * @param keys
	 *            Key集
	 */
	public void query(long[] ret, K[] keys) {
		if (greenTable == null) {
			Arrays.fill(ret, 0);
		} else {
			System.arraycopy(greenTable, 0, ret, 0, ret.length);
		}
		if (keys == null || keys.length == 0) {
			return;
		}
		if (whiteTable != null) {
			for (K key : keys) {
				BitsCal.union(ret, whiteTable.get(key));
			}
		}
		if (blackTable != null) {
			for (K key : keys) {
				BitsCal.minus(ret, blackTable.get(key));
			}
		}
		return;
	}

	@Override
	public String toString() {
		StringBuilder buf = new StringBuilder();
		if (whiteTable != null && whiteTable.size() > 0) {
			buf.append("\n========[white]========\n");
			for (Entry<K, long[]> entry : whiteTable.entrySet()) {
				buf.append(entry.getKey()).append("=").append(BitsCal.toHexString(entry.getValue())).append("\n");
			}
		}
		if (blackTable != null && blackTable.size() > 0) {
			buf.append("\n========[black]========\n");
			for (Entry<K, long[]> entry : blackTable.entrySet()) {
				buf.append(entry.getKey()).append("=").append(BitsCal.toHexString(entry.getValue())).append("\n");
			}
		}
		if (greenTable != null && greenTable.length > 0) {
			buf.append("\n========[green]========\n");
			buf.append(BitsCal.toHexString(greenTable).toString()).append("\n");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

}
