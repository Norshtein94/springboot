package com.dada.shen.springboot.annotation;

import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Copyright © 2016-2017Yoongoo.
 *
 * @Title: com.dada.shen.springboot.annotation.ClassPathXMLApplicationContext
 * @Project:
 * @author: dada.shen
 * @date: 2017-12-29 19:11
 * @Description:
 */
public class ClassPathXMLApplicationContext {
    private Logger log = Logger.getLogger(ClassPathXMLApplicationContext.class);

    private List<BeanDefine> beanList = new ArrayList<>();

    private Map<String, Object> singletons = new HashMap<>();


    public ClassPathXMLApplicationContext(String fileName) {
        // 读取配置文件
        this.readXML(fileName);
        // 实例化Bean
        this.instanceBean();
        // 注解处理器
        this.annotationInject();
    }

    /**
     * 读取配置文件
     * @param fileName 配置文件路径
     */
    @SuppressWarnings("unchecked")
    private void readXML(String fileName) {
        Document document = null;
        SAXReader saxReader = new SAXReader();
        try {
            ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            document = saxReader.read(classLoader.getResourceAsStream(fileName));
            Element beans = document.getRootElement();
            for (Iterator<Element> beansList = beans.elementIterator(); beansList.hasNext();) {
                Element element = beansList.next();
                BeanDefine bean = new BeanDefine(element.attributeValue("id"),
                                                element.attributeValue("class"));
                beanList.add(bean);
            }
        } catch (DocumentException e) {
            log.error("读取配置文件出错。。。");
        }
    }

    /**
     * 实例化Bean
     */
    private void instanceBean() {
        for (BeanDefine bean : beanList) {
            try {
                singletons.put(bean.getId(),
                        Class.forName(bean.getClassName()).newInstance());
            } catch (Exception e) {
                log.error("实例化Bean出错。。。");
            }
        }
    }


    /**
     * 注解处理器
     * 如果注解SddResource配置了name属性，则根据name所指定的名称获取要注入的实例引用
     * 如果注解SddResource未配置name属性，则根据属性所属类型来扫描配置文件获取要注入的示例引用
     */
    private void annotationInject() {
        for (String beanName : singletons.keySet()) {
            Object bean = singletons.get(beanName);
            if (bean != null){
                this.propertyAnnotation(bean);
                this.fieldAnnotation(bean);
            }
        }
    }

    /**
     * 处理setter方法上的注解
     * @param bean 处理的bean
     */
    private void propertyAnnotation(Object bean) {
        try {
            // 1.获取其属性的描述
            PropertyDescriptor[] ps = Introspector.getBeanInfo(
                    bean.getClass()).getPropertyDescriptors();
            for (PropertyDescriptor propertyDesc : ps) {
                // 2.获取属性的set方法
                Method setter = propertyDesc.getWriteMethod();
                // 3.判断set方法是否定义了注解
                if (setter != null && setter.isAnnotationPresent(SddResource.class)){
                    // 4.获取当前注解，并判断name属性是否为空
                    SddResource sddResource = setter.getAnnotation(SddResource.class);
                    String name = "";
                    Object value = null;
                    if (!"".equals(sddResource.name())){
                        // 5.获取注解的name属性对应的内容
                        name = sddResource.name();
                        value = singletons.get(name);
                    }else { // 如果当前注解未指定name属性，则根据类型进行匹配
                        for (String key : singletons.keySet()) {
                            // 5.判断当前属性所属的类型在配置文件中是否存在
                            if (propertyDesc.getPropertyType().isAssignableFrom(singletons.get(key).getClass())){
                                // 获取类型匹配的实例对象
                                value = singletons.get(key);
                                break;
                            }
                        }
                    }
                    // 6.允许访问private方法
                    setter.setAccessible(true);
                    // 7.把引用对象注入属性
                    setter.invoke(bean, value);
                }
            }
        } catch (Exception e) {
            log.error("set方法注解解析异常。。。");
        }
    }

    /**
     * 处理字段上的注解
     * @param bean 处理的bean
     */
    private void fieldAnnotation(Object bean) {
        try {
            // 1.获取其全部的字段描述
            Field[] fields = bean.getClass().getFields();
            for (Field f : fields) {
                // 2.判断字段是否定义了注解
                if (f != null && f.isAnnotationPresent(SddResource.class)) {
                    // 3.获取当前注解，并判断name属性是否为空
                    SddResource sddResource = f.getAnnotation(SddResource.class);
                    String name = "";
                    Object value = null;
                    if (!"".equals(sddResource.name())) {
                        // 4.获取注解的name属性对应的内容
                        name = sddResource.name();
                        value = singletons.get(name);
                    } else { // 如果当前注解未指定name属性，则根据类型进行匹配
                        for (String key : singletons.keySet()) {
                            // 4.判断当前属性所属的类型在配置文件中是否存在
                            if (f.getType().isAssignableFrom(singletons.get(key).getClass())) {
                                // 获取类型匹配的实例对象
                                value = singletons.get(key);
                                break;
                            }
                        }
                    }
                    // 5.允许访问private字段
                    f.setAccessible(true);
                    // 6.把引用对象注入属性
                    f.set(bean, value);
                }
            }
        }catch (Exception e){
            log.error("字段注解解析异常。。。");
        }
    }

    /**
     * 根据bean的名称获取Map中的bean实例
     * @param beanId bean的名称
     * @return bean实例
     */
    private Object getBean(String beanId){
        return singletons.get(beanId);
    }
    
    public static void main(String[] args){
        ClassPathXMLApplicationContext context = new ClassPathXMLApplicationContext("configAnnotation.xml");
        TestServiceImpl testService = (TestServiceImpl) context.getBean("testService");
        testService.greet();
    }
}
