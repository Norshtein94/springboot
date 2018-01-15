package com.dada.shen.springboot;

import java.util.Scanner;
import java.util.concurrent.ArrayBlockingQueue;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.Message
 * @Project:
 * @author: dada.shen
 * @date: 2017-12-31 15:15
 * @Description:
 */
class Message {

    public static final int KEY_MSG = 1;
    public static final int MOUSE_MSG = 2;
    public static final int SYS_MSG = 3;

    private Object source; // 来源
    private int type;      // 类型
    private String info;// 信息

    public Message(Object source, int type, String message) {
        this.source = source;
        this.type = type;
        this.info = message;
    }

    public Object getSource() {
        return source;
    }

    public void setSource(Object source) {
        this.source = source;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }
}

interface MessageProcess{
        public void doMessage(Message msg);
}
/**
 * 窗口模拟类
 */
class WindowSimulator implements MessageProcess{
    private ArrayBlockingQueue msgQueue;
    public WindowSimulator(ArrayBlockingQueue msgQueue) {
        this.msgQueue = msgQueue;
    }

    public void GenerateMsg() {
        while(true) {
            Scanner scanner = new Scanner(System.in);
            int msgType = scanner.nextInt();
            if(msgType < 0) {           //输入负数结束循环
                break;
            }
            String msgInfo = scanner.next();
            Message msg = new Message(this, msgType, msgInfo);
            try {
                msgQueue.put(msg);      //新消息加入到队尾
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    /**
     * 消息处理
     */
    public void doMessage(Message msg) {
        switch(msg.getType()) {
            case Message.KEY_MSG:
                onKeyDown(msg);
                break;
            case Message.MOUSE_MSG:
                onMouseDown(msg);
                break;
            default:
                onSysEvent(msg);
        }
    }

    //键盘事件
    public static void onKeyDown(Message msg) {
        System.out.println("键盘事件：");
        System.out.println("type:" + msg.getType());
        System.out.println("info:" + msg.getInfo());
    }

    //鼠标事件
    public static void onMouseDown(Message msg) {
        System.out.println("鼠标事件：");
        System.out.println("type:" + msg.getType());
        System.out.println("info:" + msg.getInfo());
    }

    //操作系统产生的消息
    public static void onSysEvent(Message msg) {
        System.out.println("系统事件：");
        System.out.println("type:" + msg.getType());
        System.out.println("info:" + msg.getInfo());
    }
}

/**
 * 消息模拟
 * @author luoweifu
 */
public class MessageSimulator {
    //消息队列
    private static ArrayBlockingQueue<Message> messageQueue = new ArrayBlockingQueue<Message>(100);

    public static void main(String[] args) {
        WindowSimulator generator = new WindowSimulator(messageQueue);
        //产生消息
        generator.GenerateMsg();

        //消息循环
        Message msg = null;
        while((msg = messageQueue.poll()) != null) {
            ((MessageProcess) msg.getSource()).doMessage(msg);
        }
    }
}