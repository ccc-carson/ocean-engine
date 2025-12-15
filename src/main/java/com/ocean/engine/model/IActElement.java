package com.ocean.engine.model;

import lombok.Data;

@Data
public class IActElement {

    private Long id;

    private String code;

    private String nameSpace;

    private String[] appIds;

    private String os;

    private String[] channels;

    private String[] provinces;

    private Integer age;

    private Long installTime;
}
