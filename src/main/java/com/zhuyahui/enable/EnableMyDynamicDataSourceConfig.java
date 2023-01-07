package com.zhuyahui.enable;

import com.zhuyahui.MyDynamicDataSourceConfig;
import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * 此类用于把MyDynamicDataSourceConfig这个配置类通过@Import的方式注入到spring容器中
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/2
 */
public class EnableMyDynamicDataSourceConfig implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{MyDynamicDataSourceConfig.class.getName()};
    }
}
