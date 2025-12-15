package com.ocean.engine.model;

import com.ocean.engine.BIndex.Indexes;

import java.util.List;

public class ITable {

    private Indexes<IActElement, EPicking> indexes;

    private int maxMatcherExe = Integer.MAX_VALUE;

    /**
     * 生成决策表
     */
    private ITable makeTable(List<Exe> exes) {
        if (exes == null || exes.size() == 0) {
            logger.warn("无在投计划，决策表置为空");
            return null;
        } else {
            ITable table = new ITable(exes, irPoolCapacity,retargetingCache);
            logger.info("决策表创建完成");
            logger.info("\n{}", table.toString());
            loggerDistinct(table);
            return table;
        }
    }
}
