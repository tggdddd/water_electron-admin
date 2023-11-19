package com.ruoyi.business.aspect.annotation;

import java.lang.annotation.*;

/**
 * @ClassName AutoDict
 * @Description 在Controller方法上注解AutoDict实现自动字典翻译
 * @Author 肖润杰
 * @Time 2023/4/3 16:51
 * @Version 1.0
 */
@Target({ElementType.METHOD,ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface AutoDict {

    /**
     * 暂时无用
     * @return
     */
    String value() default "";

}
