package org.cmcc.service.log.annotation;

import java.lang.annotation.*;


/**
 * 标志该字段为主键
 * 也可通过注解：org.cmcc.service.log.annotation.Column的isKey属性实现
 * @author cmcc
 * @version 2020年5月26日 下午6:13:15
 */
// 该注解用于方法声明
@Target(ElementType.FIELD)
// VM将在运行期也保留注释，因此可以通过反射机制读取注解的信息
@Retention(RetentionPolicy.RUNTIME)
// 将此注解包含在javadoc中
@Documented
public @interface IsKey {
}
