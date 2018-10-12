package com.example.demo01.bean;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.*;

public class StudentBean implements InitializingBean, DisposableBean, BeanNameAware, BeanFactoryAware {

    private String name;
    private int age;

    private String beanName;//实现了BeanNameAware接口，Spring可以将BeanName注入该属性中
    private BeanFactory beanFactory;//实现了BeanFactory接口，Spring可将BeanFactory注入该属性中

    public StudentBean(){
        System.out.println("【Bean构造方法】学生类的无参构造方法");
    }

    @Override
    public String toString() {
        return "StudentBean{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", beanName='" + beanName + '\'' +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getBeanName() {
        return beanName;
    }

    public BeanFactory getBeanFactory() {
        return beanFactory;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        System.out.println("BeanFactoryAware的方法执行了");
    }

    @Override
    public void setBeanName(String s) {
        System.out.println("BeanNameAware的方法执行了");
    }

    @Override
    public void destroy() throws Exception {
        System.out.println("DisposableBean的方法执行了");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        System.out.println("InitializingBean的方法执行了");
    }
}
