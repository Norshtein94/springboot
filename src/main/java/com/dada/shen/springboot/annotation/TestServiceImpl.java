package com.dada.shen.springboot.annotation;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.annotation.TestServiceImpl
 * @Project:
 * @author: dada.shen
 * @date: 2017-12-29 19:13
 * @Description:
 */
public class TestServiceImpl {

    public TestDaoImpl testDao;
    public Test1DaoImpl test1Dao;

    @SddResource
    public Test2DaoImpl test2Dao;

    @SddResource(name="test3Dao")
    public Test3DaoImpl test3Dao;

    @SddResource
    public void setTestDao(TestDaoImpl testDao) {
        this.testDao = testDao;
    }
    @SddResource(name="test1Dao")
    public void setTest1Dao(Test1DaoImpl test1Dao) {
        this.test1Dao = test1Dao;

    }

    public void greet(){
        testDao.greet();
        test1Dao.greet();
        test2Dao.greet();
        test3Dao.greet();
        System.out.println("这里是Service方法----------------------");
    }
}
