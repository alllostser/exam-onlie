///**
// * Copyright (c) 2018-2028, Chill Zhuang 庄骞 (smallchill@163.com).
// * <p>
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// * <p>
// * http://www.apache.org/licenses/LICENSE-2.0
// * <p>
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// */
//package org.springblade.auth.controller;
//
//import com.exam.user.entity.UserInfo;
//import io.swagger.annotations.Api;
//import lombok.AllArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springblade.auth.granter.ITokenGranter;
//import org.springblade.auth.granter.TokenGranterBuilder;
//import org.springblade.auth.granter.TokenParameter;
//import org.springblade.core.secure.AuthInfo;
//import org.springblade.core.tool.api.R;
//import org.springblade.core.tool.utils.Func;
//import org.springblade.core.tool.utils.RedisUtil;
//import org.springblade.core.tool.utils.WebUtil;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//
///**
// * 认证模块
// *
// * @author Chill
// */
//@RestController
//@AllArgsConstructor
//@Slf4j
//@Api(value = "用户授权认证", tags = "授权接口")
//public class ExamAuthController {
//
//	private RedisUtil redisUtil;
//
//	@PostMapping("token")
//	public R<AuthInfo> token(
//							 @RequestParam(required = false) String username,
//							 @RequestParam(required = false) String password)
//	{
//
//		String userType = Func.toStr(WebUtil.getRequest().getHeader(TokenUtil.USER_TYPE_HEADER_KEY), TokenUtil.DEFAULT_USER_TYPE);
//		TokenParameter tokenParameter = new TokenParameter();
//		tokenParameter.getArgs()
//			.set("username", username)
//			.set("password", password)
//			.set("userType", userType);
//
//		ITokenGranter granter = TokenGranterBuilder.getGranter("password");
//		UserInfo userInfo = granter.grant(tokenParameter);
//		if (userInfo == null || userInfo.getUser() == null || userInfo.getUser().getId() == null) {
//			return R.fail(TokenUtil.USER_NOT_FOUND);
//		}
//
//		return R.data(TokenUtil.createAuthInfo(userInfo));
//	}
//
//}
