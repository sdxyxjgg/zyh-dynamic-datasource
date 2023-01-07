package com.zhuyahui.util;

/**
 * 提供接口来代替数据库得执行
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/7
 */
@FunctionalInterface
public interface MyDynamicDataSourceParamInterface {
    /**
     * 提供使用lambda表达式
     *
     * @return 执行数据库的返回值
     */
    Object execute();
}
