package com.ocean.engine.BIndex;

import java.util.*;

/**
 * Bit流编码器
 * 
 * @author sunmingyuan
 *
 * @param <E>
 *            元素类型
 */
public class BitsCodec<E> {

	/**
	 * 正向表
	 */
	private final List<E> rs;

	/**
	 * 反向映射
	 */
	private final Map<E, Integer> is;

	/**
	 * 数组长度
	 */
	private final int arrayLength;

	/**
	 * 构造Bit流解码器
	 * 
	 * @param completeSet
	 *            全集，应不存在重复元素
	 */
	public BitsCodec(Collection<E> completeSet) {
		if (completeSet == null) {
			completeSet = new ArrayList<>();
		}
		int esize = completeSet.size();
		rs = new ArrayList<>(esize);
		is = new HashMap<>(esize);
		if (esize > 0) {
			arrayLength = (esize - 1) / 64 + 1;
		} else {
			arrayLength = 0;
		}
		int index = 0;
		// 建立正向反向表
		for (E e : completeSet) {
			rs.add(e);
			is.put(e, index);
			index++;
		}
	}

	/**
	 * 取得编码数组长度
	 * 
	 * @return 编码数组长度
	 */
	public int arrayLength() {
		return arrayLength;
	}

	/**
	 * 集合长度
	 * 
	 * @return 集合长度
	 */
	public int elementSize() {
		return rs.size();
	}

	/**
	 * 取得元素
	 * 
	 * @param index
	 *            元素索引
	 * @return 元素对象，如index超出范围，抛出异常
	 */
	public E getElement(int index) {
		return rs.get(index);
	}

	/**
	 * 取得元素索引
	 * 
	 * @param element
	 *            元素对象
	 * @return 元素对象编号，如不存在，返回-1
	 */
	public int getIndex(E element) {
		Integer ret = is.get(element);
		if (ret == null) {
			return -1;
		}
		return ret.intValue();
	}

	/**
	 * 编码子集
	 * 
	 * @param sub
	 *            子集
	 * @return 子集Bit流
	 */
	public long[] encode(Collection<E> sub) {
		if (sub == null || sub.size() == 0) {
			return null;
		}
		long[] ret = new long[arrayLength];
		Arrays.fill(ret, 0L);
		for (E element : sub) {
			int index = getIndex(element);
			if (index >= 0) {
				ret[index / 64] |= (1L << (63 - index % 64));
			}
		}
		return ret;
	}

	/**
	 * 将Bit流反解为对象集合
	 * 
	 * @param code
	 *            Bit流
	 * @return 对象列表
	 */
	public List<E> decode(long[] code) {
		List<E> ret = new ArrayList<>();
		if (code == null || code.length == 0 || arrayLength == 0) {
			return ret;
		}
		for (int i = 0; i < code.length - 1; i++) {
			for (int j = 0; j < 64; j++) {
				int index = i * 64 + j;
				if ((code[i] & (1L << (63 - j))) != 0) {
					ret.add(rs.get(index));
				}
			}
		}
		int i = code.length - 1;
		for (int j = 0; j < 64; j++) {
			int index = i * 64 + j;
			if (index >= rs.size()) {
				break;
			}
			if ((code[i] & (1L << (63 - j))) != 0) {
				ret.add(rs.get(index));
			}
		}
		return ret;
	}
}
