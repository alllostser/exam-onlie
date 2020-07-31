package com.exam.question;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.launch.BladeApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients({CommonConstant.BASE_PACKAGES,"org.springblade"})
public class QuestionApplication {
	public static void main(String[] args) {
		BladeApplication.run("exam-question",QuestionApplication.class,args);
	}
}
