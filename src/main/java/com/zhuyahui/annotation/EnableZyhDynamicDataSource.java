package com.zhuyahui.annotation;

import com.zhuyahui.enable.EnableMyDynamicDataSourceConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 负责开关的作用
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/2
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import({EnableMyDynamicDataSourceConfig.class})
public @interface EnableZyhDynamicDataSource {
}
