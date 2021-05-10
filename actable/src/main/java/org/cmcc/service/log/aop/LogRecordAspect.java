package org.cmcc.service.log.aop;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.cmcc.service.log.bean.PcacOptLog;
import org.cmcc.service.log.manager.common.BaseCRUDManagerImpl;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.Date;

/**
 * Created by ldy
 */
@Aspect
@Configuration
@Slf4j
public class LogRecordAspect {

    // 定义切点Pointcut
    @Pointcut("(execution(* org.cmcc..*.dao..*.*(..)) || execution(* org.cmcc..*.mapper..*.*(..))) && !execution(* org.cmcc.service.log.dao..*.*(..)))")
    public void excudeService() {
    }

    @Around("excudeService()")
    public Object doAround(ProceedingJoinPoint pjp) throws Throwable {
        BaseCRUDManagerImpl crudManager = SpringBeanUtil.getBean("baseCRUDManagerImpl", BaseCRUDManagerImpl.class);
        // result的值就是被拦截方法的返回值
        Object result = pjp.proceed();
        Object[] args = pjp.getArgs();
        String argsStr = Arrays.toString(args);
        PcacOptLog pcacOptLog = new PcacOptLog();
        pcacOptLog.setCreatedBy("cmcc");
        pcacOptLog.setCreatedTime(new Date());
        pcacOptLog.setUpdatedTime(new Date());
        pcacOptLog.setUpdatedBy("cmcc");
        pcacOptLog.setOptContent("请求参数：" + argsStr + "；响应结果：" + result.toString());
        crudManager.insert(pcacOptLog);
        log.info("请求结束，controller的返回值是 " + result);
        return result;
    }
}
