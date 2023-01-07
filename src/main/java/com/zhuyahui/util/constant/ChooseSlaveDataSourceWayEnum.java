package com.zhuyahui.util.constant;

/**
 * 多个从数据库具体选择方式
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/3
 */
public enum ChooseSlaveDataSourceWayEnum {
    /**
     * 随机
     */
    RANDOM,

    /**
     * 轮询
     */
    POLLING;
}
