package com.dada.shen.springboot.zookeeper;

import org.apache.zookeeper.KeeperException;

import java.io.IOException;
import java.util.List;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.zookeeper.ListGroup
 * @Project:
 * @author: dada.shen
 * @date: 2018-01-13 20:40
 * @Description:
 */
public class ListGroup extends ConnectionWatcher {
    public void list(String groupNmae) throws KeeperException, InterruptedException{
        String path ="/"+groupNmae;
        try {
            List<String> children = zk.getChildren(path, false);
            if(children.isEmpty()){
                System.out.printf("No memebers in group %s\n",groupNmae);
                System.exit(1);
            }
            for(String child:children){
                System.out.println(child);
            }
        } catch (KeeperException.NoNodeException e) {
            System.out.printf("Group %s does not exist \n", groupNmae);
            System.exit(1);
        }
    }
    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ListGroup listGroup = new ListGroup();
        listGroup.connect(args[0]);
        listGroup.list(args[1]);
        listGroup.close();
    }
}
