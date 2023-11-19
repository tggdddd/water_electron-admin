package com.ruoyi.business.aspect.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @ClassName Dict
 * @Description 字典注解
 * @Author 肖润杰
 * @Time 2023/4/3 16:42
 * @Version 1.0
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface Dict {
    /** 字典类型  普通字典为type    表字典为table：id字段：label字段  */
    String value();
}
