package cn.huang.core.annotation;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ComponentScans;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author huangl
 * @description EnableInfluxWrapper
 * @date 2023/10/12 19:03:51
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@ComponentScans({
        @ComponentScan("cn.huang.core.config"),
        @ComponentScan("cn.huang.core.service.impl")
})
public @interface EnableInfluxWrapper {
}
