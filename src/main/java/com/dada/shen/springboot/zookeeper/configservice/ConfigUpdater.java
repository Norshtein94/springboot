package com.dada.shen.springboot.zookeeper.configservice;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.zookeeper.configservice.ConfigUpdater
 * @Project:
 * @author: dada.shen
 * @date: 2018-01-13 21:58
 * @Description:
 */
public class ConfigUpdater {
    public static final String  PATH="/config";

    private ActiveKeyValueStore store;
    private Random random=new Random();

    public ConfigUpdater(String hosts) throws IOException, InterruptedException {
        store = new ActiveKeyValueStore();
        store.connect(hosts);
    }
    public void run() throws InterruptedException, KeeperException {
        while(true){
            String value=random.nextInt(100)+"";
            store.write(PATH, value);
            System.out.printf("Set %s to %s\n",PATH,value);
            TimeUnit.SECONDS.sleep(random.nextInt(10));

        }
    }
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ConfigUpdater configUpdater = new ConfigUpdater(args[0]);
        configUpdater.run();
    }
}
