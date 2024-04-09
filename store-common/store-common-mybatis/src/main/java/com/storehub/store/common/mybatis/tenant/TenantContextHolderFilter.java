package com.storehub.store.common.mybatis.tenant;

import cn.hutool.core.util.StrUtil;
import com.storehub.store.common.core.constant.CommonConstants;
import com.storehub.store.common.mybatis.holder.TenantContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.GenericFilterBean;

import java.io.IOException;

@Slf4j
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class TenantContextHolderFilter extends GenericFilterBean {

	private final static String UNDEFINED_STR = "undefined";

	@Override
	@SneakyThrows
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) servletRequest;
		HttpServletResponse response = (HttpServletResponse) servletResponse;

		String headerTenantId = request.getHeader(CommonConstants.TENANT_ID);
		String paramTenantId = request.getParameter(CommonConstants.TENANT_ID);

		log.debug("获取 header 中租户ID:{}", headerTenantId);

		if (StrUtil.isNotBlank(headerTenantId) && !StrUtil.equals(UNDEFINED_STR, headerTenantId)) {
			TenantContextHolder.setTenantId(Long.parseLong(headerTenantId));
		} else if (StrUtil.isNotBlank(paramTenantId) && !StrUtil.equals(UNDEFINED_STR, paramTenantId)) {
			TenantContextHolder.setTenantId(Long.parseLong(paramTenantId));
		} else {
			TenantContextHolder.setTenantId(CommonConstants.TENANT_ID_1);
		}

		filterChain.doFilter(request, response);
		TenantContextHolder.clear();
	}
}
