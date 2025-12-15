package com.ocean.engine.indexer;


import com.ocean.engine.BIndex.Bw;

/**
 * 索引编制器，用于编制索引，从请求数据中获取Key
 *
 * @param <E> 元素类型
 * @param <K> Key类型
 * @param <R> 请求类型
 * @author sunmingyuan
 */
public interface Indexer<E, K, R> {

    /**
     * 索引名称
     *
     * @return 索引名称
     */
    default String name() {
        String cls = this.getClass().getSimpleName();
        String suf = "Indexer";
        if (cls.endsWith(suf)) {
            return cls.substring(0, cls.length() - suf.length());
        }
        return cls;
    }

    /**
     * 元素转字符串，便于输出调试
     *
     * @param element 元素
     * @return 字符串
     */
    default String elementToString(E element) {
        return element.toString();
    }

    /**
     * 从元素中获取黑白名单
     *
     * @param element 元素
     * @return 黑白名单，null指位置黑白名单，即不限制
     */
    Bw<K> getBw(E element);

    /**
     * 从请求中取得Key
     *
     * @param request 请求
     * @return Key集
     */
    K[] getRequestKeys(R request);

}
