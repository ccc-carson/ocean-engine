package com.ocean.engine.indexer;

import com.ocean.engine.BIndex.Bw;
import com.ocean.engine.model.EPicking;
import com.ocean.engine.model.IActElement;

import java.util.Collections;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * In索引器 - 用于包含关系的索引
 * 例如：appId、channel等字段
 */
public class InIndexer implements Indexer<IActElement, String, EPicking> {
    
    private final String fieldName;
    
    public InIndexer(String fieldName) {
        this.fieldName = fieldName;
    }
    
    @Override
    public Bw<String> getBw(IActElement element) {
        String[] values = getFieldValues(element);
        if (values == null || values.length == 0) {
            return null; // 空值表示不限制
        }
        return new Bw<>(Set.of(values), Collections.emptySet());
    }
    
    @Override
    public String[] getRequestKeys(EPicking request) {
        return getFieldValues(request);
    }
    
    private String[] getFieldValues(Object obj) {
        if (obj instanceof IActElement) {
            IActElement element = (IActElement) obj;
            switch (fieldName) {
                case "appId": return element.getAppIds();
                case "channel": return element.getChannels();
                default: return null;
            }
        } else if (obj instanceof EPicking) {
            EPicking request = (EPicking) obj;
            switch (fieldName) {
                case "appId": return request.getAppIds();
                case "channel": return request.getChannels();
                default: return null;
            }
        }
        return null;
    }
    
    @Override
    public String name() {
        return fieldName + "In";
    }
}