package com.dada.shen.springboot.zookeeper.configservice;

import com.dada.shen.springboot.zookeeper.ConnectionWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.zookeeper.configservice.ChangedActiveKeyValueStore
 * @Project:
 * @author: dada.shen
 * @date: 2018-01-13 22:22
 * @Description:
 */
public class ChangedActiveKeyValueStore  extends ConnectionWatcher {
    private static final Charset CHARSET= Charset.forName("UTF-8");
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_PERIOD_SECONDS = 5;

    public void write(String path,String value) throws InterruptedException, KeeperException{
        int retries=0;
        while(true){
            try {
                Stat stat = zk.exists(path, false);
                if(stat==null){
                    zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
                }else{
                    zk.setData(path, value.getBytes(CHARSET),stat.getVersion());
                }
            } catch (KeeperException.SessionExpiredException e) {
                throw e;
            } catch (KeeperException e) {
                if(retries++==MAX_RETRIES){
                    throw e;
                }
                //sleep then retry
                TimeUnit.SECONDS.sleep(RETRY_PERIOD_SECONDS);
            }
        }
    }
    public String read(String path,Watcher watch) throws KeeperException, InterruptedException{
        byte[] data = zk.getData(path, watch, null);
        return new String(data,CHARSET);
    }
}
