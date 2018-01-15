package com.dada.shen.springboot.zookeeper.configservice;

import com.dada.shen.springboot.zookeeper.ConnectionWatcher;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

import java.nio.charset.Charset;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.zookeeper.configservice.ActiveKeyValueStore
 * @Project:
 * @author: dada.shen
 * @date: 2018-01-13 21:55
 * @Description:
 */
public class ActiveKeyValueStore extends ConnectionWatcher {
    private static final Charset CHARSET= Charset.forName("UTF-8");
    public void write(String path,String value) throws KeeperException, InterruptedException {
        Stat stat = zk.exists(path, false);
        if(stat==null){
            zk.create(path, value.getBytes(CHARSET), ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
        }else{
            zk.setData(path, value.getBytes(CHARSET),-1);
        }
    }
    public String read(String path,Watcher watch) throws KeeperException, InterruptedException{
        byte[] data = zk.getData(path, watch, null);
        return new String(data,CHARSET);
    }

}
