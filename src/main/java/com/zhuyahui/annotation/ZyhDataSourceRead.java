package com.zhuyahui.annotation;

import java.lang.annotation.*;

/**
 * 此注解为读注解，读取数据无需事务
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2022/12/31
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface ZyhDataSourceRead {
}
