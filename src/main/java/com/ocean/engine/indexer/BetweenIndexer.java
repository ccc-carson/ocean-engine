package com.ocean.engine.indexer;

import com.ocean.engine.BIndex.Bw;
import com.ocean.engine.model.EPicking;
import com.ocean.engine.model.IActElement;

import java.util.Collections;
import java.util.Set;

/**
 * Between索引器 - 用于范围关系的索引
 * 例如：installTime等时间范围字段
 */
public class BetweenIndexer implements Indexer<IActElement, Long, EPicking> {
    
    private final String fieldName;
    
    public BetweenIndexer(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public Bw<Long> getBw(IActElement element) {
        Long value = getFieldValue(element);
        if (value == null) {
            return null; // 空值表示不限制
        }
        // 范围关系：黑名单包含所有不在范围内的值
        // 这里简化处理，实际应该处理范围上下限
        return new Bw<>(Collections.emptySet(), Set.of(value));
    }
    
    @Override
    public Long[] getRequestKeys(EPicking request) {
        Long value = getFieldValue(request);
        return value != null ? new Long[]{value} : new Long[0];
    }
    
    private Long getFieldValue(Object obj) {
        if (obj instanceof IActElement) {
            IActElement element = (IActElement) obj;
            switch (fieldName) {
                case "installTime": return element.getInstallTime();
                default: return null;
            }
        } else if (obj instanceof EPicking) {
            EPicking request = (EPicking) obj;
            switch (fieldName) {
                case "installTime": return request.getInstallTime();
                default: return null;
            }
        }
        return null;
    }
    
    @Override
    public String name() {
        return fieldName + "Between";
    }
}