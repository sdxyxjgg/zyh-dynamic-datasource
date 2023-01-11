package com.zhuyahui;

import com.alibaba.druid.pool.DruidDataSource;
import com.zaxxer.hikari.HikariDataSource;
import com.zhuyahui.aop.MyDynamicDataSourceAop;
import com.zhuyahui.properties.MyCreateDynamicDruidDataSourceBean;
import com.zhuyahui.properties.MyCreateDynamicHikariDataSourceBean;
import com.zhuyahui.properties.MyHandleDataSourceParam;
import com.zhuyahui.properties.common.MyCreateDefaultDataSourceBean;
import com.zhuyahui.util.MyAloneHandlerReadWrite;
import com.zhuyahui.util.constant.MyDynamicDataSourceConstant;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 功能：配置动态数据源
 *
 * @author : Zhu Yahui
 * @version : 1.0.4
 * @date : 2023/1/11
 */
@Import({
        MyHandleDataSourceParam.class,
        MyDynamicDataSourceAop.class,
        MyAloneHandlerReadWrite.class,
        MyDynamicDataSourceConfig.MyDynamicHikariDataSourceConfig.class
})
@EnableConfigurationProperties
public class MyDynamicDataSourceConfig {
    /**
     * 创建hikari数据源
     */
    @ConditionalOnClass(HikariDataSource.class)
    @ConfigurationProperties(prefix = "zyh-datasource")
    @Bean
    public MyCreateDefaultDataSourceBean myCreateDefaultDataSourceBean() {
        return new MyCreateDefaultDataSourceBean();
    }

    /**
     * 创建hikari数据源
     */
    @ConditionalOnClass(HikariDataSource.class)
    @ConfigurationProperties(prefix = "zyh-datasource.hikari")
    @Bean
    public MyCreateDynamicHikariDataSourceBean myCreateDynamicHikariDataSourceBean() {
        return new MyCreateDynamicHikariDataSourceBean();
    }

    /**
     * 创建druid数据源
     */
    @ConditionalOnClass(DruidDataSource.class)
    @ConfigurationProperties(prefix = "zyh-datasource.druid")
    @Bean
    public MyCreateDynamicDruidDataSourceBean myCreateDynamicDruidDataSourceBean() {
        return new MyCreateDynamicDruidDataSourceBean();
    }

    /**
     * 功能：解决循环依赖的问题
     *
     * @author : Zhu Yahui
     * @version : 1.0.4
     * @date : 2023/1/11
     */
    public static class MyDynamicHikariDataSourceConfig {

        private final MyHandleDataSourceParam myHandleDataSourceParam;

        public MyDynamicHikariDataSourceConfig(MyHandleDataSourceParam myHandleDataSourceParam) {
            this.myHandleDataSourceParam = myHandleDataSourceParam;
        }

        /**
         * 注册AbstractRoutingDataSource这个bean，实现动态切换数据源
         *
         * @return 返回多数据源bean
         */
        @Bean
        @Primary
        public MyDynamicDataSource getMyDynamicHikariDataSource() {
            DataSource masterDataSource = myHandleDataSourceParam.getMasterDataSource();
            List<DataSource> slaveDataSourceList = myHandleDataSourceParam.getSlaveDataSourceList();
            //设置hashmap初始值
            Map<Object, Object> dataSourceMap = new HashMap<>(slaveDataSourceList.size() + 1);
            dataSourceMap.put(MyDynamicDataSourceConstant.DEFAULT_DATASOURCE_BEAN_NAME, masterDataSource);
            for (int i = 0; i < slaveDataSourceList.size(); i++) {
                String slaveName = String.format("slave%dDataSource", i + 1);
                MyHandleDataSourceParam.SLAVE_NAME.add(slaveName);
                dataSourceMap.put(slaveName, slaveDataSourceList.get(i));
            }
            MyDynamicDataSource myDynamicDataSource = new MyDynamicDataSource();
            myDynamicDataSource.setTargetDataSources(dataSourceMap);
            myDynamicDataSource.setDefaultTargetDataSource(masterDataSource);
            return myDynamicDataSource;
        }
    }
}