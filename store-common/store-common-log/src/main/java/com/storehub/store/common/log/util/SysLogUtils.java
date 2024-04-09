/*
 * Copyright (c) 2020 store4cloud Authors. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.storehub.store.common.log.util;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.extra.servlet.JakartaServletUtil;
import cn.hutool.extra.spring.SpringUtil;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONUtil;
import com.storehub.store.common.core.constant.CommonConstants;
import com.storehub.store.common.core.util.SpringContextHolder;
import com.storehub.store.common.log.config.StoreLogProperties;
import com.storehub.store.common.log.event.SysLogEvent;
import com.storehub.store.common.log.event.SysLogEventSource;
import com.storehub.store.common.mybatis.holder.TenantContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.StandardReflectionParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.util.Map;
import java.util.Objects;

/**
 * 系统日志工具类
 *
 * @author L.cm
 */
@Slf4j
@UtilityClass
public class SysLogUtils {

	public Object proceedingJoinPoint(ProceedingJoinPoint point, com.storehub.store.common.log.annotation.SysLog sysLog) throws Throwable {
		String strClassName = point.getTarget().getClass().getName();
		String strMethodName = point.getSignature().getName();
		log.debug("[类名]:{},[方法]:{}", strClassName, strMethodName);

		SysLogEventSource logVo = SysLogUtils.getSysLog();

		if (!Objects.isNull(sysLog)) {
			String value = sysLog.value();
			String expression = sysLog.expression();
			// 当前表达式存在 SPEL，会覆盖 value 的值
			if (StrUtil.isNotBlank(expression)) {
				// 解析SPEL
				MethodSignature signature = (MethodSignature) point.getSignature();
				EvaluationContext context = SysLogUtils.getContext(point.getArgs(), signature.getMethod());
				try {
					value = SysLogUtils.getValue(context, expression, String.class);
				}
				catch (Exception e) {
					// SPEL 表达式异常，获取 value 的值
					log.error("@SysLog 解析SPEL {} 异常", expression);
				}
			}
			logVo.setTitle(value);
		}

		// 获取请求body参数
		if (StrUtil.isBlank(logVo.getParams())) {
			logVo.setBody(point.getArgs());
		}
		// 发送异步日志事件
		Long startTime = System.currentTimeMillis();
		Object obj;

		try {
			obj = point.proceed();
			logVo.setResponse(handlerResult(obj));
		}
		catch (Exception e) {
			logVo.setLogType(LogTypeEnum.ERROR.getType());
			logVo.setException(e.getMessage());
			throw e;
		}
		finally {
			Long endTime = System.currentTimeMillis();
			logVo.setTime(endTime - startTime);
			SpringContextHolder.publishEvent(new SysLogEvent(logVo));
		}

		return obj;
	}

	public SysLogEventSource getSysLog() {
		HttpServletRequest request = ((ServletRequestAttributes) Objects
			.requireNonNull(RequestContextHolder.getRequestAttributes())).getRequest();
		SysLogEventSource sysLog = new SysLogEventSource();
		sysLog.setLogType(LogTypeEnum.NORMAL.getType());
		sysLog.setRequestUri(URLUtil.getPath(request.getRequestURI()));
		sysLog.setMethod(request.getMethod());
		sysLog.setRemoteAddr(JakartaServletUtil.getClientIP(request));
		sysLog.setUserAgent(request.getHeader(HttpHeaders.USER_AGENT));
		sysLog.setCreateBy(getUsername());
		sysLog.setServiceId(SpringUtil.getProperty("spring.application.name"));
		sysLog.setTenantId(TenantContextHolder.getTenantId());
		sysLog.setTraceId(request.getHeader(CommonConstants.TRACE_HEADER));

		// get 参数脱敏
		StoreLogProperties logProperties = SpringContextHolder.getBean(StoreLogProperties.class);
		Map<String, String[]> paramsMap = MapUtil.removeAny(request.getParameterMap(),
				ArrayUtil.toArray(logProperties.getExcludeFields(), String.class));
		try {
			sysLog.setParams(URLDecoder.decode(HttpUtil.toParams(paramsMap), "utf-8"));
		} catch (UnsupportedEncodingException e) {
			sysLog.setParams(HttpUtil.toParams(paramsMap));
		}
		return sysLog;
	}

	/**
	 * 获取用户名称
	 * @return username
	 */
	private String getUsername() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null) {
			return null;
		}
		return authentication.getName();
	}

	/**
	 * 获取spel 定义的参数值
	 * @param context 参数容器
	 * @param key key
	 * @param clazz 需要返回的类型
	 * @param <T> 返回泛型
	 * @return 参数值
	 */
	public <T> T getValue(EvaluationContext context, String key, Class<T> clazz) {
		SpelExpressionParser spelExpressionParser = new SpelExpressionParser();
		Expression expression = spelExpressionParser.parseExpression(key);
		return expression.getValue(context, clazz);
	}

	/**
	 * 获取参数容器
	 * @param arguments 方法的参数列表
	 * @param signatureMethod 被执行的方法体
	 * @return 装载参数的容器
	 */
	public EvaluationContext getContext(Object[] arguments, Method signatureMethod) {
		String[] parameterNames = new StandardReflectionParameterNameDiscoverer().getParameterNames(signatureMethod);
		EvaluationContext context = new StandardEvaluationContext();
		if (parameterNames == null) {
			return context;
		}
		for (int i = 0; i < arguments.length; i++) {
			context.setVariable(parameterNames[i], arguments[i]);
		}
		return context;
	}

	/**
	 * 返回结果简单处理
	 * 1）把返回结果转成String，方便输出。
	 *
	 * @param result 原方法调用的返回结果
	 * @return 处理后的
	 */
	private String handlerResult(Object result) {
		if (result == null) {
			return null;
		}
		String resultStr;
		try {
			if (result instanceof String) {
				resultStr = (String) result;
			} else {
				resultStr = JSONUtil.toJsonStr(result);// 如果返回结果非String类型，转换成JSON格式的字符串
			}
		} catch (Exception e) {
			resultStr = result.toString();
			log.error("接口出入参日志打印切面处理返回参数异常", e);
		}
		return resultStr;
	}

}
