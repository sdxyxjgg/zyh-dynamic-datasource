package com.zhuyahui.properties.common;


import com.zaxxer.hikari.HikariDataSource;
import com.zhuyahui.util.constant.ChooseDataSourceTypeEnum;
import com.zhuyahui.util.constant.ChooseSlaveDataSourceWayEnum;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * 默认的配置方式，获取配置文件中的多个数据源
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/3
 */
@ConfigurationProperties(prefix = "zyh-datasource")
public class MyCreateDefaultDataSourceBean {
    private ChooseDataSourceTypeEnum dataSourceType;
    private ChooseSlaveDataSourceWayEnum switchSlaveType;
    @NestedConfigurationProperty
    private HikariDataSource master;
    @NestedConfigurationProperty
    private List<HikariDataSource> slaves;

    public void setDataSourceType(ChooseDataSourceTypeEnum dataSourceType) {
        this.dataSourceType = dataSourceType;
    }

    public void setSwitchSlaveType(ChooseSlaveDataSourceWayEnum switchSlaveType) {
        this.switchSlaveType = switchSlaveType;
    }

    public ChooseDataSourceTypeEnum getDataSourceType() {
        return dataSourceType;
    }

    public ChooseSlaveDataSourceWayEnum getSwitchSlaveType() {
        return switchSlaveType;
    }

    public HikariDataSource getMaster() {
        return master;
    }

    public void setMaster(HikariDataSource master) {
        this.master = master;
    }

    public List<HikariDataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<HikariDataSource> slaves) {
        this.slaves = slaves;
    }
}
