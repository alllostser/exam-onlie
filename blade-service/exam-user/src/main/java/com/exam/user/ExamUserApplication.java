package com.exam.user;

import org.springblade.core.launch.BladeApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients("com.exam.user.feign")
public class ExamUserApplication {
	public static void main(String[] args) {
		BladeApplication.run("exam-user",ExamUserApplication.class,args);
	}
}
