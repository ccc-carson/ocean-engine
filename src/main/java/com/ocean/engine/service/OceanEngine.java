package com.ocean.engine.service;

import com.ocean.engine.BIndex.BitsPool;
import com.ocean.engine.BIndex.Indexes;
import com.ocean.engine.indexer.*;
import com.ocean.engine.model.EPicking;
import com.ocean.engine.model.IActElement;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class OceanEngine {

    private Indexes<IActElement, EPicking> indexes;

    public void reloadAll(List<IActElement> elements) {
        Indexer<IActElement, ?, EPicking>[] indexers = new Indexer[] {
              new AppIdIndexer(),new ChannelIndexer()
        };

        this.indexes = new Indexes<>(indexers, elements, 2000);
    }

    public List<IActElement> match(EPicking condition) {
        return indexes.query(condition);
    }

    public static void main(String[] args) {

        //mock待檢索元素
        IActElement element = new IActElement();
        element.setId(1L);
        element.setAppIds(new String[]{"100","200"});
        element.setChannels(new String[]{"1001","1018"});

        IActElement element2 = new IActElement();
        element2.setId(2L);
        element2.setAppIds(new String[]{"100","300"});
        element2.setChannels(new String[]{"1003","1015"});

        List<IActElement> elements = List.of(element, element2);

        //mock請求參數
        OceanEngine oceanEngine = new OceanEngine();
        oceanEngine.reloadAll(elements);
        EPicking ePicking = new EPicking();
        ePicking.setAppIds(new String[]{"100"});
        ePicking.setChannels(new String[]{"1003"});

        System.out.println("檢索到如下元素：==========================");
        for (IActElement match : oceanEngine.match(ePicking)) {
            System.out.println(match.toString());
        }

    }
}
