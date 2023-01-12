package com.zhuyahui.properties;

import com.alibaba.druid.pool.DruidDataSource;
import com.zhuyahui.util.constant.MyDynamicDataSourceConstant;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * druid的方式，获取配置文件中的多个数据源
 *
 * @author : Zhu Yahui
 * @version : 1.0.5
 * @date : 2023/1/12
 */
public class MyCreateDynamicDruidDataSourceBean {
    @NestedConfigurationProperty
    private DruidDataSource master;
    @NestedConfigurationProperty
    private List<DruidDataSource> slaves;

    public void setMaster(DruidDataSource master) {
        this.master = master;
        if (ObjectUtils.isEmpty(this.master.getDriverClassName())) {
            //如果没有设置驱动，就自动帮他设置上
            this.master.setDriverClassName(MyDynamicDataSourceConstant.DEFAULT_DRIVER_NAME);
        }
    }

    public void setSlaves(List<DruidDataSource> slaves) {
        this.slaves = slaves;
        //如果没有设置驱动，就自动帮他设置上
        this.slaves.forEach(v -> {
            if (ObjectUtils.isEmpty(v.getDriverClassName())) {
                v.setDriverClassName(MyDynamicDataSourceConstant.DEFAULT_DRIVER_NAME);
            }
        });
    }

    public DruidDataSource getMaster() {
        return master;
    }

    public List<DruidDataSource> getSlaves() {
        return slaves;
    }
}
