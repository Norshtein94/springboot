package com.dada.shen.springboot;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.Test
 * @Project:
 * @author: dada.shen
 * @date: 2018-01-01 20:27
 * @Description:
 */
public class Test {
    public static void main(String[] args){
        long start = System.currentTimeMillis();
        for(int i = 0; i < 100000; i++){
            System.out.println(i);
        }
        System.out.println(System.currentTimeMillis() - start);
    }
}
