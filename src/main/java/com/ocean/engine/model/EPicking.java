package com.ocean.engine.model;

import lombok.Data;

@Data
public class EPicking extends Action {

    private String[] appIds;
    
    // 新增字段用于索引
    private String[] channels;
    private String[] provinces;
    private Integer age;
    private Long installTime;
}
