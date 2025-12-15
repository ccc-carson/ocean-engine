package com.ocean.engine.BIndex;

import java.util.Arrays;

/**
 * Bit流计算器 <br>
 * 使用long[]存储bit流
 * 
 * @author sunmingyuan
 *
 */
public class BitsCal {

	/**
	 * 将long序列转为十六制字符串序列
	 * 
	 * @param code
	 *            long序列
	 * @return 十六进制字符串，两数之间以空格分隔
	 */
	public static String toHexString(long[] code) {
		if (code == null) {
			return null;
		}
		if (code.length == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder(code.length * 17);
		for (int i = 0; i < code.length; i++) {
			String s = Long.toHexString(code[i]);
			for (int j = s.length(); j < 16; j++) {
				buf.append('0');
			}
			buf.append(s);
			buf.append(' ');
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * 将long序列转为二制字符串序列
	 * 
	 * @param code
	 *            long序列
	 * @return 二进制字符串，两数之间以空格分隔
	 */
	public static String toBinaryString(long[] code) {
		if (code == null) {
			return null;
		}
		if (code.length == 0) {
			return "";
		}
		StringBuilder buf = new StringBuilder(code.length * 65);
		for (int i = 0; i < code.length; i++) {
			String s = Long.toBinaryString(code[i]);
			for (int j = s.length(); j < 64; j++) {
				buf.append('0');
			}
			buf.append(s);
			buf.append(' ');
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}

	/**
	 * 交集
	 * 
	 * @param source
	 *            源集&存储计算结果，不允许为null
	 * @param code
	 *            待交入集，允许为null，null表示全0
	 */
	public static void intersect(long[] source, long[] code) {
		if (code == null) {
			Arrays.fill(source, 0);
			return;
		}
		for (int j = 0; j < source.length; j++) {
			source[j] &= code[j];
		}
	}

	/**
	 * 并集
	 * 
	 * @param source
	 *            源集&存储计算结果，不允许为null
	 * @param code
	 *            待并入集，允许为null，null表示全0
	 */
	public static void union(long[] source, long[] code) {
		if (code == null) {
			return;
		}
		for (int j = 0; j < source.length; j++) {
			source[j] |= code[j];
		}
	}

	/**
	 * 差集
	 * 
	 * @param source
	 *            源集&存储计算结果，不允许为null
	 * @param code
	 *            待减去集，允许为null，null表示全0
	 */
	public static void minus(long[] source, long[] code) {
		if (code == null) {
			return;
		}
		for (int j = 0; j < source.length; j++) {
			source[j] &= ~code[j];
		}
	}
}
