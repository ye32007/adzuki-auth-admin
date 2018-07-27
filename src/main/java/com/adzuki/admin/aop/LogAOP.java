package com.adzuki.admin.aop;

import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.adzuki.admin.annotation.LogAspect;
import com.adzuki.admin.common.filter.SecurityFilter;
import com.adzuki.admin.common.support.IPUtil;
import com.adzuki.admin.common.support.SecurityContext;
import com.adzuki.admin.entity.OperLog;
import com.adzuki.admin.manager.OperLogManager;
import com.adzuki.admin.vo.LoginUser;
import com.alibaba.fastjson.JSON;

@Aspect
@Component
public class LogAOP {

    private static Logger logger = LoggerFactory.getLogger(LogAOP.class);

    @Autowired
    private OperLogManager operLogManager;

    @Around("execution(* com.bitsdaq..controller..*.*(..)) and @annotation(logAspect)")
    // 指定拦截器规则；也可以直接把"execution(* com.xjj………)"写进这里
    public Object Interceptor(ProceedingJoinPoint pjp, LogAspect logAspect) throws Throwable {
        LoginUser user = (LoginUser) SecurityContext.getAuthUser();
        MethodSignature signature = (MethodSignature) pjp.getSignature();
        Method method = signature.getMethod(); // 获取被拦截的方法
        String methodName = method.getName(); // 获取被拦截的方法名

        logger.info("请求开始，类：{}，方法：{}", signature.getDeclaringType().getSimpleName(), methodName);

        this.addLog(user, logAspect, pjp.getArgs());//增加操作日志

        Object result = pjp.proceed();

        if (user == null && "authLogin".equals(methodName)) {
            HttpSession session = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest().getSession();
            user = (LoginUser) session.getAttribute(SecurityFilter.SESSION_USER_KEY);
            this.addLog(user, logAspect, pjp.getArgs());
        }
        return result;
    }

    private void addLog(LoginUser user, LogAspect logAspect, Object[] args) {
        if (user != null) {
            try {
                operLogManager.save(this.buildLog(user, logAspect, args));
            } catch (Exception e) {
                logger.error("记录操作日志异常", e);
            }
        }
    }

    private OperLog buildLog(LoginUser user, LogAspect logAspect, Object[] args) {
        StringBuilder operData = new StringBuilder();

        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        //对象信息
        if (logAspect.objectNames() != null && logAspect.objectNames().length > 0) {
            Map<String, Object> map = new HashMap<String, Object>();
            if (args != null && args.length > 0) {
                for (Object arg : args) {
                    if (arg != null) {
                        map.put(arg.getClass().getSimpleName(), arg);
                    }
                }
            }
            for (Class<?> clazz : logAspect.objectNames()) {
                if (map.containsKey(clazz.getSimpleName())) {
                    operData.append(clazz.getSimpleName()).append("=").append(JSON.toJSONString(map.get(clazz.getSimpleName()))).append(",");
                }
            }
        }
        //参数信息
        if (logAspect.argNames() != null && logAspect.argNames().length > 0) {
            for (String paraName : logAspect.argNames()) {
                String value = request.getParameter(paraName);
                if (StringUtils.isNoneBlank(value)) {
                    operData.append(paraName).append("=").append(value).append(",");
                }
            }
        }
        if (operData.length() > 0) {
            operData = operData.deleteCharAt(operData.lastIndexOf(","));
        }

        String companyName = "系统";

        OperLog operLog = new OperLog();
        operLog.setUsername(user.getUsername());
        operLog.setMobile(user.getMobile());
        operLog.setCompanyName(companyName);
        operLog.setContent(logAspect.type() != null ? logAspect.type().getMsg() : logAspect.value());
        operLog.setOperData(operData.toString());
        operLog.setIp(IPUtil.getIpAddress(request));
        operLog.setOperTime(new Date());
        return operLog;
    }
}
