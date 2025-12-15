package com.ocean.engine.indexer;

import com.ocean.engine.BIndex.Bw;
import com.ocean.engine.model.EPicking;
import com.ocean.engine.model.IActElement;

import java.util.Collections;
import java.util.Set;

/**
 * Gt索引器 - 用于大于关系的索引
 * 例如：age等数值字段
 */
public class GtIndexer implements Indexer<IActElement, Integer, EPicking> {
    
    private final String fieldName;
    
    public GtIndexer(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public Bw<Integer> getBw(IActElement element) {
        Integer value = getFieldValue(element);
        if (value == null) {
            return null; // 空值表示不限制
        }
        // 大于关系：黑名单包含所有小于等于该值的数字
        return new Bw<>(Collections.emptySet(), Set.of(value));
    }
    
    @Override
    public Integer[] getRequestKeys(EPicking request) {
        Integer value = getFieldValue(request);
        return value != null ? new Integer[]{value} : new Integer[0];
    }
    
    private Integer getFieldValue(Object obj) {
        if (obj instanceof IActElement) {
            IActElement element = (IActElement) obj;
            switch (fieldName) {
                case "age": return element.getAge();
                default: return null;
            }
        } else if (obj instanceof EPicking) {
            EPicking request = (EPicking) obj;
            switch (fieldName) {
                case "age": return request.getAge();
                default: return null;
            }
        }
        return null;
    }
    
    @Override
    public String name() {
        return fieldName + "Gt";
    }
}