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

package com.storehub.store.common.security.service;

import com.storehub.store.admin.api.dto.UserDTO;
import com.storehub.store.admin.api.dto.UserInfo;
import com.storehub.store.admin.api.feign.RemoteUserService;
import com.storehub.store.common.core.constant.CacheConstants;
import com.storehub.store.common.core.constant.SecurityConstants;
import com.storehub.store.common.core.util.R;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * 用户详细信息
 *
 * @author lengleng hccake
 */
@Slf4j
@RequiredArgsConstructor
public class StoreAppUserDetailsServiceImpl implements StoreUserDetailsService {

	private final RemoteUserService remoteUserService;

	private final CacheManager cacheManager;

	/**
	 * 手机号登录
	 * @param phone 手机号
	 * @return
	 */
	@Override
	@SneakyThrows
	public UserDetails loadUserByUsername(String phone) {
		Cache cache = cacheManager.getCache(CacheConstants.USER_DETAILS);
		if (cache != null && cache.get(phone) != null) {
			return (StoreUser) cache.get(phone).get();
		}

		UserDTO userDTO = new UserDTO();
		userDTO.setPhone(phone);
		R<UserInfo> result = remoteUserService.info(userDTO, SecurityConstants.FROM_IN);

		UserDetails userDetails = getUserDetails(result);
		if (cache != null) {
			cache.put(phone, userDetails);
		}
		return userDetails;
	}

	/**
	 * check-token 使用
	 * @param storeUser user
	 * @return
	 */
	@Override
	public UserDetails loadUserByUser(StoreUser storeUser) {
		return this.loadUserByUsername(storeUser.getPhone());
	}

	/**
	 * 是否支持此客户端校验
	 * @param clientId 目标客户端
	 * @return true/false
	 */
	@Override
	public boolean support(String clientId, String grantType) {
		return SecurityConstants.MOBILE.equals(grantType);
	}

}
