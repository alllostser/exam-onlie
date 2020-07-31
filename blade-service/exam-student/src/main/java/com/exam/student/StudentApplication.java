package com.exam.student;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.launch.BladeApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients(CommonConstant.BASE_PACKAGES)
public class StudentApplication {
	public static void main(String[] args) {
		BladeApplication.run("exam-student",StudentApplication.class,args);
	}
}
