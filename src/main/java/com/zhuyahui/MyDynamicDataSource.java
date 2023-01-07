package com.zhuyahui;

import com.zhuyahui.util.MyDynamicDataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * 动态分配数据源
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2022/12/31
 */
public class MyDynamicDataSource extends AbstractRoutingDataSource {
    @Override
    protected Object determineCurrentLookupKey() {
        /**
         * 在这里提供数据源的名字，根据这个名字寻找对应的数据源，来动态切换
         */
        return MyDynamicDataSourceContextHolder.getContextKey();
    }
}
