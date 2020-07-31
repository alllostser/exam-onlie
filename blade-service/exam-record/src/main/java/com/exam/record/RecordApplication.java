package com.exam.record;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.launch.BladeApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients(CommonConstant.BASE_PACKAGES)
public class RecordApplication {
	public static void main(String[] args) {
		BladeApplication.run("exam-record",RecordApplication.class,args);
	}
}
