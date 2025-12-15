package com.ocean.engine.model;

import lombok.Data;

@Data
public class EPicking extends Action {

    private String[] appIds;
    private String[] channels;
    private String[] provinces;
    private Integer age;
    private Long installTime;
}
