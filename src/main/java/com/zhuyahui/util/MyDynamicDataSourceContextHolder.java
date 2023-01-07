package com.zhuyahui.util;


import com.zhuyahui.util.constant.MyDynamicDataSourceConstant;

/**
 * 提供和修改当前线程要使用的数据源的名字
 *
 * @author : Zhu Yahui
 * @date : 2022/12/31
 * @version : 1.0.0
 */
public class MyDynamicDataSourceContextHolder {

    /**
     * ThreadLocal 用于提供线程局部变量，在多线程环境可以保证各个线程里的变量独立于其它线程里的变量。
     */
    private static final ThreadLocal<String> DATASOURCE_BEAN_NAME = new ThreadLocal<>();

    /**
     * 设置数据源
     *
     * @param key 设置当前线程使用的datasource的key值
     */
    public static void setContextKey(String key) {
        DATASOURCE_BEAN_NAME.set(key);
    }

    /**
     * 获取数据源名称
     *
     * @return 返回当前线程的datasource对应的key
     */
    public static String getContextKey() {
        String key = DATASOURCE_BEAN_NAME.get();
        return key == null ? MyDynamicDataSourceConstant.DEFAULT_DATASOURCE_BEAN_NAME : key;
    }

    /**
     * 删除当前数据源名称
     */
    public static void removeContextKey() {
        DATASOURCE_BEAN_NAME.remove();
    }

}
