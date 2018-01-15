package com.dada.shen.springboot.zookeeper;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.ZooDefs;

import java.io.IOException;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.zookeeper.JoinGroup
 * @Project:
 * @author: dada.shen
 * @date: 2018-01-13 20:39
 * @Description:
 */
public class JoinGroup extends ConnectionWatcher{
    public void join(String groupName,String memberName) throws KeeperException, InterruptedException{
        String path="/"+groupName+"/"+memberName;
        String createdPath=zk.create(path, null, ZooDefs.Ids.OPEN_ACL_UNSAFE, CreateMode.EPHEMERAL);
        System.out.println("Created:"+createdPath);
    }
    public static void main(String[] args) throws InterruptedException, IOException, KeeperException {
        JoinGroup joinGroup = new JoinGroup();
        joinGroup.connect(args[0]);
        joinGroup.join(args[1], args[2]);

        //stay alive until process is killed or thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }
}
