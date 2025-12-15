package com.ocean.engine.pool;

/**
 * 固定容量的对象池
 * 
 * @author sunmingyuan
 *
 * @param <T>
 *            对象类型
 */
public class FixedCapacityPool<T> {

	/**
	 * 节点定义
	 */
	class Node {

		/**
		 * 数据
		 */
		private volatile T data;

		/**
		 * 下一节点
		 */
		private Node next;
	}

	/**
	 * 循环链表的最大长度
	 */
	private final int capacity;

	/**
	 * GET锁
	 */
	private final Object getLock = new Object();

	/**
	 * PUT锁
	 */
	private final Object putLock = new Object();

	/**
	 * GET游标
	 */
	private Node getCur;

	/**
	 * PUT游标
	 */
	private Node putCur;

	/**
	 * 构造一个容量为100的池
	 */
	public FixedCapacityPool() {
		this(100);
	}

	/**
	 * 构造池
	 * 
	 * @param capacity
	 *            池容量
	 */
	public FixedCapacityPool(int capacity) {
		if (capacity < 1) {
			throw new IllegalArgumentException("capacity must > 0");
		}
		this.capacity = capacity;
		Node first = new Node();
		Node current = first;
		for (int i = 1; i < capacity; i++) {
			Node node = new Node();
			current.next = node;
			current = node;
		}
		current.next = first;
		getCur = current;
		putCur = current;
	}

	/**
	 * 从池中取对象<br>
	 * 如池已空，返回null
	 * 
	 * @return 对象
	 */
	public T get() {
		T data = null;
		synchronized (getLock) {
			data = getCur.data;
			getCur.data = null;
			getCur = getCur.next;
		}
		return data;
	}

	/**
	 * 将对象置入池
	 * 
	 * @param obj
	 *            对象
	 */
	public void put(T obj) {
		synchronized (putLock) {
			putCur.data = obj;
			putCur = putCur.next;
		}
	}

	/**
	 * 取得池容量
	 * 
	 * @return 池容量
	 */
	public int capacity() {
		return capacity;
	}
}
