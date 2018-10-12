package com.example.demo01.section;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ControllerSection {

    //存放需要设置默认值的容器
    private static Map<Object,Object> map = null;
    /*
     * 定义一个切入点s
     */
//    @Pointcut("execution(public * com.example.demo01.*.*(..))")
    public void cut() {
    }

//    @Around("cut()")
    public void isAccessMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Annotation[][] methodAnnotations = method.getParameterAnnotations();
        Object[] args = null;
        //遍历寻找ParameterDefaultValue注解的属性
        for(int i = 0;i<methodAnnotations.length;i++){
            for(int j = 0;j<methodAnnotations[i].length;j++){
                //若有
                if (methodAnnotations[i][j] instanceof ParameterDefaultValue){
                    //初始化map容器
                    if(map == null){
                        initDefaultValue();
                    }
                    //获取方法参数
                    if(args == null){
                        args = joinPoint.getArgs();
                    }
                    Object obj = args[i];
                    Class targetClass = args[i].getClass();
                    Field[] filds = targetClass.getDeclaredFields();
                    //遍历该类的所有属性寻找为空的属性赋值
                    for(Field field: filds){
                        String fieldName = field.getName();
                        //获得属性的get方法
                        Method getMethod = targetClass.getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1));
                        Object invoke = getMethod.invoke(obj);
                        if(invoke == null ||(invoke instanceof String && "".equals(((String) invoke).trim()))){
                            //赋予默认值
                            defaultValue(fieldName,targetClass,obj,field);
                        }
                    }
                }
            }
        }
        if(args!=null){

            joinPoint.proceed(args);
        }else {
            joinPoint.proceed();

        }
    }

    /**
     * 初始化默认类型及初始值
     */
    private void initDefaultValue() {
        map = new HashMap<>(20);
        map.put("INT",0);
        map.put("INTEGER",0);
        map.put("DOUBLE",0);
        map.put("CHAR",'0');
        map.put("CHARACTER",'0');
        map.put("SHORT",0);
        map.put("LONG",0);
        map.put("FLOAT",0);
        map.put("BOOLEAN",false);
        map.put("BYTE",0);
        map.put("STRING","this is null");
        addDefaultValue(map);
    }

    /**
     * 自定义添加/修改属性 需要全大写
     * @param map
     */
    public void addDefaultValue(Map<Object,Object> map) {

    }

    private void defaultValue(String fieldName, Class aClass, Object obj,Field field) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException, InstantiationException {
        String setMethod = "set"+ fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
        Method method = aClass.getMethod(setMethod, field.getType());
        //获取属性类型的名称
        String fieldType = field.getType().getSimpleName().toUpperCase();
       if(map.containsKey(fieldType) ){
           method.invoke(obj,map.get(fieldType));
       }
    }


}
