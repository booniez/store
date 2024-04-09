package com.storehub.store.auth.support.core;

import com.storehub.store.common.core.constant.CommonConstants;
import com.storehub.store.common.core.constant.SecurityConstants;
import com.storehub.store.common.security.service.StoreUser;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenClaimsSet;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;

import java.util.Objects;

/**
 * token 输出增强
 *
 * @author lengleng
 * @date 2022/6/3
 */
public class CustomeOAuth2TokenCustomizer implements OAuth2TokenCustomizer<OAuth2TokenClaimsContext> {

	/**
	 * Customize the OAuth 2.0 Token attributes.
	 * @param context the context containing the OAuth 2.0 Token attributes
	 */
	@Override
	public void customize(OAuth2TokenClaimsContext context) {
		OAuth2TokenClaimsSet.Builder claims = context.getClaims();
		claims.claim(SecurityConstants.DETAILS_LICENSE, SecurityConstants.PROJECT_LICENSE);
		String clientId = context.getAuthorizationGrant().getName();
		claims.claim(SecurityConstants.CLIENT_ID, clientId);
		// 客户端模式不返回具体用户信息
		if (SecurityConstants.CLIENT_CREDENTIALS.equals(context.getAuthorizationGrantType().getValue())) {
			return;
		}

		StoreUser storeUser = (StoreUser) context.getPrincipal().getPrincipal();
		//TODO: 增强租户ID
		if (!Objects.isNull(storeUser.getTenantId())) {
			claims.claim(SecurityConstants.TENANT_ID, storeUser.getTenantId().toString());
			// DETAILS_TENANT_USER_ID
			claims.claim(SecurityConstants.DETAILS_TENANT_USER_ID, storeUser.getTenantUserId());
		} else {
			claims.claim(SecurityConstants.TENANT_ID, CommonConstants.TENANT_ID_1.toString());
		}
		claims.claim(SecurityConstants.DETAILS_USER, storeUser);
		claims.claim(SecurityConstants.DETAILS_USER_ID, storeUser.getId());
		claims.claim(SecurityConstants.USERNAME, storeUser.getUsername());
	}

}
