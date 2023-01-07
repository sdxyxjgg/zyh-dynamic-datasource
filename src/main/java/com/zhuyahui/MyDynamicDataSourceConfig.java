package com.zhuyahui;

import com.zhuyahui.aop.MyDynamicDataSourceAop;
import com.zhuyahui.properties.MyCreateDynamicDruidDataSourceBean;
import com.zhuyahui.properties.MyCreateDynamicHikariDataSourceBean;
import com.zhuyahui.properties.MyHandleDataSourceParam;
import com.zhuyahui.properties.common.MyCreateDefaultDataSourceBean;
import com.zhuyahui.util.MyAloneHandlerReadWrite;
import com.zhuyahui.util.constant.MyDynamicDataSourceConstant;
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
 * @version : 1.0.0
 * @date : 2022/12/30
 */
@Import({
        MyHandleDataSourceParam.class,
        MyDynamicDataSourceAop.class,
        MyAloneHandlerReadWrite.class
})
@EnableConfigurationProperties({
        MyCreateDynamicDruidDataSourceBean.class,
        MyCreateDynamicHikariDataSourceBean.class,
        MyCreateDefaultDataSourceBean.class
})
public class MyDynamicDataSourceConfig {

    private final MyHandleDataSourceParam myHandleDataSourceParam;

    public MyDynamicDataSourceConfig(MyHandleDataSourceParam myHandleDataSourceParam) {
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