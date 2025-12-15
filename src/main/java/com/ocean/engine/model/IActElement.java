package com.ocean.engine.model;

import lombok.Data;

import java.util.Arrays;

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

    private String config;

    @Override
    public String toString() {
        return "IActElement{" +
                "id=" + id +
                ", code='" + code + '\'' +
                ", nameSpace='" + nameSpace + '\'' +
                ", appIds=" + Arrays.toString(appIds) +
                ", os='" + os + '\'' +
                ", channels=" + Arrays.toString(channels) +
                ", provinces=" + Arrays.toString(provinces) +
                ", age=" + age +
                ", installTime=" + installTime +
                ", config='" + config + '\'' +
                '}';
    }
}
