package com.ocean.engine.service;

import com.ocean.engine.BIndex.BitsPool;
import com.ocean.engine.BIndex.Indexes;
import com.ocean.engine.indexer.*;
import com.ocean.engine.model.EPicking;
import com.ocean.engine.model.IActElement;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class OceanEngine {
    private Indexes<IActElement, EPicking> indexes;

    private final BitsPool bitsPool = new BitsPool(2000, 32, 64);

    public void reloadAll(List<RuleModel> allRules) {
        List<IActElement> elements = allRules.stream().map(IActElement::new).collect(toList());

        Indexer<IActElement, ?, EPicking>[] indexers = new Indexer[] {
                new InIndexer("appId"),
                new InIndexer("channel"),
                new OutIndexer("province"),
                new GtIndexer("age"),
                new BetweenIndexer("installTime")
        };

        this.indexes = new Indexes<>(indexers, elements, 2000);
    }

    public List<IActElement> match(EPicking condition) {
        return indexes.query(condition);
    }
}
