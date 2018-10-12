package com.example.demo01.excel;

import java.lang.annotation.*;

/**
 *
 * @Description 成员变量上加入这个属性，在将该对象转换为excel时，自动设置表头为value的值
 * @author lujj
 * @date 2018年5月22日17:40:30
 *
 */
@Documented
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelHead {
    public String value ();

}