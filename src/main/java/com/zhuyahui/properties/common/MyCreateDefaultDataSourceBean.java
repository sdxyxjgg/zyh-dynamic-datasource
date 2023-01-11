package com.zhuyahui.properties.common;


import com.zaxxer.hikari.HikariDataSource;
import com.zhuyahui.util.constant.ChooseDataSourceTypeEnum;
import com.zhuyahui.util.constant.ChooseSlaveDataSourceWayEnum;
import com.zhuyahui.util.constant.MyDynamicDataSourceConstant;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * 默认的配置方式，获取配置文件中的多个数据源
 *
 * @author : Zhu Yahui
 * @version : 1.0.4
 * @date : 2023/1/11
 */
public class MyCreateDefaultDataSourceBean {
    private ChooseDataSourceTypeEnum dataSourceType;
    private ChooseSlaveDataSourceWayEnum switchSlaveType;
    private HikariDataSource master;
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
        if (ObjectUtils.isEmpty(this.master.getDriverClassName())) {
            //如果没有设置驱动，就自动帮他设置上
            this.master.setDriverClassName(MyDynamicDataSourceConstant.DEFAULT_DRIVER_NAME);
        }
    }

    public List<HikariDataSource> getSlaves() {
        return slaves;
    }

    public void setSlaves(List<HikariDataSource> slaves) {
        this.slaves = slaves;
        //如果没有设置驱动，就自动帮他设置上
        this.slaves.forEach(v -> {
            if (ObjectUtils.isEmpty(v.getDriverClassName())) {
                v.setDriverClassName(MyDynamicDataSourceConstant.DEFAULT_DRIVER_NAME);
            }
        });
    }
}
