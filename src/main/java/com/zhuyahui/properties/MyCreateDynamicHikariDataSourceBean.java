package com.zhuyahui.properties;

import com.zaxxer.hikari.HikariDataSource;
import com.zhuyahui.util.constant.MyDynamicDataSourceConstant;
import org.springframework.util.ObjectUtils;

import java.util.List;

/**
 * hikari的方式，获取配置文件中的多个数据源
 * 对于某个属性，比较复杂，可以使用这个@NestedConfigurationProperty，嵌套配置
 *
 * @author : Zhu Yahui
 * @version : 1.0.4
 * @date : 2023/1/11
 */
public class MyCreateDynamicHikariDataSourceBean {
    private HikariDataSource master;
    private List<HikariDataSource> slaves;

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
