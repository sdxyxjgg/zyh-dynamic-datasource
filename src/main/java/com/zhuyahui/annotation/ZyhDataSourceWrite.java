package com.zhuyahui.annotation;

import org.springframework.core.annotation.AliasFor;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.lang.annotation.*;

/**
 * 这个注解声明为写注解，自带事务
 * 标记注解@Inherited，用来指定该注解可以被继承。使用 @Inherited 注解的 Class 类，表示这个注解可以被用于该 Class 类的子类。就是说如果某个类使用了被 @Inherited 修饰的注解，则其子类将自动具有该注解
 *
 * @author : Zhu Yahui
 * @version : 1.0.0
 * @date : 2022/12/31
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@Transactional
public @interface ZyhDataSourceWrite {
    @AliasFor(
            annotation = Transactional.class
    )
    String value() default "";

    @AliasFor(
            annotation = Transactional.class
    )
    String transactionManager() default "";

    @AliasFor(
            annotation = Transactional.class
    )
    Propagation propagation() default Propagation.REQUIRED;

    @AliasFor(
            annotation = Transactional.class
    )
    Isolation isolation() default Isolation.READ_COMMITTED;

    @AliasFor(
            annotation = Transactional.class
    )
    int timeout() default -1;

    @AliasFor(
            annotation = Transactional.class
    )
    boolean readOnly() default false;

    @AliasFor(
            annotation = Transactional.class
    )
    Class<? extends Throwable>[] rollbackFor() default {};

    @AliasFor(
            annotation = Transactional.class
    )
    String[] rollbackForClassName() default {};

    @AliasFor(
            annotation = Transactional.class
    )
    Class<? extends Throwable>[] noRollbackFor() default {};

    @AliasFor(
            annotation = Transactional.class
    )
    String[] noRollbackForClassName() default {};
}
