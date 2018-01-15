package com.dada.shen.springboot.zookeeper.configservice;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;

import java.io.IOException;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.zookeeper.configservice.ConfigWatcher
 * @Project:
 * @author: dada.shen
 * @date: 2018-01-13 22:00
 * @Description:
 */
public class ConfigWatcher implements Watcher {
    private ChangedActiveKeyValueStore store;

    @Override
    public void process(WatchedEvent event) {
        if(event.getType()== Watcher.Event.EventType.NodeDataChanged){
            try{
                dispalyConfig();
            }catch(InterruptedException e){
                System.err.println("Interrupted. exiting. ");
                Thread.currentThread().interrupt();
            }catch(KeeperException e){
                System.out.printf("KeeperException%s. Exiting.\n", e);
            }

        }

    }
    public ConfigWatcher(String hosts) throws IOException, InterruptedException {
        store=new ChangedActiveKeyValueStore();
        store.connect(hosts);
    }
    public void dispalyConfig() throws KeeperException, InterruptedException{
        String value=store.read(ConfigUpdater.PATH, this);
        System.out.printf("Read %s as %s\n",ConfigUpdater.PATH,value);
    }

    public static void main(String[] args) throws IOException, InterruptedException, KeeperException {
        ConfigWatcher configWatcher = new ConfigWatcher(args[0]);
        configWatcher.dispalyConfig();
        //stay alive until process is killed or Thread is interrupted
        Thread.sleep(Long.MAX_VALUE);
    }
}
