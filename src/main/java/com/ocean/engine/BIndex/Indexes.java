package com.ocean.engine.BIndex;


import com.ocean.engine.indexer.Indexer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.Map.Entry;

/**
 * 索引集
 * 
 * @author sunmingyuan
 *
 * @param <E>
 *            元素类型
 * @param <R>
 *            请求类型
 */
public class Indexes<E, R> {

	/**
	 * 日志
	 */
	private Logger logger = LoggerFactory.getLogger(this.getClass());

	/**
	 * 索引编制器
	 */
	private final Indexer<E, ?, R>[] indexers;

	/**
	 * 索引，与索引编制器一一对应
	 */
	@SuppressWarnings("rawtypes")
	private final BIndex[] indexes;

	/**
	 * Bit流编码器
	 */
	private final BitsCodec<E> codec;

	/**
	 * Bit流池
	 */
	private final BitsPool bitsPool;

	/**
	 * 
	 * 创建索引集
	 * 
	 * @param indexers
	 *            索引编制器集
	 * @param elements
	 *            元素集，应不存在重复元素
	 * @param bitsPoolCapacity
	 *            Bit流池的容量
	 */
	public Indexes(Indexer<E, ?, R>[] indexers, Collection<E> elements, int bitsPoolCapacity) {
		if (indexers == null || indexers.length == 0) {
			throw new IllegalArgumentException("indexers can not be empty");
		}
		if (elements == null) {
			elements = new ArrayList<E>();
		}
		this.codec = new BitsCodec<>(elements);
		this.indexers = indexers;
		this.bitsPool = new BitsPool(bitsPoolCapacity, indexers.length, this.codec.arrayLength());
		this.indexes = new BIndex[indexers.length];
		for (int i = 0; i < indexes.length; i++) {
			this.indexes[i] = makeIndex(this.indexers[i], elements);
		}
	}

	/**
	 * 创建索引
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private BIndex makeIndex(Indexer<E, ?, R> indexer, Collection<E> elements) {
		Map<Object, List<E>> white = null;
		Map<Object, List<E>> black = null;
		List<E> green = null;
		for (E e : elements) {
			Bw<?> bw = indexer.getBw(e);
			if (bw == null) {
				// 未设置黑白名单，加入绿表
				green = addToList(green, e);
				continue;
			}
			if (bw.getBlack() instanceof CompleteSet) {
				// 黑名单为全集，舍弃
				continue;
			}
			if (bw.getWhite() instanceof CompleteSet) {
				// 白名单为全集，加入绿表
				green = addToList(green, e);
			} else {
				white = addToMap(white, bw.getWhite(), e);
			}
			black = addToMap(black, bw.getBlack(), e);
		}
		logger.warn("创建[{}]索引,white[{}]条,black[{}]条,green[{}]条", indexer.name(), white == null ? 0 : white.size(),
				black == null ? 0 : black.size(), green == null || green.isEmpty() ? 0 : 1);
		StringBuilder buf = new StringBuilder();
		if (white != null && white.size() > 0) {
			buf.append("\n========[white]========\n");
			for (Entry<Object, List<E>> entry : white.entrySet()) {
				buf.append(entry.getKey()).append("={");
				logAppendList(indexer, entry.getValue(), buf);
				buf.append("}\n");
			}
		}
		if (black != null && black.size() > 0) {
			buf.append("\n========[black]========\n");
			for (Entry<Object, List<E>> entry : black.entrySet()) {
				buf.append(entry.getKey()).append("={");
				logAppendList(indexer, entry.getValue(), buf);
				buf.append("}\n");
			}
		}
		if (green != null && green.size() > 0) {
			buf.append("\n========[green]========\n");
			buf.append("{");
			logAppendList(indexer, green, buf);
			buf.append("}\n");
		}
		if (buf.length() > 0) {
			buf.deleteCharAt(buf.length() - 1);
		}
		if (buf.length() > 0) {
			logger.info(buf.toString());
		}
		return new BIndex(convertMap(white), convertMap(black), codec.encode(green));
	}

	/**
	 * 日志串接List
	 */
	private void logAppendList(Indexer<E, ?, R> indexer, List<E> list, StringBuilder buf) {
		if (list != null && list.size() > 0) {
			buf.append(indexer.elementToString(list.get(0)));
			for (int i = 1; i < list.size(); i++) {
				buf.append(",").append(indexer.elementToString(list.get(i)));
			}
		}
	}

	/**
	 * 加入到List
	 */
	private List<E> addToList(List<E> list, E e) {
		if (list == null) {
			list = new ArrayList<E>();
		}
		list.add(e);
		return list;
	}

	/**
	 * 加入到Map
	 */
	private Map<Object, List<E>> addToMap(Map<Object, List<E>> map, Collection<?> keys, E e) {
		if (keys == null || keys.isEmpty()) {
			return map;
		}
		if (map == null) {
			map = new HashMap<Object, List<E>>();
		}
		for (Object key : keys) {
			List<E> list = map.get(key);
			if (list == null) {
				list = new ArrayList<>();
				map.put(key, list);
			}
			list.add(e);
		}
		return map;
	}

	/**
	 * 转化Map
	 */
	private Map<Object, long[]> convertMap(Map<Object, List<E>> map) {
		if (map == null || map.size() == 0) {
			return null;
		}
		Map<Object, long[]> table = new HashMap<>(map.size());
		for (Entry<Object, List<E>> entry : map.entrySet()) {
			table.put(entry.getKey(), codec.encode(entry.getValue()));
		}
		return table;
	}

	/**
	 * 检索元素
	 * 
	 * @param request
	 *            请求
	 * @return 匹配的元素集
	 */
	@SuppressWarnings("unchecked")
	public List<E> query(R request) {
		long[][] codes = bitsPool.get();
		indexes[0].query(codes[0], indexers[0].getRequestKeys(request));
		for (int i = 1; i < indexes.length; i++) {
			this.indexes[i].query(codes[i], indexers[i].getRequestKeys(request));
			BitsCal.intersect(codes[0], codes[i]);
		}
		List<E> ret = codec.decode(codes[0]);
		bitsPool.put(codes);
		return ret;
	}

}
