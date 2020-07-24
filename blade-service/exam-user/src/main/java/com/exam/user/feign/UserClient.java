package com.exam.user.feign;

import com.exam.user.entity.UserInfo;
import com.exam.user.service.IUserService;
import lombok.AllArgsConstructor;
import org.springblade.core.tool.api.R;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class UserClient implements EUserClient {
	private IUserService userService;
	@Override
	public R<UserInfo> userInfo(Long userId) {
		return null;
	}

	@Override
	@GetMapping(API_PREFIX + "/user-info")
	public R<UserInfo> userInfo(String tenantId, String account, String password) {
		UserInfo userInfo = userService.userInfo(tenantId, account, password);
		return R.data(userInfo);
	}
}
