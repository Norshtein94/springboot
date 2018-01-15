package com.dada.shen.springboot.annotation;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.annotation.BeanDefine
 * @Project:
 * @author: dada.shen
 * @date: 2017-12-29 19:28
 * @Description:
 */
public class BeanDefine {

    private String id;

    private String className;

    public BeanDefine(String id, String className) {
        this.id = id;
        this.className = className;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
