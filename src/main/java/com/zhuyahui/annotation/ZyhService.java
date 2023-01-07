package com.zhuyahui.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * 此注解用于提供给spring容器，使用此注解代表使用读写分离，否则就是原生的效果
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Component
public @interface ZyhService {
    @AliasFor(
            annotation = Component.class
    )
    String value() default "";
}
