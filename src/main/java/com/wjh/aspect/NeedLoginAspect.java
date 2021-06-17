package com.wjh.aspect;

import com.wjh.annotation.NeedLogin;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;



@Aspect
@Component
public class NeedLoginAspect {
    

    @Pointcut("@annotation(com.wjh.annotation.NeedLogin)")
    public void powerFilterCut() {

    }

    @Around("powerFilterCut()")
    public Object powerFilter(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        NeedLogin needLogin = signature.getMethod().getAnnotation(NeedLogin.class);
//        String[] arr = {"A", "B", "C"};
//        Set<String> strings = new HashSet<>(Arrays.asList(arr));
//        List<String> abc = Arrays.asList(needLogin.value());
//        Iterator<String> iterator = abc.iterator();
//        while (iterator.hasNext()) {
//            boolean power = strings.contains(iterator.next());
//            if (power) {
//                hasPower = true;
//            }
//        }
//        if (hasPower) {
//            String result = (String) joinPoint.proceed();
//            System.out.println("Arount——after" + result);
//            return result+ "——Around";
//        }
        String principal = "";
        if (principal != null) {
            Object result = (Object) joinPoint.proceed();
            return result;
        } else {
            return "登录已失效，请重新登录";
        }
    }

}
