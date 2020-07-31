package com.exam.exampaper;

import org.springblade.common.constant.CommonConstant;
import org.springblade.core.launch.BladeApplication;
import org.springblade.core.launch.constant.AppConstant;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringCloudApplication
@EnableFeignClients(value = {CommonConstant.BASE_PACKAGES, AppConstant.BASE_PACKAGES})
public class ExamPaperApplication {
	public static void main(String[] args) {
		BladeApplication.run("exam-exampaper",ExamPaperApplication.class,args);
	}
}
