package com.zhuyahui.util;

import com.zhuyahui.aop.MyDynamicDataSourceAop;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionTemplate;
import org.springframework.util.ObjectUtils;

/**
 * 进一步减少事务粒度，注意这里的读写都会覆盖原先注解的功能，禁止重复使用
 * 如果要解决service方法里面，再写的时候要做好几次查询验证，可以放弃注解，使用这里的读写方法
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2023/1/7
 */
public class MyAloneHandlerReadWrite {
    /**
     * 当前的application
     */
    private static ApplicationContext currentApplicationContext;
    /**
     * 当前的transactionTemplate
     */
    private static TransactionTemplate transactionTemplate;

    public MyAloneHandlerReadWrite(ApplicationContext applicationContext) {
        init(applicationContext);
    }

    /**
     * 初始化
     *
     * @param applicationContext    当前得application
     */
    private static void init(ApplicationContext applicationContext) {
        currentApplicationContext = applicationContext;
        transactionTemplate = currentApplicationContext.getBean(TransactionTemplate.class);
    }

    /**
     * 解决对于不想使用注解或者想在dao层使用事务或者想在service层的方法里面使用事务或者想要进一步减少事务粒度提升效率。
     *
     * @param myDynamicDataSourceParamInterface lambda接口
     * @return 返回原方法执行后的返回值
     */
    public static Object read(MyDynamicDataSourceParamInterface myDynamicDataSourceParamInterface) {
        //需要把当前数据源改为从库
        MyDynamicDataSourceAop.handleRead();
        return myDynamicDataSourceParamInterface.execute();
    }

    /**
     * 解决对于不想使用注解或者想在dao层使用事务或者想在service层的方法里面使用事务或者想要进一步减少事务粒度提升效率。
     * 提供用户指定类型，这样就不用再强转类型了
     *
     * @param myDynamicDataSourceParamInterface lambda接口
     * @param clazz                             提供指定类型
     * @param <T>                               提供指定类型
     * @return 返回原方法执行后的返回值
     */
    public static <T> T read(MyDynamicDataSourceParamInterface myDynamicDataSourceParamInterface, Class<T> clazz) {
        //需要把当前数据源改为从库
        MyDynamicDataSourceAop.handleRead();
        return (T) myDynamicDataSourceParamInterface.execute();
    }

    /**
     * 解决对于不想使用注解或者想在dao层使用事务或者想在service层的方法里面使用事务或者想要进一步减少事务粒度提升效率。
     *
     * @param myDynamicDataSourceParamInterface lambda接口
     * @return 返回原方法执行后的返回值
     */
    public static Object write(MyDynamicDataSourceParamInterface myDynamicDataSourceParamInterface) {
        handlerDefaultTransaction();
        return transactionTemplate.execute(status -> myDynamicDataSourceParamInterface.execute());
    }

    /**
     * 解决对于不想使用注解或者想在dao层使用事务或者想在service层的方法里面使用事务或者想要进一步减少事务粒度提升效率。
     * 提供用户指定类型，这样就不用再强转类型了
     *
     * @param myDynamicDataSourceParamInterface lambda接口
     * @param clazz                             提供指定类型
     * @param <T>                               提供指定类型
     * @return 返回原方法执行后的返回值
     */
    public static <T> T write(MyDynamicDataSourceParamInterface myDynamicDataSourceParamInterface, Class<T> clazz) {
        handlerDefaultTransaction();
        return (T) transactionTemplate.execute(status -> myDynamicDataSourceParamInterface.execute());
    }

    /**
     * 解决对于不想使用注解或者想在dao层使用事务或者想在service层的方法里面使用事务或者想要进一步减少事务粒度提升效率。
     *
     * @param myDynamicDataSourceParamInterface lambda接口
     * @param transactionConfig                 事务的配置
     * @return 返回原方法执行后的返回值
     */
    public static Object write(MyDynamicDataSourceParamInterface myDynamicDataSourceParamInterface, TransactionConfig transactionConfig) {
        handlerTransaction(transactionConfig);
        return transactionTemplate.execute(status -> myDynamicDataSourceParamInterface.execute());
    }

    /**
     * 解决对于不想使用注解或者想在dao层使用事务或者想在service层的方法里面使用事务或者想要进一步减少事务粒度提升效率。
     * 提供用户指定类型，这样就不用再强转类型了
     *
     * @param myDynamicDataSourceParamInterface lambda接口
     * @param clazz                             提供指定类型
     * @param transactionConfig                 事务的配置
     * @param <T>                               提供指定类型
     * @return 返回原方法执行后的返回值
     */
    public static <T> T write(MyDynamicDataSourceParamInterface myDynamicDataSourceParamInterface, Class<T> clazz, TransactionConfig transactionConfig) {
        handlerTransaction(transactionConfig);
        return (T) transactionTemplate.execute(status -> myDynamicDataSourceParamInterface.execute());
    }

    /**
     * 设置默认的transactionTemplate
     */
    private static void handlerDefaultTransaction() {
        //需要把数据源改为主库
        MyDynamicDataSourceAop.handleWrite();
        TransactionTemplate transactionTemplate = currentApplicationContext.getBean(TransactionTemplate.class);
        transactionTemplate.setIsolationLevel(Isolation.READ_COMMITTED.value());
        transactionTemplate.setPropagationBehavior(Propagation.REQUIRED.value());
    }

    /**
     * 根据TransactionConfig设置transactionTemplate
     *
     * @param transactionConfig 事务配置类
     */
    private static void handlerTransaction(TransactionConfig transactionConfig) {
        //需要把数据源改为主库
        MyDynamicDataSourceAop.handleWrite();
        Isolation isolation = transactionConfig.getIsolation();
        Propagation propagation = transactionConfig.getPropagation();
        int timeout = transactionConfig.getTimeout();
        String transactionManager = transactionConfig.getTransactionManager();
        transactionTemplate.setTimeout(timeout);
        transactionTemplate.setPropagationBehavior(propagation.value());
        transactionTemplate.setIsolationLevel(isolation.value());
        if (!ObjectUtils.isEmpty(transactionManager)) {
            transactionTemplate.setTransactionManager((PlatformTransactionManager) currentApplicationContext.getBean(transactionManager));
        }
    }
}
