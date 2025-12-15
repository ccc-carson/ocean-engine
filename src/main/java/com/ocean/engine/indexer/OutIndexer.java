package com.ocean.engine.indexer;

import com.ocean.engine.BIndex.Bw;
import com.ocean.engine.model.EPicking;
import com.ocean.engine.model.IActElement;

import java.util.Collections;
import java.util.Set;

/**
 * Out索引器 - 用于排除关系的索引
 * 例如：province等字段
 */
public class OutIndexer implements Indexer<IActElement, String, EPicking> {
    
    private final String fieldName;
    
    public OutIndexer(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public Bw<String> getBw(IActElement element) {
        String[] values = element.getProvinces();
        if (values == null || values.length == 0) {
            return null; // 空值表示不限制
        }
        return new Bw<>(Collections.emptySet(), Set.of(values));
    }
    
    @Override
    public String[] getRequestKeys(EPicking request) {
        return request.getProvinces();
    }

    
    @Override
    public String name() {
        return fieldName + "Out";
    }
}