package com.storehub.store.common.log.aspect;

import com.storehub.store.common.log.util.SysLogUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import java.lang.reflect.Method;


/**
 * 所有 controller 拦截器
 */
@Aspect
@Component
public class ControllerAspect {

	@Before("execution(public * com.storehub.store.*.controller..*.*(..))")
	public void beforeControllerMethod(JoinPoint joinPoint) throws NoSuchMethodException {
		// 获取代理方法的签名
		MethodSignature signature = (MethodSignature) joinPoint.getSignature();

		// 获取代理的方法
		Method method = signature.getMethod();

		// 当代理方法是接口方法时，获取到的是接口类对象，需要获取具体类中的方法
		if (method.getDeclaringClass().isInterface()) {
			method = joinPoint.getTarget().getClass()
					.getDeclaredMethod(signature.getName(), method.getParameterTypes());
		}

		// 获取方法上的@RequestMapping注解
		RequestMapping requestMapping = method.getDeclaredAnnotation(RequestMapping.class);

		if (requestMapping != null) {
			// 对于@RequestMapping注解，打印出它的一个属性值，如value
			System.out.println("RequestMapping value: " + String.join(", ", requestMapping.value()));
		}
	}
}
