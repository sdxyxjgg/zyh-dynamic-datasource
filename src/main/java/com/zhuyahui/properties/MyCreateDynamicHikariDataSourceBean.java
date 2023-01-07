package com.zhuyahui.properties;

import com.zaxxer.hikari.HikariDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.List;

/**
 * hikari的方式，获取配置文件中的多个数据源
 * 对于某个属性，比较复杂，可以使用这个@NestedConfigurationProperty，嵌套配置
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2022/12/31
 */
@ConfigurationProperties(prefix = "zyh-datasource.hikari")
public class MyCreateDynamicHikariDataSourceBean {
    @NestedConfigurationProperty
    private HikariDataSource master;
    @NestedConfigurationProperty
    private List<HikariDataSource> slaves;

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
