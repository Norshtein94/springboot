package com.dada.shen.springboot.zookeeper.accesscontrollist;

import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.ACL;
import org.apache.zookeeper.data.Id;
import org.apache.zookeeper.server.auth.DigestAuthenticationProvider;

import java.util.ArrayList;
import java.util.List;

public class NewDigest {
    public static void main(String[] args) throws Exception {//new一个acl
        List<ACL> acls = new ArrayList<ACL>();
        //添加第一个id，采用用户名密码形式
        Id id1 = new Id("digest", DigestAuthenticationProvider.generateDigest("admin:admin"));
        ACL acl1 = new ACL(ZooDefs.Perms.ALL, id1);
        acls.add(acl1);
        //添加第二个id，所有用户可读权限
        Id id2 = new Id("world", "anyone");
        ACL acl2 = new ACL(ZooDefs.Perms.READ, id2);
        acls.add(acl2);
        Id id3 = new Id("digest", DigestAuthenticationProvider.generateDigest("root:root"));
        ACL acl3 = new ACL(ZooDefs.Perms.ALL, id3);
        acls.add(acl3);
        // Zk用admin认证，创建/test ZNode。
        ZooKeeper Zk = new ZooKeeper("120.55.161.125:2181,120.55.161.125:2182,120.55.161.125:2183",2000, null);
//        Zk.addAuthInfo("digest", "admin:admin".getBytes());
        Zk.create("/test", "data".getBytes(), acls, CreateMode.PERSISTENT);
    }
}
