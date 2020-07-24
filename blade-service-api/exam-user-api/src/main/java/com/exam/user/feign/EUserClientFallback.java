package com.exam.user.feign;

import com.exam.user.entity.UserInfo;
import org.springblade.core.tool.api.R;
import org.springframework.stereotype.Component;

@Component
public class EUserClientFallback implements EUserClient {
	@Override
	public R<UserInfo> userInfo(Long userId) {
		return null;
	}

	@Override
	public R<UserInfo> userInfo(String tenantId, String account, String password) {
		return R.fail("服务器错误");
	}
}
