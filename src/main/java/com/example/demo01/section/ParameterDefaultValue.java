package com.example.demo01.section;

import java.lang.annotation.*;

/**
 *
 * @Description 方法的参数上加了这个注解，若该参数中有属性为null则赋予默认值
 * @author lujj
 * @date 2018年5月8日18:39:54
 *
 */
@Documented
@Target({ ElementType.PARAMETER })
@Retention(RetentionPolicy.RUNTIME)
public @interface ParameterDefaultValue {

}
