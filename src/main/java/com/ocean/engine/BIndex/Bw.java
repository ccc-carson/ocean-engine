package com.ocean.engine.BIndex;


import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * 黑白名单
 * 
 * @author sunmingyuan
 *
 * @param <E>
 *            元素类型
 */
public class Bw<E> {

	/**
	 * 白名单
	 */
	private final Set<E> white;

	/**
	 * 黑名单
	 */
	private final Set<E> black;

	/**
	 * 构造黑白名单
	 * 
	 * @param white
	 *            白名单，不允许为空，未设置白名单应传入全集new CompleteSet();
	 * @param black
	 *            黑名单，不允许为空，未设置黑名单应传入空集new HashSet();
	 */
	public Bw(Set<E> white, Set<E> black) {
		if (white == null) {
			throw new NullPointerException("white");
		}
		if (black == null) {
			throw new NullPointerException("black");
		}
		if (white instanceof CompleteSet) {
			this.white = new CompleteSet<E>();
		} else {
			this.white = Collections.unmodifiableSet(new HashSet<E>(white));
		}
		if (black instanceof CompleteSet) {
			this.black = new CompleteSet<E>();
		} else {
			this.black = Collections.unmodifiableSet(new HashSet<E>(black));
		}
	}

	/**
	 * 交集
	 */
	private Set<E> intersect(Set<E> set1, Set<E> set2) {
		if (set1 instanceof CompleteSet && set2 instanceof CompleteSet) {
			return set1;
		}
		if (set1 instanceof CompleteSet) {
			return set2;
		}
		if (set2 instanceof CompleteSet) {
			return set1;
		}
		HashSet<E> ret = new HashSet<E>(set1);
		ret.retainAll(set2);
		return ret;
	}

	/**
	 * 并集
	 */
	private Set<E> union(Set<E> set1, Set<E> set2) {
		if (set1 instanceof CompleteSet) {
			return set1;
		}
		if (set2 instanceof CompleteSet) {
			return set2;
		}
		HashSet<E> ret = new HashSet<E>(set1);
		ret.addAll(set2);
		return ret;
	}

	/**
	 * 交集
	 * 
	 * @param bw
	 *            待交黑白名单
	 * @return 交集
	 */
	public Bw<E> intersect(Bw<E> bw) {
		return new Bw<E>(intersect(this.white, bw.white), union(this.black, bw.black));
	}

	/**
	 * 并集<br>
	 * 注：仅当作用与相同类型单元条件是才正确，OPM已限制
	 * 
	 * @param bw
	 *            待并黑白名单
	 * @return 并集
	 */
	public Bw<E> union(Bw<E> bw) {
		return new Bw<E>(union(this.white, bw.white), intersect(this.black, bw.black));
	}

	/**
	 * 补集
	 * 
	 * @return 补集
	 */
	public Bw<E> complement() {
		Set<E> ow = this.getWhite();
		Set<E> ob = this.getBlack();
		Set<E> w;
		Set<E> b;
		if (ob instanceof CompleteSet) {
			w = ob;
			b = new HashSet<E>();
		} else if (ow instanceof CompleteSet) {
			w = ob;
			b = new HashSet<E>();
		} else {
			w = new CompleteSet<E>();
			b = new HashSet<E>(ow);
			b.removeAll(ob);
		}
		return new Bw<E>(w, b);
	}

	/**
	 * 压缩<br>
	 * 去掉同时存在于黑白名单中的元素
	 * 
	 * @return 压缩后的黑白名单
	 */
	public Bw<E> compress() {
		Set<E> ow = this.getWhite();
		Set<E> ob = this.getBlack();
		Set<E> w;
		Set<E> b;
		if (ow instanceof CompleteSet) {
			w = ow;
			b = ob;
		} else if (ob instanceof CompleteSet) {
			w = new HashSet<E>(); // 白名单清空
			b = ob;
		} else {
			w = new HashSet<E>(ow);
			w.removeAll(ob);
			b = new HashSet<E>(ob);
			b.removeAll(ow);
		}
		return new Bw<E>(w, b);
	}

	/**
	 * 取得白名单<br>
	 * 注：未设置白名单返回全集CompleteSet
	 * 
	 * @return 白名单集
	 */
	public Set<E> getWhite() {
		return white;
	}

	/**
	 * 取得黑名单<br>
	 * 注：全部禁止返回全集CompleteSet
	 * 
	 * @return 黑名单集
	 */
	public Set<E> getBlack() {
		return black;
	}

	/**
	 * 元素是否匹配
	 * 
	 * @param element
	 *            元素
	 * @return true or false
	 */
	public boolean matched(E element) {
		if (white instanceof CompleteSet == false) {
			if (white.contains(element) == false) {
				return false;
			}
		}
		if (black instanceof CompleteSet) {
			return false;
		}
		return black.contains(element) == false;
	}

	/**
	 * 元素集是否匹配<br>
	 * <ul>
	 * <li>白名单中不存在任何元素，返回false，否则继续看黑名单</li>
	 * <li>黑名单中不存在任何元素，返回true，否则返回false</li>
	 * </ul>
	 * 
	 * @param elements
	 *            元素集，不允许为null或empty
	 * @return true or false
	 */
	public boolean matched(Set<E> elements) {
		if (elements == null || elements.isEmpty()) {
			throw new IllegalArgumentException("elements can not be empty");
		}
		if (white instanceof CompleteSet == false) {
			if (CollectionUtils.containsAny(white, elements) == false) {
				return false;
			}
		}
		if (black instanceof CompleteSet) {
			return false;
		}
		return CollectionUtils.containsAny(black, elements) == false;
	}
}
