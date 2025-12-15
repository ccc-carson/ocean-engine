package com.ocean.engine.indexer;

import com.ocean.engine.BIndex.Bw;
import com.ocean.engine.model.EPicking;
import com.ocean.engine.model.IActElement;

import java.util.Collections;
import java.util.Set;

public class AppIdIndexer implements Indexer<IActElement, String, EPicking>{
    @Override
    public Bw<String> getBw(IActElement element) {
        return new Bw<>(Set.of(element.getAppIds()), Collections.emptySet());
    }

    @Override
    public String[] getRequestKeys(EPicking request) {
        return request.getAppIds();
    }
}
